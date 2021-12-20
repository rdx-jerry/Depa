package com.animator.navigation.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.animator.navigation.R;
import com.animator.navigation.ui.login.NoPasswordGeneratePINFragment;
import com.animator.navigation.ui.tools.BaseURL;
import com.animator.navigation.ui.tools.Utils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;

public class MatrimonyMemberListAdapter extends RecyclerView.Adapter<MatrimonyMemberListAdapter.MyViewHolder> {

    Context context;
    ArrayList<MatrimonyMemberListModel> matrimonyMemberList;
    FragmentManager fragmentManager;

    public MatrimonyMemberListAdapter(Context context, ArrayList<MatrimonyMemberListModel> matrimonyMemberList) {
        this.context = context;
        this.matrimonyMemberList = matrimonyMemberList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.matrimony_member_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final MatrimonyMemberListModel value = matrimonyMemberList.get(position);
        //Picasso.with(context).load(BaseURL.getBaseUrl()+"uploads/members/"+familyObj.getImage()).into(holder.memberImage);
        String[] bt = value.getBirth_time().split(":");
        String btime = bt[0];
        Log.e("btime", btime);
        String bithTime;

        if (Integer.parseInt(btime) < 12) {
            bithTime = value.getBirth_time() + " am";
            Log.e("birthtime am", bithTime);

        } else {
            bithTime = value.getBirth_time() + " pm";
            Log.e("birthtime pm", bithTime);
        }

        holder.name.setText(value.getFullname());
        holder.email.setText(value.getEmail());
        holder.member_id.setText(value.getMember_no());

        String[] separated = value.getCreated_on().split(" ");
        String date = separated[0];

        holder.created.setText(Utils.convertDate(date));

        String status = value.getStatus();
        if (status.equalsIgnoreCase("1")){
            holder.status.setText("Approved");
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.green));
        }else{
            holder.status.setText("Pending");
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.orange));
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String backStateName = context.getClass().getName();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                Fragment myFragment = new MatrimonyDetailsFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);

                bundle.putString("image", value.getImage());
                bundle.putString("member_no", value.getMember_no());
                bundle.putString("name", value.getFullname());
                bundle.putString("dob", value.getDob());
                bundle.putString("height", value.getHeight());
                bundle.putString("weight", value.getWeight());
                bundle.putString("age", value.getAge());
                bundle.putString("mother_name", value.getMother_name());
                bundle.putString("birth_time", bithTime);
                bundle.putString("birth_place", value.getPlace());
                bundle.putString("education", value.getEducation());
                bundle.putString("occupation", value.getOccupation());
                bundle.putString("contact", value.getContact());
                bundle.putString("material", value.getMaterial());
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(backStateName).commit();


            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String backStateName = context.getClass().getName();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                Fragment myFragment = new MatrimonyDeleteMemberFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);

                bundle.putString("image", value.getImage());
                bundle.putString("member_no", value.getMember_no());
                bundle.putString("id", value.getMatrimonial_id());
                bundle.putString("name", value.getFullname());

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(backStateName).commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return matrimonyMemberList.size();
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

        TextView name, email, member_id, created, status, view, delete;
        LinearLayout mainLL;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.name);
            email = (TextView)itemView.findViewById(R.id.email);
            member_id = (TextView)itemView.findViewById(R.id.member_id);
            created = (TextView)itemView.findViewById(R.id.created);
            status = (TextView)itemView.findViewById(R.id.status);
            view = (TextView)itemView.findViewById(R.id.view);
            delete = (TextView)itemView.findViewById(R.id.delete);
            mainLL = (LinearLayout)itemView.findViewById(R.id.mainLL);

        }
    }
}
