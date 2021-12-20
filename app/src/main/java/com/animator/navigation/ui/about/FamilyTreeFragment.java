package com.animator.navigation.ui.about;

//import android.app.Fragment;
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

public class FamilyTreeFragment extends Fragment {

    WebView familyView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_family_tree, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Family Tree");

        familyView = (WebView) view.findViewById(R.id.familyView);
        WebSettings settings = familyView.getSettings();
        familyView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/about/family-tree");
        familyView.getSettings().setBuiltInZoomControls(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        return view;
    }
}
