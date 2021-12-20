package com.animator.navigation.ui.todays.punyatithi;

import androidx.appcompat.app.AppCompatActivity;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.home.HomeFragment;
import com.animator.navigation.ui.tools.BaseURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class PunyatithiFragment extends Fragment {

    private PunyatithiViewModel mViewModel;

    public static PunyatithiFragment newInstance() {
        return new PunyatithiFragment();
    }

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    private RecyclerView todaysRv;
    private ArrayList<Event> events;
    private EventAdapter eventAdapter;

    private String data = BaseURL.getBaseUrl()+"Api/member/getpuniyatithi";
    String imgPath = BaseURL.getBaseUrl()+"uploads/members/";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_punyatithi, container, false);
        todaysRv = (RecyclerView) view.findViewById(R.id.todaysRv);

        queue= Volley.newRequestQueue(getActivity());
        share=this.getActivity().getSharedPreferences("project",MODE_PRIVATE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        todaysRv.setLayoutManager(linearLayoutManager);
        todaysRv.setItemAnimator(new DefaultItemAnimator());


        events = new ArrayList<Event>();

        getPunyatithiList();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PunyatithiViewModel.class);
        // TODO: Use the ViewModel
    }

    public void getPunyatithiList() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, data, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);
                Event event;
                try {
                    JSONObject obj = new JSONObject(response);

                    if(obj.optString("status").equals("true")) {
                        events = new ArrayList<>();
                        JSONArray dataArray = obj.getJSONArray("data");
                        String id;
                        for (int i = 0; i < dataArray.length(); i++) {
                            Event playerModel = new Event();
                            JSONObject dataobj = dataArray.getJSONObject(i);
                            id = dataobj.getString("id");
                            playerModel.setId(id);
                            playerModel.setName(dataobj.getString("fullname"));
                            playerModel.setImage(imgPath + dataobj.getString("image"));
                            events.add(playerModel);
                        }
                        eventAdapter = new EventAdapter(getContext(), events);
                        todaysRv.setAdapter(eventAdapter);
                        eventAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
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
                    Fragment myFragment = new HomeFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();

                    return true;
                }
                return false;
            }
        });
    }
}
