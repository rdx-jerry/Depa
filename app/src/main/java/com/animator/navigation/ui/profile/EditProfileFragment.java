package com.animator.navigation.ui.profile;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.VolleyMultipartRequest;
import com.animator.navigation.ui.directory.family.Family;
import com.animator.navigation.ui.model.MasterSurnameModel;
import com.animator.navigation.ui.tools.BaseURL;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class EditProfileFragment extends Fragment implements Spinner.OnItemSelectedListener {

    SharedPreferences share;
    SharedPreferences.Editor editor;
    RequestQueue queue;

    String ADDPROFILE = BaseURL.getBaseUrl()+"Api/member/android";
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    String pathUrl;
    private int serverResponseCode = 0;
    private ProgressDialog dialog = null;
    private static final int STORAGE_PERMISSION_CODE = 123;

    DatePickerDialog picker, deathPicker, domPicker;
    TextView birthDateEt, deathDateEt, domEt;
    private JSONArray result;

    private ArrayList<String> bloodGroup;
    private ArrayList<String> surName;
    ArrayList<MasterSurnameModel> educationId;
    ArrayList<MasterSurnameModel> relationId;
    ArrayList<MasterSurnameModel> occupationId;
    ArrayList<MasterSurnameModel> sportsId;
    ArrayList<MasterSurnameModel> areaId;

    private ArrayList<String> relationArray;

    private ArrayList<String> occupationArray;
    private ArrayList<String> marritalArray;
    private ArrayList<String> sportsArray;
    private ArrayList<String> dharmikArray;
    private ArrayList<String> areaArray;
    private ArrayList<String> villageArray;
    private ArrayList<String> educationArray;

    ArrayList<MasterSurnameModel> surNameId;

    ArrayList<StateVO> listVOs;
    MyAdapter myAdapter;

    RadioGroup genderRg, insuranceRg, medicalRg, sanjivaniRg;
    RadioButton maleR, femaleR, insuranceRy, insuranceRn, insuranceMRy, insuranceMRn, sanjiviniNo, sanjiviniYes;

    String first_name, second_name, third_name, member_no, family_no, fullname, sex, dob, contact, emails, live_type,
            member_type, address_1, address_2, surnames, bloodgroup, relation, maritalstatus, education, occupation, sports_id,
            mSecondNameStr, mthirdNameStr, msurNameStr, mMobileStr, mEmailStr, achivements;

    EditText name, fname, gfname, mobile,  mobileAlt, email, mSecondName, mthirdName, msurName, mMobile, mEmail,
            mAddress,achievementEt, insEt, insMEt, sanijiviniEt, otherRelation, otherEdu, otherOcu, otherSp, otherSur;
    String nameStr, fatherName, grandFather, surname, gender, birthdate, mobileStr, mobileAltStr, bldGroup, area, maritalSts, insuaranceStr, insuaranceMStr, sports, achievement, personSts, deathDate,
            dharmik, m_thirdname, m_Surname, husbandName, m_address, m_contact, m_email, dom, dvillage, emailStr, photo_file,
            client, ip, memberId, familyId, memberNo, insStr, insMStr, sanjiviniStr, otherRelationStr, otherEduStr, otherOcuStr, otherSpStr, otherSurStr;

    private LinearLayout girlLL, deathLL, insLL, sanjiviniLL;

    String sid, aid, eid, rid, oid, spid;
    String stitle, atitle, etitle, rtitle, otitle, sptitle;

    private Spinner spinner, surnameSp, relationSp, educationSp, occupationSp, maritalSp, sportsSp, dharmikSp, areaSp, dvillageSp;
    private Button chooseImg, submitMain;

    TextView imgName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("project", MODE_PRIVATE);

        client = share.getString("client", "");
        ip = share.getString("IP", "");
//        memberId = share.getString("id", "");
        familyId = share.getString("family_id", "");
//        memberNo = share.getString("member_no", "");

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            memberId = bundle.getString("memberId", "");
            memberNo = bundle.getString("memberNo", "");

            Log.e("member id and no", memberId + " " + memberNo);
        }

        Toast.makeText(getContext(), memberId, Toast.LENGTH_SHORT).show();

        imgName = (TextView) view.findViewById(R.id.imgName);

        girlLL = (LinearLayout) view.findViewById(R.id.girlLL);
        deathLL = (LinearLayout) view.findViewById(R.id.deathLL);
        insLL = (LinearLayout) view.findViewById(R.id.insLL);
