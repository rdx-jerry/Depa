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
import androidx.recyclerview.widget.RecyclerView;

import com.animator.navigation.R;
import com.animator.navigation.ui.tools.BaseURL;
import com.animator.navigation.ui.tools.Utils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;

public class MatrimonyPublicListAdapter extends RecyclerView.Adapter<MatrimonyPublicListAdapter.MyViewHolder> {

    Context context;
    ArrayList<MatrimonyMemberListModel> matrimonyMemberList;
    FragmentManager fragmentManager;

    public MatrimonyPublicListAdapter(Context context, ArrayList<MatrimonyMemberListModel> matrimonyMemberList) {
        this.context = context;
        this.matrimonyMemberList = matrimonyMemberList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.matrimony_public_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final MatrimonyMemberListModel value = matrimonyMemberList.get(position);
        Picasso.with(context).load(BaseURL.getBaseUrl()+"uploads/members/"+value.getImage()).into(holder.imageView);
        holder.name.setText(value.getFullname());

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


        holder.itemView.setOnClickListener(new View.OnClickListener() {
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

        ImageView imageView;
        TextView name;
        LinearLayout mainLL;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.name);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            mainLL = (LinearLayout)itemView.findViewById(R.id.mainLL);

        }
    }
}
