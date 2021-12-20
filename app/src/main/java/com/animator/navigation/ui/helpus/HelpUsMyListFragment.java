package com.animator.navigation.ui.helpus;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

public class HelpUsMyListFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    private RecyclerView recyclerView;
    private HelpUsPublicListAdapter matrimonyPublicListAdapter;
    private ArrayList<HelpUsListModel> arrayList;

    String family_id, member_no;
    ProgressDialog pd;
    Button addNew;
    String id;
    TextView recordTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_helpus_mylist, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Sthanik Milkat (LEH VECH)");
        queue = Volley.newRequestQueue(getContext());
        share = getActivity().getSharedPreferences("project", MODE_PRIVATE);
        family_id = share.getString("family_no", "");
        Log.e("family_id", family_id);
        member_no = share.getString("member_no", "");
        id = share.getString("id", "");
        //Toast.makeText(getContext(), member_no, Toast.LENGTH_SHORT).show();

        recordTv = (TextView) view.findViewById(R.id.recordTv);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        addNew = (Button) view.findViewById(R.id.addNew);

        if (!id.equals("")) {
            addNew.setVisibility(View.VISIBLE);
        } else {
            addNew.setVisibility(View.GONE);
        }

        getDetails();

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //addDetails();
                //Toast.makeText(getContext(), "ADD", Toast.LENGTH_SHORT).show();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new HelpUsAddNewFragment()).addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        return view;
    }


    public void getDetails() {

        pd = ProgressDialog.show(getContext(), "Help Us", "Please wait, loading...", true);

        StringRequest request = new StringRequest(Request.Method.POST, BaseURL.getBaseUrl()+"Api/helpus/getmyhelp/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject obj = new JSONObject(response);
                    Log.e("Response", response);

                    pd.dismiss();

                    if(obj.optString("status").equals("true")) {
                        Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                        arrayList = new ArrayList<>();
                        JSONArray dataArray = obj.getJSONArray("data");

                        for (int a = 0; a < dataArray.length(); a++) {

                            JSONObject memberobj = dataArray.getJSONObject(a);
                            HelpUsListModel helpUsListModel = new HelpUsListModel();

                            helpUsListModel.setId(memberobj.getString("id"));
                            helpUsListModel.setFamily_no(memberobj.getString("family_no"));
                            helpUsListModel.setMember_no(memberobj.getString("member_no"));
                            helpUsListModel.setTitle(memberobj.getString("title"));
                            helpUsListModel.setContact_person(memberobj.getString("contact_person"));
                            helpUsListModel.setContact_number(memberobj.getString("contact_number"));
                            helpUsListModel.setDetails(memberobj.getString("details"));
                            helpUsListModel.setHelp_for(memberobj.getString("help_for"));
                            helpUsListModel.setStatus(memberobj.getString("status"));
                            helpUsListModel.setCreated_date(memberobj.getString("created_date"));
                            helpUsListModel.setDeleted(memberobj.getString("deleted"));
                            arrayList.add(helpUsListModel);
                        }

                        matrimonyPublicListAdapter = new HelpUsPublicListAdapter(getContext(), arrayList, "my");
                        recyclerView.setAdapter(matrimonyPublicListAdapter);
                        matrimonyPublicListAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
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