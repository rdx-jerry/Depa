package com.animator.navigation.ui.home;

import android.app.Dialog;
//import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.about.FamilyTreeFragment;
import com.animator.navigation.ui.about.JovarFragment;
import com.animator.navigation.ui.blogs.BlogsFragment;
import com.animator.navigation.ui.business.BusinessFragment;
import com.animator.navigation.ui.death.DeathFragment;
import com.animator.navigation.ui.directory.DirectoryFragment;
import com.animator.navigation.ui.donors.DonorsFragment;
import com.animator.navigation.ui.gallery.GalleryFragment;
import com.animator.navigation.ui.jobs.JobsFragment;
import com.animator.navigation.ui.latest.LatestFragment;
import com.animator.navigation.ui.login.LoginFragment;

import com.animator.navigation.ui.profile.ProfileFragment;
import com.animator.navigation.ui.tools.BaseURL;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    Context context;
    private PopupWindow mPopupWindow;

    private WebView popWeb;
    private ImageButton close;
    private RelativeLayout frame;

    private static ViewPager homePager, mPager, newsPager, galleryPager, blogPager;
    private CirclePageIndicator indicatorHome, indicator, indicatorNews, indicatorGallery, indicatorBlog;
    private static int currentPage4 = 0;
    private static int NUM_PAGES4 = 0;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static int currentPage1 = 0;
    private static int NUM_PAGES1 = 0;
    private static int currentPage2 = 0;
    private static int NUM_PAGES2 = 0;
    private static int currentPage3 = 0;
    private static int NUM_PAGES3 = 0;

    private ArrayList<HomeModel> homeModelArrayList;
    private ArrayList<ImageModel> imageModelArrayList;
    private ArrayList<GalleryModel> galleryModelArrayList;
    private ArrayList<NewsModel> donerArrayList;
    private ArrayList<HBlogs> blogArrayList;

    private String advertiseFolder = BaseURL.getBaseUrl()+"assets/web/images/advertisement/";
    private String advertiseImage = BaseURL.getBaseUrl()+"Api/advertisement";
    private String folderHome = BaseURL.getBaseUrl()+"assets/web/images/home/";
    private String imgHome = BaseURL.getBaseUrl()+"Api/banner/";
    private String folderNews = BaseURL.getBaseUrl()+"uploads/news/";
    private String imgNews = BaseURL.getBaseUrl()+"Api/event/";
    private String folderGallery = BaseURL.getBaseUrl()+"uploads/gallery/";
    private String imgGallery = BaseURL.getBaseUrl()+"Api/gallery/home";
    private String folderDonors = BaseURL.getBaseUrl()+"uploads/donors/";
    private String imgDonors = BaseURL.getBaseUrl()+"Api/donors/";

    String folder = BaseURL.getBaseUrl()+"uploads/blog/";
    String url = BaseURL.getBaseUrl()+"Api/blog";

    private LinearLayout business, family, directory, jobs, mataji, death;
    private CardView latest, donors, gallery, blogs;
    String id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home");
        queue = Volley.newRequestQueue(getActivity());
        share = this.getActivity().getSharedPreferences("project", MODE_PRIVATE);

        frame = (RelativeLayout) root.findViewById(R.id.frame);
        homePager = (ViewPager) root.findViewById(R.id.viewPagerHome);
        indicatorHome = (CirclePageIndicator) root.findViewById(R.id.indicatorHome);
        mPager = (ViewPager) root.findViewById(R.id.viewPager);
        indicator = (CirclePageIndicator) root.findViewById(R.id.indicator);
        newsPager = (ViewPager) root.findViewById(R.id.viewPagerNews);
        indicatorNews = (CirclePageIndicator) root.findViewById(R.id.indicatorNews);
        galleryPager = (ViewPager) root.findViewById(R.id.viewPagerGallery);
        indicatorGallery = (CirclePageIndicator) root.findViewById(R.id.indicatorGallery);
        blogPager = (ViewPager) root.findViewById(R.id.viewPagerBlogs);
        indicatorBlog = (CirclePageIndicator) root.findViewById(R.id.indicatorBlogs);

        business = (LinearLayout) root.findViewById(R.id.business);
        family = (LinearLayout) root.findViewById(R.id.familytree);
        directory = (LinearLayout) root.findViewById(R.id.directory);
        jobs = (LinearLayout) root.findViewById(R.id.jobs);
        mataji = (LinearLayout) root.findViewById(R.id.mataji);
        death = (LinearLayout) root.findViewById(R.id.death);

        latest = (CardView) root.findViewById(R.id.latest);
        donors = (CardView) root.findViewById(R.id.donors);
        gallery = (CardView) root.findViewById(R.id.gallery);
        blogs = (CardView) root.findViewById(R.id.blog);


        homeModelArrayList = new ArrayList<>();
        imageModelArrayList = new ArrayList<>();
        galleryModelArrayList = new ArrayList<>();
        donerArrayList = new ArrayList<>();
        blogArrayList = new ArrayList<>();

        id = share.getString("id", "");

        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) getContext();
                Fragment myFragment = new BusinessFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });

        family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new FamilyTreeFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                bundle.putString("id", id);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });

        directory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share = view.getContext().getSharedPreferences("project", MODE_PRIVATE);
                id = share.getString("id", "");

                if (!id.equals("")) {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = new DirectoryFragment();
                    final Bundle bundle = new Bundle();
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                } else {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = new LoginFragment();
                    final Bundle bundle = new Bundle();
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                }

            }
        });

        jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new JobsFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });

        mataji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new JovarFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });

        death.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new DeathFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });

        latest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new LatestFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });

        donors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new DonorsFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new GalleryFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });

        blogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new BlogsFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });

        homePopup();
        initHome();
        init();
        initDonors();
        initGallery();
        initBlogs();
        return root;
    }

    private void homePopup() {

        final Dialog dialog = new Dialog(getContext());

        dialog.setContentView(R.layout.layout_popup_home);


        final ImageView addvertise_image = (ImageView) dialog.findViewById(R.id.hPop);
        final LinearLayout close = (LinearLayout) dialog.findViewById(R.id.close);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, advertiseImage, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    Picasso.with(getContext()).load(advertiseFolder + jObj.getString("image")).into(addvertise_image);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
        dialog.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void initHome() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, imgHome,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject ob = array.getJSONObject(i);
                                HomeModel listData = new HomeModel();
//                                listData.setName(ob.getString("title"));
                                listData.setImageurl(folderHome + ob.getString("images"));
                                homeModelArrayList.add(listData);
                            }
                            homePager.setAdapter(new HomePagerAdapter(getActivity(), homeModelArrayList));
                            indicatorHome.setViewPager(homePager);
                            final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
                            indicatorHome.setRadius(3 * density);

                            NUM_PAGES4 = homeModelArrayList.size();

                            // Auto start of viewpager
                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (currentPage4 == NUM_PAGES4) {
                                        currentPage4 = 0;
                                    }
                                    homePager.setCurrentItem(currentPage4++, true);
                                }
                            };
                            Timer swipeTimer = new Timer();
                            swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, 7000, 7000);

                            // Pager listener over indicator
                            indicatorHome.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                                @Override
                                public void onPageSelected(int position) {
                                    currentPage4 = position;
                                }

                                @Override
                                public void onPageScrolled(int pos, float arg1, int arg2) {

                                }

                                @Override
                                public void onPageScrollStateChanged(int pos) {

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    //Latest
    private void init() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, imgNews,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject ob = array.getJSONObject(i);
                                ImageModel listData = new ImageModel();
//                                listData.setName(ob.getString("title"));
                                listData.setImageurl(folderNews + ob.getString("image"));

                                imageModelArrayList.add(listData);
                            }
                            mPager.setAdapter(new ViewPagerAdapter(getActivity(), imageModelArrayList));
                            indicator.setViewPager(mPager);
                            final float density = getResources().getDisplayMetrics().density;
