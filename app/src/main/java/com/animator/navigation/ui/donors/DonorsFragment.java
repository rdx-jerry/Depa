package com.animator.navigation.ui.donors;

import androidx.lifecycle.ViewModelProviders;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import static android.content.Context.MODE_PRIVATE;

public class DonorsFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    //    GridView galleryGv;
    private RecyclerView donorsRv;
    private ArrayList<List_data> list_data;
    private MyAdapter adapter;

    private String folderDonors = BaseURL.getBaseUrl()+"uploads/donors/";
    private String imgDonors = BaseURL.getBaseUrl()+"Api/donors/";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.donors_fragment, container, false);
        queue= Volley.newRequestQueue(getActivity());
        share=this.getActivity().getSharedPreferences("project",MODE_PRIVATE);
        donorsRv = (RecyclerView) view.findViewById(R.id.donorsRv);
        list_data=new ArrayList<>();

        getData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        donorsRv.setLayoutManager(linearLayoutManager);
        donorsRv.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void getData() {
        StringRequest stringRequest =new StringRequest(Request.Method.GET, imgDonors,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        try {

                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray array=jsonObject.getJSONArray("data");
                            String id;
                            for (int i=0; i<array.length(); i++){
                                JSONObject ob=array.getJSONObject(i);
                                List_data playerModel = new List_data();
                                id = ob.getString("id");
                                playerModel.setId(id);
                                playerModel.setName(ob.getString("donor_title"));
                                playerModel.setImageurl(folderDonors + ob.getString("donor_image"));
                                playerModel.setSlug(ob.getString("category"));
                                list_data.add(playerModel);
                            }
                            adapter = new MyAdapter(getContext(), list_data);
                            donorsRv.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

}