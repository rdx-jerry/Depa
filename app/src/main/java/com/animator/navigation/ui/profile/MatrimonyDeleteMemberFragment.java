package com.animator.navigation.ui.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MatrimonyDeleteMemberFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    String id, member_no, image, get_name;
    Button add_matrimony_request;
    ImageView imageView;
    TextView name;

    String rname;
    ProgressDialog pd;
    Button delete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matrimony_delete_member, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Sagpan Setu Delete");
        queue = Volley.newRequestQueue(getContext());

        image = getArguments().getString("image");
        id = getArguments().getString("id");
        member_no = getArguments().getString("member_no");
        get_name = getArguments().getString("name");

        add_matrimony_request = (Button) view.findViewById(R.id.add_matrimony_request);
        imageView = (ImageView) view.findViewById(R.id.image);
        name = (TextView) view.findViewById(R.id.name);
        delete = (Button) view.findViewById(R.id.delete);

        Picasso.with(getContext()).load(BaseURL.getBaseUrl()+"uploads/members/"+image).into(imageView);
        name.setText(get_name);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getContext(), id+" :: "+member_no, Toast.LENGTH_SHORT).show();
                deleteMember(id, member_no);

            }
        });

        return view;
    }


    public void deleteMember(String id, String member_no) {

        pd = ProgressDialog.show(getContext(), "Deleting", "Please wait...", true);

        StringRequest request = new StringRequest(Request.Method.POST, BaseURL.getBaseUrl()+"Api/matrimonial/updatedelete", new Response.Listener<String>() {
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

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, new MatrimonyMemberListFragment());
                        fragmentTransaction.commit();

                    }else{
                        Toast.makeText(getContext(), message+", Something went wrong...", Toast.LENGTH_SHORT).show();
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
                params.put("id", id);
                params.put("member_no", member_no);
                return params;
            }
        };
        queue.add(request);
    }

}