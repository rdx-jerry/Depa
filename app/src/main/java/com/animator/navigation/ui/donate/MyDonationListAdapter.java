package com.animator.navigation.ui.donate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.animator.navigation.R;

import java.util.ArrayList;

public class MyDonationListAdapter extends RecyclerView.Adapter<MyDonationListAdapter.MyViewHolder> {

    Context context;
    ArrayList<DonationLIst> donationLIsts;

    public  MyDonationListAdapter(Context context, ArrayList<DonationLIst> donationLIsts) {
        this.context = context;
        this.donationLIsts = donationLIsts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_my_donation_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final DonationLIst value = donationLIsts.get(position);
        holder.name.setText(value.getName());
        holder.email.setText(value.getEmail());
        holder.memberId.setText(value.getMemberId());
        holder.amount.setText(value.getAmount());

        String status = value.getStatus();
        if (status.equalsIgnoreCase("1")){
            holder.status.setText("Approved");
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.green));
        }else{
            holder.status.setText("Pending");
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.orange));
        }

//        holder.status.setText(value.getStatus());
        holder.viewBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pdfIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(value.getPdf()));
                context.startActivity(pdfIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return donationLIsts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, memberId, amount, status, viewBt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            memberId = itemView.findViewById(R.id.member_id);
            amount = itemView.findViewById(R.id.amount);
            status = itemView.findViewById(R.id.status);
            viewBt = itemView.findViewById(R.id.view);
        }
    }
}
