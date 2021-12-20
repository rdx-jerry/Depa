package com.animator.navigation.ui.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.animator.navigation.R;
import com.animator.navigation.ui.profile.ProfileFragment;
import com.animator.navigation.ui.tools.BaseURL;

import java.util.ArrayList;
import java.util.List;

public class OtherAboutFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private Spinner activitySp;
    private WebView activityView;
    ProgressDialog pd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_other_about, container, false);
        pd = ProgressDialog.show(getContext(),
                "",
                "Please wait, loading...", true);

        activitySp = (Spinner)view.findViewById(R.id.activitySp);
        activityView = (WebView)view.findViewById(R.id.activityView);
        activityView.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings = activityView.getSettings();

        //improve WebView Performance
        activityView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        activityView.getSettings().setAppCacheEnabled(false);
        activityView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        activityView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setEnableSmoothTransition(true);

        activityView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getContext(), description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pd.dismiss();
                //String webUrl = activityView.getUrl();
            }

        });

        activitySp.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Depa Aath Koti Nani Paksh Jain Sangh");
        categories.add("Depa Yuvak Mandal");
        categories.add("Depa Gunjan Mahila Mandal");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, categories);

        // attaching data adapter to spinner
        activitySp.setAdapter(dataAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String selectedClass = adapterView.getItemAtPosition(position).toString();
        switch (selectedClass) {

            case "Depa Aath Koti Nani Paksh Jain Sangh":
                activityView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/aathkotisangh");
                activityView.getSettings().setBuiltInZoomControls(true);
                break;
            case "Depa Yuvak Mandal":
                activityView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/depayuvak");
                activityView.getSettings().setBuiltInZoomControls(true);
                break;
            case "Depa Gunjan Mahila Mandal":
                activityView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/gunjanmandal");
                activityView.getSettings().setBuiltInZoomControls(true);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Fragment myFragment = new ActivityFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();

                    return true;
                }
                return false;
            }
        });
    }
}