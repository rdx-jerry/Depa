package com.animator.navigation.ui.directory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.Dialog;
//import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
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
import com.animator.navigation.ui.blogs.BlogsFragment;
import com.animator.navigation.ui.home.HomeFragment;
import com.animator.navigation.ui.tools.BaseURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class DirectoryFragment extends Fragment implements IndexAdapter.SelectListner {

    SharedPreferences share;
    SharedPreferences.Editor editor;
    RequestQueue queue;

    private EditText searchTelephone;
    private RecyclerView telephoneRv, rvIndex;
    private ArrayList<Telephone> telephoneArrayList;
    private TelephoneAdapter telephoneAdapter;
    private IndexAdapter indexAdapter;
    private HashMap<String, Integer> mapIndex;
    private LinearLayoutManager layoutManager;
    Dialog layoutCall;
    String folder = BaseURL.getBaseUrl()+"uploads/members/";
    String ip, client, search;
    ImageView filter;

    ProgressDialog progressDialog;

    String svone, svtwo, svthree, vone, vtwo, vthree;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_directory, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Directory");

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading please wait...");

        svone = "";
        svtwo = "";
        svthree = "";
        vone = "";
        vtwo = "";
        vthree = "";

        queue = Volley.newRequestQueue(getActivity());
        share = getActivity().getSharedPreferences("project", MODE_PRIVATE);
        ip = share.getString("IP", "");
        client = share.getString("client", "");

        searchTelephone = (EditText) view.findViewById(R.id.telephoneSearchEt);

        telephoneRv = (RecyclerView) view.findViewById(R.id.telephoneRv);
        rvIndex = (RecyclerView) view.findViewById(R.id.rvIndex);
        filter = (ImageView)view.findViewById(R.id.filter);
        telephoneArrayList = new ArrayList<>();
        mapIndex = new LinkedHashMap<String, Integer>();

        search = searchTelephone.getText().toString();
        //getDirectory(search, client, ip);
        getDirectory(search, client, ip, svone, svtwo, svthree, vone, vtwo, vthree);

        layoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(telephoneRv.getContext(), layoutManager.getOrientation());

        searchTelephone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.equals("")) {
                    telephoneAdapter.getFilter().filter(charSequence);
                } else {
                    //getDirectory(search, client, ip);
                    getDirectory(search, client, ip, svone, svtwo, svthree, vone, vtwo, vthree);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        telephoneRv.setLayoutManager(layoutManager);
        telephoneRv.addItemDecoration(dividerItemDecoration);

        indexAdapter = new IndexAdapter(new ArrayList<String>(mapIndex.keySet()), this);
        rvIndex.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvIndex.setAdapter(indexAdapter);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View DialogView = factory.inflate(R.layout.directory_filer_dialog, null);
                final AlertDialog Dialog = new AlertDialog.Builder(getActivity()).create();
                Dialog.setView(DialogView);

                Button dialogBtn_cancel = (Button) DialogView.findViewById(R.id.search_member);
                final EditText searchOne = (EditText) DialogView.findViewById(R.id.search_value_1);
                final EditText searchTwo = (EditText) DialogView.findViewById(R.id.search_value_2);
                final EditText searchThree = (EditText) DialogView.findViewById(R.id.search_value_3);

                final Spinner sone = (Spinner) DialogView.findViewById(R.id.search_key_1);
                final Spinner stwo = (Spinner) DialogView.findViewById(R.id.search_key_2);
                final Spinner sthree = (Spinner) DialogView.findViewById(R.id.search_key_3);

                dialogBtn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        svone = searchKey(sone.getSelectedItem().toString());
                        svtwo = searchKey(stwo.getSelectedItem().toString());
                        svthree = searchKey(sthree.getSelectedItem().toString());

                        vone = searchOne.getText().toString().trim();
                        vtwo = searchTwo.getText().toString().trim();
                        vthree = searchThree.getText().toString().trim();

                        if (svone.equalsIgnoreCase("") && svtwo.equalsIgnoreCase("") && svthree.equalsIgnoreCase("")){
                            Dialog.dismiss();
                        }else{
                            //telephoneAdapter.clear();
                            //telephoneAdapter.setAdapter(null);
                            telephoneArrayList.clear();
                            telephoneAdapter.notifyDataSetChanged();
                            getDirectory(search, client, ip, svone, svtwo, svthree, vone, vtwo, vthree);
                            Dialog.dismiss();
                        }
                    }
                });

                Dialog.show();
            }
        });


        return view;
    }





    public String searchKey(String title){

        String key = "";

        if (title.equalsIgnoreCase("Select Search Field")){
            key = "";
        }else if (title.equalsIgnoreCase("Name")){
            key = "first_name";
        }else if (title.equalsIgnoreCase("Father / Husband Name")){
            key = "second_name";
        }else if (title.equalsIgnoreCase("Grand Father Name")){
            key = "third_name";
        }else if (title.equalsIgnoreCase("Surname")){
            key = "surname";
        }else if (title.equalsIgnoreCase("Area")){
            key = "area";
        }else if (title.equalsIgnoreCase("Gender")){
            key = "sex";
        }else if (title.equalsIgnoreCase("Relation")){
            key = "relation";
        }else if (title.equalsIgnoreCase("Occupation")){
            key = "occupation";
        }else if (title.equalsIgnoreCase("Education")){
            key = "education";
        }else if (title.equalsIgnoreCase("Blood Group")){
            key = "bloodgroup";
        }else if (title.equalsIgnoreCase("Business")){
            key = "business";
        }else if (title.equalsIgnoreCase("Company Name")){
            key = "company";
        }else if (title.equalsIgnoreCase("Designation")){
            key = "designation";
        }else if (title.equalsIgnoreCase("Business Area")){
            key = "bizarea";
        }else{
            key = "";
        }

        return key;
    }




    private void getDirectory(final String search, final String ip, final String client, final String k1, final String k2, final String k3, final String v1, final String v2, final String v3) {


        //telephoneArrayList = new ArrayList<Telephone>();

        Toast.makeText(getActivity(), k1+" : "+v1
                                            +"\n"+k2+" : "+v2
                                            +"\n"+k3+" : "+v3, Toast.LENGTH_SHORT).show();

        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, "https://panjodepa.com/depa/Api/member/search", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    progressDialog.dismiss();

                    JSONObject obj = new JSONObject(response);
                    Log.e("surname", response);
                    JSONArray dataArray = obj.getJSONArray("data");
                    String id;
                    Log.d("surname", String.valueOf(dataArray.length()));
                    for (int i = 0; i < dataArray.length(); i++) {
                        Telephone telephone = new Telephone();
                        JSONObject dataobj = dataArray.getJSONObject(i);
                        id = dataobj.getString("id");
                        telephone.setId(id);
                        telephone.setProfileImg(folder + dataobj.getString("image"));
                        telephone.setName(dataobj.getString("membername"));
                        telephone.setFamily(dataobj.getString("family_no"));
//                        telephone = new Telephone(dataobj.getString("id"), dataobj.getString("image"), dataobj.getString("fullname") + " " + dataobj.getString("surname"));
                        telephoneArrayList.add(telephone);
                    }
                    Set<String> temp = new HashSet<>();
                    for (int i = 0; i < telephoneArrayList.size(); i++) {
                        String obj1 = Character.toString(telephoneArrayList.get(i).getName().charAt(0)).toUpperCase();
                        String obj2;
                        try {
                            obj2 = Character.toString(telephoneArrayList.get(i + 1).getName().charAt(0)).toUpperCase();
                        } catch (IndexOutOfBoundsException e) {
                            obj2 = "#";
                        }

                        if (!obj1.equalsIgnoreCase(obj2)) {
                            temp.add(obj1);
                        }
                    }
                    for (String title : temp) {
                        Telephone member = new Telephone();
                        member.setIndex(title);
                        member.setName(title);
                        member.setProfileImg(title);
                        telephoneArrayList.add(member);
                    }

                    Collections.sort(telephoneArrayList, new Comparator<Telephone>() {
                        public int compare(Telephone obj1, Telephone obj2) {
                            return obj1.getName().compareToIgnoreCase(obj2.getName());
                        }
                    });

                    getIndexList(telephoneArrayList);


                    telephoneAdapter = new TelephoneAdapter(getActivity(), telephoneArrayList);
                    telephoneRv.setAdapter(telephoneAdapter);
                    if (telephoneRv == null) {
                        searchTelephone.setVisibility(View.GONE);
                    } else {
                        searchTelephone.setVisibility(View.VISIBLE);
                    }
                    telephoneAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("Volley Error", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("search", search);
                params.put("result", "json");
                params.put("client", client);
                params.put("ip", ip);

                params.put("search_k1", k1);
                params.put("search_v1", v1);

                params.put("search_k2", k2);
                params.put("search_v2", v2);

                params.put("search_k3", k3);
                params.put("search_v3", v3);

                return params;
            }
        };
        queue.add(request);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void getIndexList(List<Telephone> memberList) {

        for (int i = 0; i < memberList.size(); i++) {
            if (memberList.get(i).getIndex() != null) {
                String index = memberList.get(i).getIndex();
                if (mapIndex.get(index) == null)
                    mapIndex.put(index, i);
            }
        }
    }

    @Override
    public void selectedIndex(View view) {
        TextView selectedIndex = (TextView) view;
        layoutManager.scrollToPositionWithOffset(mapIndex.get(selectedIndex.getText()), 0);
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
                    HomeFragment myFragment = new HomeFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();

                    return true;
                }
                return false;
            }
        });
    }
}
