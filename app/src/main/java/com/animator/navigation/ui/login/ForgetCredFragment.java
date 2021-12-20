package com.animator.navigation.ui.login;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.animator.navigation.ui.home.HomeFragment;
import com.animator.navigation.ui.model.MasterSurnameModel;
import com.animator.navigation.ui.profile.ProfileFragment;
import com.animator.navigation.ui.tools.BaseURL;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class ForgetCredFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    Random random;
    private String GETUSER = BaseURL.getBaseUrl()+"Api/member/getusername";
    private String OTP = BaseURL.getBaseUrl()+"Api/member/resetpassword";
    private String RESETPASS = BaseURL.getBaseUrl()+"Api/member/resetpassword";

    LinearLayout linerCred, linerOtp, linerPass;
    EditText name, secondName;
    TextView dob;
    String nameStr, secondNameStr, sirNameStr, dobStr, otpStr, passStr, conpassStr, memberIdStr, sid, stitle, otpResponse, member_id;
    EditText otpET1;
    EditText passET, conpassET;
    private DatePickerDialog picker;
    Button loginBt, verifyBt, resetBt;

    Spinner surnameSp;
    ArrayList<String> surNameArray;
    ArrayList<MasterSurnameModel> surNameId;
    private JSONArray result;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forget_cred, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Forget Password");
        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("project", MODE_PRIVATE);

        memberIdStr = share.getString("id", "");

        surNameArray = new ArrayList<String>();
        surNameId = new ArrayList<MasterSurnameModel>();

        linerCred = (LinearLayout)view.findViewById(R.id.linearCred);
        linerOtp = (LinearLayout)view.findViewById(R.id.linearOtp);
        linerPass = (LinearLayout)view.findViewById(R.id.linearPass);
        name = (EditText)view.findViewById(R.id.userNameEt);
        secondName = (EditText)view.findViewById(R.id.secondEt);
//        surName = (EditText)view.findViewById(R.id.surnameEt);
        dob = (TextView) view.findViewById(R.id.dobEt);
        otpET1 = (EditText)view.findViewById(R.id.otpET1);
        passET = (EditText)view.findViewById(R.id.newPassEt);
        conpassET = (EditText)view.findViewById(R.id.conPassEt);

        surnameSp = (Spinner)view.findViewById(R.id.surnameSp);
//        memberIdEt = (EditText)view.findViewById(R.id.memberid);
        loginBt = (Button) view.findViewById(R.id.loginBt);
        verifyBt = (Button) view.findViewById(R.id.verifyBt);
        resetBt = (Button) view.findViewById(R.id.resetBt);

        getSurnameData();

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                dob.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        surnameSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                MasterSurnameModel masterSurnameModel = surNameId.get(position);

                sid = masterSurnameModel.getId();
                stitle = masterSurnameModel.getSurname();

//                Toast.makeText(getContext(), stitle+" : "+sid, Toast.LENGTH_SHORT).show();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().trim().isEmpty()) {
                    requestFocus(name);
                } else {
                    nameStr = name.getText().toString().trim();
                }

                if (secondName.getText().toString().trim().isEmpty()) {
                    requestFocus(secondName);
                } else {
                    secondNameStr = secondName.getText().toString().trim();
                }

                sirNameStr = sid;

                dobStr = dob.getText().toString().trim();

                sendCred(nameStr, secondNameStr, sirNameStr, dobStr);
            }
        });

        verifyBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otpStr = otpET1.getText().toString().trim();
//                verify(otpStr);
                if (otpStr.equals(otpResponse)) {
                    linerOtp.setVisibility(View.GONE);
                    linerPass.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getContext(), "Otp Not Match! ", Toast.LENGTH_LONG).show();
                }
            }
        });

        resetBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passStr = passET.getText().toString().trim();
                conpassStr = conpassET.getText().toString().trim();

                if (passStr.equals(conpassStr)) {
                    resetPass(passStr, conpassStr, member_id);
                } else {
                    Toast.makeText(getContext(), "Please Enter Valid Password", Toast.LENGTH_LONG).show();
                }

            }
        });
        return view;
    }

    private void sendCred(final String firstName, final String secondName, final String sirName, final String dob) {
        StringRequest request = new StringRequest(Request.Method.POST, GETUSER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);
                try {
                    JSONObject jObj = new JSONObject(response);

                    if (jObj.getString("status") == "true") {
                        otpResponse = jObj.getString("code");
                        member_id = jObj.getString("member_id");
                        Toast.makeText(getContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        linerCred.setVisibility(View.GONE);
//                        random = new Random();
//                        String generatedPassword = String.format("%04d", random.nextInt(10000));
                        linerOtp.setVisibility(View.VISIBLE);
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
                Log.e("VolleyError", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("first_name", firstName);
                params.put("second_name", secondName);
                params.put("surname_id", sirName);
                params.put("dob", dob);
                Log.e("send cred params", params.toString());
                return params;
            }
        };
        queue.add(request);
    }

    private void verify(final String otp) {
        StringRequest request = new StringRequest(Request.Method.POST, OTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);
//                try {
//                    JSONObject jObj = new JSONObject(response);

//                    if (jObj.getString("status") == "true") {
//                        Toast.makeText(getContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        linerOtp.setVisibility(View.GONE);
                        linerPass.setVisibility(View.VISIBLE);
//                    } else {
//                        Toast.makeText(getContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("otp", otp);
                return params;
            }
        };
        queue.add(request);
    }

    private void resetPass(final String reset_password, final String reset_repassword, final String member_id) {
        StringRequest request = new StringRequest(Request.Method.POST, RESETPASS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);
                try {
                    JSONObject jObj = new JSONObject(response);

                    if (jObj.getString("status") == "true") {
                        Toast.makeText(getContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
//                        linerOtp.setVisibility(View.GONE);
//                        linerPass.setVisibility(View.VISIBLE);
                        AppCompatActivity activity = (AppCompatActivity) getContext();
                        Fragment myFragment = new LoginFragment();
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
                Log.e("VolleyError", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("reset_password", reset_password);
                params.put("reset_repassword", reset_repassword);
                params.put("member_id", member_id);
                Log.e("reset  passwod", params.toString());
                return params;
            }
        };
        queue.add(request);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void getSurnameData() {
        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/masters/surname",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray("data");
                            getSurname(result);
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

    private void getSurname(JSONArray j) {

        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject jSon = j.getJSONObject(i);
                MasterSurnameModel masterSurnameModel = new MasterSurnameModel();
                masterSurnameModel.setSurname(jSon.getString("surname"));
                masterSurnameModel.setId(jSon.getString("id"));
                surNameArray.add(jSon.getString("surname"));
                surNameId.add(masterSurnameModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        surnameSp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, surNameArray));
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
