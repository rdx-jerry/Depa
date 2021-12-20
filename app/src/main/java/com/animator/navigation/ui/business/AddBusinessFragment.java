package com.animator.navigation.ui.business;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.animator.navigation.ui.jobs.JobsFragment;
import com.animator.navigation.ui.tools.VolleyMultipartRequest;
import com.animator.navigation.ui.tools.BaseURL;
import com.animator.navigation.ui.tools.Utils;

import net.gotev.uploadservice.ContentType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class AddBusinessFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    private Button chooseImg, chooseImages, chooseCard, chooseVideo, selectPdf, uploadButton;

    private int serverResponseCode = 0;
    private ProgressDialog dialog = null;

    private String upLoadServerUri = BaseURL.getBaseUrl()+"Api/B2b/android";
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_IMAGE_REQUEST2 = 2;
    private static final int PICK_IMAGE_REQUEST3 = 22;
    private static final int PICK_PDF_REQUEST = 222;
    private static final int SELECT_VIDEO = 3;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    String displayName = null;
    Uri filePath, filePath1, filePath2, pdfFilePath;
    private String pathUrl, pathUrl1, selectedPath, updfPathUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private EditText firm_name, contactPerson, contact_number, email, description, subscription, address, client, ip, website, facebook, instagram, twitter,
            skype_id, whatsapp_number;
    private Spinner subscriptionSp;
    private Spinner cat_id;
    private String firm_nameStr, cat_idStr, contactPersonStr, contact_numberStr, emailStr, descriptionStr, subscriptionStr, addressStr, clientStr,
            ipStr, websiteStr,
            facebookStr, instagramStr, twitterStr, skype_idStr, whatsapp_numberStr;

    private ArrayList<String> categoryArray;
    private ArrayList<String> subscriptionArray;
    private JSONArray result;
    String memberId, memId;

    private Bitmap bitmap, bitmap1, bitmap2;


    List<byte[]> images = new ArrayList(3);
    byte[] logoBytes = null;
    byte[] visitingCardBytes = null;
    byte[] videoBytes = null;
    byte[] pdfFileBytes = null;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_business, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add Business");
        queue = Volley.newRequestQueue(getActivity());
        share = getActivity().getSharedPreferences("project", MODE_PRIVATE);
        memId = share.getString("id", "");
        memberId = share.getString("member_no", "");
        chooseImg = (Button) view.findViewById(R.id.chooseImage);
        chooseImages = (Button) view.findViewById(R.id.multiIv);
        chooseCard = (Button) view.findViewById(R.id.chooseCard);
        chooseVideo = (Button) view.findViewById(R.id.chooseVideo);
        selectPdf = (Button) view.findViewById(R.id.pdfBt);
        uploadButton = (Button) view.findViewById(R.id.mButton);

        firm_name = (EditText) view.findViewById(R.id.businessName);
        cat_id = (Spinner) view.findViewById(R.id.categorySp);
        contactPerson = (EditText) view.findViewById(R.id.contactPerson);
        contact_number = (EditText) view.findViewById(R.id.contact);
        email = (EditText) view.findViewById(R.id.email);
        description = (EditText) view.findViewById(R.id.description);
        subscriptionSp = (Spinner)view.findViewById(R.id.subscriptionSp);
        address = (EditText) view.findViewById(R.id.house);
        website = (EditText) view.findViewById(R.id.website);
        facebook = (EditText) view.findViewById(R.id.fblink);
        instagram = (EditText) view.findViewById(R.id.instalink);
        twitter = (EditText) view.findViewById(R.id.twitterlink);
        skype_id = (EditText) view.findViewById(R.id.skype_id);
        whatsapp_number = (EditText) view.findViewById(R.id.whatsapp);

        categoryArray = new ArrayList<String>();
        getCategoryData();
        subscriptionArray = new ArrayList<String>();
        getSubscriptionData();

        chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        chooseImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImages();
            }
        });

        chooseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseLogo();
            }
        });

        chooseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseVideo();
            }
        });

        selectPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePdf();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firm_nameStr = firm_name.getText().toString();
                cat_idStr = String.valueOf(cat_id.getSelectedItemId());
                contactPersonStr = contactPerson.getText().toString();
                contact_numberStr = contact_number.getText().toString();
                emailStr = email.getText().toString();
                descriptionStr = description.getText().toString();
                subscriptionStr = String.valueOf(subscriptionSp.getSelectedItemId());
                addressStr = address.getText().toString();
                clientStr = share.getString("client", "");
                ipStr = share.getString("ip", "");
                websiteStr = website.getText().toString();
                facebookStr = facebook.getText().toString();
                instagramStr = instagram.getText().toString();
                twitterStr = twitter.getText().toString();
                skype_idStr = skype_id.getText().toString();
                whatsapp_numberStr = whatsapp_number.getText().toString();
                pathUrl = getPath(filePath);
                pathUrl1 = getPath1(filePath1);
                selectedPath = getPath2(filePath2);
                updfPathUrl = getPath3(pdfFilePath);

                if (firm_nameStr.equals("") && cat_idStr.equals("") && contactPersonStr.equals("") && contact_numberStr.equals("") &&
                        emailStr.equals("") && descriptionStr.equals("") && subscriptionStr.equals("") && addressStr.equals("")) {
                    Toast.makeText(getContext(), "Please Enter Valid Fields", Toast.LENGTH_SHORT).show();
                } else {
                    dialog = ProgressDialog.show(getContext(), "Add Business", "Uploading file...", true);
                    new Thread(new Runnable() {
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
//                                messageText.setText("uploading started.....");
                                }
                            });
