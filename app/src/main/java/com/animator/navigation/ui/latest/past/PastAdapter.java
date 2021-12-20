package com.animator.navigation.ui.latest.past;

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
import com.animator.navigation.ui.todays.birthday.BirthdayDetailsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PastAdapter extends RecyclerView.Adapter<PastAdapter.MyViewHolder> {

    Context context;
    ArrayList<Past> newsArrayList;

    public PastAdapter(Context context, ArrayList<Past> newsArrayList) {
        this.context = context;
        this.newsArrayList = newsArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_news, parent,false);
        return new PastAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Past newsObj = newsArrayList.get(position);
//        holder.newsIv.setImageURI(newsObj.getnewsImage());
        Picasso.with(context).load(newsObj.getNewsImage()).error(R.drawable.blood).into(holder.newsIv);
        holder.newsTitleTv.setText(newsObj.getNewsTitle());
        holder.dateTv.setText(newsObj.getEvent_date());
        holder.descTv.setText(newsObj.getDescription() + " . . . ");
        holder.newsCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new PastDetailFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                bundle.putString("id", newsObj.getId());
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView newsIv;
        TextView newsTitleTv, dateTv, descTv;
        CardView newsCv;
        String id;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            newsIv = (ImageView)itemView.findViewById(R.id.newsIv);
            newsTitleTv = (TextView)itemView.findViewById(R.id.newsTv);
            dateTv = (TextView)itemView.findViewById(R.id.dateTv);
            descTv = (TextView)itemView.findViewById(R.id.readTv);
            newsCv = (CardView)itemView.findViewById(R.id.newsCv);
        }
    }
}
