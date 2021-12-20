package com.animator.navigation.ui.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.animator.navigation.R;
import com.animator.navigation.ui.tools.BaseURL;

public class CommitteeFragment extends Fragment {

    WebView committeeView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_committee, container, false);
        committeeView = (WebView) view.findViewById(R.id.comitiView);
        committeeView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/donate");
        committeeView.getSettings().setJavaScriptEnabled(true);
        committeeView.getSettings().setBuiltInZoomControls(true);
        committeeView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                try{
                    System.out.println("url called:::" + url);
                    if(url.startsWith("tel:")) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        return true;
                    } else if (url.startsWith("whatsapp://")) {
                        committeeView.stopLoading();
                        try {
                            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                            whatsappIntent.setType("text/plain");
                            whatsappIntent.setPackage("com.whatsapp");

                            whatsappIntent.putExtra(Intent.EXTRA_TEXT, committeeView.getUrl() + "  - Shared from webview ");

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
        return view;
    }
}
