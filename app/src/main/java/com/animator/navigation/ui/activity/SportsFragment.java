package com.animator.navigation.ui.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.animator.navigation.R;
import com.animator.navigation.ui.tools.BaseURL;

public class SportsFragment extends Fragment {

    WebView sportsView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sports, container, false);
        sportsView = (WebView) view.findViewById(R.id.sportsWv);
        sportsView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/sports");
        sportsView.getSettings().setBuiltInZoomControls(true);
        return view;
    }
}
