package com.animator.navigation.ui.jobs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.MyViewHolder> {

    Context context;
    ArrayList<Jobs> jobsArrayList;

    public JobsAdapter(Context context, ArrayList<Jobs> jobsArrayList) {
        this.context = context;
        this.jobsArrayList = jobsArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_jobs, parent,false);
        return new JobsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Jobs jobObj = jobsArrayList.get(position);
//        holder.jobIv.setImageURI(jobObj.getJobImage());
//        Picasso.with(context).load(jobObj.getJobImage()).error(R.drawable.blood).into(holder.jobIv);
        holder.jobTitleTv.setText(jobObj.getJobTitle());
        holder.jobDesignationTv.setText(jobObj.getJobDesignation());
        Log.e("id", jobObj.getId());
        holder.jobCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new JobDetailsFragment();
                String img = jobObj.getJobImage();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                bundle.putString("id", jobObj.getId());
//                bundle.putString("title", holder.jobTitleTv.getText().toString());
//                bundle.putString("image", jobObj.getJobImage());
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
//        ImageView jobIv;
        TextView jobTitleTv, jobDesignationTv;
        CardView jobCv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            jobIv = (ImageView)itemView.findViewById(R.id.jobIv);
            jobTitleTv = (TextView)itemView.findViewById(R.id.jobTv);
            jobDesignationTv = (TextView)itemView.findViewById(R.id.designationTv);
            jobCv = (CardView)itemView.findViewById(R.id.jobCv);
        }
    }
}
