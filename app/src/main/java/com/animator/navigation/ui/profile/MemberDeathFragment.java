package com.animator.navigation.ui.profile;

import android.app.DatePickerDialog;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class MemberDeathFragment extends Fragment {

    SharedPreferences share;
    SharedPreferences.Editor editor;
    RequestQueue queue;
    Toolbar toolbar;

    LinearLayout hofLL, newHeadLL;
    TextView familyIdTv, memberIdTv, memberNmTv;
    EditText dodEt;
    String dodStr;
    DatePickerDialog dodPicker;
    String memberId, memberNm, familyId, ip, client, relationStr;
    Spinner memberSp;
    String hof;
    Family family;
    FamilyDeathAdapter familyAdapter;
    SpinAdapter spinAdapter;
    ArrayList<Family> familyArrayList;
    ArrayList<Family> memberArrayList;
    JSONArray result;
    private RecyclerView familyDetailsRv;
    Family playerModel;
    Button submitDeath;
    ArrayList<String> splitArray;
    ArrayList<String> memberArray;
    ArrayList<String> relationArray;
    ArrayList<Family> removed;
    String deathName;
    SpinAdapter mCustomAdapter;
    String spItem, spId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_member_death, container, false);
        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("project", MODE_PRIVATE);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add Death Details");

        memberId = getArguments().getString("memberId");
        memberNm = getArguments().getString("memberNm");
        relationStr = getArguments().getString("relation_id");

        familyId = share.getString("family_no", "");
        ip = share.getString("IP","");
        client = share.getString("client", "");

        hofLL = (LinearLayout) view.findViewById(R.id.hofLL);
        newHeadLL = (LinearLayout) view.findViewById(R.id.newHeadLL);

        familyIdTv = (TextView) view.findViewById(R.id.familyIdTv);
        memberIdTv = (TextView) view.findViewById(R.id.memberIdTv);
        memberNmTv = (TextView) view.findViewById(R.id.nameTv);
        dodEt = (EditText) view.findViewById(R.id.dodEt);
        memberSp = (Spinner) view.findViewById(R.id.memberSp);
        submitDeath = (Button) view.findViewById(R.id.deathBt);

        familyIdTv.setText(familyId);
        memberIdTv.setText(memberId);
        memberNmTv.setText(memberNm);

        dodEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                dodPicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        dodEt.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        dodStr = dodEt.getText().toString();
                    }
                }, year, month, day);
                dodPicker.show();
                return false;
            }
        });

        familyDetailsRv = (RecyclerView)view.findViewById(R.id.familyDetailsRv);

        memberArrayList = new ArrayList<Family>();
        familyArrayList = new ArrayList<Family>();
        removed = new ArrayList<Family>();
        relationArray = new ArrayList<>();

        getFamilyDetails();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        familyDetailsRv.setLayoutManager(linearLayoutManager);
        familyDetailsRv.setItemAnimator(new DefaultItemAnimator());

        if (relationStr.equals("22")){
            hofLL.setVisibility(View.VISIBLE);
            newHeadLL.setVisibility(View.VISIBLE);
        } else {
            hofLL.setVisibility(View.GONE);
            newHeadLL.setVisibility(View.GONE);
        }
        memberSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

                spItem = memberArrayList.get(position).getMemberName();
                spId = memberArrayList.get(position).getId();
                Toast.makeText(getContext(), spItem, Toast.LENGTH_SHORT).show();

                familyArrayList.remove(memberArrayList.get(position));
                familyAdapter = new FamilyDeathAdapter(getContext(), familyArrayList);
                familyDetailsRv.setAdapter(familyAdapter);
                familyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        submitDeath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                splitArray = new ArrayList<>();
                memberArray = new ArrayList<>();

                if (splitArray.size() == 0) {
                    splitArray.add("");
                    memberArray.add("");
                    addDeath(splitArray, memberArray);
                } else {
                    splitArray.add(String.valueOf(familyAdapter.checked_items));
                    memberArray.add(String.valueOf(familyAdapter.member_id));
                    addDeath(splitArray, memberArray);
                }


            }
        });
        return view;
    }

    public void getFamilyDetails() {
        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/family/"+familyId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.optString("status").equals("true")) {

                        memberArrayList = new ArrayList<>();
                        JSONArray dataArray = obj.getJSONArray("data");
                        String id;

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataobj = dataArray.getJSONObject(i);

                            JSONArray memberArray = dataobj.getJSONArray("members");
                            for (int a = 0; a < memberArray.length(); a++) {
                                playerModel = new Family();
                                JSONObject memberobj = memberArray.getJSONObject(a);
                                id = memberobj.getString("id");
                                playerModel.setId(id);
                                playerModel.setImage(memberobj.getString("image"));
                                playerModel.setMemberId(memberobj.getString("member_no"));
                                playerModel.setMemberName(memberobj.getString("fullname"));
                                playerModel.setRelation(memberobj.getString("relation_id"));
                                playerModel.setLive_type(memberobj.getString("live_type"));
                                familyArrayList.add(playerModel);
                                memberArrayList.add(playerModel);

                                if (memberNm.equals(playerModel.getMemberName())) {
                                    memberArrayList.remove(playerModel);
                                }

                                if (playerModel.getLive_type().equals("Death")) {
                                    deathName = playerModel.getMemberName();
                                    memberArrayList.remove(playerModel);
                                }
                            }
                            mCustomAdapter = new SpinAdapter(getContext(), R.layout.spinnerid, memberArrayList);
                            memberSp.setAdapter(mCustomAdapter);

                        }
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

    public void addDeath(final ArrayList<String> splitArray, final ArrayList<String> memberArray) {
        StringRequest request = new StringRequest(Request.Method.POST, BaseURL.getBaseUrl()+"Api/member/deathself", new Response.Listener<String>() {
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

                params.put("member_id", memberId);
                params.put("family_id", familyId);
                params.put("dod", dodStr);
                params.put("client", client);
                params.put("ip", ip);

                for (int i = 0; i < splitArray.size(); i++) {
//                    relationArray.add(Integer.parseInt(memberArray.get(i)), splitArray.get(i));
                    params.put("relation[0][id]", spId);
                    params.put("relation[0][relation_id]", "22");
                    if (splitArray.size() >= i)
                    params.put("relation[" + i +"][id]", memberArray.get(i));
                    params.put("relation[" + i +"][relation_id]", splitArray.get(i));
                }
//                params.put("relation[]", relationArray.toString());


//                if(splitArray.size() == 1) {
//                    for (int i = 0; i < splitArray.size(); i++) {
//                        params.put("relation[0][id]", spId);
//                        params.put("relation[0][relation_id]", "22");
//                    }
//                } else if(splitArray.size() == 2) {
//                    for (int i = 0; i < splitArray.size(); i++) {
//                        params.put("relation[0][id]", spId);
//                        params.put("relation[0][relation_id]", "22");
//                        params.put("relation[1][id]", memberArray.get(1));
//                        params.put("relation[1][relation_id]", splitArray.get(1));
//                    }
//                } else if(splitArray.size() == 3) {
//                    for (int i = 0; i < splitArray.size(); i++) {
//                        params.put("relation[0][id]", spId);
//                        params.put("relation[0][relation_id]", "22");
//                        params.put("relation[1][id]", memberArray.get(1));
//                        params.put("relation[1][relation_id]", splitArray.get(1));
//                        params.put("relation[2][id]", memberArray.get(2));
//                        params.put("relation[2][relation_id]", splitArray.get(2));
//                    }
//                } else if(splitArray.size() == 4) {
//                    for (int i = 0; i < splitArray.size(); i++) {
//                        params.put("relation[0][id]", spId);
//                        params.put("relation[0][relation_id]", "22");
//                        params.put("relation[1][id]", memberArray.get(1));
//                        params.put("relation[1][relation_id]", splitArray.get(1));
//                        params.put("relation[2][id]", memberArray.get(2));
//                        params.put("relation[2][relation_id]", splitArray.get(2));
//                        params.put("relation[3][id]", memberArray.get(3));
//                        params.put("relation[3][relation_id]", splitArray.get(3));
//                    }
//                } else if(splitArray.size() == 6) {
//                    for (int i = 0; i < splitArray.size(); i++) {
//                        params.put("relation[0][id]", spId);
//                        params.put("relation[0][relation_id]", "22");
//                        params.put("relation[1][id]", memberArray.get(1));
//                        params.put("relation[1][relation_id]", splitArray.get(1));
//                        params.put("relation[2][id]", memberArray.get(2));
//                        params.put("relation[2][relation_id]", splitArray.get(2));
//                        params.put("relation[3][id]", memberArray.get(3));
//                        params.put("relation[3][relation_id]", splitArray.get(3));
//                        params.put("relation[4][id]", memberArray.get(4));
//                        params.put("relation[4][relation_id]", splitArray.get(4));
//                        params.put("relation[5][id]", memberArray.get(5));
//                        params.put("relation[5][relation_id]", splitArray.get(5));
//                    }
//                }
                return params;
            }
        };
        queue.add(request);
    }
}