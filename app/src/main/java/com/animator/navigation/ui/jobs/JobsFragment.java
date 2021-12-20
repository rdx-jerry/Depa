package com.animator.navigation.ui.jobs;

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
import android.widget.ImageView;

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

import static android.content.Context.MODE_PRIVATE;

public class JobsFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    private ImageView addJob;
    private RecyclerView jobsRv;
    private JobsAdapter jobsAdapter;
    private ArrayList<Jobs> jobsArrayList;
    private JSONArray result;
    String id;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jobs, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Jobs");

        queue= Volley.newRequestQueue(getActivity());
        share=this.getActivity().getSharedPreferences("project",MODE_PRIVATE);

        id =  share.getString("id", "");
        Log.e("memberId", id);

        addJob = (ImageView) view.findViewById(R.id.addJob);
        jobsRv = (RecyclerView)view.findViewById(R.id.jobsRv);

        if (!id.equals("")) {
            addJob.setVisibility(View.VISIBLE);
        } else {
            addJob.setVisibility(View.GONE);
        }

        addJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new AddJobFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        jobsRv.setLayoutManager(linearLayoutManager);
        jobsRv.setItemAnimator(new DefaultItemAnimator());

        jobsArrayList = new ArrayList<Jobs>();
        getJobList();

        return view;
    }

    private void getJobList() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/Job/confirm",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.optString("status").equals("true")){
                                jobsArrayList = new ArrayList<>();

                                JSONArray dataArray  = obj.getJSONArray("data");
                                String id;
                                for (int i = 0; i < dataArray.length(); i++) {
                                    Jobs playerModel = new Jobs();
                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                    playerModel.setJobTitle(dataobj.getString("firm_name"));
                                    playerModel.setJobDesignation(dataobj.getString("designation"));
                                    id = dataobj.getString("id");
                                    playerModel.setId(id);
//                                    playerModel.setJobImage(dataobj.getString("image"));
                                    jobsArrayList.add(playerModel);
                                }
                                jobsAdapter = new JobsAdapter(getContext(), jobsArrayList);
                                jobsRv.setAdapter(jobsAdapter);
                                jobsAdapter.notifyDataSetChanged();
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
}
