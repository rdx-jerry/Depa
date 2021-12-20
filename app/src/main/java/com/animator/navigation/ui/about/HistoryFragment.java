package com.animator.navigation.ui.about;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.animator.navigation.R;
import com.animator.navigation.ui.tools.BaseURL;

public class HistoryFragment extends Fragment {

    WebView historyView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        historyView = (WebView) view.findViewById(R.id.historyView);
        historyView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/about/history");
        historyView.getSettings().setBuiltInZoomControls(true);
        return view;
    }
}
