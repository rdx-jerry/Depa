package com.animator.navigation.ui.todays.prarthana;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.directory.family.FamilyDetailsFragment;
import com.animator.navigation.ui.gallery.ImageFragment;
import com.animator.navigation.ui.home.HomeFragment;
import com.animator.navigation.ui.todays.TodaysFragment;
import com.squareup.picasso.Picasso;
import com.zolad.zoominimageview.ZoomInImageView;

import static android.content.Context.MODE_PRIVATE;

public class PrarthanaDetailsFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_prarthana_details, container, false);
        queue = Volley.newRequestQueue(getContext());


        share = getActivity().getSharedPreferences("project", MODE_PRIVATE);
        id = share.getString("id", "");

        memberid = getArguments().getString("memberId");
        member_no = getArguments().getString("member_no");
        family_no = getArguments().getString("family_no");
        fullname = getArguments().getString("fullname");
        profile = getArguments().getString("profile");
        mdate = getArguments().getString("date");

        member_noTv = (TextView)view.findViewById(R.id.member_no);
        family_noTv = (TextView)view.findViewById(R.id.family_no);
        fullnameTv = (TextView)view.findViewById(R.id.fullname);

        date = (TextView)view.findViewById(R.id.date);
        profileIv = (ImageView) view.findViewById(R.id.profileIv);

        member_noTv.setText(member_no);
        family_noTv.setText(family_no+" (Show Family)");
        fullnameTv.setText(fullname);
        date.setText(mdate);

        Picasso.with(getContext()).load(profile).into(profileIv);

        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity)getContext();
                Fragment myFragment = new ImageFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                bundle.putString("image", profile);
                bundle.putString("id", "0");
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });

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
