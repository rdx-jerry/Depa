package com.animator.navigation.ui.business;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.business.Business;
import com.animator.navigation.ui.business.BusinessAdapter;
import com.animator.navigation.ui.tools.BaseURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class BusinessFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    private ImageView addBusiness;
    private RecyclerView businessRv;
    private BusinessAdapter businessAdapter;
    private ArrayList<Business> businessArrayList;
    String id;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Business");
        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("project", MODE_PRIVATE);
        id = share.getString("id", "");

        businessRv = (RecyclerView) view.findViewById(R.id.businessRv);
        addBusiness = (ImageView) view.findViewById(R.id.addBusiness);

        if (!id.equals("")) {
            addBusiness.setVisibility(View.VISIBLE);
        } else {
            addBusiness.setVisibility(View.GONE);
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Business");

        addBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new AddBusinessFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
//                bundle.putString("id", businessObj.getId());
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        businessRv.setLayoutManager(linearLayoutManager);
        businessRv.setItemAnimator(new DefaultItemAnimator());

        businessArrayList = new ArrayList<Business>();

        getJobList();

        return view;
    }

    public void getJobList() {
        businessArrayList = new ArrayList<Business>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/b2b/androidb2b/confirm",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.optString("status").equals("true")) {
                                businessArrayList = new ArrayList<>();
                                JSONArray dataArray = obj.getJSONArray("data");
                                String id;
                                for (int i = 0; i < dataArray.length(); i++) {
                                    Business playerModel = new Business();
                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                    playerModel.setbusinessTitle(dataobj.getString("firm_name"));
                                    playerModel.setbusinessImage(dataobj.getString("logo"));
                                    playerModel.setBusinessCategory(dataobj.getString("category"));
                                    id = dataobj.getString("id");
                                    playerModel.setId(id);

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
                });
        queue.add(stringRequest);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
