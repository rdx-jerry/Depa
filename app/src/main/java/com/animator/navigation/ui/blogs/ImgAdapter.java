package com.animator.navigation.ui.blogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.animator.navigation.R;
import com.animator.navigation.ui.tools.BaseURL;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.MyViewHolder> {

    Context context;
    ArrayList<BlogImage> blogsArrayList;

    public ImgAdapter(Context context, ArrayList<BlogImage> blogsArrayList) {
        this.context = context;
        this.blogsArrayList = blogsArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_blog_img, parent,false);
        return new ImgAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final BlogImage blogObj = blogsArrayList.get(position);
        Picasso.with(context).load(BaseURL.getBaseUrl()+"uploads/blog/" + blogObj.getImg()).into(holder.img);
        final String image = BaseURL.getBaseUrl()+"uploads/blog/" + blogObj.getImg();
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new BlogImageFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                //Toast.makeText(getContext(), "GG:"+image, Toast.LENGTH_SHORT).show();
                bundle.putString("image", image);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return blogsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.icon);
        }
    }
}
