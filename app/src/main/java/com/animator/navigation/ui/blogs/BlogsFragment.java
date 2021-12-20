package com.animator.navigation.ui.blogs;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.jobs.AddJobFragment;
import com.animator.navigation.ui.jobs.Jobs;
import com.animator.navigation.ui.jobs.JobsAdapter;
import com.animator.navigation.ui.tools.BaseURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class BlogsFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    private ImageView addBlog;
    private RecyclerView blogRv;
    private BlogAdapter blogAdapter;
    ArrayList<Blogs> blogsArrayList;
//    private Button nextBtn, prevBtn;
//    Paginator p = new Paginator();
//    private int totalPages = Paginator.TOTAL_NUM_ITEMS / Paginator.ITEMS_PER_PAGE;
//    private int currentPage = 0;

    String folder = BaseURL.getBaseUrl()+"uploads/blog/";
    String url = BaseURL.getBaseUrl()+"Api/blog";
    String id;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blogs, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Blogs");
        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("project", MODE_PRIVATE);
        id = share.getString("id", "");

        addBlog = (ImageView) view.findViewById(R.id.addBlog);
        blogRv = (RecyclerView) view.findViewById(R.id.blogRv);

        if (!id.equals("")) {
            addBlog.setVisibility(View.VISIBLE);
        } else {
            addBlog.setVisibility(View.GONE);
        }
//        nextBtn = (Button) view.findViewById(R.id.nextBtn);
//        prevBtn = (Button) view.findViewById(R.id.prevBtn);
//        prevBtn.setEnabled(false);

        addBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new AddBlogFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        blogRv.setLayoutManager(linearLayoutManager);
        blogRv.setItemAnimator(new DefaultItemAnimator());

        getBlogList();

        //NAVIGATE
//        nextBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                currentPage += 1;
//                blogRv.setAdapter(new BlogAdapter(getContext(), p.generatePage(currentPage)));
//                toggleButtons();
//            }
//        });
//        prevBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                currentPage -= 1;
//                blogRv.setAdapter(new BlogAdapter(getContext(), p.generatePage(currentPage)));
//                toggleButtons();
//            }
//        });
//        blogRv.setAdapter(blogAdapter);

        return view;
    }

    public void getBlogList() {
//        blogsArrayList = new ArrayList<Blogs>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.optString("status").equals("true")){
                                blogsArrayList = new ArrayList<>();

                                JSONArray dataArray  = obj.getJSONArray("data");
                                String id;
                                for (int i = 0; i < dataArray.length(); i++) {
                                    Blogs playerModel = new Blogs();
                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                    id = dataobj.getString("id");
                                    playerModel.setId(id);
                                    playerModel.setBlogTitle(dataobj.getString("title"));
                                    playerModel.setBlogImage(folder+dataobj.getString("image"));
                                    playerModel.setSlug(dataobj.getString("slug"));
                                    blogsArrayList.add(playerModel);
                                }
                                blogAdapter = new BlogAdapter(getContext(), blogsArrayList);

//                                nextBtn.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//
//                                        currentPage += 1;
//                                        blogRv.setAdapter(new BlogAdapter(getContext(), p.generatePage(currentPage)));
//                                        toggleButtons();
//                                    }
//                                });
//                                prevBtn.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        currentPage -= 1;
//                                        blogRv.setAdapter(new BlogAdapter(getContext(), p.generatePage(currentPage)));
//                                        toggleButtons();
//                                    }
//                                });

                                blogRv.setAdapter(blogAdapter);
//                                blogRv.setAdapter(new BlogAdapter(getContext(), p.generatePage(currentPage)));
                                blogAdapter.notifyDataSetChanged();
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

//    private void toggleButtons() {
//        if (currentPage == totalPages) {
//            nextBtn.setEnabled(false);
//            prevBtn.setEnabled(true);
//        } else if (currentPage == 0) {
//            prevBtn.setEnabled(false);
//            nextBtn.setEnabled(true);
//        } else if (currentPage >= 1 && currentPage <= totalPages) {
//            nextBtn.setEnabled(true);
//            prevBtn.setEnabled(true);
//        }
//    }
}