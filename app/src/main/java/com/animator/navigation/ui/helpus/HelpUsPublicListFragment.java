package com.animator.navigation.ui.helpus;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.animator.navigation.ui.profile.MatrimonyMemberListFragment;
import com.animator.navigation.ui.tools.BaseURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class HelpUsPublicListFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    private RecyclerView recyclerView;
    private HelpUsPublicListAdapter matrimonyPublicListAdapter;
    private ArrayList<HelpUsListModel> arrayList;

    String family_id;
    ProgressDialog pd;
    Button myListBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_public_helpus, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Sthanik Milkat (LEH VECH)");
        queue = Volley.newRequestQueue(getContext());
        share = getActivity().getSharedPreferences("project", MODE_PRIVATE);
        family_id = share.getString("family_no", "");

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myListBtn = (Button) view.findViewById(R.id.myListBtn);

        Toast.makeText(getContext(), family_id, Toast.LENGTH_SHORT).show();

        if (family_id != null && !family_id.equalsIgnoreCase("")){
            myListBtn.setVisibility(View.VISIBLE);

            myListBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, new HelpUsMyListFragment());
                    fragmentTransaction.commit();

                }
            });

        }

        getDetails();

        return view;
    }


    public void getDetails() {

        pd = ProgressDialog.show(getContext(), "Help Us", "Please wait, loading...", true);

        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/helpus/api_list", new Response.Listener<String>() {
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

                        matrimonyPublicListAdapter = new HelpUsPublicListAdapter(getContext(), arrayList, "public");
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