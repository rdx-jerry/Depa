package com.animator.navigation.ui.login;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.format.DateFormat;
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
import com.animator.navigation.MainActivity;
import com.animator.navigation.R;
import com.animator.navigation.ui.model.MasterSurnameModel;

import com.animator.navigation.ui.tools.BaseURL;
import com.animator.navigation.ui.tools.Utils;
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

public class Login1Fragment extends Fragment implements Spinner.OnItemSelectedListener {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    private String LOGIN = BaseURL.getBaseUrl()+"Api/member/getaltlogin";

//    private TextView birthDateTv;
    private DatePickerDialog picker;
    TextInputLayout inputUser, inputSecond,  inputDob; //inputSur,
    EditText name, secondName;
    TextView birthDateTv;

    String nameStr, secondNameStr, surNameStr, dobStr, dt;
    Button altLogin;

    private JSONArray result;
    ArrayList<String> surName;
    private Spinner surnameSp;
    ArrayList<MasterSurnameModel> surNameId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login1, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.login);
        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("project", MODE_PRIVATE);

        surName = new ArrayList<String>();
        surNameId = new ArrayList<MasterSurnameModel>();
        surnameSp = (Spinner)view.findViewById(R.id.surnameSp);
        name = (EditText)view.findViewById(R.id.userNameEt);
        secondName = (EditText)view.findViewById(R.id.secondEt);
        //surName = (EditText)view.findViewById(R.id.surnameEt);
        birthDateTv = (TextView) view.findViewById(R.id.birthDateEt);
        inputUser = (TextInputLayout)view.findViewById(R.id.inputUser);
        inputSecond = (TextInputLayout)view.findViewById(R.id.inputSecond);
        //inputSur = (TextInputLayout)view.findViewById(R.id.inputSurname);
        inputDob = (TextInputLayout)view.findViewById(R.id.inputDob);
        altLogin = (Button)view.findViewById(R.id.loginBt);

        getSurnameData();

        surnameSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                MasterSurnameModel masterSurnameModel = surNameId.get(position);
                String sid = masterSurnameModel.getId();
                String stitle = masterSurnameModel.getSurname();

                surNameStr = sid;

                //Toast.makeText(getContext(), stitle+" : "+sid, Toast.LENGTH_SHORT).show();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        birthDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                final int day = cldr.get(Calendar.DAY_OF_MONTH);
                final int month = cldr.get(Calendar.MONTH);
                final int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                birthDateTv.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        altLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name.getText().toString().trim().isEmpty()) {
                    inputUser.setError("Enter User Name");
                    requestFocus(name);
                } else {
                    inputUser.setErrorEnabled(false);
                    nameStr = name.getText().toString().trim();
                }

                if (secondName.getText().toString().trim().isEmpty()) {
                    inputSecond.setError("Enter Second Name");
                    requestFocus(secondName);
                } else {
                    inputSecond.setErrorEnabled(false);
                    secondNameStr = secondName.getText().toString().trim();
                }

                /*if (surName.getText().toString().trim().isEmpty()) {
                    inputSur.setError("Enter Surname");
                    requestFocus(surName);
                } else {
                    inputSur.setErrorEnabled(false);
                    surNameStr=surName.getText().toString();
                }*/

                if (birthDateTv.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please add Date Of Birth", Toast.LENGTH_SHORT).show();
                }else {
                    dobStr = birthDateTv.getText().toString().trim();
                    dt = Utils.convertDateUrdu(dobStr);
                }

                if (!name.getText().toString().trim().isEmpty() && !secondName.getText().toString().trim().isEmpty() && !birthDateTv.getText().toString().trim().isEmpty()){
                    login(nameStr, secondNameStr, surNameStr, dt);
                }else{
                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }

    private void login(final String firstName, final String secondName, final String sirName, final String dob) {
        StringRequest request = new StringRequest(Request.Method.POST, LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("PIN Alt Response : ", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("status");

                    if (jObj.getString("status").equals("true")) {

                        Toast.makeText(getContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();

                        Log.e("PIN Data : ", jObj.getString("data"));
                        JSONObject data = new JSONObject(jObj.getString("data"));
                        String id = data.getString("id");
                        String contact = data.getString("contact");
                        String email = data.getString("email");
                        String status = data.getString("status");
                        Log.e("Print DATA : ", id+contact+email+status);

                        if (status.equalsIgnoreCase("true")){
                            //Member With Username & Password Found - Go to admin OR reset password

                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frame, new YesPasswordGoAdminFragment());
                            fragmentTransaction.commit();

                        }else{
                            // No Member With Username & Password Found - Generate PIN and Set Username & Password

                            /*FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frame, new NoPasswordGeneratePINFragment());
                            fragmentTransaction.commit();*/

                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            Fragment myFragment = new NoPasswordGeneratePINFragment();
                            final Bundle bundle = new Bundle();
                            myFragment.setArguments(bundle);
                            bundle.putString("id", id);
                            bundle.putString("contact", contact);
                            bundle.putString("email", email);
                            bundle.putString("status", status);
                            fragmentTransaction.replace(R.id.frame, myFragment).commit();

                        }

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
                surName.add(jSon.getString("surname"));
                surNameId.add(masterSurnameModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        surnameSp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner, surName));
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
