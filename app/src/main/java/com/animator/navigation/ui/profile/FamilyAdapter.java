package com.animator.navigation.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Family> families;

    public FamilyAdapter(Context context, ArrayList<Family> families) {
        this.context = context;
        this.families = families;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_family_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Family familyObj = families.get(position);

        if (familyObj.getStatus().equals("1")) {
            holder.mainLL.setVisibility(View.VISIBLE);
            holder.statusLL.setVisibility(View.GONE);
        } else {
            holder.mainLL.setVisibility(View.GONE);
            holder.statusLL.setVisibility(View.VISIBLE);
        }
//        holder.memberImage.setImageResource(familyObj.getImage());
        if (familyObj.getLive_type().equals("Death")) {
            holder.death.setVisibility(View.GONE);
            holder.memberName.setText(familyObj.getMemberName() + " " + "(Late)");
        } else {
            holder.death.setVisibility(View.VISIBLE);
            holder.memberName.setText(familyObj.getMemberName());
        }
        Picasso.with(context).load(BaseURL.getBaseUrl()+"uploads/members/"+familyObj.getImage()).into(holder.memberImage);;

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

        holder.viewStatus.setOnClickListener(new View.OnClickListener() {
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

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("member id and no", familyObj.getId() + " " + familyObj.getMemberId());
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new EditProfileFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                bundle.putString("memberId", familyObj.getId());
                bundle.putString("memberNo", familyObj.getMemberId());
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });

        holder.business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new MyBusinessFragment();
                final Bundle bundle = new Bundle();
                myFragment.setArguments(bundle);
                bundle.putString("memberId", familyObj.getId());
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });

        holder.death.setOnClickListener(new View.OnClickListener() {
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
//                AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                Fragment myFragment = new DeathFragment();
//                final Bundle bundle = new Bundle();
//                myFragment.setArguments(bundle);
//                bundle.putString("memberId", familyObj.getId());
//                bundle.putString("relation_id", familyObj.getRelation());
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new DeleteFragment();
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

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView memberImage;
        TextView memberName, memberId, view, viewStatus,  edit, business, death, delete;
        LinearLayout statusLL, mainLL;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            memberImage = (ImageView)itemView.findViewById(R.id.picIv);
            memberName = (TextView)itemView.findViewById(R.id.mNameTv);
            memberId = (TextView)itemView.findViewById(R.id.mIdTv);
            view = (TextView)itemView.findViewById(R.id.viewBt);
            viewStatus = (TextView)itemView.findViewById(R.id.viewStatusBt);
            edit = (TextView)itemView.findViewById(R.id.editBt);
            business = (TextView)itemView.findViewById(R.id.businessBt);
            death = (TextView)itemView.findViewById(R.id.deathBt);
            delete = (TextView)itemView.findViewById(R.id.deleteBt);
            statusLL = (LinearLayout)itemView.findViewById(R.id.statusLL);
            mainLL = (LinearLayout)itemView.findViewById(R.id.memberLL);
        }
    }
}
