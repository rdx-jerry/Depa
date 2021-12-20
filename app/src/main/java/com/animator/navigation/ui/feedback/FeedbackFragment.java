package com.animator.navigation.ui.feedback;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.tools.BaseURL;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FeedbackFragment extends Fragment {

    SharedPreferences share;
    SharedPreferences.Editor editor;
    RequestQueue queue;

    String memberId;
    EditText feedbackEt;
    String feedbackStr;
    Button saveBt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("private", MODE_PRIVATE);
        memberId = share.getString("memberId", "");
        feedbackEt = (EditText)view.findViewById(R.id.feedbackEt);
        saveBt = (Button)view.findViewById(R.id.feedbackBt);

        saveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedbackStr = feedbackEt.getText().toString().trim();
                saveFeedback(feedbackStr);
            }
        });
        return view;
    }

    public void saveFeedback(final String feedbackStr) {
        StringRequest request = new StringRequest(Request.Method.POST, BaseURL.getBaseUrl()+"Api/contact", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("member_no", memberId);
                params.put("feedback", feedbackStr);
                return params;
            }
        };
        queue.add(request);
    }
}
