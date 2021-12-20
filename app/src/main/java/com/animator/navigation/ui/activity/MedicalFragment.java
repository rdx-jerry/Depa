package com.animator.navigation.ui.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.animator.navigation.R;
import com.animator.navigation.ui.tools.BaseURL;

public class MedicalFragment extends Fragment {

    WebView medcalView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medical, container, false);
        medcalView = (WebView) view.findViewById(R.id.medicalWv);
        medcalView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/medical");
        medcalView.getSettings().setBuiltInZoomControls(true);
        return view;
    }
}
