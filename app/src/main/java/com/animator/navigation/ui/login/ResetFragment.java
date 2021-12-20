package com.animator.navigation.ui.login;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.animator.navigation.ui.blogs.BlogDetailsFragment;
import com.animator.navigation.ui.tools.BaseURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ResetFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

//    String RESETPASS = BaseURL.getBaseUrl()+"Api/member/resetpassword";
    String RESETPASS = BaseURL.getBaseUrl()+"Api/helpus/resetpassword";
    TextView memberId;
    EditText oldPassEt, resetPassEt, confirmPass, userOld, userNew;
    Button resetPassBt;

    String memberIdStr, oldPassStr, resetPassStr, confirmPassStr, oldUserStr, newUserStr;
    int flag = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.reset_password);
        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("project", MODE_PRIVATE);

        memberId = (TextView)view.findViewById(R.id.memberId);

        oldPassEt = (EditText)view.findViewById(R.id.oldPass);
        resetPassEt = (EditText)view.findViewById(R.id.resetPass);
        confirmPass = (EditText)view.findViewById(R.id.confirmPass);
        resetPassBt = (Button)view.findViewById(R.id.resetPassBt);

        memberIdStr = share.getString("id", "");
        memberId.setText(memberIdStr);

        resetPassBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPassStr = oldPassEt.getText().toString();
                resetPassStr = resetPassEt.getText().toString();
                confirmPassStr = confirmPass.getText().toString();

                if (resetPassStr.equals(confirmPassStr)) {

                    reset(memberIdStr, oldPassStr, resetPassStr, confirmPassStr);
                } else {
                    Toast.makeText(getContext(), "Password Mismatch", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void reset(final String memberIdStr, final String oldPassStr, final String resetPassStr, final String confirmPassStr){
        Log.e("Password details: ", memberIdStr + " " + resetPassStr);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RESETPASS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);

                try {
                    JSONObject jObj = new JSONObject(response);

                    if (jObj.getString("status").equals("true")){
                        editor = share.edit();
                        editor.clear();
                        editor.apply();
                        editor.commit();
                        AppCompatActivity activity = (AppCompatActivity) getContext();
                        Fragment myFragment = new LoginFragment();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                        Toast.makeText(getContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", memberIdStr);
                params.put("old_password", oldPassStr);
                params.put("new_password", resetPassStr);
                params.put("confirm_password", confirmPassStr);
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
                    Fragment myFragment = new LoginFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();

                    return true;
                }
                return false;
            }
        });
    }
}