//                            uploadBitmap(bitmap, bitmap1, bitmap2, displayName, pdfUri);
                            submitToApi();
                        }
                    }).start();
                }
            }
        });
        return view;
    }

    private void getCategoryData() {
        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/Business/category",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray("data");
                            getCategory(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        queue.add(request);
    }

    private void getCategory(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject jSon = j.getJSONObject(i);
                categoryArray.add(jSon.getString("category"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cat_id.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner, categoryArray));
    }

    private void getSubscriptionData() {
        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/b2b/subscription",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray("data");
                            getSubscription(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        queue.add(request);
    }

    private void getSubscription(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject jSon = j.getJSONObject(i);
                subscriptionArray.add(jSon.getString("subscription"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        subscriptionSp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.layout_spinner, subscriptionArray));
    }

    //method to show file chooser
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    private void chooseLogo() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
    }

    private void chooseImages() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 22);
    }

    private void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select a Video "), 3);
    }

    private void choosePdf() {
        Intent intent = new Intent();
        intent.setType(ContentType.APPLICATION_PDF);
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == -1 && data != null && data.getData() != null) {
            filePath = data.getData();
            logoBytes = Utils.getImageFileToByte(getActivity(), data.getData());
        } else if (requestCode == 2 && resultCode == -1 && data != null && data.getData() != null) {
            filePath1 = data.getData();
            visitingCardBytes = Utils.getImageFileToByte(getActivity(), data.getData());
        } else if (requestCode == PICK_PDF_REQUEST && resultCode == -1 && data != null && data.getData() != null) {
            pdfFilePath = data.getData();
            pdfFileBytes = Utils.getPdfFileToByte(getActivity().getApplicationContext(), data.getData());
        } else if (requestCode == 22 && resultCode == -1) {
            chooseImages.setText("Select Images");
            images.clear();
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                if (count > 5) {
                    Utils.showToast(getContext(), "Kindly select maximum 5 images");
                    return;
                }
                for (int i = 0; i < count; i++) {
                    images.add(Utils.getImageFileToByte(getActivity(), data.getClipData().getItemAt(i).getUri()));
                }
            } else if (data.getData() != null) {
                images.add(Utils.getImageFileToByte(getActivity(), data.getData()));
            } else {
                Utils.showToast(getActivity().getApplicationContext(), "Not Image Selected");
            }
            if (images.size() > 0) {
                chooseImages.setText(String.format("Select Images(%d)", new Object[]{Integer.valueOf(images.size())}));
            }
        }
        if (resultCode == -1 && requestCode == 3) {
            System.out.println("SELECT_VIDEO");
            videoBytes = Utils.getPdfFileToByte(getActivity().getApplicationContext(), data.getData());
            filePath2 = data.getData();
        }
    }

    public String getPath(Uri uri) {
        if (uri == null) {
            return "";
        }
        Cursor cursor = getActivity().getContentResolver().query(uri, (String[]) null, (String) null, (String[]) null, (String) null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        String document_id2 = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        Cursor cursor2 = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, (String[]) null, "_id = ? ", new String[]{document_id2}, (String) null);
        cursor2.moveToFirst();
        String path = cursor2.getString(cursor2.getColumnIndex("_data"));
        cursor2.close();
        return path;
    }

    public String getPath1(Uri uri) {
        if (uri == null) {
            return "";
        }
        Cursor cursor = getActivity().getContentResolver().query(uri, (String[]) null, (String) null, (String[]) null, (String) null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        String document_id2 = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        Cursor cursor2 = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, (String[]) null, "_id = ? ", new String[]{document_id2}, (String) null);
        cursor2.moveToFirst();
        String path = cursor2.getString(cursor2.getColumnIndex("_data"));
        cursor2.close();
        return path;
    }

    public String getPath2(Uri uri) {
        if (uri == null) {
            return "";
        }
        Cursor cursor = getActivity().getContentResolver().query(uri, (String[]) null, (String) null, (String[]) null, (String) null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        String document_id2 = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        Cursor cursor2 = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, (String[]) null, "_id = ? ", new String[]{document_id2}, (String) null);
        cursor2.moveToFirst();
        String path = cursor2.getString(cursor2.getColumnIndex("_data"));
        cursor2.close();
        return path;
    }

    public String getPath3(Uri uri) {
        if (uri == null) {
            return "";
        }
        Cursor cursor = getActivity().getContentResolver().query(uri, new String[]{"_data"}, (String) null, (String[]) null, (String) null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex("_data"));
        cursor.close();
        return path;
    }

    public void submitToApi() {
        VolleyMultipartRequest r4 = new VolleyMultipartRequest(1, upLoadServerUri, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                try {
                    Toast.makeText(getContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                    AppCompatActivity activity = (AppCompatActivity) getContext();
                    Fragment myFragment = new BusinessFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("business", "Response body: " + response.toString());
            }
        }, new Response.ErrorListener() {
            public final void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            /* access modifiers changed from: protected */
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("memberid", memId);
                params.put("subscription", subscriptionStr);
                params.put("firm_name", firm_nameStr);
                params.put("cat_id", cat_idStr);
                params.put("description", descriptionStr);
                params.put("website", websiteStr);
                params.put("facebook", facebookStr);
                params.put("instagram", instagramStr);
                params.put("twitter", twitterStr);
                params.put("skype_id", skype_idStr);
                params.put("whatsapp_number", whatsapp_numberStr);
                params.put("contact_person", contactPersonStr);
                params.put("contact_number", contact_numberStr);
                params.put("email", emailStr);
                params.put("address", addressStr);
                params.put("ip", "0");
                params.put("client", "app");
                Log.e("params", params.toString());
                return params;
            }

            /* access modifiers changed from: protected */
            public Map<String, ArrayList<VolleyMultipartRequest.DataPart>> getByteData() {
                Map<String, ArrayList<VolleyMultipartRequest.DataPart>> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                ArrayList<VolleyMultipartRequest.DataPart> dataPart = new ArrayList<>();
                ArrayList<VolleyMultipartRequest.DataPart> pdfPart = new ArrayList<>();
                ArrayList<VolleyMultipartRequest.DataPart> logoPart = new ArrayList<>();
                ArrayList<VolleyMultipartRequest.DataPart> visitingPart = new ArrayList<>();
                ArrayList<VolleyMultipartRequest.DataPart> videoPart = new ArrayList<>();
                for (int i = 0; i < images.size(); i++) {
                    byte[] image = images.get(i);
                    Log.d("business", "image byte : " + image);
                    dataPart.add(new VolleyMultipartRequest.DataPart("bimages[]" + i, image, ContentType.IMAGE_JPEG));
                }
                params.put("bimages[]", dataPart);
                if (pdfFileBytes != null) {
                    pdfPart.add(new VolleyMultipartRequest.DataPart("pdf", pdfFileBytes, ContentType.APPLICATION_PDF));
                }
                params.put("pdf", pdfPart);
                if (logoBytes != null) {
                    logoPart.add(new VolleyMultipartRequest.DataPart("logo", logoBytes, ContentType.IMAGE_JPEG));
                }
                params.put("logo", logoPart);
                if (visitingCardBytes != null) {
                    visitingPart.add(new VolleyMultipartRequest.DataPart("visitingcard", visitingCardBytes, ContentType.IMAGE_JPEG));
                }
                params.put("visitingcard", visitingPart);
                if (videoBytes != null) {
                    videoPart.add(new VolleyMultipartRequest.DataPart("bvideo", videoBytes, "video/mp4"));
                }
                params.put("bvideo", videoPart);
                Log.e("params", params.toString());
                return params;
            }
        };
        queue.add(r4);
        try {
            Log.d("business", "Request body: " + new String(r4.getBody()));
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(getContext(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getContext(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
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
                    Fragment myFragment = new BusinessFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                    return true;
                }
                return false;
            }
        });
    }
}