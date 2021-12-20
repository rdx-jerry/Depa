package com.animator.navigation.ui.login;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class CreateUsernamePasswordFragment extends Fragment {

    private RequestQueue queue;
    private String CUP = BaseURL.getBaseUrl()+"Api/member/create_username";

    EditText username, password, cpassword;
    Button submit;

    String id, getUsername, getPassword, getCPassword;
    final Bundle bundle = new Bundle();
    ProgressDialog pd;

    public CreateUsernamePasswordFragment() {}

    public static CreateUsernamePasswordFragment newInstance(String param1, String param2) {
        return new CreateUsernamePasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_username_password, container, false);

        queue = Volley.newRequestQueue(getActivity());

        id = getArguments().getString("id");

        username = (EditText) view.findViewById(R.id.usernameEt);
        password = (EditText) view.findViewById(R.id.passwordEt);
        cpassword = (EditText) view.findViewById(R.id.cpasswordEt);
        submit = (Button) view.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getUsername = username.getText().toString().trim();
                getPassword = password.getText().toString().trim();
                getCPassword = cpassword.getText().toString().trim();

                if (!getUsername.isEmpty() && !getPassword.isEmpty() && !getCPassword.isEmpty()){
                    if (getPassword.equals(getCPassword) && getPassword.length() >= 6 && getCPassword.length() >= 6){

                        createUsernamePassword(id, getUsername, getPassword, getCPassword);

                    }else{
                        Toast.makeText(getContext(), "Password does not matched\nOR\nMinimum 6 Character", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
                }


            }
        });

        return view;

    }


    public void createUsernamePassword(String member_id, String username, String password, String cpassword){

        pd = ProgressDialog.show(getContext(), "", "Please wait, creating...", true);

        StringRequest request = new StringRequest(Request.Method.POST, CUP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("create User Pass ", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    String status = jObj.getString("status");
                    String validate = jObj.getString("validate");
                    String message = jObj.getString("message");

                    pd.dismiss();

                    if (status.equalsIgnoreCase("true") && validate.equalsIgnoreCase("true")){

                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, new LoginFragment());
                        fragmentTransaction.commit();

                    }else{
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Log.e("VolleyError", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("member_id", member_id);
                params.put("user_name", username);
                params.put("newpassword", password);
                params.put("repassword", cpassword);
                return params;
            }
        };
        queue.add(request);
    }


}