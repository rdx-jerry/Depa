package com.animator.navigation.ui.latest.past;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.animator.navigation.ui.gallery.ImageFragment;
import com.animator.navigation.ui.latest.LatestFragment;
import com.animator.navigation.ui.tools.BaseURL;
import com.animator.navigation.ui.tools.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class PastDetailFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    private String FOLDER = BaseURL.getBaseUrl()+"uploads/news/";
    private String NEWSDATA = BaseURL.getBaseUrl()+"Api/news/";
    TextView titleTv, descriptionTv, dateTv, linksTv;
    ImageView detailIv;
    private String id;
    final Bundle bundle = new Bundle();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_past_detail, container, false);
        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("project", MODE_PRIVATE);
        titleTv = (TextView) view.findViewById(R.id.titleDetail);
        detailIv = (ImageView) view.findViewById(R.id.detailIv);
        descriptionTv = (TextView) view.findViewById(R.id.descTv);
        dateTv = (TextView) view.findViewById(R.id.dateTv);
        linksTv = (TextView) view.findViewById(R.id.linkTv);

        id = getArguments().getString("id");
        getDetails(id);
        return view;
    }

    private void getDetails(final String id) {
        StringRequest request = new StringRequest(Request.Method.GET, NEWSDATA + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("responseDetails", response);

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.optString("status").equals("true")) {
                        JSONArray dataArray = obj.getJSONArray("data");
                        String imgPath;

                        for (int i = 0; i < dataArray.length(); i++) {
                            Past playerModel = new Past();
                            JSONObject dataobj = dataArray.getJSONObject(i);
                            titleTv.setText(dataobj.getString("title"));
                            imgPath = FOLDER + dataobj.getString("image");
                            Picasso.with(getContext()).load(imgPath).into(detailIv);
                            descriptionTv.setText("Description: " + dataobj.getString("description"));
                            dateTv.setText("Event Date: " + Utils.convertDate(dataobj.getString("event_date")));
                            linksTv.setText("Link: " + dataobj.getString("link"));

                            final String image = imgPath;
                            detailIv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                    Fragment myFragment = new ImageFragment();
                                    final Bundle bundle = new Bundle();
                                    myFragment.setArguments(bundle);
                                    bundle.putString("image", image);
                                    bundle.putString("id", "0");
                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                                }
                            });

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
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

                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Fragment myFragment = new LatestFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                    return true;
                }
                return false;
            }
        });
    }
}
