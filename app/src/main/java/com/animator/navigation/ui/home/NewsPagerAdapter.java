package com.animator.navigation.ui.home;

import android.content.Context;
import android.os.Parcelable;
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

import com.animator.navigation.R;
import com.animator.navigation.ui.donors.DonorsFragment;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsPagerAdapter extends PagerAdapter {

    private ArrayList<NewsModel> newsModelArrayList;
    private LayoutInflater inflater;
    private Context context;
    private String imgPath;

    public NewsPagerAdapter(Context context, ArrayList<NewsModel> newsModelArrayList) {
        this.context = context;
        this.newsModelArrayList = newsModelArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return newsModelArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout1, view, false);

        assert imageLayout != null;
//        final TextView title = (TextView) imageLayout.findViewById(R.id.title);
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image1);
        final NewsModel newsObj = newsModelArrayList.get(position);
//        title.setText(newsObj.getName());
        imgPath = newsObj.getImageurl();
        Picasso.with(context).load(imgPath).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new DonorsFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });
//        imageView.setImageResource(newsModelArrayList.get(position).getImage_drawable());
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}