//        insMLL = (LinearLayout) view.findViewById(R.id.insMLL);
        sanjiviniLL = (LinearLayout) view.findViewById(R.id.sanjiviniLL);

        name = (EditText)view.findViewById(R.id.name);
        fname = (EditText)view.findViewById(R.id.fname);
        gfname = (EditText)view.findViewById(R.id.gfname);
        mobile = (EditText)view.findViewById(R.id.mobile);
        mobileAlt = (EditText)view.findViewById(R.id.mobileAlt);
        email = (EditText)view.findViewById(R.id.email);
        mSecondName = (EditText)view.findViewById(R.id.mSecondName);
        mthirdName = (EditText)view.findViewById(R.id.mthirdName);
        msurName = (EditText)view.findViewById(R.id.msurName);
        mAddress = (EditText)view.findViewById(R.id.mAddress);
        mMobile = (EditText)view.findViewById(R.id.mMobile);
        mEmail = (EditText)view.findViewById(R.id.mEmail);
        achievementEt = (EditText)view.findViewById(R.id.achievementEt);
        maleR = (RadioButton) view.findViewById(R.id.radioMale);
        femaleR = (RadioButton) view.findViewById(R.id.radioFemale);
        insuranceRy = (RadioButton) view.findViewById(R.id.radioYes);
        insuranceRn = (RadioButton) view.findViewById(R.id.radioNo);
        insEt = (EditText)view.findViewById(R.id.insEt);
        insuranceMRy = (RadioButton) view.findViewById(R.id.radioMYes);
        insuranceMRn = (RadioButton) view.findViewById(R.id.radioMNo);
        genderRg = (RadioGroup) view.findViewById(R.id.radioSex);
        insuranceRg = (RadioGroup) view.findViewById(R.id.radioPolice);
        medicalRg = (RadioGroup) view.findViewById(R.id.radioPoliceM);
        sanjivaniRg = (RadioGroup) view.findViewById(R.id.radioSanijivini);
        insMEt = (EditText)view.findViewById(R.id.insMEt);

        sanjiviniNo = (RadioButton) view.findViewById(R.id.radioSanijiviniNo);
        sanjiviniYes = (RadioButton) view.findViewById(R.id.radioSanijiviniYes);
        sanijiviniEt = (EditText)view.findViewById(R.id.sanjiviniEt);

        otherRelation = (EditText)view.findViewById(R.id.otherRelation);
        otherEdu = (EditText)view.findViewById(R.id.otherEdu);
        otherOcu = (EditText)view.findViewById(R.id.otherOcu);
        otherSp = (EditText)view.findViewById(R.id.otherSp);
        otherSur = (EditText)view.findViewById(R.id.otherSur);
