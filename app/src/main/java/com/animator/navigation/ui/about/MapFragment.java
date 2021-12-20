package com.animator.navigation.ui.about;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.animator.navigation.R;
import com.animator.navigation.ui.tools.BaseURL;

public class MapFragment extends Fragment {

    WebView mapView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (WebView) view.findViewById(R.id.mapView);
        mapView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/about/depa-map");
        mapView.getSettings().setBuiltInZoomControls(true);
        return view;
    }
}
