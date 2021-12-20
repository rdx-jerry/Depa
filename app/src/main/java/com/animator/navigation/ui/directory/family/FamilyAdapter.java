package com.animator.navigation.ui.directory.family;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.animator.navigation.R;
import com.animator.navigation.ui.directory.DetailsFragment;
import com.animator.navigation.ui.profile.MemberFragment;
import com.animator.navigation.ui.tools.BaseURL;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Family> families;

    public FamilyAdapter(Context context, ArrayList<Family> families) {
        this.context = context;
        this.families = families;
    }

    @NonNull
    @Override
    public FamilyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dir_family_list, parent, false);
        return new FamilyAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FamilyAdapter.MyViewHolder holder, int position) {
        final Family familyObj = families.get(position);
//        holder.memberImage.setImageResource(familyObj.getImage());
        Picasso.with(context).load(BaseURL.getBaseUrl()+"uploads/members/"+familyObj.getImage()).into(holder.memberImage);;
        holder.memberName.setText(familyObj.getMemberName());
        holder.memberId.setText(familyObj.getMemberId());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new MemberFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                bundle.putString("memberId", familyObj.getId());
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return families.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView memberImage;
        TextView view, memberName, memberId;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = (TextView)itemView.findViewById(R.id.viewBt);
            memberImage = (ImageView)itemView.findViewById(R.id.picIv);
            memberName = (TextView)itemView.findViewById(R.id.mNameTv);
            memberId = (TextView)itemView.findViewById(R.id.mIdTv);
        }
    }
}
