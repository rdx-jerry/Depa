package com.animator.navigation.ui.login;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.animator.navigation.ui.contactus.ContactUsFragment;
import com.animator.navigation.ui.tools.BaseURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NoPasswordGeneratePINFragment extends Fragment {

    private RequestQueue queue;
    private String PIN = BaseURL.getBaseUrl()+"Api/member/generatepinandroid";

    RadioGroup pinYesNo;
    RadioButton radioYes, radioNo;

    Button contactBtn, generatePin, verifyPINBtn;
    TextView emailTv, phoneTv;
    EditText pinEt;
    LinearLayout llVerifyPIN, radioBtn;

    String id, contact, email, status, compareToken;
    final Bundle bundle = new Bundle();

    ProgressDialog pd;

    public static NoPasswordGeneratePINFragment newInstance() { return new NoPasswordGeneratePINFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_password_generate_p_i_n, container, false);

        queue = Volley.newRequestQueue(getActivity());

        id = getArguments().getString("id");
        contact = getArguments().getString("contact");
        email = getArguments().getString("email");
        status = getArguments().getString("status");

        radioYes = (RadioButton) view.findViewById(R.id.radioYes);
        radioNo = (RadioButton) view.findViewById(R.id.radioNo);
        pinYesNo = (RadioGroup) view.findViewById(R.id.pinYesNo);

        contactBtn = (Button) view.findViewById(R.id.contactBtn);
        generatePin = (Button) view.findViewById(R.id.generatePin);
        verifyPINBtn = (Button) view.findViewById(R.id.verifyPINBtn);
        emailTv = (TextView) view.findViewById(R.id.emailTv);
        phoneTv = (TextView) view.findViewById(R.id.phoneTv);
        pinEt = (EditText) view.findViewById(R.id.pinEt);
        llVerifyPIN = (LinearLayout) view.findViewById(R.id.llVerifyPIN);
        radioBtn = (LinearLayout) view.findViewById(R.id.radioBtn);

        emailTv.setText("Email ID: "+email);
        phoneTv.setText("Phone: "+contact);

        pinYesNo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioYes.isChecked()) {

                    //Toast.makeText(getContext(), "Yes", Toast.LENGTH_SHORT).show();
                    contactBtn.setVisibility(View.GONE);
                    generatePin.setVisibility(View.VISIBLE);


                } else if(radioNo.isChecked()) {

                    //Toast.makeText(getContext(), "No", Toast.LENGTH_SHORT).show();
                    generatePin.setVisibility(View.GONE);
                    contactBtn.setVisibility(View.VISIBLE);

                }
            }
        });

        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new ContactUsFragment());
                fragmentTransaction.commit();

            }
        });

        generatePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Random rand = new Random();
                @SuppressLint("DefaultLocale") String token = String.format("%04d", rand.nextInt(10000));
                //Toast.makeText(getContext(), "PIN: "+id, Toast.LENGTH_SHORT).show();

                compareToken = token;
                generatePIN(id, token);

                /*contactBtn.setVisibility(View.GONE);
                generatePin.setVisibility(View.GONE);
                radioBtn.setVisibility(View.GONE);
                llVerifyPIN.setVisibility(View.VISIBLE);*/

            }
        });

        verifyPINBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //compareToken
                String PIN = pinEt.getText().toString();
                int getPIN = Integer.parseInt(pinEt.getText().toString());

                if (PIN.length() == 4){

                    if (compareToken.equals(PIN)){
                        pinEt.setText("");
                        pinEt.setFocusable(false);
                        Toast.makeText(getContext(), "Verified, Please Create Username & Password", Toast.LENGTH_LONG).show();

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Fragment myFragment = new CreateUsernamePasswordFragment();
                        final Bundle bundle = new Bundle();
                        myFragment.setArguments(bundle);
                        bundle.putString("id", id);
                        fragmentTransaction.replace(R.id.frame, myFragment).commit();

                    }else{
                        Toast.makeText(getContext(), "Invalid, Failed to verify PIN", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(getContext(), "Please enter 4 Digit PIN", Toast.LENGTH_LONG).show();
                }

            }
        });

        return view;
    }


    public void generatePIN(String member_id, String token){

        pd = ProgressDialog.show(getContext(), "", "Please wait, submitting...", true);

        StringRequest request = new StringRequest(Request.Method.POST, PIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Generate PIN Response ", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    String status = jObj.getString("status");
                    String validate = jObj.getString("validate");
                    String message = jObj.getString("message");

                    pd.dismiss();
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                    if (status.equalsIgnoreCase("true") && validate.equalsIgnoreCase("true")){
                        contactBtn.setVisibility(View.GONE);
                        generatePin.setVisibility(View.GONE);
                        radioBtn.setVisibility(View.GONE);
                        llVerifyPIN.setVisibility(View.VISIBLE);
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
                params.put("token", token);
                return params;
            }
        };
        queue.add(request);
    }


}