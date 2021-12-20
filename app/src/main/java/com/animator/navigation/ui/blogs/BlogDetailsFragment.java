package com.animator.navigation.ui.blogs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.animator.navigation.ui.gallery.ImageFragment;
import com.animator.navigation.ui.tools.BaseURL;
import com.animator.navigation.ui.tools.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class BlogDetailsFragment extends Fragment {
    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    TextView titleTv, slugTv, descriptionTv, uploadTv, uploadDateTv;
    ImageView detailIv;
    RecyclerView blogImgRv;
    String slug, imgStr;
    int imgPath;
    ImageButton shareTitle;
    ArrayList<BlogImage> blogsArrayList;
    ImgAdapter imgAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog_details, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Blog");
        queue= Volley.newRequestQueue(getActivity());
        share=this.getActivity().getSharedPreferences("project",MODE_PRIVATE);

        imgStr = getArguments().getString("img");
        Log.e("imgStr", imgStr);
        shareTitle = (ImageButton) view.findViewById(R.id.share);
        titleTv = (TextView)view.findViewById(R.id.titleDetail);
        slugTv = (TextView)view.findViewById(R.id.slug);
        descriptionTv = (TextView)view.findViewById(R.id.description);
        uploadTv = (TextView)view.findViewById(R.id.uploadBy);
        uploadDateTv = (TextView)view.findViewById(R.id.uploadDate);
        detailIv = (ImageView) view.findViewById(R.id.detailIv);
        blogImgRv = (RecyclerView) view.findViewById(R.id.blogImgRv);
        blogImgRv.setNestedScrollingEnabled(false);
        blogImgRv.setHasFixedSize(false);
        Picasso.with(getContext()).load(imgStr).into(detailIv);

        detailIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new ImageFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                //Toast.makeText(getContext(), "GG:"+image, Toast.LENGTH_SHORT).show();
                bundle.putString("image", imgStr);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        blogImgRv.setLayoutManager(linearLayoutManager);
        blogImgRv.setItemAnimator(new DefaultItemAnimator());

        slug = getArguments().getString("slug");
        getDetails();

        shareTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Shree Depa Jain Mahajan Trust");
                    String shareMessage= "\nShree Depa Jain Mahajan Trust \n\n";
                    shareMessage = shareMessage + "Title: " + titleTv.getText().toString()  +"\n\n";

                    shareMessage = shareMessage + "Description: " + descriptionTv.getText().toString()  +"\n\n"; //Designation
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                }
            }
        });
        return view;
    }

    public void getDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/blog/getbyid/"+slug, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jArray = jObj.getJSONArray("data");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject object = jArray.getJSONObject(i);

                        JSONArray jsonArray = object.getJSONArray("blogdata");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject blogObject = jsonArray.getJSONObject(j);

                            String title = blogObject.getString("title").trim();
                            String slug = blogObject.getString("slug").trim();
                            String description = blogObject.getString("description").trim();
                            String uploadBy = blogObject.getString("uploaded_by").trim();
                            String upDate = blogObject.getString("added_on").trim();
                            titleTv.setText(Html.fromHtml(Html.fromHtml(title).toString()));
                            slugTv.setText(Html.fromHtml(Html.fromHtml(slug).toString()));
                            descriptionTv.setText(Html.fromHtml(Html.fromHtml(description).toString()));
                            uploadTv.setText("Uploaded By: " + uploadBy);
                            uploadDateTv.setText("Upload On: " + Utils.convertDate(upDate));
//                            Picasso.with(getContext()).load(BaseURL.getBaseUrl()+"uploads/blog/" + object.getString("image")).into(detailIv);
                        }
                        blogsArrayList = new ArrayList<>();
                        JSONArray jsonIArray = object.getJSONArray("blogimg");
                        for (int a = 0; a < jsonIArray.length(); a++) {
                            JSONObject blogIObject = jsonIArray.getJSONObject(a);
//                            Picasso.with(getContext()).load(BaseURL.getBaseUrl()+"uploads/blog/" + blogIObject.getString("image")).into(detailIv);
                            BlogImage blogImage = new BlogImage();
                            blogImage.setImg(blogIObject.getString("image"));
                            blogsArrayList.add(blogImage);
                        }
                        imgAdapter = new ImgAdapter(getContext(), blogsArrayList);
                        blogImgRv.setAdapter(imgAdapter);
                        imgAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley error: ", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("id", getid);
                return params;
            }
        };
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
                    Fragment myFragment = new BlogsFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();

                    return true;
                }
                return false;
            }
        });
    }
}

