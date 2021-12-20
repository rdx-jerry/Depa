package com.animator.navigation.ui.matrimonial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.animator.navigation.R;

import java.util.ArrayList;

public class MarriageAdapter extends RecyclerView.Adapter<MarriageAdapter.MyViewHolder> {

    Context context;
    ArrayList<Marriage> marriageArrayList;

    public MarriageAdapter(Context context, ArrayList<Marriage> marriageArrayList) {
        this.context = context;
        this.marriageArrayList = marriageArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.layout_marriage, parent, false);
        return new MarriageAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Marriage marriage = marriageArrayList.get(position);
        holder.name.setText("Name: " + marriage.getName());
        holder.age.setText("Age: " + marriage.getAge());
    }

    @Override
    public int getItemCount() {
        return marriageArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, age;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.name);
            age = (TextView)itemView.findViewById(R.id.age);
        }
    }
}
