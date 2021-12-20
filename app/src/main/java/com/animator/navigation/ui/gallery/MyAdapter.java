package com.animator.navigation.ui.gallery;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.animator.navigation.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
//    ArrayAdapter<List_data>
    Context context;
    ArrayList<List_data> listdata;

    public MyAdapter(Context context, ArrayList<List_data> listdata) {
        this.context = context;
        this.listdata = listdata;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_gallery, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final List_data listObj = listdata.get(position);

//        holder.img.setImageResource(listObj.getBlogImage());
        Picasso.with(context).load(listObj.getImageurl()).into(holder.img);
//        Picasso.with(context).load(listObj.getImageurl()).error(R.drawable.blood).into(holder.blogImg);
        holder.title.setText(listObj.getName() + " Images- " + listObj.getCount());
        holder.galleryCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new AlbumDetailFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                bundle.putString("slug", listObj.getSlug());
                bundle.putString("title", listObj.getName());
                bundle.putString("link", listObj.getLink());
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView img;
        CardView galleryCv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.titleTv);
            img = (ImageView)itemView.findViewById(R.id.icon);
            galleryCv = (CardView) itemView.findViewById(R.id.galleryCv);
        }
    }
}