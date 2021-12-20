package com.animator.navigation.ui.profile;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class MergeFamilyFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    private TextView familyid, contact, address;

    private RecyclerView familyDetailsRv;
    private FamilyMergeAdapter familyAdapter;
    private ArrayList<Family> familyArrayList;
    ArrayList<String> memberArray;
    Button mergeBt;

    String family_id, client, ip, member_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_merge_family, container, false);

        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("project", MODE_PRIVATE);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Merge Family");

        queue = Volley.newRequestQueue(getContext());
        share = getActivity().getSharedPreferences("project", MODE_PRIVATE);
        family_id = share.getString("family_no", "");
        Log.e("family_id", family_id);
        client = share.getString("client", "");
        ip = share.getString("IP", "");
        member_id = share.getString("id", "");

        familyDetailsRv = (RecyclerView)view.findViewById(R.id.familyDetailsRv);
        familyid = (TextView)view.findViewById(R.id.familyid);
        contact = (TextView)view.findViewById(R.id.contact);
        address = (TextView)view.findViewById(R.id.address);
        mergeBt = (Button)view.findViewById(R.id.mergeBt);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        familyDetailsRv.setLayoutManager(linearLayoutManager);
        familyDetailsRv.setItemAnimator(new DefaultItemAnimator());

        familyArrayList = new ArrayList<Family>();
//        getRelationData();
        getFamilyDetails();

        mergeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memberArray = new ArrayList<>();

                memberArray.add(String.valueOf(familyAdapter.member_id));
                addMerge(memberArray);
            }
        });

        return view;
    }

    public void getFamilyDetails() {
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
                                address.setText(familyobj.getString("address_1") +", "+ familyobj.getString("address_2"));
                            }
                            JSONArray contactArray = dataobj.getJSONArray("contact");
                            for (int j = 0; j < contactArray.length(); j++) {

                                JSONObject contactobj = contactArray.getJSONObject(j);
                                contact.setText(contactobj.getString("contact"));
                            }
                            JSONArray memberArray = dataobj.getJSONArray("members");

                            for (int a = 0; a < memberArray.length(); a++) {
                                JSONObject memberobj = memberArray.getJSONObject(a);
                                Family relation = new Family();
                                Family playerModel = new Family();

                                id = memberobj.getString("id");
                                playerModel.setId(id);
                                playerModel.setImage(memberobj.getString("image"));
                                playerModel.setMemberId(memberobj.getString("member_no"));
                                playerModel.setMemberName(memberobj.getString("fullname"));
                                familyArrayList.add(playerModel);
                            }
                        }
                        familyAdapter = new FamilyMergeAdapter(getContext(), familyArrayList);
                        familyDetailsRv.setAdapter(familyAdapter);
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

                return params;
            }
        };
        queue.add(request);
    }

    public void addMerge(final ArrayList<String> memberArray) {
        StringRequest request = new StringRequest(Request.Method.POST, "http://www.nlblinds.com/depa/Api/member/mergefamily", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getString("status") == "true") {
                        Toast.makeText(getContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        AppCompatActivity activity = (AppCompatActivity) getContext();
                        Fragment myFragment = new ProfileFragment();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                    } else {
                        Toast.makeText(getContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("family_id", family_id);
                params.put("client", client);
                params.put("ip", ip);
                if(memberArray.size() == 1) {
                    for (int i = 0; i < memberArray.size(); i++) {
                        params.put("member_id[0]", memberArray.get(0));
                    }
                } else if(memberArray.size() == 2) {
                    for (int i = 0; i < memberArray.size(); i++) {
                        params.put("member_id[0]", memberArray.get(0));
                        params.put("member_id[1]", memberArray.get(1));
                    }
                } else if(memberArray.size() == 3) {
                    for (int i = 0; i < memberArray.size(); i++) {
                        params.put("member_id[0]", memberArray.get(0));
                        params.put("member_id[1]", memberArray.get(1));
                        params.put("member_id[2]", memberArray.get(2));
                    }
                }
                return params;
            }
        };
        queue.add(request);
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
