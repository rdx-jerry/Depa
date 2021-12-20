package com.animator.navigation.ui.blogs;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.jobs.JobsFragment;
import com.animator.navigation.ui.tools.VolleyMultipartRequest;
import com.animator.navigation.ui.tools.BaseURL;
import com.animator.navigation.ui.tools.Utils;

import net.gotev.uploadservice.ContentType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class AddBlogFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    String memberId;
    private String upLoadServerUri = BaseURL.getBaseUrl()+"Api/blog/android";
    private EditText firm_name, description;
    private String titleStr, descriptionStr;
    private Button chooseImg, uploadBlog;
    private static final int PICK_IMAGE_REQUEST = 1;
    private int serverResponseCode = 0;
//    private Uri filePath;

//    private Bitmap bitmap;
//    private String filePath;

    private ProgressDialog dialog = null;
    private String pathUrl;


    private Uri filePath;
    List<byte[]> images = new ArrayList(3);

    int imgCount = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_add_blog, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add Blog");
        queue = Volley.newRequestQueue(getActivity());
        share = getActivity().getSharedPreferences("project", MODE_PRIVATE);
        memberId = share.getString("member_no", "");

        firm_name = (EditText)view.findViewById(R.id.blogName);
//        contactPerson = (EditText)view.findViewById(R.id.businessName);
//        contact_number = (EditText)view.findViewById(R.id.businessName);
        description = (EditText)view.findViewById(R.id.blog);

        chooseImg = (Button)view.findViewById(R.id.chooseImage);
        uploadBlog = (Button)view.findViewById(R.id.mButton) ;

        chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        uploadBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleStr = firm_name.getText().toString();
                descriptionStr = description.getText().toString();
//                pathUrl = getPath(filePath);

                if (titleStr.equals("") && descriptionStr.equals("")) {
                    Toast.makeText(getContext(), "Enter Title and Description and Select Image", Toast.LENGTH_SHORT).show();
                } else {
                    dialog = ProgressDialog.show(getContext(), "", "Uploading file...", true);
                    new Thread(new Runnable() {
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
//                                messageText.setText("uploading started.....");
                                }
                            });
//                        uploadFile(uploadFilePath + "" + uploadFileName);
//                        uploadFile(pathUrl);
                            UploadDataMultiImages(titleStr, descriptionStr);
//                            uploadBitmap(bitmap);
                        }
                    }).start();
                }


            }
        });

        return view;
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imgCount = 0;
        images.clear();
        chooseImg.setText("  Select Images");
        if (requestCode != 1 || resultCode != -1) {
            Toast.makeText(getContext(), "Not Selected Image", 0).show();
        } else if (data.getClipData() != null) {
            int count = data.getClipData().getItemCount();
            if (count > 3) {
                Utils.showToast(getContext(), "Kindly select maximum 3 images");
                return;
            }
            for (int i = 0; i < count; i++) {
                imgCount++;
                images.add(Utils.getImageFileToByte(getActivity(), data.getClipData().getItemAt(i).getUri()));
            }
        } else if (data.getData() != null) {
            images.add(Utils.getImageFileToByte(getActivity(), data.getData()));
        }
        if (images.size() > 0) {
            chooseImg.setText(String.format("  Select Images(%d)", new Object[]{Integer.valueOf(images.size())}));
        }
    }

    public String getPath(Uri uri) {
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

    public void UploadDataMultiImages(String gTitle, String gDesc) {
   
        final String str = gTitle;
        final String str2 = gDesc;
        VolleyMultipartRequest r4 = new VolleyMultipartRequest(1, upLoadServerUri, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                try {
                    Toast.makeText(getContext(), response.getString("message"), 1).show();
                    AppCompatActivity activity = (AppCompatActivity) getContext();
                    Fragment myFragment = new BlogsFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Context context = getContext();
                Toast.makeText(context, "Error : " + error.getMessage(), 0).show();
                dialog.dismiss();
            }
        }) {
            /* access modifiers changed from: protected */
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("memberid", memberId);
                params.put("title", titleStr);
                params.put("description", descriptionStr);
                params.put("ip", "0");
                params.put("client", "app");
                Log.e("params", params.toString());
                return params;

            }

            /* access modifiers changed from: protected */
            public Map<String, ArrayList<VolleyMultipartRequest.DataPart>> getByteData() {
                new HashMap();
                Map<String, ArrayList<VolleyMultipartRequest.DataPart>> imageList = new HashMap<>();
                ArrayList<VolleyMultipartRequest.DataPart> dataPart = new ArrayList<>();
                for (int i = 0; i < images.size(); i++) {
                    byte[] image = images.get(i);
                    Log.d("Add_Blog_Images", "image byte : " + image);
                    dataPart.add(new VolleyMultipartRequest.DataPart("image_" + i, image, ContentType.IMAGE_JPEG));
                }
                imageList.put("images[]", dataPart);
                Log.e("imageList", imageList.toString());
                return imageList;
            }
        };
        queue.add(r4);
        try {
            Log.d("add_blog_request", "Request body: " + new String(r4.getBody()));
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
