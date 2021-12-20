package com.animator.navigation.ui.donors;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.animator.navigation.R;
import com.animator.navigation.ui.gallery.ImageFragment;
import com.squareup.picasso.Picasso;

public class DonorImageFragment extends Fragment {

    ImageView detailIv;
    String imgPath;
    String id;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donor_image, container, false);
        detailIv = (ImageView) view.findViewById(R.id.imgOpen);

        imgPath = getArguments().getString("image");

        Picasso.with(getContext()).load(imgPath).into(detailIv);
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scaleGestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });
        return view;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            detailIv.setScaleX(mScaleFactor);
            detailIv.setScaleY(mScaleFactor);
            return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

               /* if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Fragment myFragment;
                    if (id.equals("Business")) {
                        myFragment = new BusinessDetailsFragment();
                    } else {
                        myFragment = new GalleryFragment();
                    }

                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                    return true;
                }*/
                return false;
            }
        });
    }
}