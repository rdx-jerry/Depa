package com.animator.navigation.ui.profile;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.tools.BaseURL;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class MemberBusDetailFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    TextView company_nameTv, designationTv, addressTv, pincodeTv, emailTv, contactTv, websiteTv, areaAp, edit;
    String id, member_id, family_id, bussness, designation, address, pincode, area, email, contact, website;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_member_bus_detail, container, false);

        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("project", MODE_PRIVATE);

        id = getArguments().getString("id");
        member_id = getArguments().getString("memberid");
        family_id = getArguments().getString("familyid");
        bussness = getArguments().getString("bussness");
        designation = getArguments().getString("designation");
        address = getArguments().getString("address");
        pincode = getArguments().getString("pincode");
        area = getArguments().getString("area");
        email = getArguments().getString("email");
        contact = getArguments().getString("contact");
        website = getArguments().getString("website");

        company_nameTv = (TextView) view.findViewById(R.id.businessName);
        designationTv = (TextView) view.findViewById(R.id.designationEt);
        addressTv = (TextView) view.findViewById(R.id.house);
        pincodeTv = (TextView) view.findViewById(R.id.pin);
        emailTv = (TextView) view.findViewById(R.id.emailEt);
        contactTv = (TextView) view.findViewById(R.id.contactEt);
        websiteTv = (TextView) view.findViewById(R.id.websiteRt);
        areaAp = (TextView) view.findViewById(R.id.areaSp);
        edit = (TextView) view.findViewById(R.id.view);

        company_nameTv.setText(bussness);
        designationTv.setText(designation);
        addressTv.setText(address);
        pincodeTv.setText(pincode);
        areaAp.setText(area);
        emailTv.setText(email);
        contactTv.setText(contact);
        websiteTv.setText(website);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new BusinessEditFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                bundle.putString("id", id);
                bundle.putString("memberid", member_id);
                bundle.putString("familyid", family_id);
                bundle.putString("bussness", bussness);
                bundle.putString("designation", designation);
                bundle.putString("address", address);
                bundle.putString("pincode", pincode);
                bundle.putString("area", area);
                bundle.putString("email", email);
                bundle.putString("contact", contact);
                bundle.putString("website", website);

                Log.e("bundle ", bundle.toString());

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });

        return view;
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