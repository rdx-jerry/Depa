 package com.animator.navigation.ui.jobs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.blogs.BlogsFragment;
import com.animator.navigation.ui.tools.BaseURL;
import com.animator.navigation.ui.tools.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class JobDetailsFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    String id;
    TextView titleTv, designationTv, contactPersonTv, contactTv, experianceTv, emailTv, dateTv, openingTv, salaryTv, descriptionTv, uploadByTv;
//    ImageView detailIv;
    String titleStr;
//    String imgPath;
    ImageButton shareTitle;
    String location;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_details, container, false);

        queue= Volley.newRequestQueue(getActivity());
        share=this.getActivity().getSharedPreferences("project",MODE_PRIVATE);

        shareTitle = (ImageButton) view.findViewById(R.id.share);
        titleTv = (TextView)view.findViewById(R.id.titleDetail);
        designationTv = (TextView)view.findViewById(R.id.designation);
        contactPersonTv = (TextView)view.findViewById(R.id.conPerson);
        contactTv = (TextView)view.findViewById(R.id.contact);
        experianceTv = (TextView)view.findViewById(R.id.experiance);
        emailTv = (TextView)view.findViewById(R.id.email);
        dateTv = (TextView)view.findViewById(R.id.startDate);
        openingTv = (TextView)view.findViewById(R.id.opening);
        salaryTv = (TextView)view.findViewById(R.id.salary);
        uploadByTv = (TextView)view.findViewById(R.id.uploadBy);
        descriptionTv = (TextView)view.findViewById(R.id.description);
//        detailIv = (ImageView) view.findViewById(R.id.detailIv);

        id = getArguments().getString("id");
        getDetails();

        shareTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Shree Depa Jain Mahajan Trust");
                    String shareMessage= "\nShree Depa Jain Mahajan Trust \n\n";

                    shareMessage = shareMessage + "Company: " + titleTv.getText().toString()  +"\n\n"; //Title
                    shareMessage = shareMessage + "Designation: " + designationTv.getText().toString()  +"\n\n"; //Designation
                    shareMessage = shareMessage + "Contact Person: " + contactPersonTv.getText().toString()  +"\n\n"; //Contact Person
                    shareMessage = shareMessage + "Contact No.: " + contactTv.getText().toString()  +"\n\n"; //Contact
                    shareMessage = shareMessage + "Email: " + emailTv.getText().toString()  +"\n\n"; //Email
                    shareMessage = shareMessage + "Experience" + experianceTv.getText().toString()  +"\n\n"; //Experience
                    shareMessage = shareMessage + "Date: " + dateTv.getText().toString()  +"\n\n"; //Start Date
                    shareMessage = shareMessage + "Vacancy: " + openingTv.getText().toString()  +"\n\n"; //Opening
                    shareMessage = shareMessage + "Salary Range: " + salaryTv.getText().toString()  +"\n\n"; //Salary
                    shareMessage = shareMessage + "Location: " + location +"\n\n";
                    shareMessage = shareMessage + "Description: " + descriptionTv.getText().toString() + "\n\n"; //Description

                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });
        return view;
    }

    public void getDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/Job/getjobbyid/"+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jArray = jObj.getJSONArray("data");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject object = jArray.getJSONObject(i);
                        String title = object.getString("firm_name").trim();
                        String designation = object.getString("designation").trim();
                        String contactPerson = object.getString("contact_person").trim();
                        String contact = object.getString("contact_number").trim();
                        String experiance = object.getString("experience").trim();
                        String email = object.getString("contact_email").trim();
                        String dateRange =  Utils.convertDate(object.getString("start_date").trim()) + " to " + Utils.convertDate(object.getString("end_date").trim());
                        String opening = object.getString("openings").trim();
                        String salary = object.getString("salary_range_start").trim() + " to " +object.getString("salary_range_end").trim();
                        String uploadBy = object.getString("uploaded_by").trim();
                        String description = object.getString("description");
                        location = object.getString("location").trim();
                        titleTv.setText(title);
                        designationTv.setText(designation);
                        contactPersonTv.setText(contactPerson);

                        /*contactTv.setText(contact);
                        emailTv.setText(email);*/

                        contactTv.setText(Html.fromHtml("<a href='"+contact+"'>"+contact+"</a>"));
                        emailTv.setText(Html.fromHtml("<a href='"+email+"'>"+email+"</a>"));

                        experianceTv.setText(experiance);
                        dateTv.setText(dateRange);
                        openingTv.setText(opening);
                        salaryTv.setText(salary);
                        uploadByTv.setText(uploadBy);

                        descriptionTv.setText(description);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley error: ", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("id", getid);
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
                    Fragment myFragment = new JobsFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();

                    return true;
                }
                return false;
            }
        });
    }
}
