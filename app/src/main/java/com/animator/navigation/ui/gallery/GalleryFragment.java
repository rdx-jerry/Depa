package com.animator.navigation.ui.gallery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.blogs.BlogsFragment;
import com.animator.navigation.ui.home.HBlogs;
import com.animator.navigation.ui.home.HomeFragment;
import com.animator.navigation.ui.profile.FamilyDetailsFragment;
import com.animator.navigation.ui.tools.BaseURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class GalleryFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

//    GridView galleryGv;
    private RecyclerView galleryRv;
    private ArrayList<List_data> list_data;
    private MyAdapter adapter;

    String folder = BaseURL.getBaseUrl()+"uploads/gallery/";
    String imgPath = BaseURL.getBaseUrl()+"Api/gallery/androidlist";

//    String videoListPath = BaseURL.getBaseUrl()+"Api/galleryvideo/"; video list api {data: title, link}

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Gallery");
        queue= Volley.newRequestQueue(getActivity());
        share=this.getActivity().getSharedPreferences("project",MODE_PRIVATE);
//        galleryGv = (GridView)root.findViewById(R.id.galleryGv);
        galleryRv = (RecyclerView) view.findViewById(R.id.galleryRv);
        list_data=new ArrayList<>();

        getData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        galleryRv.setLayoutManager(linearLayoutManager);
        galleryRv.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    private void getData() {
        StringRequest stringRequest =new StringRequest(Request.Method.GET, imgPath,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray array=jsonObject.getJSONArray("data");
                            String id;
                            for (int i=0; i<array.length(); i++){
                                JSONObject ob=array.getJSONObject(i);
//                                List_data listData = new List_data(ob.getString("title"),folder+ob.getString("thumbnail"));
                                List_data playerModel = new List_data();
//                                JSONObject dataobj = dataArray.getJSONObject(i);
                                id = ob.getString("id");
                                playerModel.setId(id);
                                playerModel.setName(ob.getString("title"));
                                playerModel.setImageurl(folder+ob.getString("thumbnail"));
                                playerModel.setSlug(ob.getString("slug"));
                                playerModel.setLink(ob.getString("gdrive_link"));
                                playerModel.setCount(ob.getString("count"));
                                list_data.add(playerModel);
                            }
                            adapter = new MyAdapter(getContext(), list_data);
                            galleryRv.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
//                            galleryGv.setAdapter((ListAdapter) adapter);

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
                    Fragment myFragment = new HomeFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();

                    return true;
                }
                return false;
            }
        });
    }
}