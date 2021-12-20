package com.animator.navigation.ui.profile;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import com.animator.navigation.ui.model.MasterSurnameModel;
import com.animator.navigation.ui.tools.BaseURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class AddMemberFragment extends Fragment implements Spinner.OnItemSelectedListener{

    SharedPreferences share;
    SharedPreferences.Editor editor;
    RequestQueue queue;

    String ADDPROFILE = BaseURL.getBaseUrl()+"Api/member/android";
    private static final int PICK_IMAGE_REQUEST = 1;
    //    private Uri filePath;
    String pathUrl;
    private int serverResponseCode = 0;
    private ProgressDialog dialog = null;
    private static final int STORAGE_PERMISSION_CODE = 123;

    DatePickerDialog picker, deathPicker, domPicker;
    TextView birthDateEt, deathDateEt, domEt;
    private JSONArray result;

    private ArrayList<String> bloodGroup;
    ArrayList<String> surName;
    ArrayList<MasterSurnameModel> surNameId;
    ArrayList<MasterSurnameModel> educationId;
    ArrayList<MasterSurnameModel> relationId;
    ArrayList<MasterSurnameModel> occupationId;
    ArrayList<MasterSurnameModel> sportsId;
    ArrayList<MasterSurnameModel> areaId;

    ArrayList<String> relationArray;
    ArrayList<String> educationArray;

    ArrayList<String> occupationArray;
    ArrayList<String> marritalArray;
    ArrayList<String> sportsArray;
    ArrayList<String> dharmikArray;
    ArrayList<String> areaArray;
    ArrayList<String> villageArray;

    ArrayList<StateVO> listVOs;
    MyAdapter myAdapter;

    RadioGroup genderRg, insuranceRg, medicalRg, sanjivaniRg;
    RadioButton maleR, femaleR, insuranceRy, insuranceRn, insuranceMRy, insuranceMRn, sanjiviniNo, sanjiviniYes;


    EditText name, fname, gfname, mobile, email, mSecondName, mthirdName, msurName, mAddress, mMobile, altMobile, mEmail,
            achievementEt, insEt, insMEt, sanijiviniEt, otherRelation, otherEdu, otherOcu, otherSp, otherSur;
    String nameStr, fatherName, grandFather, surname, gender, birthdate, mobileStr, altMobileStr, bldGroup, area,
            education, occupation, maritalSts, insuaranceStr, insuaranceMStr, sports, achievement, personSts, deathDate,
            dharmik, m_thirdname, m_Surname, husbandName, m_address, m_contact, m_email, relation, dom, dvillage, member_type, emailStr, photo_file,
            client, ip, memberId, familyId, memberNo, insStr, insMStr, sanjiviniStr, otherRelationStr, otherEduStr, otherOcuStr, otherSpStr, otherSurStr;

    private LinearLayout girlLL, deathLL, insLL, sanjiviniLL;

    private Spinner spinner, surnameSp, relationSp, educationSp, occupationSp, maritalSp, sportsSp, dharmikSp, areaSp, dvillageSp;
    private Bitmap bitmap;
    private String filePath;

    String sid, aid, eid, rid, oid, spid;
    String stitle, atitle, etitle, rtitle, otitle, sptitle;

    TextView imgName;

    private Button chooseImg, submitMain;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_member, container, false);
        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("project", MODE_PRIVATE);

        client = share.getString("client", "");
        ip = share.getString("IP", "");
        memberId = share.getString("id", "");
        familyId = share.getString("family_id", "");
        memberNo = share.getString("member_no", "");

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
        email = (EditText)view.findViewById(R.id.email);
        mSecondName = (EditText)view.findViewById(R.id.mSecondName);
        mthirdName = (EditText)view.findViewById(R.id.mthirdName);
        msurName = (EditText)view.findViewById(R.id.msurName);
        mMobile = (EditText)view.findViewById(R.id.mMobile);
        altMobile = (EditText)view.findViewById(R.id.mobileAlt);
        mAddress = (EditText)view.findViewById(R.id.mAddress);
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

        gender = maleR.getText().toString();
        insuaranceStr = "1";
        insuaranceMStr = "1";
        sanjiviniStr = "1";

        genderRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (maleR.isChecked()) {
                    gender = maleR.getText().toString();
                } else if(femaleR.isChecked()) {
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

        spinner = (Spinner)view.findViewById(R.id.bloodSp);
        surnameSp = (Spinner)view.findViewById(R.id.surnameSp);
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
        domEt = (TextView)view.findViewById(R.id.domEt);
        chooseImg = (Button)view.findViewById(R.id.chooseImage);
        submitMain = (Button)view.findViewById(R.id.mainBt);

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
        } else if(personSts == "No") {
            deathLL.setVisibility(View.GONE);
        }

        maritalSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Married") || selectedItem.equals("Widow") && gender.equals("Female"))
                {
                    girlLL.setVisibility(View.VISIBLE);
                } else {
                    girlLL.setVisibility(View.GONE);
                }
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

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
                mobileStr = mobile.getText().toString();
                altMobileStr = altMobile.getText().toString();
                bldGroup = String.valueOf(spinner.getSelectedItemId());
                maritalSts = String.valueOf(maritalSp.getSelectedItemId());

                area = aid;
                relation = rid;
                education = eid;
                occupation = oid;
                sports = spid;

                achievement = achievementEt.getText().toString();
                deathDate = deathDateEt.getText().toString();
                dharmik = String.valueOf(dharmikSp.getSelectedItemId());
                husbandName = mSecondName.getText().toString();
                m_thirdname = mthirdName.getText().toString();
                m_Surname = msurName.getText().toString();
                dom = domEt.getText().toString();

                dvillage = dvillageSp.getSelectedItem().toString();
                m_contact = mMobile.getText().toString();
                m_address = mAddress.getText().toString();
                m_email = mEmail.getText().toString();
                photo_file = name.getText().toString();
                emailStr = email.getText().toString();
                insStr = insEt.getText().toString();
                insMStr = insMEt.getText().toString();
                sanjiviniStr = sanijiviniEt.getText().toString();


                if (nameStr.equals("") &&  fatherName.equals("") &&  grandFather.equals("") &&  surname.equals("") &&
                        gender.equals("") &&  birthdate.equals("") &&  mobileStr.equals("") &&  bldGroup.equals("") &&
                        area.equals("") && education.equals("") &&  occupation.equals("") &&  maritalSts.equals("") &&
                        insuaranceStr.equals("") &&  insuaranceMStr.equals("") &&  sports.equals("")){
                    Toast.makeText(getContext(), "Please Enter Valid Fields!", Toast.LENGTH_SHORT).show();
                }
                dialog = ProgressDialog.show(getContext(), "", "Uploading file...", true);
                new Thread(new Runnable() {
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {

                            }
                        });
                        uploadBitmap(bitmap);

                    }
                }).start();
            }
        });
        return view;
    }

    private void uploadBitmap(final Bitmap bitmap) {

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ADDPROFILE,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getView().getContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            if (obj.getBoolean("status")) {
                                AppCompatActivity activity = (AppCompatActivity) getContext();
                                Fragment myFragment = new ProfileFragment();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
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

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("member_id", "0");
                params.put("family_id", familyId);
                params.put("member_no", "0");
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
                params.put("altcontact", altMobileStr);
                params.put("email", emailStr);
                params.put("achivements", achievement);
                params.put("sports", sports);
                params.put("dharmik", dharmik);
                params.put("life_insurance", insuaranceStr);
                params.put("medical_insurance", insuaranceMStr);
                params.put("life_insurance_text", insuaranceStr);
                params.put("sanjeevni", sanjiviniStr);
                params.put("medical_insurance_text", insuaranceMStr);

                /*if married female*/
                params.put("m_secondname", husbandName);
                params.put("m_thirdname", m_thirdname);
                params.put("m_surname", m_Surname);
                params.put("m_village", dvillage);
                params.put("m_address", m_address);
                params.put("m_type", "");
                params.put("m_contact", m_contact);
                params.put("m_email", m_email);

                params.put("live_type", "Live");
                params.put("dod", deathDate);

                params.put("relation_input", otherRelationStr);
                params.put("surname_input", otherSurStr);
                params.put("education_input", otherEduStr);
                params.put("occupation_input", otherOcuStr);
                params.put("sports_input", otherSpStr);


//                params.put("member_type", member_type);

                params.put("ip", ip);
                params.put("client", client);

                Log.e("params",params.toString());
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
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
            Uri picUri = data.getData();
            filePath = getPath(picUri);

            String fine_name = getPath(picUri);
            String img = fine_name.substring(fine_name.lastIndexOf("/") + 1);
            imgName.setText(img);
            imgName.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "File Selected!", Toast.LENGTH_LONG).show();

            if (filePath != null) {
                try {
                    Log.d("filePath", String.valueOf(filePath));
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(
                        getActivity(), "no image selected",
                        Toast.LENGTH_LONG).show();
            }
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
        }){
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
        spinner.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner, bloodGroup));
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
        }){
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
        educationSp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner, educationArray));
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
        }){
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
        relationSp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner, relationArray));
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
        }){
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
        occupationSp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner, occupationArray));
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
        }){
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
        maritalSp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner, marritalArray));
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
        }){
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
        sportsSp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner, sportsArray));
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
        }){
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
        dharmikSp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner, dharmikArray));
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
        }){
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
        areaSp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner, areaArray));
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
        }){
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
        dvillageSp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner, villageArray));
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

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

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
