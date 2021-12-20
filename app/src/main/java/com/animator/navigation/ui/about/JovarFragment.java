package com.animator.navigation.ui.about;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.animator.navigation.R;
import com.animator.navigation.ui.tools.BaseURL;

public class JovarFragment extends Fragment {

    WebView jovarView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_jovar, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Mataji");
        jovarView = (WebView) view.findViewById(R.id.jovarView);
        WebSettings settings = jovarView.getSettings();
        jovarView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/about/jovar");
        jovarView.getSettings().setBuiltInZoomControls(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        return view;
    }
}
