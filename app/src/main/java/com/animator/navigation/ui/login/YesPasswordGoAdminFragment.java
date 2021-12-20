package com.animator.navigation.ui.login;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.contactus.ContactUsFragment;

import static android.content.Context.MODE_PRIVATE;
import static com.animator.navigation.ui.login.LoginFragment.REQUEST_CODE_PHONE_STATE_READ;

public class YesPasswordGoAdminFragment extends Fragment {

    public static final int REQUEST_CODE_PHONE_STATE_READ = 100;
    private int checkedPermission = PackageManager.PERMISSION_DENIED;

    public SharedPreferences share;
    public SharedPreferences.Editor editor;
    public RequestQueue queue;

    Button contactBtn;

    public static YesPasswordGoAdminFragment newInstance() {return new YesPasswordGoAdminFragment(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_yes_password_go_admin, container, false);

        View view = inflater.inflate(R.layout.fragment_yes_password_go_admin, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.login);
        checkedPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE);
        if (Build.VERSION.SDK_INT >= 23 && checkedPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else
            checkedPermission = PackageManager.PERMISSION_GRANTED;

        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("project", MODE_PRIVATE);

        contactBtn = (Button) view.findViewById(R.id.contactBtn);

        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new ContactUsFragment());
                fragmentTransaction.commit();
            }
        });

        return view;

    }

    private void requestPermission() {
        Toast.makeText(getContext(), "Requesting permission", Toast.LENGTH_SHORT).show();
        this.requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                REQUEST_CODE_PHONE_STATE_READ);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_PHONE_STATE_READ:
                if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED ) {
                    checkedPermission = PackageManager.PERMISSION_GRANTED;
                }
                break;
        }
    }

}