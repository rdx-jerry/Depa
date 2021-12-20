package com.animator.navigation.ui.home;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.animator.navigation.R;
import com.animator.navigation.ui.blogs.BlogDetailsFragment;
import com.animator.navigation.ui.donors.DonorsFragment;

import com.animator.navigation.ui.latest.LatestFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    private ArrayList<ImageModel> imageModelArrayList;
    private LayoutInflater inflater;
    private Context context;
    private String imgPath;

    public ViewPagerAdapter(Context context, ArrayList<ImageModel> imageModelArrayList) {
        this.context = context;
        this.imageModelArrayList = imageModelArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
//        final TextView title = (TextView) imageLayout.findViewById(R.id.title);
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
        final ImageModel  imgObj = imageModelArrayList.get(position);

//        title.setText(imgObj.getName());
        imgPath = imgObj.getImageurl();
        Picasso.with(context).load(imgPath).into(imageView);
        view.addView(imageLayout, 0);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new LatestFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();

            }
        });
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
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