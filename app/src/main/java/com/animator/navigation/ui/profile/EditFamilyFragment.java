package com.animator.navigation.ui.profile;

import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.animator.navigation.R;
import com.animator.navigation.ui.blogs.BlogsFragment;
import com.animator.navigation.ui.tools.BaseURL;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class EditFamilyFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    private String updateFamilyUrl = BaseURL.getBaseUrl()+"Api/member/androidaddress";

    String id, family_Id;
    TextView familyIdTv;
    private EditText contactEt, emailEt, address1, address2, pinEt;
    private Spinner areaSp;
    private String familyId, contactStr, emailStr, address1Str, address2Str, areaStr, pinStr, area_id;
    private Button updateDetails;
    private ArrayList<String> areaArray;
    private JSONArray result;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_family, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Update Family Details");

        queue = Volley.newRequestQueue(getContext());
        share = getActivity().getSharedPreferences("project", MODE_PRIVATE);

        id = share.getString("id", "");
        family_Id = share.getString("family_no", "");

        familyIdTv = (TextView)view.findViewById(R.id.familyId);
        contactEt = (EditText)view.findViewById(R.id.upContactEt);
        emailEt = (EditText)view.findViewById(R.id.upEmailEt);
        address1 = (EditText)view.findViewById(R.id.address1);
        address2 = (EditText)view.findViewById(R.id.address2);
        areaSp = (Spinner)view.findViewById(R.id.upareaSp);
        pinEt = (EditText)view.findViewById(R.id.upPinEt);
        updateDetails = (Button)view.findViewById(R.id.updateFamily);

        //Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();

        areaArray = new ArrayList<String>();
        getDetails();
        //getAreaData();

        updateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                contactStr = contactEt.getText().toString().trim();
                emailStr = emailEt.getText().toString().trim();
                address1Str = address1.getText().toString().trim();
                address2Str = address2.getText().toString().trim();
                areaStr = areaSp.getSelectedItem().toString().trim();
                pinStr = pinEt.getText().toString().trim();

                updateDetails(id, contactStr, emailStr, address1Str, address2Str,areaStr, pinStr);
            }
        });

        return view;
    }

    public void getDetails() {
        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/family/" + family_Id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    Log.e("family", String.valueOf(obj));
                    if(obj.optString("status").equals("true")) {
                        JSONArray dataArray = obj.getJSONArray("data");
                        String id, familyidStr;

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataobj = dataArray.getJSONObject(i);
                            JSONObject familyobj = null;
                            JSONArray familyArray = dataobj.getJSONArray("family");
                            for (int j = 0; j < familyArray.length(); j++) {

                                familyobj = familyArray.getJSONObject(j);
                                familyidStr = familyobj.getString("id");
                                familyIdTv.setText("Family Id: " + familyobj.getString("family_id"));
                                address1.setText(familyobj.getString("address_1"));
                                address2.setText(familyobj.getString("address_2"));
                                area_id = familyobj.getString("area_id");
                                emailEt.setText(familyobj.getString("email"));
                                pinEt.setText(familyobj.getString("pincode"));
                                contactEt.setText(familyobj.getString("landline"));

                                //area_name
                                //Toast.makeText(getContext(), "AREA: "+familyobj.getString("area_name"), Toast.LENGTH_SHORT).show();

                                getAreaData(familyobj.getString("area_name"));

                            }
                            /*JSONArray contactArray = dataobj.getJSONArray("contact");
                            for (int j = 0; j < contactArray.length(); j++) {

                                JSONObject contactobj = contactArray.getJSONObject(j);
                                contactEt.setText(contactobj.getString("contact"));
                            }*/
                        }

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
                params.put("familyId", familyId);
                return params;
            }
        };
        queue.add(request);
    }

    private void getAreaData(final String areaName) {
        StringRequest request = new StringRequest(Request.Method.GET,BaseURL.getBaseUrl()+"Api/masters/area",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray("data");
                            getArea(result, areaName);
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

    private void getArea(JSONArray j, String areaStr) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject jSon = j.getJSONObject(i);
                areaArray.add(jSon.getString("area"));
                //Log.d("AREA AJAY:: ", Arrays.toString(new ArrayList[]{areaArray}));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //areaSp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner, areaArray));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, areaArray);
        areaSp.setAdapter(adapter);
        areaSp.setSelection(adapter.getPosition(areaStr));//Optional to set the selected item.
    }

    public void updateDetails(final String id, final String contactStr, final String emailStr, final String houseStr,
                              final String streetStr, final String areaStr, final String pinStr) {
        StringRequest request = new StringRequest(Request.Method.POST, updateFamilyUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getString("status") == "true") {
                        Toast.makeText(getContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        AppCompatActivity activity = (AppCompatActivity) getContext();
                        Fragment myFragment = new ProfileFragment();
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
                Log.e("Volley Error", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("memberid", id);
                params.put("landline", contactStr);
                params.put("email", emailStr);
                params.put("address1", houseStr);
                params.put("address2", streetStr);
                params.put("area_id", areaStr);
                params.put("pincode", pinStr);
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
