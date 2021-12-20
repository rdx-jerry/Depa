package com.animator.navigation.ui.profile;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
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
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class MatrimonyPublicListFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    private Button addNewMatrimony;
    private RecyclerView recyclerView;
    private MatrimonyPublicListAdapter matrimonyPublicListAdapter;
    private ArrayList<MatrimonyMemberListModel> arrayList;

    String family_id;
    ProgressDialog pd;

    public MatrimonyPublicListFragment() {}

    public static MatrimonyPublicListFragment newInstance(String param1, String param2) {
        return new MatrimonyPublicListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matrimony_public_list, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Sagpan Setu");
        queue = Volley.newRequestQueue(getContext());

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getDetails();

        return view;
    }


    public void getDetails() {

        pd = ProgressDialog.show(getContext(), "", "Please wait, loading...", true);

        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/matrimonial/alllist", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject obj = new JSONObject(response);

                    pd.dismiss();

                    if(obj.optString("status").equals("true")) {
                        arrayList = new ArrayList<>();
                        JSONArray dataArray = obj.getJSONArray("data");

                        for (int a = 0; a < dataArray.length(); a++) {

                            JSONObject memberobj = dataArray.getJSONObject(a);
                            MatrimonyMemberListModel matrimonyMemberListModel = new MatrimonyMemberListModel();

                            matrimonyMemberListModel.setMatrimonial_id(memberobj.getString("matrimonial_id"));
                            matrimonyMemberListModel.setFullname(memberobj.getString("fullname"));
                            matrimonyMemberListModel.setEmail(memberobj.getString("email"));
                            matrimonyMemberListModel.setMember_no(memberobj.getString("member_no"));
                            matrimonyMemberListModel.setCreated_on(memberobj.getString("created_on"));
                            matrimonyMemberListModel.setStatus(memberobj.getString("status"));

                            matrimonyMemberListModel.setImage(memberobj.getString("image"));
                            matrimonyMemberListModel.setDob(memberobj.getString("dob"));
                            matrimonyMemberListModel.setAge(memberobj.getString("age"));
                            matrimonyMemberListModel.setHeight(memberobj.getString("height"));
                            matrimonyMemberListModel.setWeight(memberobj.getString("weight"));
                            matrimonyMemberListModel.setBirth_time(memberobj.getString("birth_time"));
                            matrimonyMemberListModel.setPlace(memberobj.getString("place"));
                            matrimonyMemberListModel.setMother_name(memberobj.getString("mother_name"));
                            matrimonyMemberListModel.setEducation(memberobj.getString("education"));
                            matrimonyMemberListModel.setOccupation(memberobj.getString("occupation"));
                            matrimonyMemberListModel.setContact(memberobj.getString("contact"));
                            matrimonyMemberListModel.setMaterial(memberobj.getString("material"));

                            arrayList.add(matrimonyMemberListModel);

                        }

                        matrimonyPublicListAdapter = new MatrimonyPublicListAdapter(getContext(), arrayList);
                        recyclerView.setAdapter(matrimonyPublicListAdapter);
                        matrimonyPublicListAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Log.e("Volley Error", error.getMessage());
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

}