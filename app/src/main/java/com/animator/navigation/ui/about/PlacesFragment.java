package com.animator.navigation.ui.about;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.animator.navigation.R;
import com.animator.navigation.ui.tools.BaseURL;

public class PlacesFragment extends Fragment {

    WebView placesView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places, container, false);
        placesView = (WebView) view.findViewById(R.id.placesView);
        placesView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/about/places-to-visit");
        placesView.getSettings().setBuiltInZoomControls(true);
        return view;
    }
}
