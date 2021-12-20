package com.animator.navigation.ui.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.animator.navigation.ui.business.BusinessFragment;
import com.animator.navigation.ui.model.MasterSurnameModel;
import com.animator.navigation.ui.tools.BaseURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class MemberBusinessFragment extends Fragment {
    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;
    private ProgressDialog dialog = null;

    String MEMBERBUSINESS = BaseURL.getBaseUrl()+"Api/member/business";
    EditText company_nameTv, designationTv, addressTv, pincodeTv, emailTv, contactTv, websiteTv;
    Spinner areaSp;
    String ip, client, member_id, family_id, company_name, designation, address, pincode, area, email, contact, website;
    Button submit;
    private JSONArray result;
    private ArrayList<String> areaArray;
    ArrayList<MasterSurnameModel> areaId;
    String aid;
    String atitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_member_business, container, false);
        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("project", MODE_PRIVATE);
        ip = share.getString("IP", "");
        client = share.getString("client", "");
//        member_id = share.getString("member_no", "");
        family_id = share.getString("family_id", "");
        member_id = getArguments().getString("memberId");
        Log.e("Add business member", member_id);
        company_nameTv = (EditText) view.findViewById(R.id.businessName);
        designationTv = (EditText) view.findViewById(R.id.designationEt);
        addressTv = (EditText) view.findViewById(R.id.house);
        pincodeTv = (EditText) view.findViewById(R.id.pin);
        emailTv = (EditText) view.findViewById(R.id.emailEt);
        contactTv = (EditText) view.findViewById(R.id.contactEt);
        websiteTv = (EditText) view.findViewById(R.id.websiteRt);
        areaSp = (Spinner) view.findViewById(R.id.areaSp);
        submit = (Button)view.findViewById(R.id.mButton);

        areaArray = new ArrayList<String>();
        areaId = new ArrayList<MasterSurnameModel>();

        areaSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MasterSurnameModel masterSurnameModel = areaId.get(position);
                aid = masterSurnameModel.getAid();
                atitle = masterSurnameModel.getArea();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        getAreaData();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                company_name = company_nameTv.getText().toString();
                designation = designationTv.getText().toString();
                address = addressTv.getText().toString();
                pincode = pincodeTv.getText().toString();
                email = emailTv.getText().toString();
                contact = contactTv.getText().toString();
                website = websiteTv.getText().toString();
                area = aid;
                addBusiness();
            }
        });
        return view;
    }

    private void getAreaData() {
        StringRequest request = new StringRequest(Request.Method.GET,BaseURL.getBaseUrl()+"Api/masters/area",
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
                masterSurnameModel.setArea(jSon.getString("area"));
                masterSurnameModel.setAid(jSon.getString("id"));
                areaArray.add(jSon.getString("area"));
                areaId.add(masterSurnameModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        areaSp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner, areaArray));
    }

    private void addBusiness() {
        company_nameTv.setText("");
        designationTv.setText("");
        addressTv.setText("");
        pincodeTv.setText("");
        emailTv.setText("");
        contactTv.setText("");
        websiteTv.setText("");
            dialog = ProgressDialog.show(getContext(), "Update Business", "Uploading file...", true);
        StringRequest request = new StringRequest(Request.Method.POST, BaseURL.getBaseUrl()+"Api/member/business", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getString("status").equals("ture")) {
                        Toast.makeText(getContext(), jObj.getString("message"), Toast.LENGTH_LONG).show();
                        Log.e("Message", jObj.getString("message"));

                        AppCompatActivity activity = (AppCompatActivity) getContext();
                        Fragment myFragment = new ProfileFragment();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                    } else {
                        Toast.makeText(getContext(), jObj.getString("message"), Toast.LENGTH_LONG).show();
                        Log.e("Message", jObj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("member_id", member_id);
                params.put("family_id", family_id);
                params.put("company_name", company_name);
                params.put("designation", designation);
                params.put("address", address);
                params.put("pincode", pincode);
                params.put("area", area);
                params.put("email", email);
                params.put("contact", contact);
                params.put("website", website);
                params.put("business_id", "0");
                Log.e("params", params.toString());
                return params;
            }
        };
        queue.add(request);
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