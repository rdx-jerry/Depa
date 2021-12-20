package com.animator.navigation.ui.todays.marannondh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.directory.family.FamilyDetailsFragment;
import com.animator.navigation.ui.home.HomeFragment;
import com.animator.navigation.ui.todays.TodaysFragment;
import com.animator.navigation.ui.tools.BaseURL;
import com.squareup.picasso.Picasso;
import com.zolad.zoominimageview.ZoomInImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class MaranNondhDetailsFragment extends Fragment {

    private SharedPreferences share;
    private RequestQueue queue;
    ImageView profileIv, contact1, contact2, whatsApp1, whatsApp2;
    TextView member_noTv, family_noTv, fullnameTv, date, tvcontact1, tvcontact2, description;
    private ZoomInImageView imgiv;
    private LinearLayout mainLL, imgLL, bottomLL;
    private String profile, member_no, family_no, fullname, contact, mdate, mdescription, cc1, cc2;

    private int linearlayout = 0;
    String id, memberid, c1, c2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_marannondh_details, container, false);
        queue = Volley.newRequestQueue(getContext());


        share = getActivity().getSharedPreferences("project", MODE_PRIVATE);
        id = share.getString("id", "");

        memberid = getArguments().getString("memberId");
        member_no = getArguments().getString("member_no");
        family_no = getArguments().getString("family_no");
        fullname = getArguments().getString("fullname");
        profile = getArguments().getString("profile");
        contact = getArguments().getString("contact");
        mdate = getArguments().getString("date");
        mdescription = getArguments().getString("description");

        int checkContatSplt = contact.indexOf(',');

        if (checkContatSplt != -1){
            String[] contactArray = contact.split(",");
            c1 = contactArray [0];
            c2 = contactArray [1];

            String[] contact1Array = c1.split(" - ");
            cc1 = contact1Array [1];

            String[] contact2Array = c2.split(" - ");
            cc2 = contact2Array [1];
        }else{

            c1 = contact;
            c2 = "Not available";

            String[] contact1Array = contact.split(" - ");
            cc1 = contact1Array [1];
            cc2 = "0000000000";
        }




        member_noTv = (TextView)view.findViewById(R.id.member_no);
        family_noTv = (TextView)view.findViewById(R.id.family_no);
        fullnameTv = (TextView)view.findViewById(R.id.fullname);

        date = (TextView)view.findViewById(R.id.date);
        tvcontact1 = (TextView)view.findViewById(R.id.contact1);
        tvcontact2 = (TextView)view.findViewById(R.id.contact2);
        description = (TextView)view.findViewById(R.id.description);

        profileIv = (ImageView) view.findViewById(R.id.profileIv);
        contact1 = (ImageView) view.findViewById(R.id.callContact1);
        contact2 = (ImageView) view.findViewById(R.id.callContact2);
        whatsApp1 = (ImageView) view.findViewById(R.id.whatsappContact1);
        whatsApp2 = (ImageView) view.findViewById(R.id.whatsappContact2);

        member_noTv.setText(member_no);
        family_noTv.setText(family_no+" (Show Family)");
        fullnameTv.setText(fullname);
        date.setText(mdate);
        tvcontact1.setText(c1);
        tvcontact2.setText(c2);
        description.setText(mdescription);

        Picasso.with(getContext()).load(profile).into(profileIv);

        family_noTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) getContext();
                Fragment myFragment = new FamilyDetailsFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                bundle.putString("family_no", family_no);
                bundle.putString("redirect", "true");
                assert activity != null;
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();

                //Toast.makeText(getContext(), family_no, Toast.LENGTH_SHORT).show();

            }
        });

        contact1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cc1.equals("")){
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + cc1));
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext(), "Phone Number Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        contact2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cc2.equals("")){
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + cc2));
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext(), "Phone Number Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        whatsApp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cc1.equals("")){

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send/?phone="+cc1+"&text=Hey+I+have+a+query"));
                    startActivity(browserIntent);

                }else{
                    Toast.makeText(getContext(), "Number Not Available For WhatsApp", Toast.LENGTH_SHORT).show();
                }
            }
        });

        whatsApp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cc2.equals("")){

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send/?phone="+cc2+"&text=Hey+I+have+a+query"));
                    startActivity(browserIntent);

                }else{
                    Toast.makeText(getContext(), "Number Not Available For WhatsApp", Toast.LENGTH_SHORT).show();
                }
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
                    Fragment myFragment = new HomeFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();

                    return true;
                }
                return false;
            }
        });
    }
}