//Set circle indicator radius
                            indicator.setRadius(3 * density);
                            NUM_PAGES = imageModelArrayList.size();
                            // Auto start of viewpager
                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (currentPage == NUM_PAGES) {
                                        currentPage = 0;
                                    }
                                    mPager.setCurrentItem(currentPage++, true);
                                }
                            };
                            Timer swipeTimer = new Timer();
                            swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, 7000, 7000);

                            // Pager listener over indicator
                            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageSelected(int position) {
                                    currentPage = position;
                                }

                                @Override
                                public void onPageScrolled(int pos, float arg1, int arg2) {
                                }

                                @Override
                                public void onPageScrollStateChanged(int pos) {
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);

    }

    //Donors
    private void initDonors() {
//        donerArrayList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, imgDonors,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject ob = array.getJSONObject(i);
                                NewsModel listData = new NewsModel();
//                                listData.setName(ob.getString("donor_title"));
                                listData.setImageurl(folderDonors + ob.getString("donor_image"));
                                donerArrayList.add(listData);
                            }
                            newsPager.setAdapter(new NewsPagerAdapter(getActivity(), donerArrayList));
                            indicatorNews.setViewPager(newsPager);
                            final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
                            indicatorNews.setRadius(3 * density);

                            NUM_PAGES1 = donerArrayList.size();

                            // Auto start of viewpager
                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (currentPage1 == NUM_PAGES1) {
                                        currentPage1 = 0;
                                    }
                                    newsPager.setCurrentItem(currentPage1++, true);
                                }
                            };
                            Timer swipeTimer = new Timer();
                            swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, 7000, 7000);

                            // Pager listener over indicator
                            indicatorNews.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                                @Override
                                public void onPageSelected(int position) {
                                    currentPage1 = position;
                                }

                                @Override
                                public void onPageScrolled(int pos, float arg1, int arg2) {

                                }

                                @Override
                                public void onPageScrollStateChanged(int pos) {

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    //Gallery
    private void initGallery() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, imgGallery,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject ob = array.getJSONObject(i);
                                GalleryModel listData = new GalleryModel();
//                                listData.setName(ob.getString("title"));
                                listData.setImageurl(folderGallery + ob.getString("image"));
                                galleryModelArrayList.add(listData);
                            }
                            galleryPager.setAdapter(new GalleryPagerAdapter(getActivity(), galleryModelArrayList));
                            indicatorGallery.setViewPager(galleryPager);
                            final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
                            indicatorGallery.setRadius(3 * density);

                            NUM_PAGES2 = galleryModelArrayList.size();

                            // Auto start of viewpager
                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (currentPage2 == NUM_PAGES2) {
                                        currentPage2 = 0;
                                    }
                                    galleryPager.setCurrentItem(currentPage2++, true);
                                }
                            };
                            Timer swipeTimer = new Timer();
                            swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, 7000, 7000);

                            // Pager listener over indicator
                            indicatorGallery.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                                @Override
                                public void onPageSelected(int position) {
                                    currentPage2 = position;
                                }

                                @Override
                                public void onPageScrolled(int pos, float arg1, int arg2) {

                                }

                                @Override
                                public void onPageScrollStateChanged(int pos) {

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    private void initBlogs() {
//        donerArrayList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject ob = array.getJSONObject(i);
                                HBlogs listData = new HBlogs();
                                listData.setBlogTitle(ob.getString("title"));
                                listData.setBlogImage(folder + ob.getString("image"));
                                blogArrayList.add(listData);
                            }
                            blogPager.setAdapter(new BlogPagerAdapter(getActivity(), blogArrayList));
                            indicatorBlog.setViewPager(blogPager);
                            final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
                            indicatorBlog.setRadius(3 * density);

                            NUM_PAGES3 = blogArrayList.size();

                            // Auto start of viewpager
                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (currentPage3 == NUM_PAGES3) {
                                        currentPage3 = 0;
                                    }
                                    blogPager.setCurrentItem(currentPage3++, true);
                                }
                            };
                            Timer swipeTimer = new Timer();
                            swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, 7000, 7000);

                            // Pager listener over indicator
                            indicatorBlog.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                                @Override
                                public void onPageSelected(int position) {
                                    currentPage3 = position;
                                }

                                @Override
                                public void onPageScrolled(int pos, float arg1, int arg2) {

                                }

                                @Override
                                public void onPageScrollStateChanged(int pos) {

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

}
