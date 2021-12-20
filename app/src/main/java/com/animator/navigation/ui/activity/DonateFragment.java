package com.animator.navigation.ui.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.animator.navigation.R;
import com.animator.navigation.ui.tools.BaseURL;


public class DonateFragment extends Fragment {

    WebView donateView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donate, container, false);
        donateView = (WebView) view.findViewById(R.id.educatioWv);
        donateView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/donate");
        donateView.getSettings().setBuiltInZoomControls(true);
        return view;
    }
}