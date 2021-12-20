package com.animator.navigation.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.MainActivity;
import com.animator.navigation.R;
import com.animator.navigation.ui.gallery.GalleryFragment;
import com.animator.navigation.ui.home.HomeFragment;
import com.animator.navigation.ui.profile.FamilyDetailsFragment;
import com.animator.navigation.ui.register.RegisterFragment;
import com.animator.navigation.ui.tools.BaseURL;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.content.ContextCompat.getSystemService;

public class LoginFragment extends Fragment {

    public static final int REQUEST_CODE_PHONE_STATE_READ = 100;
    private int checkedPermission = PackageManager.PERMISSION_DENIED;

    private ProgressDialog dialog = null;

    private LoginViewModel mViewModel;

    public SharedPreferences share;
    public SharedPreferences.Editor editor;
    public RequestQueue queue;

    private String LOGIN = BaseURL.getBaseUrl()+"Api/login";


    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    String model = null;
    String IPaddress;
    private TextInputLayout inputLayoutUser, inputLayoutPass;
    private EditText usernameEt, passwordEt;
    private AppCompatCheckBox remembermeCb, showpassCb;
    private String userStr, passwordStr;
    private Button loginBt, forgetCredBt, altLoginBt, skipLoginBt;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.login);
        checkedPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE);
        if (Build.VERSION.SDK_INT >= 23 && checkedPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else
            checkedPermission = PackageManager.PERMISSION_GRANTED;

        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("project", MODE_PRIVATE);

        inputLayoutUser = (TextInputLayout) view.findViewById(R.id.inputUser);
        inputLayoutPass = (TextInputLayout) view.findViewById(R.id.inputPassword);
        usernameEt = (EditText) view.findViewById(R.id.userNameEt);
        passwordEt = (EditText) view.findViewById(R.id.passwordEt);
//        remembermeCb = (AppCompatCheckBox) view.findViewById(R.id.remembermeCb);
        showpassCb = (AppCompatCheckBox) view.findViewById(R.id.showpassCb);
        loginBt = (Button) view.findViewById(R.id.loginBt);
        forgetCredBt = (Button) view.findViewById(R.id.forgetCredBt);
        altLoginBt = (Button) view.findViewById(R.id.altLoginBt);
        skipLoginBt = (Button) view.findViewById(R.id.skipBt);

        showDeviceInfo();
        NetwordDetect();

        usernameEt.addTextChangedListener(new MyTextWatcher(usernameEt));
        passwordEt.addTextChangedListener(new MyTextWatcher(passwordEt));

        showpassCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    passwordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    passwordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login(userStr, passwordStr);
                loginPage();
            }
        });

        forgetCredBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //fragmentTransaction.replace(R.id.fragment_container, new ForgetCredFragment());
                fragmentTransaction.replace(R.id.frame, new ForgetCredFragment());
                fragmentTransaction.commit();
            }
        });

        altLoginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //fragmentTransaction.replace(R.id.fragment_container, new Login1Fragment());
                fragmentTransaction.replace(R.id.frame, new Login1Fragment());
                fragmentTransaction.commit();
            }
        });

        skipLoginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainAct = new Intent(getContext(), MainActivity.class);
                startActivity(mainAct);
                getActivity().finish();
            }
        });
        return view;
    }

    private void loginPage() {
        if (!validateUser()) {
            return;
        }
        if (!validatePass()) {
            return;
        }
    }

    private boolean validateUser() {
        if (usernameEt.getText().toString().trim().isEmpty()) {
            inputLayoutUser.setError(getString(R.string.err_msg_user));
            requestFocus(usernameEt);
            return false;
        } else {
            inputLayoutUser.setErrorEnabled(false);
            userStr = usernameEt.getText().toString().trim();
        }
        return true;
    }

    private boolean validatePass() {
        if (passwordEt.getText().toString().trim().isEmpty()) {
            inputLayoutPass.setError(getString(R.string.err_msg_pass));
            requestFocus(passwordEt);
            return false;
        } else {
            inputLayoutPass.setErrorEnabled(false);
            passwordStr = passwordEt.getText().toString().trim();
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        public MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.userNameEt:
                    validateUser();
                    break;
                case R.id.passwordEt:
                    validatePass();
                    break;
            }
        }
    }

    //Check the internet connection.
    private void NetwordDetect() {
        boolean WIFI = false;
        boolean MOBILE = false;
        ConnectivityManager CM = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = CM.getAllNetworkInfo();
        for (NetworkInfo netInfo : networkInfo) {
            if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))
                if (netInfo.isConnected())
                    WIFI = true;
            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))
                if (netInfo.isConnected())
                    MOBILE = true;
        }
        if(WIFI == true)
        {
            IPaddress = GetDeviceipWiFiData();
//            textview.setText(IPaddress);
        }
        if(MOBILE == true)
        {
            IPaddress = GetDeviceipMobileData();
//            textview.setText(IPaddress);
        }
    }

    public String GetDeviceipMobileData(){
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements();) {
                NetworkInterface networkinterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkinterface.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("Current IP", ex.toString());
        }
        return null;
    }

    public String GetDeviceipWiFiData()
    {
        WifiManager wm = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        @SuppressWarnings("deprecation")
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return ip;
    }

    public void showDeviceInfo() {
        TelephonyManager manager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
//        AlertDialog.Builder dBuilder = new AlertDialog.Builder(getContext());
        StringBuilder stringBuilder = new StringBuilder();

        if (checkedPermission != PackageManager.PERMISSION_DENIED) {
//            dBuilder.setTitle("Device Info");
            model = stringBuilder.append(Build.BRAND + " " + Build.MODEL).toString();
        } else {
//            dBuilder.setTitle("Permission denied");
            stringBuilder.append("Can't access device info !");
        }
//        dBuilder.setMessage(model);
//        dBuilder.show();
    }

    private void login(final String userStr, final String passStr) {
        dialog = ProgressDialog.show(getContext(), "Login", "Please wait...", true);
        StringRequest request = new StringRequest(Request.Method.POST, LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);
//                {"status":true,"validate":true,"message":"Successfully Login!","user":{"id":"3","member_no":"DPF0001-03","family_no":"DPF0001","family_id":"1","full_name":"Seema Uday Gangji","dob":"12\/October\/1970","dom":null,"email":"","member_type":"","live_type":"Live","contact":"9773018330"}}
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getString("status") == "true") {
                        Toast.makeText(getContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONObject jObj1 = new JSONObject(jObj.getString("user"));
                        editor=share.edit();
                        editor.putString("id", jObj1.getString("id"));
                        editor.putString("member_no", jObj1.getString("member_no"));
                        editor.putString("family_no", jObj1.getString("family_no"));
                        editor.putString("family_id", jObj1.getString("family_id"));
                        editor.putString("full_name", jObj1.getString("full_name"));
                        editor.putString("client", model);
                        editor.putString("IP", IPaddress);

                        editor.apply();
                        editor.commit();

                        dialog.dismiss();
                        startActivity(new Intent(getContext(), MainActivity.class));
                        getActivity().finish();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", userStr);
                params.put("password", passStr);
                params.put("client", model);
                params.put("ip", IPaddress);
                return params;
            }
        };
        queue.add(request);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        // TODO: Use the ViewModel
    }

}
