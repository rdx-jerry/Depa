package com.animator.navigation.ui.todays.anniversary;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.animator.navigation.R;
import com.animator.navigation.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;

public class AnniversaryDetailsFragment extends Fragment {

    TextView titleTv;
    ImageView detailIv, detailHIv;
    String titleStr;
    String imgPath, imagePathH;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_anniversary_details, container, false);
        titleTv = (TextView)view.findViewById(R.id.titleDetail);
        detailIv = (ImageView) view.findViewById(R.id.detailIv);
        detailHIv = (ImageView) view.findViewById(R.id.detailHIv);
        titleStr = getArguments().getString("title");
        imgPath = getArguments().getString("image");
        imagePathH = getArguments().getString("imageH");

        titleTv.setText(titleStr);
        Picasso.with(getContext()).load(imgPath).into(detailIv);
        Picasso.with(getContext()).load(imagePathH).into(detailHIv);
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
