package com.animator.navigation.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.animator.navigation.R;
import com.animator.navigation.ui.blogs.BlogDetailsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HBlogAdapter extends RecyclerView.Adapter<HBlogAdapter.MyViewHolder> {

    Context context;
    ArrayList<HBlogs> blogsArrayList;

    public HBlogAdapter(Context context, ArrayList<HBlogs> blogsArrayList) {
        this.context = context;
        this.blogsArrayList = blogsArrayList;
    }

    @NonNull
    @Override
    public HBlogAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_blog_home, parent,false);
        return new HBlogAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HBlogAdapter.MyViewHolder holder, int position) {
        final HBlogs blogObj = blogsArrayList.get(position);
//        holder.blogImg.setImageResource(blogObj.getBlogImage());
        Picasso.with(context).load(blogObj.getBlogImage()).error(R.drawable.blood).into(holder.blogImg);
        holder.blogTitle.setText(blogObj.getBlogTitle());
        holder.blogImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new BlogDetailsFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                bundle.putString("id", blogObj.getId());
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
