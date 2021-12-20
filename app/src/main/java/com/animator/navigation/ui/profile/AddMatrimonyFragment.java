package com.animator.navigation.ui.profile;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.model.MasterSurnameModel;
import com.animator.navigation.ui.tools.BaseURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class AddMatrimonyFragment extends Fragment implements Spinner.OnItemSelectedListener {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;
    String family_id;

    private JSONArray jsonArrayResult;
    ArrayList<String> familyNameArray;
    private Spinner familySpinner;
    ArrayList<Family> familyArrayList;
    ProgressDialog pd;
    String member_no;

    EditText education, occupation, mother_name, age, height, weight, place, contact, village;
    TextView birth_time;
    String get_education, get_occupation, get_mother_name, get_age, get_height, get_weight, get_birth_time, get_place, get_contact, get_village;
    Button submit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_matrimony, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Sagpan Setu");
        queue = Volley.newRequestQueue(getContext());
        share = getActivity().getSharedPreferences("project", MODE_PRIVATE);
        family_id = share.getString("family_no", "");

        familyNameArray = new ArrayList<String>();
        familyArrayList = new ArrayList<Family>();
        familySpinner = (Spinner) view.findViewById(R.id.familySpinner);

        education = (EditText) view.findViewById(R.id.education);
        occupation = (EditText) view.findViewById(R.id.occupation);
        mother_name = (EditText) view.findViewById(R.id.mother_name);
        age = (EditText) view.findViewById(R.id.age);
        height = (EditText) view.findViewById(R.id.height);
        weight = (EditText) view.findViewById(R.id.weight);
        birth_time = (TextView) view.findViewById(R.id.birth_time);
        place = (EditText) view.findViewById(R.id.place);
        contact = (EditText) view.findViewById(R.id.contact);
        village = (EditText) view.findViewById(R.id.village);
        submit = (Button) view.findViewById(R.id.mainBt);

        getFamilyMembersData();

        familySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Family family = familyArrayList.get(position);
                String sid = family.getMemberId();
                member_no = sid;
                getEduOccData(member_no);
                //Toast.makeText(getContext(), member_no, Toast.LENGTH_SHORT).show();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        birth_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                int day = mcurrentTime.get(Calendar.AM_PM);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        birth_time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                get_mother_name = mother_name.getText().toString().trim();
                get_age = age.getText().toString().trim();
                get_height = height.getText().toString().trim();
                get_weight = weight.getText().toString().trim();
                get_birth_time = birth_time.getText().toString().trim();
                get_place = place.getText().toString().trim();
                get_contact = contact.getText().toString().trim();
                get_village = village.getText().toString().trim();

                if (!get_mother_name.isEmpty() && !get_age.isEmpty()
                        && !get_height.isEmpty() && !get_weight.isEmpty() && !get_birth_time.isEmpty() && !get_place.isEmpty()
                        && !get_contact.isEmpty() && !get_village.isEmpty()){

                    createMatrimony(get_mother_name, get_age, get_height, get_weight, get_birth_time, get_place, get_contact, get_village);

                }else{
                    Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_LONG).show();
                }


            }
        });

        return view;
    }

    private void getFamilyMembersData() {

        pd = ProgressDialog.show(getContext(), "Family Members", "Please wait, loading...", true);

        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/family/api_getmembers/"+family_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            jsonArrayResult = j.getJSONArray("data");
                            getFamily(jsonArrayResult);
                        } catch (JSONException e) {
                            pd.dismiss();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
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


    private void getFamily(JSONArray j) {

        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject jSon = j.getJSONObject(i);
                Family family = new Family();
                family.setId(jSon.getString(("id")));
                family.setMemberName(jSon.getString("fullname"));
                family.setMemberId(jSon.getString("member_no"));
                familyNameArray.add(jSon.getString("fullname"));
                familyArrayList.add(family);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        familySpinner.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner, familyNameArray));

        pd.dismiss();

    }


    private void getEduOccData(String member_no) {

        pd = ProgressDialog.show(getContext(), "Education & Occupation", "Please wait, loading...", true);

        StringRequest request = new StringRequest(Request.Method.POST, BaseURL.getBaseUrl()+"Api/member/api_getmatrimony",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pd.dismiss();

                        try {

                            JSONObject obj = new JSONObject(response);
                            String rstatus = obj.optString("status");
                            String rvalidate = obj.optString("validate");
                            String rmessage = obj.optString("message");
                            String reducation = obj.optString("education");
                            String roccupation = obj.optString("occupation");

                            if (rstatus.equalsIgnoreCase("true") && rvalidate.equalsIgnoreCase("true")){
                                occupation.setText(roccupation);
                                education.setText(reducation);
                            }else{
                                occupation.setText("No Occupation Found");
                                education.setText("No Education Found");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", member_no);
                return params;
            }
        };
        queue.add(request);
    }


    private void createMatrimony(String up_mother_name, String up_age, String up_height, String up_weight,
                                 String up_birth_time, String up_place, String up_contact, String up_village) {

        pd = ProgressDialog.show(getContext(), "Add Matrimony", "Please wait, creating...", true);

        StringRequest request = new StringRequest(Request.Method.POST, BaseURL.getBaseUrl()+"Api/matrimonial/android",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pd.dismiss();

                        try {

                            JSONObject obj = new JSONObject(response);
                            String rstatus = obj.optString("status");
                            String rvalidate = obj.optString("validate");
                            String rmessage = obj.optString("message");

                            if (rstatus.equalsIgnoreCase("true") && rvalidate.equalsIgnoreCase("true")){
                                Toast.makeText(getContext(), rmessage,  Toast.LENGTH_LONG).show();

                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.frame, new MatrimonyMemberListFragment());
                                fragmentTransaction.commit();

                            }else{
                                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mother_name", up_mother_name);
                params.put("age", up_age);
                params.put("height", up_height);
                params.put("weight", up_weight);
                params.put("birth_time", up_birth_time);
                params.put("place", up_place);
                params.put("contact", up_contact);
                params.put("maternal", up_village);
                params.put("member_no", member_no);
                params.put("client", "app");
                params.put("id", "0");

                params.put("family_no", family_id);
                params.put("rashi", "");
                params.put("kundali", "");
                params.put("dharmik", "");

                return params;
            }
        };
        queue.add(request);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {}

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

}