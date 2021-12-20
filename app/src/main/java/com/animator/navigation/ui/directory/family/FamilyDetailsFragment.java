package com.animator.navigation.ui.directory.family;

import android.content.SharedPreferences;
import android.os.Bundle;

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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.directory.DirectoryFragment;
import com.animator.navigation.ui.todays.TodaysFragment;
import com.animator.navigation.ui.tools.BaseURL;
import com.animator.navigation.ui.tools.ToolsFragment;

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

    private TextView familyid, contact, address;

    private RecyclerView familyDetailsRv;
    private FamilyAdapter familyAdapter;
    private ArrayList<Family> familyArrayList;

    String family_id, familyNo, redirect, redirect_to;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_family_details2, container, false);
        queue = Volley.newRequestQueue(getContext());
        share = getActivity().getSharedPreferences("project", MODE_PRIVATE);
        family_id = share.getString("family_id", "");
        Log.e("family_id", family_id);

        familyNo = getArguments().getString("family_no");
        redirect = getArguments().getString("redirect");

        familyDetailsRv = (RecyclerView) view.findViewById(R.id.familyDetailsRv);
        familyid = (TextView) view.findViewById(R.id.familyid);
        contact = (TextView) view.findViewById(R.id.contact);
        address = (TextView) view.findViewById(R.id.address);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        familyDetailsRv.setLayoutManager(linearLayoutManager);
        familyDetailsRv.setItemAnimator(new DefaultItemAnimator());

        familyArrayList = new ArrayList<Family>();
        getFamilyDetails();

        return view;
    }

    public void getFamilyDetails() {
        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/family/" + familyNo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.e("response", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    Log.e("family", String.valueOf(obj));
                    if (obj.optString("status").equals("true")) {
                        familyArrayList = new ArrayList<>();
                        JSONArray dataArray = obj.getJSONArray("data");
                        String id, familyidStr;

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataobj = dataArray.getJSONObject(i);

                            JSONArray familyArray = dataobj.getJSONArray("family");
                            for (int j = 0; j < familyArray.length(); j++) {

                                JSONObject familyobj = familyArray.getJSONObject(j);
                                familyidStr = familyobj.getString("id");
//                                familyNo = familyobj.getString("family_no");
                                familyid.setText(familyobj.getString("family_id"));
                                address.setText(familyobj.getString("address_1") + ", " + familyobj.getString("address_2"));
                            }
                            JSONArray contactArray = dataobj.getJSONArray("contact");
                            for (int j = 0; j < contactArray.length(); j++) {

                                JSONObject contactobj = contactArray.getJSONObject(j);
                                contact.setText(contactobj.getString("contact"));
                            }
                            JSONArray memberArray = dataobj.getJSONArray("members");
                            for (int a = 0; a < memberArray.length(); a++) {
                                JSONObject memberobj = memberArray.getJSONObject(a);

                                Family playerModel = new Family();
                                id = memberobj.getString("id");
                                playerModel.setId(id);
                                playerModel.setImage(memberobj.getString("image"));
                                playerModel.setMemberId(memberobj.getString("member_no"));
                                playerModel.setMemberName(memberobj.getString("fullname"));
                                familyArrayList.add(playerModel);
                            }

                            familyAdapter = new FamilyAdapter(getContext(), familyArrayList);
                            familyDetailsRv.setAdapter(familyAdapter);
                            familyAdapter.notifyDataSetChanged();
                        }
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("familyId", familyId);
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

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    if (redirect.equalsIgnoreCase("true")){

                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        Fragment myFragment = new TodaysFragment();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                        return true;

                    }else {

                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        Fragment myFragment = new Fragment();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();

                        return true;
                    }
                }
                return false;
            }
        });
    }
}
