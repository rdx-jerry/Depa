package com.animator.navigation.ui.profile;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.business.AddBusinessFragment;
import com.animator.navigation.ui.tools.BaseURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class MyBusinessFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    TextView recordTv;
    private ImageView addBusiness;
    private RecyclerView businessRv;
    private BusinessAdapter businessAdapter;
    private ArrayList<Business> businessArrayList;
    String id, memberId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_business, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Business");
        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("project", MODE_PRIVATE);
        id = share.getString("id", "");
        memberId = getArguments().getString("memberId");
        Log.e("my member", memberId);
        recordTv = (TextView) view.findViewById(R.id.recordTv);
        addBusiness = (ImageView) view.findViewById(R.id.addBusiness);
        businessRv = (RecyclerView) view.findViewById(R.id.businessRv);

//        if (!id.equals("")) {
//            addBusiness.setVisibility(View.VISIBLE);
//        } else {
//            addBusiness.setVisibility(View.GONE);
//        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Business");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        businessRv.setLayoutManager(linearLayoutManager);
        businessRv.setItemAnimator(new DefaultItemAnimator());

        businessArrayList = new ArrayList<Business>();

        addBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new MemberBusinessFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                bundle.putString("memberId", memberId);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });

        getBusinessList();

        return view;
    }

    public void getBusinessList() {
        businessArrayList = new ArrayList<Business>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseURL.getBaseUrl()+"Api/member/getbusiness",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("response", response);
                            if (obj.optString("status").equals("false")) {
                                recordTv.setVisibility(View.VISIBLE);
                                businessRv.setVisibility(View.GONE);
                            } else {
                                recordTv.setVisibility(View.GONE);
                                businessRv.setVisibility(View.VISIBLE);
                                    businessArrayList = new ArrayList<>();
                                    JSONArray dataArray = obj.getJSONArray("data");
                                    String id;
                                    for (int i = 0; i < dataArray.length(); i++) {
                                        Business playerModel = new Business();
                                        JSONObject dataobj = dataArray.getJSONObject(i);
                                        playerModel.setId(dataobj.getString("id"));
                                        playerModel.setMemberid(dataobj.getString("member_id"));
                                        playerModel.setFamilyid(dataobj.getString("family_id"));
                                        playerModel.setBusinessTitle(dataobj.getString("company_name"));
                                        playerModel.setDesignation(dataobj.getString("designation"));
                                        playerModel.setAddress(dataobj.getString("address_1") + dataobj.getString("address_2"));
                                        playerModel.setPincode(dataobj.getString("pincode"));
                                        playerModel.setArea(dataobj.getString("area"));
                                        playerModel.setEmail(dataobj.getString("email"));
                                        playerModel.setContact(dataobj.getString("contact"));
                                        playerModel.setWebsite(dataobj.getString("website"));
                                        businessArrayList.add(playerModel);
                                    }
                                    businessAdapter = new BusinessAdapter(getContext(), businessArrayList);
                                    businessRv.setAdapter(businessAdapter);
                                    businessAdapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("member_id", memberId);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}