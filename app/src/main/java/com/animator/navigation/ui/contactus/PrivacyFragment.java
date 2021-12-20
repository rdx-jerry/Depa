package com.animator.navigation.ui.contactus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.animator.navigation.R;
import com.animator.navigation.ui.tools.BaseURL;

public class PrivacyFragment extends Fragment {
    WebView privacy;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_privacy, container, false);
        privacy = (WebView) view.findViewById(R.id.privacyWv);
        privacy.loadUrl(BaseURL.getBaseUrl()+"Api/pages/privacy");
        privacy.getSettings().setBuiltInZoomControls(true);
        return view;
    }
}