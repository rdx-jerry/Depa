package com.animator.navigation.ui.donate;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.profile.MatrimonyMemberListAdapter;
import com.animator.navigation.ui.profile.MatrimonyMemberListModel;
import com.animator.navigation.ui.tools.BaseURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class MyDonationFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;
    String donationList = BaseURL.getBaseUrl()+"Api/member/getdonations";
    String folder = BaseURL.getBaseUrl()+"/uploads/receipts/";

    private RecyclerView recyclerView;
    private MyDonationListAdapter myDonationListAdapter;
    private ArrayList<DonationLIst> donationLIsts;

    String family_id;
    ProgressDialog pd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_donation, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Donation List");
        queue = Volley.newRequestQueue(getContext());
        share = getActivity().getSharedPreferences("project", MODE_PRIVATE);
        family_id = share.getString("family_no", "");

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getDonationLIst();
        return view;
    }

    public void getDonationLIst() {
        pd = ProgressDialog.show(getContext(), "", "Please wait, loading...", true);
        StringRequest request = new StringRequest(Request.Method.POST, donationList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    Log.e("response", response);
                    pd.dismiss();
                    if(obj.optString("status").equals("true")) {
                        donationLIsts = new ArrayList<>();
                        JSONArray dataArray = obj.getJSONArray("data");

                        for (int a = 0; a < dataArray.length(); a++) {

                            JSONObject memberobj = dataArray.getJSONObject(a);
                            DonationLIst donationLIst = new DonationLIst();

                            donationLIst.setMemberId(memberobj.getString("family_id"));
                            donationLIst.setName(memberobj.getString("donor_name"));
                            donationLIst.setEmail(memberobj.getString("email"));
                            donationLIst.setAddress(memberobj.getString("address"));
                            donationLIst.setAmount(memberobj.getString("amount"));
                            donationLIst.setRefNo(memberobj.getString("refernumber"));
                            donationLIst.setStatus(memberobj.getString("status"));
                            donationLIst.setPdf(folder+memberobj.getString("recept_number"));
                            donationLIsts.add(donationLIst);
                        }
                        myDonationListAdapter = new MyDonationListAdapter(getContext(), donationLIsts);
                        recyclerView.setAdapter(myDonationListAdapter);
                        myDonationListAdapter.notifyDataSetChanged();
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
                params.put("family_no", family_id);
                Log.e("params", params.toString());
                return params;
            }
        };
        queue.add(request);
    }
}