package com.animator.navigation.ui.contactus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.animator.navigation.R;
import com.animator.navigation.ui.tools.BaseURL;

public class TermsFragment extends Fragment {

    WebView termsWv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_terms, container, false);
        termsWv = (WebView) view.findViewById(R.id.termsWv);
        termsWv.loadUrl(BaseURL.getBaseUrl()+"Api/pages/terms");
        termsWv.getSettings().setBuiltInZoomControls(true);
        return view;
    }
}