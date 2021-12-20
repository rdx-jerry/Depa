package com.animator.navigation.ui.gallery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.blogs.BlogsFragment;
import com.animator.navigation.ui.tools.BaseURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class AlbumDetailFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    GridView galleryGv;
    private ArrayList<List_data> list_data;
    Adapter adapter;

    TextView titleTv, linkTv;
    String title, slug, link;
    String folder = BaseURL.getBaseUrl()+"uploads/gallery/";
    String imgPath = BaseURL.getBaseUrl()+"Api/gallery/detail/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_detail, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Gallery");
        queue= Volley.newRequestQueue(getActivity());
        share=this.getActivity().getSharedPreferences("project",MODE_PRIVATE);

        titleTv = (TextView)view.findViewById(R.id.titleTv);
        linkTv = (TextView)view.findViewById(R.id.linkTv);
        galleryGv = (GridView)view.findViewById(R.id.galleryGv);
        list_data=new ArrayList<>();
        title = getArguments().getString("title");
        slug = getArguments().getString("slug");
        link = getArguments().getString("link");
        titleTv.setText(title);
//        linkTv.setText(link);

        if (link.length() == 0) {
            linkTv.setVisibility(View.GONE);
        } else {
            linkTv.setVisibility(View.VISIBLE);
        }

        linkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(link)));
            }
        });
//        linkTv.setMovementMethod(LinkMovementMethod.getInstance());
        getData();
        return view;
    }

    private void getData() {
        StringRequest stringRequest =new StringRequest(Request.Method.GET, imgPath+slug,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray array=jsonObject.getJSONArray("data");
                            for (int i=0; i<array.length(); i++){
                                JSONObject ob=array.getJSONObject(i);
                                List_data playerModel = new List_data();
                                playerModel.setImageurl(folder+ob.getString("image"));
                                list_data.add(playerModel);
                            }
                            adapter=new MyAdapterDetail(getContext(),R.layout.layout_gallery_detail,list_data);
                            galleryGv.setAdapter((ListAdapter) adapter);
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
                    Fragment myFragment = new GalleryFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();

                    return true;
                }
                return false;
            }
        });
    }
}
