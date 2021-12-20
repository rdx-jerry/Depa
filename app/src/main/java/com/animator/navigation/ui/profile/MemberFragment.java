package com.animator.navigation.ui.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.animator.navigation.ui.login.LoginFragment;
import com.animator.navigation.ui.tools.BaseURL;
import com.animator.navigation.ui.tools.Utils;
import com.squareup.picasso.Picasso;
import com.zolad.zoominimageview.ZoomInImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class MemberFragment extends Fragment {

    private SharedPreferences share;
    private RequestQueue queue;
    String MEMBER = BaseURL.getBaseUrl()+"Api/Member/detail/";
    ImageView profileIv;
    TextView member_noTv, family_noTv, fullnameTv, sexTv, dobTv, contactTv, emailTv, live_typeTv, member_typeTv, address_1Tv, address_2Tv,
            surnameTv, bloodgroupTv, relationTv, maritalstatusTv, educationTv, occupationTv,
            mNameTv, mMiddleTv, mSurnameTv, mVillageTv, mContactTv, mEmailTv, mAddressTv, achivementsTv, marriageDateTv,
            bCompanyTv, bPositionTv, bEmailTv, bContactTv, bWebsiteTv, bAddressTv, altCon, dharmik, sports;
    private ZoomInImageView imgiv;
    private LinearLayout mainLL, imgLL, bottomLL, marriedLL, businessLL;

    private String img, family_no, fullname, sex, dob, contact, email, live_type, member_type, address_1, address_2,
            surname, bloodgroup, relation, maritalstatus, education, occupation, achivements, marriageDate;

    private int linearlayout = 0;
    String id, memberid;

    ImageView phoneCall, whatsApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_member, container, false);
        queue = Volley.newRequestQueue(getContext());
        share = getActivity().getSharedPreferences("project", MODE_PRIVATE);
        id = share.getString("id", "");


        memberid = getArguments().getString("memberId");
        Log.e("member id", memberid);

        phoneCall = (ImageView) view.findViewById(R.id.phoneCall);
        whatsApp = (ImageView) view.findViewById(R.id.whatsApp);

        member_noTv = (TextView)view.findViewById(R.id.member_no);
        family_noTv = (TextView)view.findViewById(R.id.family_no);
        fullnameTv = (TextView)view.findViewById(R.id.fullname);
        sexTv = (TextView)view.findViewById(R.id.sex);
        dobTv = (TextView)view.findViewById(R.id.dob);
        contactTv = (TextView)view.findViewById(R.id.contact);
        emailTv = (TextView)view.findViewById(R.id.email);
        live_typeTv = (TextView)view.findViewById(R.id.live_type);
        member_typeTv = (TextView)view.findViewById(R.id.member_type);
        address_1Tv = (TextView)view.findViewById(R.id.address_1);
        address_2Tv = (TextView)view.findViewById(R.id.address_2);
        surnameTv = (TextView)view.findViewById(R.id.surname);
        bloodgroupTv = (TextView)view.findViewById(R.id.bloodgroup);
        relationTv = (TextView)view.findViewById(R.id.relation);
        maritalstatusTv = (TextView)view.findViewById(R.id.maritalstatus);
        marriageDateTv = (TextView)view.findViewById(R.id.maritaldate);
        achivementsTv = (TextView)view.findViewById(R.id.achivements);
        educationTv = (TextView)view.findViewById(R.id.education);
        occupationTv = (TextView)view.findViewById(R.id.occupation);

        altCon = (TextView)view.findViewById(R.id.altCon);
        dharmik = (TextView)view.findViewById(R.id.dharmik);
        sports = (TextView)view.findViewById(R.id.sports);
        profileIv = (ImageView) view.findViewById(R.id.profileIv);
        mainLL = (LinearLayout) view.findViewById(R.id.mainLL);
        imgLL = (LinearLayout) view.findViewById(R.id.imgLL);
        imgiv = (ZoomInImageView)view.findViewById(R.id.imgIv);
        marriedLL = (LinearLayout)view.findViewById(R.id.marriedLL);
        businessLL = (LinearLayout)view.findViewById(R.id.businessLL);
        bottomLL = (LinearLayout) view.findViewById(R.id.bottomLL);

        mNameTv = (TextView)view.findViewById(R.id.fatherMName);
        mMiddleTv = (TextView)view.findViewById(R.id.middleMName);
        mSurnameTv = (TextView)view.findViewById(R.id.mSurname);
        mVillageTv = (TextView)view.findViewById(R.id.mVillage);
        mContactTv = (TextView)view.findViewById(R.id.mContact);
        mEmailTv = (TextView)view.findViewById(R.id.mEmail);
        mAddressTv = (TextView)view.findViewById(R.id.mAddress);
        bCompanyTv = (TextView)view.findViewById(R.id.bCompany);
        bPositionTv = (TextView)view.findViewById(R.id.bPosition);
        bEmailTv = (TextView)view.findViewById(R.id.bEmail);
        bContactTv = (TextView)view.findViewById(R.id.bContact);
        bWebsiteTv = (TextView)view.findViewById(R.id.bWebsite);
        bAddressTv = (TextView)view.findViewById(R.id.bAddress);



        phoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getNumber = contactTv.getText().toString();
                if (!getNumber.equals("")){
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getNumber));
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext(), "Phone Number Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        whatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getNumber = contactTv.getText().toString();
                if (!getNumber.equals("")){

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send/?phone=91"+getNumber+"&text=Hey+I+have+a+query"));
                    startActivity(browserIntent);

                }else{
                    Toast.makeText(getContext(), "Number Not Available For WhatsApp", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if (id != share.getString("id", "")) {
            //Toast.makeText(getContext(), "ProfileFragment (1): "+id, Toast.LENGTH_LONG).show();
            //getDetails();

            AppCompatActivity activity = (AppCompatActivity)getContext();
            Fragment myFragment = new LoginFragment();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();

        } else {
            getDetails();

            //Toast.makeText(getContext(), "ProfileFragment (2): "+id, Toast.LENGTH_LONG).show();

            /*Toast.makeText(getContext(), "ProfileFragment (2): "+id, Toast.LENGTH_LONG).show();
            AppCompatActivity activity = (AppCompatActivity)getContext();
            Fragment myFragment = new LoginFragment();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();*/
        }
        //getDetails();

        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainLL.setVisibility(View.INVISIBLE);
                bottomLL.setVisibility(View.INVISIBLE);
                imgLL.setVisibility(View.VISIBLE);
                imgiv.setImageDrawable(profileIv.getDrawable());
                linearlayout = 1;
            }
        });

        return view;
    }

    public void getDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,MEMBER + memberid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jArray = jObj.getJSONArray("data");

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject object = jArray.getJSONObject(i);
                        JSONArray memArray = object.getJSONArray("member");
                        for (int m = 0; m < memArray.length(); m++) {
                            JSONObject memObj = memArray.getJSONObject(m);
                            img = memObj.getString("image").trim();
                            Picasso.with(getContext()).load("https://panjodepa.com/depa/uploads/members/" + img).into(profileIv);

                            member_noTv.setText(memObj.getString("member_no").trim());
                            family_noTv.setText(memObj.getString("family_no").trim());
                            //fullnameTv.setText(memObj.getString("fullname").trim() + " " + memObj.getString("surname").trim());
                            fullnameTv.setText(memObj.getString("fullname").trim());
                            sexTv.setText(memObj.getString("sex").trim());
                            dobTv.setText(Utils.convertDate(memObj.getString("dob").trim()));
                            contactTv.setText(memObj.getString("contact").trim());
                            emailTv.setText(memObj.getString("email").trim());
                            live_typeTv.setText(memObj.getString("live_type").trim());
                            member_typeTv.setText(memObj.getString("member_type").trim());
                            address_1Tv.setText(memObj.getString("address_1").trim() + " " + memObj.getString("address_2").trim() + " " + memObj.getString("area_name").trim() + " " + memObj.getString("pincode").trim());
//                            address_2Tv.setText(address_2);
                            surnameTv.setText(memObj.getString("surname").trim());
                            bloodgroupTv.setText(memObj.getString("bloodgroup").trim());
                            relationTv.setText(memObj.getString("relation").trim());
                            maritalstatusTv.setText(memObj.getString("maritalstatus").trim());
                            marriageDateTv.setText(memObj.getString("dom").trim());
                            educationTv.setText(memObj.getString("education").trim());
                            occupationTv.setText(memObj.getString("occupation").trim());
                            achivementsTv.setText(memObj.getString("achivements").trim());

                            altCon.setText(memObj.getString("altcontact").trim());
                            dharmik.setText(memObj.getString("dharmikknowledge").trim());
                            sports.setText(memObj.getString("sports").trim());

                            sex = memObj.getString("sex").trim();
                            maritalstatus = memObj.getString("maritalstatus").trim();
                            Log.e("married", sex + " " + maritalstatus);

                            if (sex.equals("F") && maritalstatus.equals("Married")) {
                                marriedLL.setVisibility(View.VISIBLE);
                            } else {
                                marriedLL.setVisibility(View.GONE);
                            }

                            occupation = memObj.getString("occupation").trim();
                            Log.e("occupation", occupation);
                            if (!occupation.equals("Student") && !occupation.equals("House-wife") && occupation.equals("")) {
                                businessLL.setVisibility(View.GONE);
                            } else {
                                businessLL.setVisibility(View.VISIBLE);
                            }


                            mNameTv.setText(memObj.getString("m_firsts_name").trim());
                            mMiddleTv.setText(memObj.getString("m_second_name").trim() + memObj.getString("m_third_name").trim());
                            mSurnameTv.setText(memObj.getString("m_surname").trim());
                            mVillageTv.setText(memObj.getString("m_village").trim());
                            mContactTv.setText(memObj.getString("md_contact").trim());
                            mEmailTv.setText(memObj.getString("f_email").trim());
                            mAddressTv.setText(memObj.getString("m_address").trim());


                        }

                        JSONArray businessArray = object.getJSONArray("business");
                        for (int b = 0; b < businessArray.length(); b++) {
                            JSONObject businessObj = businessArray.getJSONObject(b);
                            bCompanyTv.setText(businessObj.getString("company_name").trim());
                            bPositionTv.setText(businessObj.getString("designation").trim());
                            bEmailTv.setText(businessObj.getString("email").trim());
                            bContactTv.setText(businessObj.getString("contact").trim());
                            bWebsiteTv.setText(businessObj.getString("website").trim());
                            bAddressTv.setText(businessObj.getString("address_1").trim() + " " + businessObj.getString("address_2").trim() + " " + businessObj.getString("area").trim() + " " + businessObj.getString("pincode").trim());

                        }
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

                if (linearlayout == 1) {
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        Fragment myFragment = new ProfileFragment();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();

                        return true;
                    }
                }
                return false;
            }
        });
    }
}