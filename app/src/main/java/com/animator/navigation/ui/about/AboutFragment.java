package com.animator.navigation.ui.about;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

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
import com.animator.navigation.ui.tools.BaseURL;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AboutFragment extends Fragment implements AdapterView.OnItemSelectedListener {

//    private TabAdapter tabAdapter;
//    private TabLayout tabLayout;
//    private ViewPager viewPager;

    private Spinner aboutSp;
    private WebView aboutView;
    ProgressDialog pd;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        pd = ProgressDialog.show(getContext(),
                "",
                "Please wait, loading...", true);

        aboutSp = (Spinner)view.findViewById(R.id.aboutSp);
        aboutView = (WebView)view.findViewById(R.id.aboutView);
        aboutView.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings = aboutView.getSettings();
        aboutView.getSettings().setBuiltInZoomControls(true);

        //improve WebView Performance
        aboutView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        aboutView.getSettings().setAppCacheEnabled(false);
        aboutView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        aboutView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setEnableSmoothTransition(true);

        aboutView.setWebViewClient(new WebViewClient() {

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

        aboutSp.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("History");
        categories.add("President's Message");
        categories.add("Committee (Mahajan)");
        categories.add("Depa Map");
        categories.add("Family Tree");
        categories.add("Mataji Jovar Info");
        categories.add("Places To Visit");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, categories);

//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        aboutSp.setAdapter(dataAdapter);

//        tabLayout = (TabLayout)view.findViewById(R.id.aboutTab);
//        viewPager = (ViewPager)view.findViewById(R.id.tabViewPager);
//
//        tabAdapter = new TabAdapter(getFragmentManager());
//        tabAdapter.addFragment(new HistoryFragment(), "History");
//        tabAdapter.addFragment(new MessageFragment(), "President");
//        tabAdapter.addFragment(new CommitteeFragment(), "Committee");
//        tabAdapter.addFragment(new MapFragment(), "Depa Map");
//        tabAdapter.addFragment(new FamilyTreeFragment(), "Family Tree");
//        tabAdapter.addFragment(new JovarFragment(), "Jovar");
//        tabAdapter.addFragment(new PlacesFragment(), "Places");
//
//        viewPager.setAdapter(tabAdapter);
//        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String selectedClass = adapterView.getItemAtPosition(position).toString();
        switch (selectedClass) {
            case "History":
                aboutView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/about/history");
                aboutView.getSettings().setJavaScriptEnabled(true);
                aboutView.getSettings().setBuiltInZoomControls(true);
                /*aboutView.getSettings().setJavaScriptEnabled(true);
                WebSettings webSettings = aboutView.getSettings();

                //improve WebView Performance
                aboutView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                aboutView.getSettings().setAppCacheEnabled(false);
                aboutView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

                aboutView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                webSettings.setDomStorageEnabled(true);
                webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
                webSettings.setUseWideViewPort(true);
                webSettings.setAllowFileAccess(true);
                webSettings.setSavePassword(true);
                webSettings.setSaveFormData(true);
                webSettings.setEnableSmoothTransition(true);*/

            break;
            case "President's Message":
                aboutView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/about/president-message");
                aboutView.getSettings().setJavaScriptEnabled(true);
                aboutView.getSettings().setBuiltInZoomControls(true);
                break;
            case "Committee (Mahajan)":
                aboutView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/about/committee");
                aboutView.getSettings().setJavaScriptEnabled(true);
                aboutView.getSettings().setBuiltInZoomControls(true);
                aboutView.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        try{
                            System.out.println("url called:::" + url);
                            if(url.startsWith("tel:")) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse(url));
                                startActivity(intent);
                                aboutView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/about/committee");
                                return true;
                            } else if (url.startsWith("whatsapp://")) {
                                aboutView.stopLoading();
                                try {
                                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                                    whatsappIntent.setType("text/plain");
                                    whatsappIntent.setPackage("com.whatsapp");

                                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, aboutView.getUrl() + "  - Shared from webview ");

                                    startActivity(whatsappIntent);
                                } catch (android.content.ActivityNotFoundException ex) {

                                    String MakeShortText = "Whatsapp has not been installed";

                                    Toast.makeText(getContext(), MakeShortText, Toast.LENGTH_SHORT).show();
                                }
                            } else if (url.startsWith("http:")
                                    || url.startsWith("https:")) {

                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(intent);
                                return true;
                            }
                            else {
                                return false;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                        return true;
                    }
                });
                break;
            case "Depa Map":
                aboutView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/about/depa-map");
                aboutView.getSettings().setJavaScriptEnabled(true);
                aboutView.getSettings().setBuiltInZoomControls(true);
                break;
            case "Family Tree":
                WebSettings settings = aboutView.getSettings();
                aboutView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/about/family-tree");
                aboutView.getSettings().setBuiltInZoomControls(true);
                settings.setDomStorageEnabled(true);
                settings.setJavaScriptEnabled(true);
                settings.setJavaScriptCanOpenWindowsAutomatically(true);
                break;
            case "Mataji Jovar Info":
                WebSettings settingJovar = aboutView.getSettings();
                aboutView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/about/jovar");
                aboutView.getSettings().setBuiltInZoomControls(true);
                settingJovar.setDomStorageEnabled(true);
                settingJovar.setJavaScriptEnabled(true);
                settingJovar.setJavaScriptCanOpenWindowsAutomatically(true);
                break;
            case "Places To Visit":
                aboutView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/about/places-to-visit");
                aboutView.getSettings().setJavaScriptEnabled(true);
                aboutView.getSettings().setBuiltInZoomControls(true);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
