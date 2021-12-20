package com.animator.navigation.ui.jobs;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.VolleyMultipartRequest;
import com.animator.navigation.ui.tools.BaseURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class AddJobFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;
    String id;
    private String URL = BaseURL.getBaseUrl()+"Api/Job/android";

    private EditText title, contactPerson, contact, email, rangeStart, rangeEnd, experiance, opening, location, designation, description;
    TextView startDate, endDate;
//    private Spinner designation;
    private String titleStr, designationStr, contactPersonStr, contactStr, emailStr,
            rangeStartStr, rangeEndStr, openingStr, locationStr, startDateStr, endDateStr, experianceStr, descriptionStr;
    private ArrayList<String> occupationArray;
    private JSONArray result;
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog deathPicker;
    private Button chooseImg, submit;
    private Bitmap bitmap;
    private String filePath;
    private ProgressDialog dialog = null;
    private int PICK_IMAGE_REQUEST = 1;
//    private Uri filePath;
    String pathUrl;
    String uploadImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_job, container, false);
        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("project", MODE_PRIVATE);
        id =  share.getString("id", "");

        Log.e("MemberId", id);

        title = (EditText) view.findViewById(R.id.JobName);
        designation = (EditText) view.findViewById(R.id.designationSp);
        contactPerson = (EditText) view.findViewById(R.id.contactPerson);
        contact = (EditText) view.findViewById(R.id.contact);
        email = (EditText) view.findViewById(R.id.email);
        rangeStart = (EditText) view.findViewById(R.id.starteCtc);
        rangeEnd = (EditText) view.findViewById(R.id.endtCtc);
        opening = (EditText) view.findViewById(R.id.opening);
        location = (EditText) view.findViewById(R.id.house);
        startDate = (TextView) view.findViewById(R.id.startDate);
        endDate = (TextView) view.findViewById(R.id.endtDate);
        experiance = (EditText) view.findViewById(R.id.experiance);
        description = (EditText) view.findViewById(R.id.description);
        chooseImg = (Button)view.findViewById(R.id.chooseImage);
        submit = (Button)view.findViewById(R.id.mButton);

        occupationArray = new ArrayList<String>();
//        getOccupationData();

        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }

        };

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cldr = Calendar.getInstance();
                int day = cldr.get(5);
                int month = cldr.get(2);
                int year = cldr.get(1);
                deathPicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        startDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, year, month, day);
                deathPicker.show();
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cldr = Calendar.getInstance();
                int day = cldr.get(5);
                int month = cldr.get(2);
                int year = cldr.get(1);
                deathPicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        endDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, year, month, day);
                deathPicker.show();
            }
        });

        chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                titleStr = title.getText().toString();
                designationStr = designation.getText().toString();
                contactPersonStr = contactPerson.getText().toString();
                contactStr = contact.getText().toString();
                emailStr = email.getText().toString();
                rangeStartStr = rangeStart.getText().toString();
                rangeEndStr = rangeEnd.getText().toString();
                openingStr = opening.getText().toString();
                experianceStr = experiance.getText().toString();
                locationStr = location.getText().toString();
                startDateStr = startDate.getText().toString();
                endDateStr = endDate.getText().toString();
                descriptionStr = description.getText().toString();

                if (titleStr.equals("") && descriptionStr.equals("") && contactPersonStr.equals("") && emailStr.equals("") && rangeStartStr.equals("") &&
                        rangeEndStr.equals("") && openingStr.equals("") &&experianceStr.equals("") && locationStr.equals("") &&
                        startDateStr.equals("") && endDateStr.equals("") && descriptionStr.equals("")) {
                    Toast.makeText(getContext(), "Please Enter Valid Fields!", Toast.LENGTH_SHORT).show();
                } else {
                    dialog = ProgressDialog.show(getContext(), "Add Job", "Uploading file...", true);
                    addJob(bitmap);
                }
            }
        });
        return view;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri picUri = data.getData();
            filePath = getPath(picUri);
            if (filePath != null) {
                try {

//                    textView.setText("File Selected");
                    Log.d("filePath", String.valueOf(filePath));
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);
//                    uploadBitmap(bitmap);
//                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(
                        getActivity(), "no image selected",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public String getPath(Uri uri) {

        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void addJob(final Bitmap bitmap){
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        dialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            if (obj.getString("status") == "true") {
                                Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                Log.e("obj.getString(message) ", obj.getString("message"));
                                AppCompatActivity activity = (AppCompatActivity) getContext();
                                Fragment myFragment = new JobsFragment();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                            } else {
                                Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                Log.e("obj.getString(message) ", obj.getString("message"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("GotError", "" + error.getMessage());
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("memberid", id);
                params.put("firm_name", titleStr);
                params.put("designation", designationStr);
                params.put("contact_person", contactPersonStr);
                params.put("contact_number", contactStr);
                params.put("contact_email", emailStr);
                params.put("openings", openingStr);
                params.put("location", locationStr);
                params.put("salary_range_start", rangeStartStr);
                params.put("salary_range_end", rangeEndStr);
                params.put("description", descriptionStr);
                params.put("experience", experianceStr);
                params.put("start_date", startDateStr);
                params.put("end_date", endDateStr);
                params.put("ip", "1.1.1.1");
                params.put("client", "app");
                Log.e("Params ", params.toString());
                return params;
            }

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("logo", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                Log.e("Image Params ", params.toString());
                return params;
            }


        };
        //adding the request to volley
        Volley.newRequestQueue(getContext()).add(volleyMultipartRequest);
    }
}
