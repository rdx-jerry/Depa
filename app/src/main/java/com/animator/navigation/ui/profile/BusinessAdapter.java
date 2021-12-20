package com.animator.navigation.ui.profile;

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
                .inflate(R.layout.layout_business1, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Business businessObj = businessArrayList.get(position);
        holder.businessTitleTv.setText(businessObj.getBusinessTitle());
        holder.designationTv.setText(businessObj.getDesignation());
        holder.businessCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new MemberBusDetailFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                bundle.putString("id", businessObj.getId());
                bundle.putString("memberid", businessObj.getMemberid());
                bundle.putString("familyid", businessObj.getFamilyid());
                bundle.putString("bussness", businessObj.getBusinessTitle());
                bundle.putString("designation", businessObj.getDesignation());
                bundle.putString("address", businessObj.getAddress());
                bundle.putString("pincode", businessObj.getPincode());
                bundle.putString("area", businessObj.getArea());
                bundle.putString("email", businessObj.getEmail());
                bundle.putString("contact", businessObj.getContact());
                bundle.putString("website", businessObj.getWebsite());

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return businessArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView businessTitleTv, designationTv;
        CardView businessCv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            businessTitleTv = (TextView)itemView.findViewById(R.id.businessTv);
            designationTv = (TextView)itemView.findViewById(R.id.designationTv);
            businessCv = (CardView)itemView.findViewById(R.id.businessCv);
        }
    }
}
