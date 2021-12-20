package com.animator.navigation.ui.business;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.blogs.BlogsFragment;
import com.animator.navigation.ui.gallery.ImageFragment;
import com.animator.navigation.ui.tools.BaseURL;
import com.animator.navigation.ui.tools.Utils;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class BusinessDetailsFragment extends Fragment {
    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    String id;
    TextView titleTv, memberTv, catergoryTv, descriptionTv, websiteTv, facebookTv, instagramTv, twitterTv, youtubeTv, skype_idTv, pdfTv;
    TextView whatsappTv, subscriptionTv, contact_personTv, contact_numberTv, emailTv, addressTv, exDateTv;
    ImageView detailIv, visitingIv, imgOne, imgTwo, imgThree;
    VideoView video;
    LinearLayout pdfLL, videoLL;
    MediaController mediaController;
    Uri uri;
    String folder = BaseURL.getBaseUrl()+"uploads/b2b/";
    String businessById = BaseURL.getBaseUrl()+"Api/b2b/getbyid/";
    ImageButton shareTitle;
    String ImageLogo = "" ,ImageVisiting = "", ImageOne = "", ImageTwo = "", ImageThree = "";

    // creating a variable for exoplayerview.
    SimpleExoPlayerView exoPlayerView;

    // creating a variable for exoplayer
    SimpleExoPlayer exoPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_business_details, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Business");
        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("project", MODE_PRIVATE);

        exoPlayerView = view.findViewById(R.id.idExoPlayerVIew);

        shareTitle = (ImageButton) view.findViewById(R.id.share);
        titleTv = (TextView) view.findViewById(R.id.titleDetail);
        detailIv = (ImageView) view.findViewById(R.id.businessIv);
        visitingIv = (ImageView) view.findViewById(R.id.visitingIv);
        memberTv = (TextView) view.findViewById(R.id.member);
        catergoryTv = (TextView) view.findViewById(R.id.catergory);
        descriptionTv = (TextView) view.findViewById(R.id.description);
        websiteTv = (TextView) view.findViewById(R.id.website);
        facebookTv = (TextView) view.findViewById(R.id.facebook);
        instagramTv = (TextView) view.findViewById(R.id.instagram);
        youtubeTv = (TextView) view.findViewById(R.id.youtube);
        twitterTv = (TextView) view.findViewById(R.id.twitter);
        skype_idTv = (TextView) view.findViewById(R.id.skype_id);
        whatsappTv = (TextView) view.findViewById(R.id.whatsapp);
        subscriptionTv = (TextView) view.findViewById(R.id.subscription);
        contact_personTv = (TextView) view.findViewById(R.id.contact_person);
        contact_numberTv = (TextView) view.findViewById(R.id.contact_number);
        emailTv = (TextView) view.findViewById(R.id.email);
        addressTv = (TextView) view.findViewById(R.id.address);
        exDateTv = (TextView) view.findViewById(R.id.exDateTv);
        pdfTv = (TextView) view.findViewById(R.id.pdfTv);
        imgOne = (ImageView) view.findViewById(R.id.imgOne);
        imgTwo = (ImageView) view.findViewById(R.id.imgTwo);
        imgThree = (ImageView) view.findViewById(R.id.imgThree);
        video = (VideoView) view.findViewById(R.id.video);
        mediaController = new MediaController(getContext());
        pdfLL = (LinearLayout) view.findViewById(R.id.pdfLL);
        videoLL = (LinearLayout) view.findViewById(R.id.videoLL);

        id = getArguments().getString("id");
        Log.e("details id", id);

        Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();


        getDetails();

        detailIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ImageLogo.equalsIgnoreCase("")){
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = new ImageFragment();
                    final Bundle bundle = new Bundle();
                    myFragment.setArguments(bundle);
                    bundle.putString("image", ImageLogo);
                    bundle.putString("id", "Business");
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                }else{
                    Toast.makeText(getContext(), "No Image Available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        visitingIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ImageVisiting.equalsIgnoreCase("")){
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = new ImageFragment();
                    final Bundle bundle = new Bundle();
                    myFragment.setArguments(bundle);
                    bundle.putString("image", ImageVisiting);
                    bundle.putString("id", "Business");
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                }else{
                    Toast.makeText(getContext(), "No Image Available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ImageOne.equalsIgnoreCase("")){
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = new ImageFragment();
                    final Bundle bundle = new Bundle();
                    myFragment.setArguments(bundle);
                    bundle.putString("image", ImageOne);
                    bundle.putString("id", "Business");
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                }else{
                    Toast.makeText(getContext(), "No Image Available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ImageTwo.equalsIgnoreCase("")){
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = new ImageFragment();
                    final Bundle bundle = new Bundle();
                    myFragment.setArguments(bundle);
                    bundle.putString("image", ImageTwo);
                    bundle.putString("id", "Business");
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                }else{
                    Toast.makeText(getContext(), "No Image Available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ImageThree.equalsIgnoreCase("")){
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = new ImageFragment();
                    final Bundle bundle = new Bundle();
                    myFragment.setArguments(bundle);
                    bundle.putString("image", ImageThree);
                    bundle.putString("id", "Business");
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                }else{
                    Toast.makeText(getContext(), "No Image Available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        shareTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Shree Depa Jain Mahajan Trust");
                    String shareMessage = "\nShree Depa Jain Mahajan Trust \n\n";
                    shareMessage = shareMessage + "Business Name:  " + titleTv.getText().toString() + "\n\n" + "Category: " + catergoryTv.getText().toString() + "\n\n" +
                            "Contact Person: " + contact_personTv.getText().toString() + "\n\n" +
                            "Contact No.: " + contact_numberTv.getText().toString() + "\n\n" +  "Email: " + emailTv.getText().toString() + "\n\n" +
                            "Address: " + addressTv.getText().toString() + "\n\n" +  "WebSite: " + websiteTv.getText().toString() + "\n\n" +
                            "Facebook: " + facebookTv.getText().toString() + "\n\n" +  "Instagram: " + instagramTv.getText().toString(); //Company name
//                    shareMessage = shareMessage + catergoryTv.getText().toString() + "\n\n"; //category
//                    shareMessage = shareMessage + contact_personTv.getText().toString() + "\n\n"; //contact person
//                    shareMessage = shareMessage + contact_numberTv.getText().toString() + "\n\n"; //contact number
//                    shareMessage = shareMessage + emailTv.getText().toString() + "\n\n"; //email id
//                    shareMessage = shareMessage + websiteTv.getText().toString() + "\n\n"; //website
//                    shareMessage = shareMessage + addressTv.getText().toString() + "\n\n"; // address

                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });

        ((ScrollView) view.findViewById(R.id.scrollView)).post(new Runnable() {
            public void run() {
                ((ScrollView) view.findViewById(R.id.scrollView)).fullScroll(View.FOCUS_UP);
            }
        });

        return view;


    }

    public void getDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, businessById + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jArray = jObj.getJSONArray("businessdata");
                    Log.d("response", jArray.toString());
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject object = jArray.getJSONObject(i);
                        String member = object.getString("memid").trim();
                        String title = object.getString("firm_name").trim();
                        String catergory = object.getString("catergory").trim();
                        String description = object.getString("description").trim();
                        String website = object.getString("website").trim();
                        String facebook = object.getString("facebook").trim();
                        String instagram = object.getString("instagram").trim();
                        String twitter = object.getString("twitter").trim();
                        String youtube = object.getString("youtube").trim();
                        String skype_id = object.getString("skype_id").trim();
                        String whatsapp = object.getString("whatsapp_number").trim();
                        String subscription = object.getString("subscription").trim();
                        String contact_person = object.getString("contact_person").trim();
                        String contact_number = object.getString("contact_number").trim();
                        String email = object.getString("email").trim();
                        String address = object.getString("address").trim();
                        String exDate = Utils.convertDate(object.getString("expiry_date").trim());
                        String pdf = object.getString("pdf").trim();

                        if (pdf.equals("")) {
                            pdfLL.setVisibility(View.GONE);
                        } else {
                            pdfLL.setVisibility(View.VISIBLE);
                        }

                        titleTv.setText(title);
                        memberTv.setText(member);
                        catergoryTv.setText(catergory);
                        descriptionTv.setText(description);

                        websiteTv.setText(Html.fromHtml("<a href='"+website+"'>"+website+"</a>"));
                        facebookTv.setText(Html.fromHtml("<a href='"+facebook+"'>"+facebook+"</a>"));
                        instagramTv.setText( Html.fromHtml("<a href='"+instagram+"'>"+instagram+"</a>"));
                        youtubeTv.setText( Html.fromHtml("<a href='"+youtube+"'>"+youtube+"</a>"));
                        twitterTv.setText(Html.fromHtml("<a href='"+twitter+"'>"+twitter+"</a>"));
                        skype_idTv.setText(skype_id);
                        whatsappTv.setText(Html.fromHtml("<a href='https://wa.me/91"+whatsapp+"?Hello'>"+whatsapp+"</a>"));
                        //whatsappTv.setText(Html.fromHtml("<a href='"+facebook+"'>"+facebook+"</a>"));
                        whatsappTv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send/?phone=91"+whatsapp+"&text=Hey+I+have+a+query"));
                                startActivity(browserIntent);
                            }
                        });
                        subscriptionTv.setText(subscription);
                        contact_personTv.setText(contact_person);
                        contact_numberTv.setText(contact_number);
                        emailTv.setText(email);
                        addressTv.setText(address);
                        exDateTv.setText(exDate);
                        pdfTv.setText(pdf);
                        pdfTv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(folder+pdf));
                                startActivity(browserIntent);
                            }
                        });
                        Picasso.with(getContext()).load(folder + object.getString("logo")).into(detailIv);
                        ImageLogo = folder + object.getString("logo");
                        Picasso.with(getContext()).load(folder + object.getString("visitingcard")).into(visitingIv);
                        ImageVisiting = folder + object.getString("visitingcard");
                        String videoPath = folder + object.getString("video");
                        Log.e("video", videoPath);

                        if (videoPath.equals("")) {
                           videoLL.setVisibility(View.GONE);
                        } else {
                            videoLL.setVisibility(View.VISIBLE);
                        }

                        try {

                            // bandwisthmeter is used for
                            // getting default bandwidth
                            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

                            // track selector is used to navigate between
                            // video using a default seekbar.
                            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

                            // we are adding our track selector to exoplayer.
                            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

                            // we are parsing a video url
                            // and parsing its video uri.
                            Uri videouri = Uri.parse(videoPath);

                            // we are creating a variable for datasource factory
                            // and setting its user agent as 'exoplayer_view'
                            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");

                            // we are creating a variable for extractor factory
                            // and setting it to default extractor factory.
                            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

                            // we are creating a media source with above variables
                            // and passing our event handler as null,
                            MediaSource mediaSource = new ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null);

                            // inside our exoplayer view
                            // we are setting our player
                            exoPlayerView.setPlayer(exoPlayer);

                            // we are preparing our exoplayer
                            // with media source.
                            exoPlayer.prepare(mediaSource);

                            // we are setting our exoplayer
                            // when it is ready.
                            //exoPlayer.setPlayWhenReady(true);

                        } catch (Exception e) {
                            // below line is used for
                            // handling our errors.
                            Log.e("TAG", "Error : " + e.toString());
                        }

                        //andExoPlayerView.setSource(""+videoPath, "");

                        //video.start();
                        JSONArray jArray1 = jObj.getJSONArray("busenessimg");
                        Log.e("businessimg", jArray1.toString());
                        for (int j = 0; j < jArray1.length(); j++) {
                            JSONObject jO = jArray1.getJSONObject(j);

                            if (j == 0){
                                Picasso.with(getContext()).load(folder + jO.getString("image")).into(imgOne);
                                ImageOne = folder + jO.getString("image");
                            }

                            if (j == 1){
                                Picasso.with(getContext()).load(folder + jO.getString("image")).into(imgTwo);
                                ImageTwo = folder + jO.getString("image");
                            }

                            if (j == 2){
                                Picasso.with(getContext()).load(folder + jO.getString("image")).into(imgThree);
                                ImageThree = folder + jO.getString("image");
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley error: ", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        //getView().setFocusableInTouchMode(true);
        //getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {



                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    Toast.makeText(getContext(), "here i am ", Toast.LENGTH_SHORT).show();

                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Fragment myFragment = new BusinessFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                    return true;
                }
                return false;
            }
        });
    }



    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void releasePlayer(){
        if(exoPlayer!=null){
            exoPlayer.release();
            exoPlayer.clearVideoSurface();
            exoPlayerView.getPlayer().release();;

            exoPlayer = null;
            exoPlayerView =null;
        }
    }


}