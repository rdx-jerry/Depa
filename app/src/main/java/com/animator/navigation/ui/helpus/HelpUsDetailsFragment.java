package com.animator.navigation.ui.helpus;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.tools.BaseURL;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HelpUsDetailsFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    ViewPager viewPager;
    ArrayList<String> image = new ArrayList<>();
    HelpUsImageSliderAdapter adapter;

    String member_no, id, get_name, get_dob, get_age, family_no, get_title, contact_person, contact_number, get_details, get_help_for;
    TextView name, title, contact, help_for, details, genBy;
    ProgressDialog pd;
    ImageButton leftNav, rightNav;
    LinearLayout viewPagerLL;

    @SuppressLint("HardwareIds")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.helpus_details_fragment, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Sthanik Milkat (LEH VECH)");
        queue = Volley.newRequestQueue(getContext());

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        leftNav = (ImageButton) view.findViewById(R.id.left_nav);
        rightNav = (ImageButton) view.findViewById(R.id.right_nav);
        viewPagerLL = (LinearLayout) view.findViewById(R.id.viewPagerLL);

        // Images left navigation
        leftNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = viewPager.getCurrentItem();
                if (tab > 0) {
                    tab--;
                    viewPager.setCurrentItem(tab);
                } else if (tab == 0) {
                    viewPager.setCurrentItem(tab);
                }
            }
        });

        // Images right navigatin
        rightNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = viewPager.getCurrentItem();
                tab++;
                viewPager.setCurrentItem(tab);
            }
        });

        id = getArguments().getString("id");
        Log.e("ID ", id);
        family_no = getArguments().getString("family_no");
        member_no = getArguments().getString("member_no");
        get_title = getArguments().getString("title");
        contact_person = getArguments().getString("contact_person");
        contact_number = getArguments().getString("contact_number");
        get_details = getArguments().getString("details");
        get_help_for = getArguments().getString("help_for");

        name = (TextView) view.findViewById(R.id.name);
        title = (TextView) view.findViewById(R.id.title);
        help_for = (TextView) view.findViewById(R.id.help_for);
        details = (TextView) view.findViewById(R.id.details);
        contact = (TextView) view.findViewById(R.id.contact);
        genBy = (TextView) view.findViewById(R.id.genBy);

        title.setText(get_title);
        name.setText("Name: "+contact_person);
        help_for.setText("For: "+get_help_for);
        contact.setText("Contact: "+contact_number);
        details.setText("Details: "+get_details);
        genBy.setText("Generated By: " + member_no);

        getDetails();

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!contact.equals("")){
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact));
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext(), "Phone Number Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void getDetails() {

        pd = ProgressDialog.show(getContext(), "Image Banner", "Please wait, loading...", true);

        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/helpus/api_getimgbyid/"+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject obj = new JSONObject(response);

                    pd.dismiss();

                    if(obj.optString("status").equals("true")) {

                        JSONArray dataArray = obj.getJSONArray("data");

                        for (int i=0;i<dataArray.length();i++){

                            JSONObject memberobj = dataArray.getJSONObject(i);
                            image.add(memberobj.getString("image"));

                        }

                        String[] imageUrls = new String[image.size()];
                        imageUrls = image.toArray(imageUrls);

                        for (String s : imageUrls){
                            Log.e("IMG String : ", s);
                        }

                        adapter=new HelpUsImageSliderAdapter(getContext(), imageUrls);
                        viewPager.setAdapter(adapter);

                    }else{

                        viewPagerLL.setVisibility(View.GONE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Log.e("Volley Error", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        queue.add(request);
    }


}