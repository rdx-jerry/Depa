package com.animator.navigation.ui.profile;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class SplitFragment extends Fragment {

    SharedPreferences share;
    SharedPreferences.Editor editor;
    RequestQueue queue;

    private Spinner areaSp;
    private JSONArray result;
    private ArrayList<String> areaArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_split, container, false);

        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("project", MODE_PRIVATE);

        areaSp = (Spinner) view.findViewById(R.id.areaSp);

        areaArray = new ArrayList<String>();

        getAreaData();
        return view;
    }

    private void getAreaData() {
        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/masters/area",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray("data");
                            getArea(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        queue.add(request);
    }

    private void getArea(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject jSon = j.getJSONObject(i);
                areaArray.add(jSon.getString("area"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        areaSp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner, areaArray));
    }
}