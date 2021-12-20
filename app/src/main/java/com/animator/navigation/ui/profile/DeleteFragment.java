package com.animator.navigation.ui.profile;

import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.animator.navigation.ui.tools.BaseURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class DeleteFragment extends Fragment {

    private SharedPreferences share;
    private RequestQueue queue;
    String upLoadServerUri = BaseURL.getBaseUrl();
    String client, ip, memberId, reasonStr;
    TextView memberTv;
    EditText reasonEt;
    Button delete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_delete, container, false);
        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("project", MODE_PRIVATE);

        client = share.getString("client", "");
        ip = share.getString("IP", "");
        memberId = getArguments().getString("memberId");

        memberTv = (TextView)view.findViewById(R.id.memberTv);
        reasonEt = (EditText)view.findViewById(R.id.reason);
        delete = (Button)view.findViewById(R.id.deleteBt);
        memberTv.setText("MemberId:" + memberId);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reasonStr = reasonEt.getText().toString();
                submitReason(reasonStr);
            }
        });

        return view;
    }

    public void submitReason(final String reason) {
        StringRequest request = new StringRequest(Request.Method.POST, upLoadServerUri+"Api/member/delete", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getString("status") == "true") {
                        Toast.makeText(getContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        AppCompatActivity activity = (AppCompatActivity) getContext();
                        Fragment myFragment = new FamilyDetailsFragment();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                    } else {
                        Toast.makeText(getContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("member_id", memberId);
                params.put("reason", reason);
                params.put("client", client);
                params.put("ip", ip);
                return params;
            }
        };
        queue.add(request);
    }
}