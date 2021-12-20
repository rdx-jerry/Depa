package com.animator.navigation.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ViewFragment extends Fragment {

    private SharedPreferences share;
    private RequestQueue queue;
    private TextView familyIdTv, memberIdTv, nameTv, ageTv, bloodGroupTv, mobileTv, emailTv, addressTv, educationTv, occupationTv;
    String familyIdStr, memberIdStr, ageStr, bloodGroupStr, mobileStr, houseStr, streetStr, areaStr, pinStr, addressTvStr, educationTvStr, occupationStr, emailStr;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view, container, false);
        queue = Volley.newRequestQueue(getContext());
        share = getActivity().getSharedPreferences("project", MODE_PRIVATE);

        memberIdStr = share.getString("memberId", "");

        familyIdTv = (TextView)view.findViewById(R.id.fIdTv);
        memberIdTv = (TextView)view.findViewById(R.id.mIdTv);
        nameTv = (TextView)view.findViewById(R.id.nameTv);
        ageTv = (TextView)view.findViewById(R.id.ageTv);
        bloodGroupTv = (TextView)view.findViewById(R.id.bloodTv);
        mobileTv = (TextView)view.findViewById(R.id.mobileTv);
        emailTv = (TextView)view.findViewById(R.id.emailTv);
        addressTv = (TextView)view.findViewById(R.id.addressTv);
        educationTv = (TextView)view.findViewById(R.id.educationTv);
        occupationTv = (TextView)view.findViewById(R.id.occupationTv);

        getProfile(memberIdStr);

        return view;
    }

    public void getProfile(final String memberIdStr) {
        StringRequest request = new StringRequest(Request.Method.POST, "URL", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    familyIdTv.setText(jObj.getString("familyId"));
                    memberIdTv.setText(memberIdStr);
                    nameTv.setText(jObj.getString("nameTv"));
                    ageTv.setText(jObj.getString("age"));
                    bloodGroupTv.setText(jObj.getString("bloodGroup"));
                    mobileTv.setText(jObj.getString("mobile"));
                    emailTv.setText(jObj.getString("email"));
                    addressTv.setText(jObj.getString("address"));
                    educationTv.setText(jObj.getString("education"));
                    occupationTv.setText(jObj.getString("occupation"));
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
                params.put("memberId", memberIdStr);
                return super.getParams();
            }
        };
        queue.add(request);
    }

}
