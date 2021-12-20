package com.animator.navigation.ui.about;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.animator.navigation.R;
import com.animator.navigation.ui.tools.BaseURL;

public class MessageFragment extends Fragment {

    WebView messageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        messageView = (WebView) view.findViewById(R.id.messageView);
        messageView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/about/president-message");
        messageView.getSettings().setBuiltInZoomControls(true);
        return view;
    }
}
