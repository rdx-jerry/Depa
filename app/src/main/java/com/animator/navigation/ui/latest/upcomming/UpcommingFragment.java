package com.animator.navigation.ui.latest.upcomming;

import android.content.SharedPreferences;
import android.os.Bundle;

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
import com.animator.navigation.ui.latest.past.Past;
import com.animator.navigation.ui.tools.BaseURL;
import com.animator.navigation.ui.tools.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class UpcommingFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    private RecyclerView newsRv;
    private UpcommingAdapter newsAdapter;
    private ArrayList<Upcomming> newsArrayList;
    private JSONArray result;
    private String FOLDER = BaseURL.getBaseUrl()+"uploads/news/";
    private String NEWDATA = BaseURL.getBaseUrl()+"Api/event/getlatest";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcomming, container, false);
        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("project", MODE_PRIVATE);

        newsRv = (RecyclerView) view.findViewById(R.id.newsRv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        newsRv.setLayoutManager(linearLayoutManager);
        newsRv.setItemAnimator(new DefaultItemAnimator());
        newsRv.setBackgroundResource(R.drawable.bg_gradient);

        newsArrayList = new ArrayList<Upcomming>();
        getNewsList();
        return view;
    }
    private void getNewsList() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, NEWDATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.optString("status").equals("true")) {
                                newsArrayList = new ArrayList<>();
                                JSONArray dataArray = obj.getJSONArray("data");

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject jObj = dataArray.getJSONObject(i);
                                    JSONArray jArray = jObj.getJSONArray("upcomingevent");
                                    Log.d("upcomingevent", String.valueOf(jObj.getJSONArray("upcomingevent")));
                                    for (int j = 0; j < jArray.length(); j++) {
                                        Upcomming playerModel = new Upcomming();
                                        JSONObject dataobj = jArray.getJSONObject(j);
                                        playerModel.setNewsTitle(dataobj.getString("title"));
                                        playerModel.setDescription(dataobj.getString("description"));
                                        playerModel.setEvent_date(Utils.convertDate(dataobj.getString("event_date")));
                                        playerModel.setLink(dataobj.getString("link"));
                                        playerModel.setNewsImage(FOLDER + dataobj.getString("image"));
                                        playerModel.setId(dataobj.getString("id"));
                                        newsArrayList.add(playerModel);
                                    }
                                }
                                newsAdapter = new UpcommingAdapter(getContext(), newsArrayList);
                                newsRv.setAdapter(newsAdapter);
                                newsAdapter.notifyDataSetChanged();
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

    /*public String convertDate(String date){

        Date oneWayTripDate = null;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat("yyyy-mm-dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat("dd-mm-yyyy");
        try {
            oneWayTripDate = input.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return output.format(oneWayTripDate);
    }*/


}