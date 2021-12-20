package com.animator.navigation.ui.blogs;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.animator.navigation.R;
import com.animator.navigation.ui.gallery.ImageFragment;
import com.animator.navigation.ui.profile.FamilyDetailsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.MyViewHolder> {

    Context context;
    ArrayList<Blogs> blogsArrayList;

    public BlogAdapter(Context context, ArrayList<Blogs> blogsArrayList) {
        this.context = context;
        this.blogsArrayList = blogsArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_blogs, parent,false);
        return new BlogAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Blogs blogObj = blogsArrayList.get(position);
        Picasso.with(context).load(blogObj.getBlogImage()).error(R.drawable.blood).into(holder.blogImg);
        holder.blogTitle.setText(blogObj.getBlogTitle());
        final String image = blogObj.getBlogImage();
        holder.todayCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new BlogDetailsFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                bundle.putString("slug", blogObj.getSlug());
                bundle.putString("img", blogObj.getBlogImage());
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return blogsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView blogImg;
        TextView blogTitle;
        CardView todayCv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            blogImg = (ImageView)itemView.findViewById(R.id.blogIv);
            blogTitle = (TextView)itemView.findViewById(R.id.blogTv);
            todayCv = (CardView) itemView.findViewById(R.id.todayCv);
        }
    }
}
