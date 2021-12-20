package com.animator.navigation.ui.profile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.animator.navigation.ui.tools.BaseURL;
import com.animator.navigation.ui.tools.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class MatrimonyDetailsFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    String member_no, image, get_name, get_dob, get_age, get_height, get_weight, get_mother_name, get_birth_place, get_birth_time, get_education, get_occupation, get_contact, get_material;
    Button add_matrimony_request;
    ImageView imageView;
    TextView name, dob, age, height, weight, mother_name, birth_place, birth_time, education, occupation, contact, nana;

    String rname, remail, rphone, birthTime;
    ProgressDialog pd;
    private String android_id;

    @SuppressLint("HardwareIds")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matrimony_details, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Sagpan Setu");
        queue = Volley.newRequestQueue(getContext());

        rname = "";
        remail = "";
        rphone = "";

        android_id = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        SimpleDateFormat h_mm_a   = new SimpleDateFormat("h:mm a");
        SimpleDateFormat hh_mm_ss = new SimpleDateFormat("HH:mm:ss");
        Log.e("birth time", getArguments().getString("birth_time"));

        image = getArguments().getString("image");
        member_no = getArguments().getString("member_no");
        get_name = getArguments().getString("name");
        get_dob = getArguments().getString("dob");
        get_age = getArguments().getString("age");
        get_height = getArguments().getString("height");
        get_weight = getArguments().getString("weight");
        get_mother_name = getArguments().getString("mother_name");
        get_birth_place = getArguments().getString("birth_place");
        get_birth_time = getArguments().getString("birth_time");
        get_education = getArguments().getString("education");
        get_occupation = getArguments().getString("occupation");
        get_contact = getArguments().getString("contact");
        get_material = getArguments().getString("material");

        try {
            Date d1 = hh_mm_ss.parse(get_birth_time);
            birthTime = h_mm_a.format(d1);
            System.out.println (h_mm_a.format(d1));

        } catch (Exception e) {
            e.printStackTrace();
        }


        add_matrimony_request = (Button) view.findViewById(R.id.add_matrimony_request);
        imageView = (ImageView) view.findViewById(R.id.image);
        name = (TextView) view.findViewById(R.id.name);
        dob = (TextView) view.findViewById(R.id.dob);
        age = (TextView) view.findViewById(R.id.age);
        height = (TextView) view.findViewById(R.id.height);
        weight = (TextView) view.findViewById(R.id.weight);
        mother_name = (TextView) view.findViewById(R.id.mother_name);
        birth_place = (TextView) view.findViewById(R.id.birth_place);
        birth_time = (TextView) view.findViewById(R.id.birth_time);
        education = (TextView) view.findViewById(R.id.education);
        occupation = (TextView) view.findViewById(R.id.occupation);
        contact = (TextView) view.findViewById(R.id.contact);
        nana = (TextView) view.findViewById(R.id.nanaTv);

        Picasso.with(getContext()).load(BaseURL.getBaseUrl()+"uploads/members/"+image).into(imageView);
        name.setText(get_name);
        dob.setText("DOB: "+ Utils.convertDate(get_dob));
        age.setText("AGE: "+get_age);
        height.setText("HEIGHT: "+get_height);
        weight.setText("WEIGHT: "+get_weight);
        mother_name.setText("MOTHER NAME: "+get_mother_name);
        birth_place.setText("BIRTH PLACE: "+get_birth_place);
        birth_time.setText("BIRTH TIME: "+birthTime);
        education.setText("EDUCATION: "+get_education);
        occupation.setText("OCCUPATION: "+get_occupation);
        contact.setText("CONTACT: "+get_contact);
        nana.setText("NANA NANI'S VILLAGE: "+ get_material);

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

        add_matrimony_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater factory = LayoutInflater.from(getContext());
                final View DialogView = factory.inflate(R.layout.request_matrimony_dialog, null);
                final AlertDialog Dialog = new AlertDialog.Builder(getContext()).create();
                Dialog.setView(DialogView);

                Button dialogBtn_cancel = (Button) DialogView.findViewById(R.id.submit);
                final EditText name = (EditText) DialogView.findViewById(R.id.nameEt);
                final EditText email = (EditText) DialogView.findViewById(R.id.emailEt);
                final EditText phone = (EditText) DialogView.findViewById(R.id.phoneEt);

                dialogBtn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        rname = name.getText().toString().trim();
                        remail = email.getText().toString().trim();
                        rphone = phone.getText().toString().trim();

                        if (rname.equalsIgnoreCase("") || remail.equalsIgnoreCase("") || rphone.equalsIgnoreCase("")){
                            Toast.makeText(getActivity(),"All fields are required to request", Toast.LENGTH_LONG).show();
                            Dialog.dismiss();
                        }else{
                            sendMatrimonyRequest(rname, remail, rphone, member_no);
                            Dialog.dismiss();
                        }
                    }
                });
                Dialog.show();
            }
        });

        return view;
    }



    public void sendMatrimonyRequest(String name, String email, String phone, String member_no) {

        pd = ProgressDialog.show(getContext(), "", "Please wait, loading...", true);

        StringRequest request = new StringRequest(Request.Method.POST, BaseURL.getBaseUrl()+"Api/Matrirequiests/rcd", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    pd.dismiss();

                    JSONObject obj = new JSONObject(response);
                    String message = obj.optString("message");
                    String status = obj.optString("status");
                    String validate = obj.optString("validate");

                    if (status.equalsIgnoreCase("true") && validate.equalsIgnoreCase("true")){
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(), "Something went wrong, check your input details", Toast.LENGTH_SHORT).show();
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
                params.put("name", name);
                params.put("email", email);
                params.put("phone", phone);
                params.put("client", "android");
                params.put("ip", android_id);
                params.put("member_no", member_no);
                return params;
            }
        };
        queue.add(request);
    }


}