package com.animator.navigation.ui.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.tools.BaseURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FamilyDetailsFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    private TextView familyid, contact, address, email;
    private Button addMemberBt, editFamilyBt, splitFamilyBt, mergeFamilyBt;

    private RecyclerView familyDetailsRv;
    private FamilyAdapter familyAdapter;
    private ArrayList<Family> familyArrayList;

    String family_id,  family_no;
    LinearLayout llSplitMerge;
    Button btnSplit, btnMatrimony;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_family_details, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Family Details");
        queue = Volley.newRequestQueue(getContext());
        share = getActivity().getSharedPreferences("project", MODE_PRIVATE);
        family_id = share.getString("family_no", "");
        Log.e("family_id", family_id);

        family_no = getArguments().getString("family_no");

        familyDetailsRv = (RecyclerView)view.findViewById(R.id.familyDetailsRv);
        familyid = (TextView)view.findViewById(R.id.familyid);
        contact = (TextView)view.findViewById(R.id.contact);
        address = (TextView)view.findViewById(R.id.address);
        email = (TextView)view.findViewById(R.id.femail);
        addMemberBt = (Button)view.findViewById(R.id.addMemberBt);
        editFamilyBt = (Button)view.findViewById(R.id.editFamilyBt);
        splitFamilyBt = (Button)view.findViewById(R.id.splitFamilyBt);
        mergeFamilyBt = (Button)view.findViewById(R.id.mergeFamilyBt);
        btnSplit = (Button) view.findViewById(R.id.btnSplit);
        btnMatrimony = (Button) view.findViewById(R.id.btnMatrimony);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        familyDetailsRv.setLayoutManager(linearLayoutManager);
        familyDetailsRv.setItemAnimator(new DefaultItemAnimator());

        familyArrayList = new ArrayList<Family>();
        getFamilyDetails();

        btnSplit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String url = "https://panjodepa.com/depa/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);*/
                showSplitMerge();
            }
        });

        btnMatrimony.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new MatrimonyMemberListFragment());
                fragmentTransaction.commit();

            }
        });

        addMemberBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new AddMemberFragment());
                fragmentTransaction.commit();
            }
        });

        editFamilyBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new EditFamilyFragment());
                fragmentTransaction.commit();
            }
        });

        splitFamilyBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new SplitFamilyFragment());
                fragmentTransaction.commit();
            }
        });

        mergeFamilyBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new MergeFamilyFragment());
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    public void getFamilyDetails() {
        Toast.makeText(getContext(), "FID:"+family_id, Toast.LENGTH_SHORT).show();
        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/family/"+family_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.optString("status").equals("true")) {
                        familyArrayList = new ArrayList<>();
                        JSONArray dataArray = obj.getJSONArray("data");
                        String id, familyidStr;

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataobj = dataArray.getJSONObject(i);
                            JSONObject familyobj = null;
                            JSONArray familyArray = dataobj.getJSONArray("family");
                            for (int j = 0; j < familyArray.length(); j++) {

                                familyobj = familyArray.getJSONObject(j);
                                familyidStr = familyobj.getString("id");
                                familyid.setText("Family Id: " + familyobj.getString("family_id"));
                                //address.setText(familyobj.getString("address_1") +", "+ familyobj.getString("address_2"));
                                String ad2;
                                if (familyobj.getString("address_2")!=null){
                                    ad2 = ", "+familyobj.getString("address_2");
                                }else{
                                    ad2 = "";
                                }
                                address.setText(familyobj.getString("address_1") + ad2 + " " + familyobj.getString("area_name") + familyobj.getString("pincode"));

                                contact.setText(familyobj.getString("landline"));
                                email.setText(familyobj.getString("email"));

                            }

                            /*JSONArray contactArray = dataobj.getJSONArray("contact");
                            for (int j = 0; j < contactArray.length(); j++) {
                                JSONObject contactobj = contactArray.getJSONObject(j);
                                contact.setText(contactobj.getString("contact"));
                            }*/



                            JSONArray memberArray = dataobj.getJSONArray("members");

                            for (int a = 0; a < memberArray.length(); a++) {
                                JSONObject memberobj = memberArray.getJSONObject(a);
                                Family playerModel = new Family();

                                id = memberobj.getString("id");
                                playerModel.setId(id);
                                playerModel.setImage(memberobj.getString("image"));
                                playerModel.setMemberId(memberobj.getString("member_no"));
                                playerModel.setMemberName(memberobj.getString("fullname"));
                                playerModel.setRelation(memberobj.getString("relation_id"));
                                playerModel.setLive_type(memberobj.getString("live_type"));
                                playerModel.setStatus(memberobj.getString("fm_status"));
                                familyArrayList.add(playerModel);
                            }
                        }
                        familyAdapter = new FamilyAdapter(getContext(), familyArrayList);
                        familyDetailsRv.setAdapter(familyAdapter);
                        familyAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volly Error", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("familyId", familyId);
                return params;
            }
        };
        queue.add(request);
    }



    public void showSplitMerge(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final View customLayout = getLayoutInflater() .inflate( R.layout.split_merge_custom_dialog, null);
        builder.setView(customLayout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
         });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Fragment myFragment = new ProfileFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();

                    return true;
                }
                return false;
            }
        });
    }
}
