package com.animator.navigation.ui.todays.marannondh;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.animator.navigation.R;
import com.animator.navigation.ui.login.LoginFragment;
import com.animator.navigation.ui.todays.birthday.BirthdayDetailsFragment;
import com.animator.navigation.ui.todays.birthday.Event;
import com.animator.navigation.ui.todays.prarthana.PrarthanaDetailsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MaranNondhAdapter extends RecyclerView.Adapter<MaranNondhAdapter.MyViewHolder> {

    //    private LayoutInflater inflater;
    private ArrayList<MaranNondhModel> maranNondhModels;
    Context context;
    String adapter_type;

    public MaranNondhAdapter(Context context, ArrayList<MaranNondhModel> maranNondhModels, String type) {
        this.context = context;
        this.maranNondhModels = maranNondhModels;
        this.adapter_type = type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_todays, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final String memberId;
        final MaranNondhModel ev = maranNondhModels.get(position);
        holder.name.setText(ev.getFullname());
        memberId = ev.getId();

        Picasso.with(context).load(ev.getImage()).into(holder.photo);

        holder.todayCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences share = view.getContext().getSharedPreferences("project", MODE_PRIVATE);
                String id = share.getString("id", "");

                if (!id.equals("")) {

                    if (adapter_type.equalsIgnoreCase("prarthana")){
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        Fragment myFragment = new PrarthanaDetailsFragment();
                        final Bundle bundle = new Bundle();
                        myFragment.setArguments(bundle);
                        bundle.putString("memberId", memberId);
                        bundle.putString("member_no", ev.getFamily_person());
                        bundle.putString("family_no", ev.getFamily_no());
                        bundle.putString("fullname", ev.getFullname());
                        bundle.putString("profile", ev.getImage());
                        bundle.putString("date", ev.getDate());
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                    }else {
                        //Toast.makeText(context, ev.getContact(), Toast.LENGTH_SHORT).show();
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        Fragment myFragment = new MaranNondhDetailsFragment();
                        final Bundle bundle = new Bundle();
                        myFragment.setArguments(bundle);
                        bundle.putString("memberId", memberId);
                        bundle.putString("member_no", ev.getFamily_person());
                        bundle.putString("family_no", ev.getFamily_no());
                        bundle.putString("fullname", ev.getFullname());
                        bundle.putString("profile", ev.getImage());
                        bundle.putString("contact", ev.getContact());
                        bundle.putString("date", ev.getDate());
                        bundle.putString("description", ev.getDescription());
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                    }
                } else {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = new LoginFragment();
                    final Bundle bundle = new Bundle();
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return maranNondhModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView photo;
        CardView todayCv;


        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nameTv);
            photo = (ImageView) itemView.findViewById(R.id.imageView);
            todayCv = (CardView) itemView.findViewById(R.id.todayCv);

        }
    }
}
