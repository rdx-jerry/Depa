package com.animator.navigation.ui.contactus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.blogs.BlogsFragment;
import com.animator.navigation.ui.business.AddBusinessFragment;
import com.animator.navigation.ui.feedback.FeedbackFragment;
import com.animator.navigation.ui.home.HomeFragment;
import com.animator.navigation.ui.jobs.JobsFragment;
import com.animator.navigation.ui.tools.BaseURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

public class ContactUsFragment extends Fragment {

    SharedPreferences share;
    SharedPreferences.Editor editor;
    RequestQueue queue;

    TextView call, call1;
    String num, num1;
//    ImageButton fbIb, gmailIb, youtubeIb;
    EditText nameEt, emailEt, contactEt, subjectEt, messageEt;
    String nameStr, emailStr, contactStr, subjectStr, messageStr;
    Button feedbackBt;
    public static String FACEBOOK_URL = "https://www.facebook.com";
    public static String FACEBOOK_PAGE_ID = "YourPageName";
    String memberId;
    private ProgressDialog dialog = null;
    Pattern pattern = Patterns.EMAIL_ADDRESS;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Contact Us");
        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("private", MODE_PRIVATE);
        memberId = share.getString("memberId", "");
        call = (TextView)view.findViewById(R.id.call);
        call1 = (TextView)view.findViewById(R.id.call1);

        nameEt = (EditText)view.findViewById(R.id.nameContact);
        emailEt = (EditText)view.findViewById(R.id.emailContact);
        contactEt = (EditText)view.findViewById(R.id.contactContact);
        subjectEt = (EditText)view.findViewById(R.id.subjectContact);
        messageEt = (EditText)view.findViewById(R.id.reasonContact);

        feedbackBt = (Button)view.findViewById(R.id.feedbackBt);

        num = call.getText().toString();
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(num));
                startActivity(callIntent);
            }
        });

        num1 = call.getText().toString();
        call1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(num1));
                startActivity(callIntent);
            }
        });

        feedbackBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nameStr = nameEt.getText().toString();
                emailStr = emailEt.getText().toString();
                contactStr = contactEt.getText().toString();
                subjectStr = subjectEt.getText().toString();
                messageStr = messageEt.getText().toString();

                if (nameStr.equals("") && emailStr.equals("") && pattern.matcher(emailStr).matches() &&contactStr.equals("") &&
                        subjectStr.equals("") && messageStr.equals("")) {
                    Toast.makeText(getContext(), "Please Enter Valid Fields", Toast.LENGTH_SHORT).show();
                } else {
                    dialog = ProgressDialog.show(getContext(), "Contact Us","Uploading...", true);
                    saveFeedback(nameStr, emailStr, contactStr, subjectStr, messageStr);
                }
            }
        });
        return view;
    }

    public void saveFeedback(final String nameStr, final String emailStr, final String contactStr, final String subjectStr, final String messageStr) {

        nameEt.setText(" ");
        emailEt.setText(" ");
        contactEt.setText(" ");
        subjectEt.setText(" ");
        messageEt.setText(" ");

        StringRequest request = new StringRequest(Request.Method.POST, BaseURL.getBaseUrl()+"Api/contact", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getString("status").equals("ture")) {
                        Toast.makeText(getContext(), nameStr + " " + jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        Log.e("Message", jObj.getString("message"));

                        AppCompatActivity activity = (AppCompatActivity) getContext();
                        Fragment myFragment = new HomeFragment();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                    } else {
                        Toast.makeText(getContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        Log.e("Message", jObj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("member_no", memberId);
                params.put("name", nameStr);
                params.put("email", emailStr);
                params.put("phone", contactStr);
                params.put("subject", subjectStr);
                params.put("message", messageStr);
                params.put("client", "app");
                params.put("ip", "1.1.1.1");
                Log.e("params", params.toString());
                return params;
            }
        };
        queue.add(request);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_URL;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }
}
