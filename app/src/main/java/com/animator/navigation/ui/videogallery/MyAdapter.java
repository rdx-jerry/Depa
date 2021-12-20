package com.animator.navigation.ui.videogallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.animator.navigation.R;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    //    ArrayAdapter<List_data>
    Context context;
    ArrayList<List_data> listdata;
    Integer playlistIndex = 0;
    Glide glide;


    public MyAdapter(Context context, ArrayList<List_data> listdata) {
        this.context = context;
        this.listdata = listdata;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_video_gallery, parent,false);
        return new MyAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdapter.MyViewHolder holder, int position) {
        final List_data listObj = listdata.get(position);
        String api_key = "AIzaSyCLR0h3oLWq55zsmvz44h7CbRGWJeoCTSA";
        Picasso.with(this.context).load(listObj.getThumbnail()).into(holder.thumbnail);
        holder.title.setText(listObj.getTitle());
        holder.play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(listObj.getLink())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView play;
        ImageView thumbnail;
        TextView title;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.play = (ImageView) itemView.findViewById(R.id.play);
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }
}