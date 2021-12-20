package com.animator.navigation.ui.donate;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.login.LoginFragment;
import com.animator.navigation.ui.profile.MatrimonyMemberListFragment;
import com.animator.navigation.ui.tools.BaseURL;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class DonateFragment extends Fragment  {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;
    String id;

    WebView donateView;
    public WebSettings webSettings;
    String url = BaseURL.getBaseUrl()+"Api/pages/activity/donate";
    Button myDonationBt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donate, container, false);

        queue= Volley.newRequestQueue(getActivity());
        share=this.getActivity().getSharedPreferences("project",MODE_PRIVATE);

        id =  share.getString("id", "");
        Log.e("memberId", id);


        donateView = (WebView) view.findViewById(R.id.donateWv);
        myDonationBt = (Button) view.findViewById(R.id.myDonation);
        donateView.getSettings().setJavaScriptEnabled(true);
        donateView.loadUrl(BaseURL.getBaseUrl()+"Api/pages/activity/donate");
        donateView.getSettings().setBuiltInZoomControls(true);

        if (!id.equals("")) {
            myDonationBt.setVisibility(View.VISIBLE);
        } else {
            myDonationBt.setVisibility(View.GONE);
        }

        myDonationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                final MyDonationFragment myFragment = new MyDonationFragment();
                Bundle b = new Bundle();
                myFragment.setArguments(b);
                fragmentTransaction.replace(R.id.frame, myFragment).commit();
            }
        });

        return view;
    }

}