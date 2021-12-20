package com.animator.navigation.ui.register;

import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.directory.Telephone;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class RegisterFragment extends Fragment {

    SharedPreferences share;
    SharedPreferences.Editor editor;
    RequestQueue queue;

    DatePickerDialog picker;
    EditText nameEt, emailEt, mobileEt, addressEt, birthDateEt;
    TextView ageTv;
    Spinner stateSp;
    RadioGroup gender;
    RadioButton radioSexButton;
    String nameStr, emailStr, mobileStr, addressStr, birthDateStr, ageStr, genderStr;
    Button regBt;

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("project", MODE_PRIVATE);

        nameEt = (EditText)view.findViewById(R.id.nameEt);
        emailEt = (EditText)view.findViewById(R.id.emailEt);
        mobileEt = (EditText)view.findViewById(R.id.mobileEt);
        addressEt = (EditText)view.findViewById(R.id.addressEt);
        birthDateEt = (EditText)view.findViewById(R.id.birthDateEt);
        ageTv = (TextView) view.findViewById(R.id.ageTv);
        gender = (RadioGroup)view.findViewById(R.id.radioSex);
        stateSp = (Spinner)view.findViewById(R.id.stateSp);

//        String[] state = getResources().getStringArray(R.array.state);
//        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner, state) {
//            @Override
//            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                View view = super.getDropDownView(position, convertView, parent);
//                TextView tv = (TextView)view;
//                tv.setTextColor(Color.WHITE);
//                tv.setBackgroundColor(Color.rgb(91,90,92));
//                return view;
//            }
//        };
//        stateSp.setAdapter(stateAdapter);

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);

        String[] thisDate1 = thisDate.split("/");
        final int year1 = Integer.parseInt(thisDate1[2]);

        birthDateEt.setOnClickListener(new View.OnClickListener() {
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
                                birthDateEt.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                int yr = year - year1;
                                ageTv.setText("Age: " + yr);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        regBt = (Button)view.findViewById(R.id.regBt);

        regBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                nameStr = nameEt.getText().toString().trim();
//                emailStr = emailEt.getText().toString().trim();
//                mobileStr = mobileEt.getText().toString().trim();
//                addressStr = addressEt.getText().toString().trim();
//                birthDateStr = birthDateEt.getText().toString().trim();
//                ageStr = ageTv.getText().toString().trim();
//
//                int selectedId = gender.getCheckedRadioButtonId();
//                radioSexButton = (RadioButton)view.findViewById(selectedId);
//
//                genderStr = radioSexButton.getText().toString().trim();

//                register(nameStr, emailStr, mobileStr, addressStr, birthDateStr, ageStr, genderStr);
            }
        });
        return view;
    }

//    private void register(final String nameStr, final String emailStr, final String mobileStr, final String addressStr,
//    final String birthDateStr, final String ageStr, final String genderStr) {
//        StringRequest request = new StringRequest(Request.Method.POST, "URL", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.e("response", response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("name", nameStr);
//                params.put("email", emailStr);
//                params.put("mobile", mobileStr);
//                params.put("address", addressStr);
//                params.put("address", birthDateStr);
//                params.put("address", ageStr);
//                params.put("address", genderStr
//            }
//        };
//        queue.add(request);
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
