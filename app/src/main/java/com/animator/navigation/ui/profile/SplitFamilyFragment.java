package com.animator.navigation.ui.profile;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.animator.navigation.ui.tools.BaseURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class SplitFamilyFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    TextView familyid;
    EditText contact, address, pin;
    Spinner areaSp;

    private RecyclerView familyDetailsRv;
    private FamilySplitAdapter familyAdapter;
    private ArrayList<Family> familyArrayList;
    JSONArray result;
    ArrayList<String> areaArray;
    ArrayList<String> splitArray;
    ArrayList<String> memberArray;
    public Button splitBt;

    String id, family_id, client, ip, member_id;
    String contactSt, addressSt, pincode, area_id;
    String[][] split_arr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_split_family, container, false);
        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("project", MODE_PRIVATE);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Split Family");

        queue = Volley.newRequestQueue(getContext());
        share = getActivity().getSharedPreferences("project", MODE_PRIVATE);

        id = share.getString("id", "");
        family_id = share.getString("family_no", "");
        Log.e("family_id", family_id);
        client = share.getString("client", "");
        ip = share.getString("IP", "");
        member_id = share.getString("id", "");

        familyDetailsRv = (RecyclerView)view.findViewById(R.id.familyDetailsRv);
        familyid = (TextView)view.findViewById(R.id.familyid);
        contact = (EditText) view.findViewById(R.id.contact);
        address = (EditText)view.findViewById(R.id.address);
        pin = (EditText)view.findViewById(R.id.pinEt);
        areaSp = (Spinner)view.findViewById(R.id.areaSp);
        splitBt = (Button) view.findViewById(R.id.splitBt);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        familyDetailsRv.setLayoutManager(linearLayoutManager);
        familyDetailsRv.setItemAnimator(new DefaultItemAnimator());

        familyArrayList = new ArrayList<Family>();
        areaArray = new ArrayList<>();
        getFamilyDetails();
        getAreaData();

        splitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                splitArray = new ArrayList<>();
                memberArray = new ArrayList<>();
                contactSt = contact.getText().toString();
                addressSt = address.getText().toString();
                pincode = pin.getText().toString();
                area_id = String.valueOf(areaSp.getSelectedItemId());

                splitArray.add(String.valueOf(familyAdapter.checked_items));
                memberArray.add(String.valueOf(familyAdapter.member_id));
                addSplit(splitArray, memberArray);
            }
        });
        return view;
    }

    public void getFamilyDetails() {
        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/family/"+family_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                    if(obj.optString("status").equals("true")) {
                        familyArrayList = new ArrayList<>();
                        JSONArray dataArray = obj.getJSONArray("data");
                        String id, familyidStr;

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataobj = dataArray.getJSONObject(i);
                            JSONObject familyobj = null;
                            JSONArray familyArray = dataobj.getJSONArray("family");
                            for (int j = 0; j < familyArray.length(); j++) {

                                familyobj = familyArray.getJSONObject(j);
                                familyidStr = familyobj.getString("id");
                                familyid.setText("Family Id: " + familyobj.getString("family_id"));
                                address.setText(familyobj.getString("address_1") +", "+ familyobj.getString("address_2"));
                            }
                            JSONArray contactArray = dataobj.getJSONArray("contact");
                            for (int j = 0; j < contactArray.length(); j++) {

                                JSONObject contactobj = contactArray.getJSONObject(j);
                                contact.setText(contactobj.getString("contact"));
                            }
                            JSONArray memberArray = dataobj.getJSONArray("members");

                            for (int a = 0; a < memberArray.length(); a++) {
                                JSONObject memberobj = memberArray.getJSONObject(a);
                                Family playerModel = new Family();

                                id = memberobj.getString("id");
                                playerModel.setId(id);
                                playerModel.setImage(memberobj.getString("image"));
                                playerModel.setMemberId(memberobj.getString("member_no"));
                                playerModel.setMemberName(memberobj.getString("fullname"));
                                familyArrayList.add(playerModel);
                            }
                        }
                        familyAdapter = new FamilySplitAdapter(getContext(), familyArrayList);
                        familyDetailsRv.setAdapter(familyAdapter);
                        familyAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volly Error", error.getMessage());
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

    public void addSplit(final ArrayList<String> splitArray, final ArrayList<String> memberArray) {
        StringRequest request = new StringRequest(Request.Method.POST, "http://www.nlblinds.com/depa/Api/member/splitfamily", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);
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
                Log.e("VolleyError", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("member_id", member_id);
                params.put("landline", contactSt);
                params.put("address_1", addressSt);
                params.put("area_id", area_id);
                params.put("pincode", pincode);
                params.put("client", client);
                params.put("ip", ip);

                for (int i = 0; i < splitArray.size(); i++) {
//                    relationArray.add(Integer.parseInt(memberArray.get(i)), splitArray.get(i));
                    params.put("relation[0][id]", id);
                    params.put("relation[0][relation_id]", "22");
                    if (splitArray.size() >= i)
                        params.put("relation[" + i +"][id]", memberArray.get(i));
                    params.put("relation[" + i +"][relation_id]", splitArray.get(i));
                }
//                if(splitArray.size() == 1) {
//                    for (int i = 0; i < splitArray.size(); i++) {
//                        params.put("relation[0][id]", memberArray.get(0));
//                        params.put("relation[0][relation_id]", splitArray.get(0));
//                    }
//                } else if(splitArray.size() == 2) {
//                    for (int i = 0; i < splitArray.size(); i++) {
//                        params.put("relation[0][id]", memberArray.get(0));
//                        params.put("relation[0][relation_id]", splitArray.get(0));
//                        params.put("relation[1][id]", memberArray.get(1));
//                        params.put("relation[1][relation_id]", splitArray.get(1));
//                    }
//                } else if(splitArray.size() == 3) {
//                    for (int i = 0; i < splitArray.size(); i++) {
//                        params.put("relation[0][id]", memberArray.get(0));
//                        params.put("relation[0][relation_id]", splitArray.get(0));
//                        params.put("relation[1][id]", memberArray.get(1));
//                        params.put("relation[1][relation_id]", splitArray.get(1));
//                        params.put("relation[2][id]", memberArray.get(2));
//                        params.put("relation[2][relation_id]", splitArray.get(2));
//                    }
//                } else if(splitArray.size() == 4) {
//                    for (int i = 0; i < splitArray.size(); i++) {
//                        params.put("relation[0][id]", memberArray.get(0));
//                        params.put("relation[0][relation_id]", splitArray.get(0));
//                        params.put("relation[1][id]", memberArray.get(1));
//                        params.put("relation[1][relation_id]", splitArray.get(1));
//                        params.put("relation[2][id]", memberArray.get(2));
//                        params.put("relation[2][relation_id]", splitArray.get(2));
//                        params.put("relation[3][id]", memberArray.get(3));
//                        params.put("relation[3][relation_id]", splitArray.get(3));
//                    }
//                } else if(splitArray.size() == 5) {
//                    for (int i = 0; i < splitArray.size(); i++) {
//                        params.put("relation[0][id]", memberArray.get(0));
//                        params.put("relation[0][relation_id]", splitArray.get(0));
//                        params.put("relation[1][id]", memberArray.get(1));
//                        params.put("relation[1][relation_id]", splitArray.get(1));
//                        params.put("relation[2][id]", memberArray.get(2));
//                        params.put("relation[2][relation_id]", splitArray.get(2));
//                        params.put("relation[3][id]", memberArray.get(3));
//                        params.put("relation[3][relation_id]", splitArray.get(3));
//                        params.put("relation[4][id]", memberArray.get(4));
//                        params.put("relation[4][relation_id]", splitArray.get(4));
//                    }
//                }
                return params;
            }
        };
        queue.add(request);
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
                areaArray.add(jSon.getString("area"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        areaSp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner, areaArray));
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
