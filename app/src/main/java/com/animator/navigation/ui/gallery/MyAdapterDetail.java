package com.animator.navigation.ui.gallery;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.animator.navigation.R;
import com.animator.navigation.ui.blogs.BlogDetailsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapterDetail extends ArrayAdapter<List_data> {
    ArrayList<List_data> listdata;
    Context context;
    int resource;
    public MyAdapterDetail(@NonNull Context context, int resource, @NonNull ArrayList<List_data> listdata) {
        super(context, resource, listdata);
        this.listdata=listdata;
        this.context=context;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.layout_gallery_detail,null,true);
        }
        final List_data listdata = getItem(position);
        final ImageView img = (ImageView)convertView.findViewById(R.id.icon);
//        final ImageView imgOpen = (ImageView)convertView.findViewById(R.id.iconOpen);
        Picasso.with(context).load(listdata.getImageurl()).into(img);
        final String image = listdata.getImageurl();
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new ImageFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                //Toast.makeText(getContext(), "GG:"+image, Toast.LENGTH_SHORT).show();
                bundle.putString("image", image);
                bundle.putString("id", listdata.getId());
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });


        return convertView;
    }
}