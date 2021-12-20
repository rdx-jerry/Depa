package com.animator.navigation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.ui.donate.DonateFragment;
import com.animator.navigation.ui.helpus.HelpUsPublicListFragment;
import com.animator.navigation.ui.home.HomeFragment;
import com.animator.navigation.ui.latest.LatestFragment;
import com.animator.navigation.ui.login.LoginFragment;
import com.animator.navigation.ui.profile.MatrimonyPublicListFragment;
import com.animator.navigation.ui.profile.ProfileFragment;
import com.animator.navigation.ui.todays.TodaysFragment;
import com.animator.navigation.ui.tools.BaseURL;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.OneSignal;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private static final String ONESIGNAL_APP_ID = "05fbbf9b-6525-48f2-8dfb-4d7c3d956014";

    public SharedPreferences share;
    public SharedPreferences.Editor editor;
    private RequestQueue queue;
    public String id;

    private AppBarConfiguration mAppBarConfiguration;
    private BottomNavigationView bottomNavigationView;
    String msg;
    String count;
    BottomNavigationMenuView menuView;
    BottomNavigationItemView itemView;
    View notificationBadge;
    TextView textView;

    String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_NUMBERS,
            Manifest.permission.CALL_PHONE
    };
    int PERMISSION_ALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        queue = Volley.newRequestQueue(this);
        share = getSharedPreferences("project", MODE_PRIVATE);
        id = share.getString("id", "");

        Log.e("main id", id);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //code for asking permissions at runtime
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.e("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        msg = token;
                        Log.e("TAG", msg);
                        sendToken(msg);

//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        getCount();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        itemView = (BottomNavigationItemView) menuView.getChildAt(2);

        notificationBadge = LayoutInflater.from(this).inflate(R.layout.custom_action_item_layout, menuView, false);
        textView = notificationBadge.findViewById(R.id.cart_badge);

//        textView.setText(count);
//        itemView.addView(notificationBadge);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        toolbar.setTitle("Home");
                        break;
                    case R.id.nav_news:
                        selectedFragment = new LatestFragment();
                        toolbar.setTitle("Latest");
                        break;
                    case R.id.nav_todays:
                        selectedFragment = new TodaysFragment();
                        toolbar.setTitle("Today");
                        break;
                    case R.id.nav_profile:
                        if (!id.equals("")) {
                            selectedFragment = new ProfileFragment();
                            toolbar.setTitle("Profile");

                            Toast.makeText(getApplicationContext(), "Profile ID: "+id, Toast.LENGTH_LONG).show();

                        } else {
                            selectedFragment = new LoginFragment();
                            toolbar.setTitle("Login");

                            Toast.makeText(getApplicationContext(), "Login ID: "+id, Toast.LENGTH_LONG).show();
                        }
                        break;
                }
                FragmentManager fragmentManager = getSupportFragmentManager();
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, selectedFragment).commit();
                return true;
            }
        });
        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);

        if (!id.equals("")) {
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_reset).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_directory).setVisible(true);
        } else {
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_reset).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_directory).setVisible(false);
        }

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_about, R.id.nav_profile, R.id.nav_directory, R.id.nav_news, R.id.nav_todays, R.id.nav_activity,
                R.id.nav_blogs, R.id.nav_business, R.id.nav_jobs, R.id.nav_gallery, R.id.nav_matrimonial, R.id.nav_contactus,
                R.id.nav_login, R.id.nav_logout, R.id.nav_reset, R.id.nav_share)
                .setDrawerLayout(drawer)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                navigationView.getMenu().findItem(R.id.nav_matrimonial).setVisible(false);
