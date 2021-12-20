package com.animator.navigation.ui.home;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.animator.navigation.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomePagerAdapter extends PagerAdapter{
    private ArrayList<HomeModel> homeModelArrayList;
    private LayoutInflater inflater;
    private Context context;
    private String imgPath;

    public HomePagerAdapter(Context context, ArrayList<HomeModel> homeModelArrayList) {
        this.context = context;
        this.homeModelArrayList = homeModelArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return homeModelArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
//        final TextView title = (TextView) imageLayout.findViewById(R.id.title);
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
        final HomeModel homeObj = homeModelArrayList.get(position);
//        title.setText(homeObj.getName());
        imgPath = homeObj.getImageurl();
        Picasso.with(context).load(imgPath).into(imageView);
//        imageView.setImageResource(homeModelArrayList.get(position).getImage_drawable());

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