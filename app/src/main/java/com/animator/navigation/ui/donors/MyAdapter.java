package com.animator.navigation.ui.donors;

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
import com.animator.navigation.ui.gallery.AlbumDetailFragment;
import com.animator.navigation.ui.gallery.ImageFragment;
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final List_data listObj = listdata.get(position);
        Picasso.with(context).load(listObj.getImageurl()).into(holder.img);
        holder.title.setText(listObj.getName());
        final String image = listObj.getImageurl();
        holder.galleryCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new ImageFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                bundle.putString("image", image);
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