//
//        gender = maleR.getText().toString();
//        insuaranceStr = "1";
//        insuaranceMStr = "1";
//        sanjiviniStr = "1";

        genderRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (maleR.isChecked()) {
                    gender = maleR.getText().toString();
                } else if (femaleR.isChecked()) {
                    gender = femaleR.getText().toString();
                }
            }
        });

        insuranceRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (insuranceRy.isChecked()) {
                    insuaranceStr = "1";
                    insLL.setVisibility(View.VISIBLE);
                } else if(insuranceRn.isChecked()) {
                    insuaranceStr = "0";
                    insLL.setVisibility(View.GONE);
                }
            }
        });

        medicalRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (insuranceMRy.isChecked()) {
                    insuaranceMStr = "1";
//                    insMLL.setVisibility(View.VISIBLE);
                    sanjiviniLL.setVisibility(View.VISIBLE);
                } else if(insuranceMRn.isChecked()) {
                    insuaranceMStr = "0";
                    sanjiviniLL.setVisibility(View.GONE);
//                    insMLL.setVisibility(View.GONE);
                }
            }
        });

        sanjivaniRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (sanjiviniYes.isChecked()) {
                    sanjiviniStr = "1";
                    sanjiviniLL.setVisibility(View.GONE);
                    sanijiviniEt.setVisibility(View.GONE);
                } else if(sanjiviniNo.isChecked()) {
                    sanjiviniStr = "0";
                    sanjiviniLL.setVisibility(View.VISIBLE);
                    sanijiviniEt.setVisibility(View.VISIBLE);
                }
            }
        });

        spinner = (Spinner) view.findViewById(R.id.bloodSp);
        surnameSp = (Spinner) view.findViewById(R.id.surnameSp);
        relationSp = (Spinner) view.findViewById(R.id.relationSp);
        educationSp = (Spinner) view.findViewById(R.id.educationSp);
        occupationSp = (Spinner) view.findViewById(R.id.occupationSp);
        maritalSp = (Spinner) view.findViewById(R.id.maritalSp);
        sportsSp = (Spinner) view.findViewById(R.id.sportsSp);
        dharmikSp = (Spinner) view.findViewById(R.id.dharmikSp);
        areaSp = (Spinner) view.findViewById(R.id.areaSp);
        dvillageSp = (Spinner) view.findViewById(R.id.villageSp);

        birthDateEt = (TextView) view.findViewById(R.id.birthDateEt);
        deathDateEt = (TextView) view.findViewById(R.id.deathDateEt);
        domEt = (TextView) view.findViewById(R.id.domEt);
        chooseImg = (Button) view.findViewById(R.id.chooseImage);
        submitMain = (Button) view.findViewById(R.id.mainBt);

        bloodGroup = new ArrayList<String>();
        surName = new ArrayList<String>();
        surNameId = new ArrayList<MasterSurnameModel>();
        areaId = new ArrayList<MasterSurnameModel>();
        educationId = new ArrayList<MasterSurnameModel>();
        relationId = new ArrayList<MasterSurnameModel>();
        occupationId = new ArrayList<MasterSurnameModel>();
        sportsId = new ArrayList<MasterSurnameModel>();
        listVOs = new ArrayList<>();
        relationArray = new ArrayList<String>();
        educationArray = new ArrayList<String>();
        occupationArray = new ArrayList<String>();
        marritalArray = new ArrayList<String>();
        sportsArray = new ArrayList<String>();
        dharmikArray = new ArrayList<String>();
        areaArray = new ArrayList<String>();
        villageArray = new ArrayList<String>();

        getData();
        getSurnameData();
        getEducationData();
        getRelationData();
        getOccupationData();
        getMarritalData();
        getSportsData();
        getDharmikData();
//        getCategoryData();
        getAreaData();
        getVillageData();

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
                                birthDateEt.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        domEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                domPicker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                domEt.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                domPicker.show();
            }
        });

        deathDateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                deathPicker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                deathDateEt.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                deathPicker.show();
            }
        });

