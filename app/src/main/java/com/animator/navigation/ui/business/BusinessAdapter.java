package com.animator.navigation.ui.business;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.animator.navigation.R;
import com.animator.navigation.ui.blogs.BlogDetailsFragment;
import com.animator.navigation.ui.profile.FamilyDetailsFragment;
import com.animator.navigation.ui.tools.BaseURL;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.MyViewHolder> {

    Context context;
    ArrayList<Business> businessArrayList;

    public BusinessAdapter(Context context, ArrayList<Business> businessArrayList) {
        this.context = context;
        this.businessArrayList = businessArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_business, parent,false);
        return new BusinessAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Business businessObj = businessArrayList.get(position);
        Picasso.with(context).load(BaseURL.getBaseUrl()+"uploads/b2b/"+businessObj.getbusinessImage()).error(R.drawable.blood).into(holder.businessIv);
        holder.businessTitleTv.setText(businessObj.getbusinessTitle());
        holder.designationTv.setText(businessObj.getBusinessCategory());
        holder.businessCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new BusinessDetailsFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                bundle.putString("id", businessObj.getId());

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return businessArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView businessIv;
        TextView businessTitleTv, designationTv;
        CardView businessCv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            businessIv = (ImageView)itemView.findViewById(R.id.businessIv);
            businessTitleTv = (TextView)itemView.findViewById(R.id.businessTv);
            designationTv = (TextView)itemView.findViewById(R.id.designationTv);
            businessCv = (CardView)itemView.findViewById(R.id.businessCv);
        }
    }
}
