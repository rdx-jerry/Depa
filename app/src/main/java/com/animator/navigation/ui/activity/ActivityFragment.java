package com.animator.navigation.ui.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

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
import com.animator.navigation.ui.blogs.AddBlogFragment;
import com.animator.navigation.ui.home.HomeFragment;
import com.animator.navigation.ui.profile.ProfileFragment;
import com.animator.navigation.ui.tools.BaseURL;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ActivityFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner activitySp;
    private WebView activityView;

    ProgressDialog pd;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);

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
        categories.add("General Samiti");
        categories.add("Education Samiti");
        categories.add("Sports Samiti");
        categories.add("Medical Samiti");
        categories.add("Dharmik Samiti");
//        categories.add("Depa Gunjan Mahila Mandal");
        categories.add("Regional Samiti");
        categories.add("Senior Citizen Samiti");
//        categories.add("Depa Yuvak Mandal");
        categories.add("Sansthas");

        //categories.add("Donate");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, categories);

//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
            case "General Samiti":
                activityView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/general");
                pd.dismiss();
                activityView.getSettings().setJavaScriptEnabled(true);
                activityView.getSettings().setBuiltInZoomControls(true);
                activityView.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        try{
                            System.out.println("url called:::" + url);
                            if(url.startsWith("tel:")) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse(url));
                                startActivity(intent);
                                activityView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/general");
                                return true;
                            } else if (url.startsWith("whatsapp://")) {
                                activityView.stopLoading();
                                try {
                                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                                    whatsappIntent.setType("text/plain");
                                    whatsappIntent.setPackage("com.whatsapp");

                                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, activityView.getUrl() + "  - Shared from webview ");

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
            case "Education Samiti":
                activityView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/education");
                activityView.getSettings().setJavaScriptEnabled(true);
                activityView.getSettings().setBuiltInZoomControls(true);
                activityView.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        try{
                            System.out.println("url called:::" + url);
                            if(url.startsWith("tel:")) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse(url));
                                startActivity(intent);
                                activityView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/education");
                                return true;
                            } else if (url.startsWith("whatsapp://")) {
                                activityView.stopLoading();
                                try {
                                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                                    whatsappIntent.setType("text/plain");
                                    whatsappIntent.setPackage("com.whatsapp");

                                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, activityView.getUrl() + "  - Shared from webview ");

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
            case "Sports Samiti":
                activityView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/sports");
                activityView.getSettings().setJavaScriptEnabled(true);
                activityView.getSettings().setBuiltInZoomControls(true);
                activityView.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        try{
                            System.out.println("url called:::" + url);
                            if(url.startsWith("tel:")) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse(url));
                                startActivity(intent);
                                activityView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/sports");
                                return true;
                            } else if (url.startsWith("whatsapp://")) {
                                activityView.stopLoading();
                                try {
                                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                                    whatsappIntent.setType("text/plain");
                                    whatsappIntent.setPackage("com.whatsapp");

                                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, activityView.getUrl() + "  - Shared from webview ");

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
            case "Medical Samiti":
                activityView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/medical");
                activityView.getSettings().setJavaScriptEnabled(true);
                activityView.getSettings().setBuiltInZoomControls(true);
                activityView.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        try{
                            System.out.println("url called:::" + url);
                            if(url.startsWith("tel:")) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse(url));
                                startActivity(intent);
                                activityView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/medical");
                                return true;
                            } else if (url.startsWith("whatsapp://")) {
                                activityView.stopLoading();
                                try {
                                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                                    whatsappIntent.setType("text/plain");
                                    whatsappIntent.setPackage("com.whatsapp");

                                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, activityView.getUrl() + "  - Shared from webview ");

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
            case "Dharmik Samiti":
                activityView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/religions");
                activityView.getSettings().setJavaScriptEnabled(true);
                activityView.getSettings().setBuiltInZoomControls(true);
                activityView.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        try{
                            System.out.println("url called:::" + url);
                            if(url.startsWith("tel:")) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse(url));
                                startActivity(intent);
                                activityView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/religions");
                                return true;
                            } else if (url.startsWith("whatsapp://")) {
                                activityView.stopLoading();
                                try {
                                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                                    whatsappIntent.setType("text/plain");
                                    whatsappIntent.setPackage("com.whatsapp");

                                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, activityView.getUrl() + "  - Shared from webview ");

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
            case "Regional Samiti":
                activityView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/regional");
                activityView.getSettings().setJavaScriptEnabled(true);
                activityView.getSettings().setBuiltInZoomControls(true);
                break;
            case "Sansthas":
//                activityView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/aathkotisangh");
//                activityView.getSettings().setBuiltInZoomControls(true);

//                AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                Fragment myFragment = new OtherAboutFragment();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();

                Fragment fragment = new OtherAboutFragment();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                break;
            case "Senior Citizen Samiti":
                activityView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/seniorcitizen");
                activityView.getSettings().setJavaScriptEnabled(true);
                activityView.getSettings().setBuiltInZoomControls(true);
                activityView.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        try{
                            System.out.println("url called:::" + url);
                            if(url.startsWith("tel:")) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse(url));
                                startActivity(intent);
                                activityView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/seniorcitizen");
                                return true;
                            } else if (url.startsWith("whatsapp://")) {
                                activityView.stopLoading();
                                try {
                                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                                    whatsappIntent.setType("text/plain");
                                    whatsappIntent.setPackage("com.whatsapp");

                                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, activityView.getUrl() + "  - Shared from webview ");

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
//            case "Depa Yuvak Mandal":
//                activityView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/depayuvak");
//                activityView.getSettings().setBuiltInZoomControls(true);
//                break;
//            case "Depa Gunjan Mahila Mandal":
//                activityView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/gunjanmandal");
//                activityView.getSettings().setBuiltInZoomControls(true);
//                break;
            case "Donate":
                activityView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/donate");
                activityView.getSettings().setJavaScriptEnabled(true);
                activityView.getSettings().setBuiltInZoomControls(true);
                activityView.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        try{
                            System.out.println("url called:::" + url);
                            if(url.startsWith("tel:")) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse(url));
                                startActivity(intent);
                                activityView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/donate");
                                return true;
                            } else if (url.startsWith("whatsapp://")) {
                                activityView.stopLoading();
                                try {
                                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                                    whatsappIntent.setType("text/plain");
                                    whatsappIntent.setPackage("com.whatsapp");

                                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, activityView.getUrl() + "  - Shared from webview ");

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
                    Fragment myFragment = new HomeFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();

                    return true;
                }
                return false;
            }
        });
    }
}