//        if (maritalSts == "Married") {
//            girlLL.setVisibility(View.VISIBLE);
//        } else if (maritalSts == "Widow") {
//            girlLL.setVisibility(View.VISIBLE);
//        } else {
//            girlLL.setVisibility(View.GONE);
//        }

        if (personSts == "Yes") {
            deathLL.setVisibility(View.VISIBLE);
        } else if (personSts == "No") {
            deathLL.setVisibility(View.GONE);
        }

        maritalSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Married") || selectedItem.equals("Widow") && gender.equals("Female")) {
                    girlLL.setVisibility(View.VISIBLE);
                } else {
                    girlLL.setVisibility(View.GONE);
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        surnameSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                MasterSurnameModel masterSurnameModel = surNameId.get(position);
                sid = masterSurnameModel.getId();
                stitle = masterSurnameModel.getSurname();
                if (surnameSp.getSelectedItem().toString().trim().equals("Other")) {
                    otherSur.setVisibility(View.VISIBLE);
                    otherSurStr = otherSur.getText().toString();
                } else {
                    otherSur.setVisibility(View.GONE);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        relationSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MasterSurnameModel masterSurnameModel = relationId.get(position);
                rid = masterSurnameModel.getRid();
                rtitle = masterSurnameModel.getRelation();

                if (relationSp.getSelectedItem().toString().trim().equals("Other")){
                    otherRelation.setVisibility(View.VISIBLE);
                    otherRelationStr = otherRelation.getText().toString();
                } else {
                    otherRelation.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        educationSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MasterSurnameModel masterSurnameModel = educationId.get(i);
                eid = masterSurnameModel.getEid();
                etitle = masterSurnameModel.getEducation();
                if (educationSp.getSelectedItem().toString().trim().equals("Other")) {
                    otherEdu.setVisibility(View.VISIBLE);
                    otherEduStr = otherEdu.getText().toString();
                } else {
                    otherEdu.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        areaSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MasterSurnameModel masterSurnameModel = areaId.get(position);
                aid = masterSurnameModel.getOid();
                atitle = masterSurnameModel.getOccupation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        occupationSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MasterSurnameModel masterSurnameModel = occupationId.get(i);
                oid = masterSurnameModel.getOid();
                otitle = masterSurnameModel.getOccupation();
                if (occupationSp.getSelectedItem().toString().trim().equals("Other")) {
                    otherOcu.setVisibility(View.VISIBLE);
                    otherOcuStr = otherOcu.getText().toString();
                } else {
                    otherOcu.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sportsSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MasterSurnameModel masterSurnameModel = sportsId.get(i);
                spid = masterSurnameModel.getSid();
                sptitle = masterSurnameModel.getSports();
                if (sportsSp.getSelectedItem().toString().trim().equals("Other")) {
                    otherSp.setVisibility(View.VISIBLE);
                    otherSpStr = otherSp.getText().toString();
                } else {
                    otherSp.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        submitMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nameStr = name.getText().toString();
                fatherName = fname.getText().toString();
                grandFather = gfname.getText().toString();
                surname = sid;
                birthdate = birthDateEt.getText().toString();
                m_address = mAddress.getText().toString();
                mobileStr = mobile.getText().toString();
                mobileAltStr = mobileAlt.getText().toString();
                bldGroup = String.valueOf(spinner.getSelectedItemId());
                education = String.valueOf(educationSp.getSelectedItemId());
                occupation = String.valueOf(occupationSp.getSelectedItemId());
                maritalSts = String.valueOf(maritalSp.getSelectedItemId());
                sports = String.valueOf(sportsSp.getSelectedItemId());
                achievement = achievementEt.getText().toString();
                deathDate = deathDateEt.getText().toString();
                dharmik = String.valueOf(dharmikSp.getSelectedItemId());
                husbandName = mSecondName.getText().toString();
                m_thirdname = mthirdName.getText().toString();
                m_Surname = msurName.getText().toString();
                dom = domEt.getText().toString();
                area = areaSp.getSelectedItem().toString();
                dvillage = dvillageSp.getSelectedItem().toString();
                m_contact = mMobile.getText().toString();
                m_email = mEmail.getText().toString();
                relation = String.valueOf(relationSp.getSelectedItemId());
                photo_file = name.getText().toString();
                emailStr = email.getText().toString();
                insStr = insEt.getText().toString();
                insMStr = insMEt.getText().toString();
                sanjiviniStr = sanijiviniEt.getText().toString();

                try {
                    if (filePath != null) {
                        pathUrl = getPath(filePath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                dialog = ProgressDialog.show(getContext(), "Edit Profile", "Uploading file...", true);
                new Thread(new Runnable() {
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {

                            }
                        });

                        //uploadFile(pathUrl);

                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getView().getContext().getContentResolver(), filePath);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        uploadBitmap(bitmap);

                        //sendData(pathUrl, memberId, familyId, memberId, "", insuaranceStr, "", "", "", nameStr, husbandName , m_thirdname, m_Surname, dvillage, "", "in", m_contact, m_email, fatherName, grandFather, gender, surname, bldGroup, "0",maritalSts, education, occupation, birthdate, dom, "", mobileStr, emailStr, member_type, achievement, sports, "", ip, client, "Live");
                    }
                }).start();
            }
        });
        getDetails();
        return view;
    }

    private void uploadBitmap(final Bitmap bitmap) {

        //getting the tag from the edittext
        //final String tags = editTextTags.getText().toString().trim();

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ADDPROFILE,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Log.d("message", obj.getString("message"));
                            Toast.makeText(getView().getContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            if (obj.getBoolean("status")) {
                                AppCompatActivity activity = (AppCompatActivity) getContext();
                                Fragment myFragment = new ProfileFragment();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                            } else {
                                Log.d("message", obj.getString("message"));
                                Toast.makeText(getView().getContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            }
                            Log.d("response", response.data.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(getView().getContext(), error.toString(), Toast.LENGTH_LONG).show();
                        Log.d("error response", error.toString());
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("member_id", memberId);
                params.put("family_id", familyId);
                params.put("member_no", memberNo);
                params.put("ip", ip);
                params.put("client", client);

                params.put("first_name", nameStr);
                params.put("second_name", fatherName);
                params.put("third_name", grandFather);
                params.put("surname", surname);
                params.put("gender", gender);
                params.put("dob", birthdate);
                params.put("blood", bldGroup);
                params.put("relation", relation);
                params.put("marriage_type", maritalSts);
                params.put("education", education);
                params.put("occupation", occupation);
                params.put("dom", dom);
                params.put("contact", mobileStr);
                params.put("altcontact", mobileAltStr);
                params.put("email", emailStr);
                params.put("achivements", achievement);
                params.put("sports", sports);
                params.put("dharmik", dharmik);
                params.put("life_insurance", insuaranceStr);
                params.put("medical_insurance", insuaranceMStr);
                params.put("life_insurance_text", insStr);
                params.put("sanjeevni", sanjiviniStr);
                params.put("medical_insurance_text", insMStr);

                /*if married female*/
                params.put("m_secondname", husbandName);
                params.put("m_thirdname", m_thirdname);
                params.put("m_surname", m_Surname);
                params.put("m_village", dvillage);
                params.put("m_address", m_address);
                params.put("m_type", "in");
                params.put("m_contact", m_contact);
                params.put("m_email", m_email);

                params.put("live_type", live_type);
                params.put("dod", deathDate);

                params.put("relation_input", otherRelationStr);
                params.put("surname_input", otherSurStr);
                params.put("education_input", otherEduStr);
                params.put("occupation_input", otherOcuStr);
                params.put("sports_input", otherSpStr);

                params.put("old_first_name", first_name);
                params.put("old_second_name", second_name);
                params.put("old_third_name", third_name);
                params.put("old_surname", surname);
                params.put("old_gender", gender);
                params.put("old_dob", dob);
                params.put("old_blood", bloodgroup);
                params.put("old_relation", relation);
                params.put("old_marriage_type", maritalstatus);
                params.put("old_education_id", education);
                params.put("old_occupation", occupation);
                params.put("old_dom", dom);
                params.put("old_contact", contact);
                params.put("old_altcontact", mobileAltStr);
                params.put("old_email", emails);
                params.put("old_achivements", achivements);
                params.put("old_sports_id", sports);
                params.put("old_dharmik", dharmik);
                params.put("old_life_insurance", insuaranceStr);
                params.put("old_medical_insurance", insuaranceMStr);
                params.put("old_life_insurance_text", insStr);
                params.put("old_sanjeevni", sanjiviniStr);
                params.put("old_medical_insurance_text", insMStr);

                /*if married female*/
                params.put("old_m_secondname", husbandName);
                params.put("old_m_thirdname", m_thirdname);
                params.put("old_m_surname", m_Surname);
                params.put("old_m_village", dvillage);
                params.put("old_m_address", m_address);
                params.put("old_m_type", "in");
                params.put("old_m_contact", m_contact);
                params.put("old_m_email", m_email);

                params.put("old_live_type", "live_type");
                params.put("old_dod", deathDate);

                params.put("old_relation_input", otherRelationStr);
                params.put("old_surname_input", otherSurStr);
                params.put("old_education_input", otherEduStr);
                params.put("old_occupation_input", otherOcuStr);
                params.put("old_sports_input", otherSpStr);

//                params.put("member_type", member_type);

                Log.e("params",params.toString());
                return params;
            }

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("photo_file", new VolleyMultipartRequest.DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                Log.e("params",params.toString());
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(getView().getContext()).add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    //method to show file chooser
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            String fine_name = getPath(filePath);
            String img = fine_name.substring(fine_name.lastIndexOf("/") + 1);
            imgName.setText(img);
            imgName.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "File Selected!", Toast.LENGTH_LONG).show();

        }else{
            imgName.setText("");
            imgName.setVisibility(View.GONE);
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    public void getDetails() {
        dialog = ProgressDialog.show(getContext(), "", "Getting Data...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/Member/detail/" + memberId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jArray = jObj.getJSONArray("data");
                    com.animator.navigation.ui.directory.family.Family family = new Family();
                    JSONObject jsonObject = jArray.getJSONObject(0);
                    JSONArray jsonArray = jsonObject.getJSONArray("member");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        family.setId(object.getString("family_no"));
                        String img = object.getString("image").trim();
                        first_name = object.getString("first_name").trim();
                        second_name = object.getString("second_name").trim();
                        third_name = object.getString("third_name").trim();
                        member_no = object.getString("member_no").trim();
                        family_no = object.getString("family_no").trim();
                        fullname = object.getString("fullname").trim();
                        sex = object.getString("sex").trim();
                        dob = object.getString("dob").trim();
                        contact = object.getString("contact").trim();
                        emails = object.getString("email").trim();
                        live_type = object.getString("live_type").trim();
                        member_type = object.getString("member_type").trim();
                        address_1 = object.getString("address_1").trim();
                        address_2 = object.getString("address_2").trim();
                        surnames = object.getString("surname_id").trim();
                        bloodgroup = object.getString("blood_id").trim();
                        relation = object.getString("relation_id").trim();
                        maritalstatus = object.getString("marriage_type").trim();
                        education = object.getString("education").trim();
                        occupation = object.getString("occupation_id").trim();
                        sports_id = object.getString("sports_id").trim();
                        achivements = object.getString("achivements").trim();
                        mSecondNameStr = object.getString("achivements").trim();
                        mthirdNameStr = object.getString("achivements").trim();
                        msurNameStr = object.getString("achivements").trim();
                        mMobileStr = object.getString("achivements").trim();
                        mEmailStr = object.getString("achivements").trim();


                        name.setText(first_name);;
                        fname.setText(second_name); ;
                        gfname.setText(third_name);
                        mobile.setText(contact);
                        email.setText(emails);
                        birthDateEt.setText(dob);

                        mSecondName.setText(mSecondNameStr);
                        mthirdName.setText(mthirdNameStr);
                        msurName.setText(msurNameStr);
                        mMobile.setText(mMobileStr);
                        mEmail.setText(mEmailStr);

                        achievementEt.setText(achivements);
                        Log.e("Surname Id", surnames);

                        surnameSp.setSelection(Integer.parseInt(surnames));
                        spinner.setSelection(Integer.parseInt(bloodgroup));
                        relationSp.setSelection(Integer.parseInt(relation));

                        if (sex.equals("Male")) {
                            maleR.setChecked(true);
                        } else {
                            femaleR.setChecked(true);
                        }
                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e("Volley error: ", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("id", getid);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void getData() {
        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/masters/bloodgroup",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray("data");
                            getBloodGroup(result);
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
                return params;
            }
        };
        queue.add(request);
    }

    private void getBloodGroup(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject jSon = j.getJSONObject(i);
                bloodGroup.add(jSon.getString("bloodgroup"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spinner.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, bloodGroup));
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
        }) {
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
        surnameSp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, surName));
    }

    private void getEducationData() {
        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/masters/education",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray("data");
                            getEducation(result);
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
                return params;
            }
        };
        queue.add(request);
    }

    private void getEducation(JSONArray j) {
//        Log.e("jLength", String.valueOf(j.length()));
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject jSon = j.getJSONObject(i);
                MasterSurnameModel masterSurnameModel = new MasterSurnameModel();
                masterSurnameModel.setEducation(jSon.getString("education"));
                masterSurnameModel.setEid(jSon.getString("id"));
//                educationArray.add("");
                educationArray.add(jSon.getString("education"));
                educationId.add(masterSurnameModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        educationSp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, educationArray));
    }

    private void getRelationData() {
        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/masters/relation",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray("data");
                            getRelation(result);
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
                return params;
            }
        };
        queue.add(request);
    }

    private void getRelation(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject jSon = j.getJSONObject(i);
                MasterSurnameModel masterSurnameModel = new MasterSurnameModel();
                masterSurnameModel.setRelation(jSon.getString("relation"));
                masterSurnameModel.setRid(jSon.getString("id"));
//                relationArray.add("");
                relationArray.add(jSon.getString("relation"));
                relationId.add(masterSurnameModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        relationSp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, relationArray));
    }

    private void getOccupationData() {
        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/masters/occupation",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray("data");
                            getOccupation(result);
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
                return params;
            }
        };
        queue.add(request);
    }

    private void getOccupation(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject jSon = j.getJSONObject(i);
                MasterSurnameModel masterSurnameModel = new MasterSurnameModel();
                masterSurnameModel.setOccupation(jSon.getString("occupation"));
                masterSurnameModel.setOid(jSon.getString("id"));
//                occupationArray.add("");
                occupationArray.add(jSon.getString("occupation"));
                occupationId.add(masterSurnameModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        occupationSp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, occupationArray));
    }

    private void getMarritalData() {
        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/masters/marital_status",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray("data");
                            getMarrital(result);
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
                return params;
            }
        };
        queue.add(request);
    }

    private void getMarrital(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject jSon = j.getJSONObject(i);
                marritalArray.add(jSon.getString("maritalstatus"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        maritalSp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, marritalArray));
    }

    private void getSportsData() {
        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/masters/sports",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray("data");
                            getSports(result);
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
                return params;
            }
        };
        queue.add(request);
    }

    private void getSports(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject jSon = j.getJSONObject(i);
                MasterSurnameModel masterSurnameModel = new MasterSurnameModel();
                masterSurnameModel.setSports(jSon.getString("sports"));
                masterSurnameModel.setSid(jSon.getString("id"));
//                sportsArray.add("");
                sportsArray.add(jSon.getString("sports"));
                sportsId.add(masterSurnameModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        sportsSp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, sportsArray));
    }

    private void getDharmikData() {
        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/masters/dharmik_knowledge",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray("data");
                            getDharmik(result);
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
                return params;
            }
        };
        queue.add(request);
    }

    private void getDharmik(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject jSon = j.getJSONObject(i);
                dharmikArray.add(jSon.getString("dharmikknowledge"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        dharmikSp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, dharmikArray));
    }

    private void getAreaData() {
        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/masters/area",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray("data");
                            getArea(result);
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
                return params;
            }
        };
        queue.add(request);
    }

    private void getArea(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject jSon = j.getJSONObject(i);
                MasterSurnameModel masterSurnameModel = new MasterSurnameModel();
                masterSurnameModel.setSports(jSon.getString("area"));
                masterSurnameModel.setSid(jSon.getString("id"));
//                areaArray.add("");
                areaArray.add(jSon.getString("area"));
                areaId.add(masterSurnameModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        areaSp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, areaArray));
    }

    private void getVillageData() {
        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/Village",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray("data");
                            getVillage(result);
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
                return params;
            }
        };
        queue.add(request);
    }

    private void getVillage(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject jSon = j.getJSONObject(i);
                villageArray.add(jSon.getString("village"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        dvillageSp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, villageArray));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(getContext(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getContext(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Fragment myFragment = new ProfileFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();

                    return true;
                }
                return false;
            }
        });
    }
}