//                navigationView.getMenu().findItem(R.id.nav_share).setVisible(false);
//                navigationView.getMenu().findItem(R.id.nav_send).setVisible(false);
                if (item.getItemId() == R.id.nav_home) {
                    toolbar.setTitle("Home");
                } else if (item.getItemId() == R.id.nav_about) {
                    toolbar.setTitle("About");
                } else if (item.getItemId() == R.id.nav_news) {
                    toolbar.setTitle("Latest");
                } else if (item.getItemId() == R.id.nav_profile) {
                    toolbar.setTitle("Profile");
                } /*else if (item.getItemId() == R.id.nav_todays) {
                    toolbar.setTitle("Today");
                }*/ else if (item.getItemId() == R.id.nav_activity) {
                    toolbar.setTitle("Samiti");
                } else if (item.getItemId() == R.id.nav_blogs) {
                    toolbar.setTitle("Blogs");
                } else if (item.getItemId() == R.id.nav_business) {
                    toolbar.setTitle("Business");
                } else if (item.getItemId() == R.id.nav_jobs) {
                    toolbar.setTitle("Jobs");
                }else if (item.getItemId() == R.id.nav_matrimony) {
                    toolbar.setTitle("Sagpan Setu");

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    final MatrimonyPublicListFragment myFragment = new MatrimonyPublicListFragment();
                    Bundle b = new Bundle();
                    myFragment.setArguments(b);
                    fragmentTransaction.replace(R.id.frame, myFragment).commit();

                }else if (item.getItemId() == R.id.nav_helpus) {
                    toolbar.setTitle("Sthanik Milkat (LEH VECH)");

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    final HelpUsPublicListFragment myFragment = new HelpUsPublicListFragment();
                    Bundle b = new Bundle();
                    myFragment.setArguments(b);
                    fragmentTransaction.replace(R.id.frame, myFragment).commit();

                } else if (item.getItemId() == R.id.nav_gallery) {
                    toolbar.setTitle("Gallery");
                } else if (item.getItemId() == R.id.nav_galleryVideo) {
                    toolbar.setTitle("Video Gallery");
                } else if (item.getItemId() == R.id.nav_matrimonial) {

                } else if (item.getItemId() == R.id.nav_donate) {
                    toolbar.setTitle("Donations");
//                    Intent i = new Intent(MainActivity.this, DonateFragment.class);
//                    startActivity(i);

                    //toolbar.setTitle("Donate");
                    //Fragment donate = new DonateFragment();
                    //getSupportFragmentManager().beginTransaction().replace(R.id.frame, donate).addToBackStack(null).commit();
                } else if (item.getItemId() == R.id.nav_contactus) {
                    toolbar.setTitle("Contact Us");
                } else if (item.getItemId() == R.id.nav_share) {
                   /* Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(android.content.Intent.EXTRA_SUBJECT, "Application Name");
                    share.putExtra(android.content.Intent.EXTRA_TEXT, "Application Details");
                    startActivity(Intent.createChooser(share, "Choose sharing Application"));*/

                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                    return false;

                } /*else if (item.getItemId() == R.id.nav_send) {
                    Intent send = new Intent(Intent.ACTION_SEND);
                    send.setType("text/plain");
                    send.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Profile");
                    send.putExtra(android.content.Intent.EXTRA_TEXT, "Username \n Address \n Mobile");
                    startActivity(Intent.createChooser(send, "Choose sharing Application"));
                }*/ else if (item.getItemId() == R.id.nav_login) {
                    toolbar.setTitle("Login");

                    Toast.makeText(getApplicationContext(), "LOGIN", Toast.LENGTH_LONG).show();

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    final LoginFragment myFragment = new LoginFragment();
                    Bundle b = new Bundle();
                    myFragment.setArguments(b);
                    fragmentTransaction.replace(R.id.frame, myFragment).commit();

                } else if (item.getItemId() == R.id.nav_logout) {

                    editor = share.edit();
                    editor.clear();
                    editor.apply();
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else if (item.getItemId() == R.id.nav_reset) {
                    toolbar.setTitle("Reset Password");
                }

                //This is for maintaining the behavior of the Navigation view
                NavigationUI.onNavDestinationSelected(item, navController);
                //This is for closing the drawer after acting on it
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    public void sendToken(final String tokenStr) {
        StringRequest request = new StringRequest(Request.Method.POST,
                BaseURL.getBaseUrl()+"Api/member/new_token_service",
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e("response", response);
//                    if (jObj.getString("status") == "true") {
//                        Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
//                    }
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
                params.put("token", tokenStr);
                Log.e("paramsToken", params.toString());
                return params;
            }
        };
        queue.add(request);
    }

    public void getCount() {
        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/member/alltoday", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            Log.e("response", response);
                        if (jObj.getString("status") == "true") {
                            count = jObj.getString("count");
                            textView.setText(count);
                            itemView.addView(notificationBadge);
                            Log.e("jobjcount",  jObj.getString("count"));
                            Log.e("count", count);
                            textView.setVisibility(View.VISIBLE);
                        } else {
                            count = jObj.getString("count");
                            textView.setVisibility(View.GONE);
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
        });
        queue.add(request);
    }

    private void setAppLocale(String localeCode) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            conf.locale = new Locale(localeCode.toLowerCase());
        }
        res.updateConfiguration(conf, dm);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frag, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_english:
//                setAppLocale("en");
//                flag = 0;
//                Toast.makeText(this, "English", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
                setAppLocale("en");
                break;
            /*case R.id.action_gujarati:
//                setAppLocale("gu");
//                flag = 1;
//                Toast.makeText(this, "Gujarati", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
                setAppLocale("gu");
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
