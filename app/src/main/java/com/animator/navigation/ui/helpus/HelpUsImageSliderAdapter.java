package com.animator.navigation.ui.helpus;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.animator.navigation.R;
import com.animator.navigation.ui.gallery.ImageFragment;
import com.animator.navigation.ui.tools.BaseURL;
import com.squareup.picasso.Picasso;

public class HelpUsImageSliderAdapter extends PagerAdapter {

    LayoutInflater inflater;
    private Context context;
    private String[] imageurls;

    public HelpUsImageSliderAdapter(Context context, String[] imageurls) {
        this.context = context;
        this.imageurls = imageurls;
        //Toast.makeText(context, String.valueOf(imageurls[1]), Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getCount() {
        return imageurls.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.helpus_details_image_item, null);

        //ImageView imageView = new ImageView(context);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        Picasso.with(context).load(BaseURL.getBaseUrl()+"uploads/helpus/"+imageurls[position]).fit().into(imageView);
        ViewPager pager=(ViewPager)container;
        pager.addView(view);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(context, "Link: "+BaseURL.getBaseUrl()+"uploads/helpus/"+imageurls[position], Toast.LENGTH_SHORT).show();

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new ImageFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                bundle.putString("image", BaseURL.getBaseUrl()+"uploads/helpus/"+imageurls[position]);
                bundle.putString("id", String.valueOf(position));
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();

            }
        });

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

}
