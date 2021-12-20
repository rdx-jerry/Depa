package com.animator.navigation.ui.helpus;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.animator.navigation.R;
import com.animator.navigation.ui.profile.MatrimonyDetailsFragment;
import com.animator.navigation.ui.tools.BaseURL;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HelpUsPublicListAdapter extends RecyclerView.Adapter<HelpUsPublicListAdapter.MyViewHolder> {

    Context context;
    ArrayList<HelpUsListModel> helpUsListModels;
    FragmentManager fragmentManager;
    String getType;

    public HelpUsPublicListAdapter(Context context, ArrayList<HelpUsListModel> helpUsListModels, String type) {
        this.context = context;
        this.helpUsListModels = helpUsListModels;
        this.getType = type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.helpus_public_item_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            final HelpUsListModel value = helpUsListModels.get(position);

            holder.title.setText(value.getTitle());
            holder.name.setText("Contact Person: "+value.getContact_person());
            holder.contact.setText("Contact Number: "+value.getContact_number());
            holder.help_for.setText("Sthanik Milkat: "+value.getHelp_for());

            if (getType.equalsIgnoreCase("my")){

                holder.status.setVisibility(View.VISIBLE);

                if (value.getStatus().equalsIgnoreCase("0")) {
                    holder.status.setText("Status: Approval Pending...");
                    holder.titleBg.setBackgroundColor(ContextCompat.getColor(context, R.color.orange));
                }else if (value.getStatus().equalsIgnoreCase("1")) {
                    holder.status.setText("Status: Approved");
                    holder.titleBg.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
                }else if (value.getStatus().equalsIgnoreCase("2")) {
                    holder.status.setText("Status: Rejected");
                    holder.titleBg.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                }


            }else{
                holder.status.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String backStateName = context.getClass().getName();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();

                    Fragment myFragment = new HelpUsDetailsFragment();
                    final Bundle bundle = new Bundle();
                    myFragment.setArguments(bundle);

                    bundle.putString("id", value.getId());
                    bundle.putString("family_no", value.getFamily_no());
                    bundle.putString("member_no", value.getMember_no());
                    bundle.putString("title", value.getTitle());
                    bundle.putString("contact_person", value.getContact_person());
                    bundle.putString("contact_number", value.getContact_number());
                    bundle.putString("details", value.getDetails());
                    bundle.putString("help_for", value.getHelp_for());
                    bundle.putString("status", value.getStatus());
                    bundle.putString("created_date", value.getCreated_date());
                    bundle.putString("deleted", value.getDeleted());
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(backStateName).commit();

                }
            });



    }

    @Override
    public int getItemCount() {
        return helpUsListModels.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, name, contact, help_for, status;
        LinearLayout mainLL, titleBg, recordLL;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.title);
            name = (TextView)itemView.findViewById(R.id.name);
            contact = (TextView)itemView.findViewById(R.id.contact);
            help_for = (TextView)itemView.findViewById(R.id.help_for);
            status = (TextView)itemView.findViewById(R.id.status);
            recordLL = (LinearLayout)itemView.findViewById(R.id.recordLL);
            mainLL = (LinearLayout)itemView.findViewById(R.id.mainLL);
            titleBg = (LinearLayout)itemView.findViewById(R.id.titleBg);

        }
    }
}
