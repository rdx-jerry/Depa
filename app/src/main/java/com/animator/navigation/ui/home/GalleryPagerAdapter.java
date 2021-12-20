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
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.animator.navigation.R;
import com.animator.navigation.ui.blogs.BlogDetailsFragment;
import com.animator.navigation.ui.gallery.GalleryFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalleryPagerAdapter extends PagerAdapter {

    private ArrayList<GalleryModel> galleryModelArrayList;
    private LayoutInflater inflater;
    private Context context;
    private String imgPath;

    public GalleryPagerAdapter(Context context, ArrayList<GalleryModel> galleryModelArrayList) {
        this.context = context;
        this.galleryModelArrayList = galleryModelArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return galleryModelArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout2, view, false);

        assert imageLayout != null;
        final TextView title = (TextView) imageLayout.findViewById(R.id.title);
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image2);
        final GalleryModel galleryObj = galleryModelArrayList.get(position);
//        title.setText(galleryObj.getName());
        imgPath = galleryObj.getImageurl();
        Picasso.with(context).load(imgPath).into(imageView);
//        imageView.setImageResource(galleryObj.getImage_drawable());
        view.addView(imageLayout, 0);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new GalleryFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });
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