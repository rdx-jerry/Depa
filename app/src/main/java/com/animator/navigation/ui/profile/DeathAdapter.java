package com.animator.navigation.ui.profile;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.animator.navigation.R;
import com.animator.navigation.ui.tools.BaseURL;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DeathAdapter extends RecyclerView.Adapter<DeathAdapter.MyViewHolder> {
    Context context;
    ArrayList<Family> families;

    public DeathAdapter(Context context, ArrayList<Family> families) {
        this.context = context;
        this.families = families;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_add_death, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Family familyObj = families.get(position);
//        holder.memberImage.setImageResource(familyObj.getImage());
        Picasso.with(context).load(BaseURL.getBaseUrl()+"uploads/members/"+familyObj.getImage()).into(holder.memberImage);;
        holder.memberName.setText(familyObj.getMemberName());
        holder.memberId.setText(familyObj.getMemberId());
        if (familyObj.getLive_type().equals("Death")) {
            holder.memberName.setText(familyObj.getMemberName() + " " + "(Late)");
        } else {
            holder.memberName.setText(familyObj.getMemberName());
            holder.deathLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = new MemberDeathFragment();
                    final Bundle bundle = new Bundle();
                    myFragment.setArguments(bundle);
                    bundle.putString("memberId", familyObj.getId());
                    bundle.putString("memberNm", familyObj.getMemberName());
                    bundle.putString("relation_id", familyObj.getRelation());
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return families.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView memberImage;
        TextView memberName, memberId;
        LinearLayout deathLL;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            memberImage = (ImageView)itemView.findViewById(R.id.picIv);
            memberName = (TextView)itemView.findViewById(R.id.mNameTv);
            memberId = (TextView)itemView.findViewById(R.id.mIdTv);
            deathLL = (LinearLayout)itemView.findViewById(R.id.deathLL);
        }
    }
}
