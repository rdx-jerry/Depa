package com.animator.navigation.ui.todays.punyatithi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.animator.navigation.R;
import com.animator.navigation.ui.login.LoginFragment;
import com.animator.navigation.ui.todays.birthday.BirthdayDetailsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private ArrayList<Event> events;
    Context context;

    public EventAdapter(Context context, ArrayList<Event> events) {
        this.context = context;
        this.events = events;
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
        final Event ev = events.get(position);
        holder.name.setText(ev.getName());
        memberId = ev.getId();

        Picasso.with(context).load(ev.getImage()).into(holder.photo);

        holder.todayCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences share = view.getContext().getSharedPreferences("project", MODE_PRIVATE);
                String id = share.getString("id", "");

                if (!id.equals("")) {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = new PunyatithiDetailFragment();
                    final Bundle bundle = new Bundle();
                    myFragment.setArguments(bundle);
                    bundle.putString("memberId", ev.getId());
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
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
        return events.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, date;
        ImageView photo;
        CardView todayCv;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nameTv);
            date = (TextView) itemView.findViewById(R.id.dateTv);
            photo = (ImageView) itemView.findViewById(R.id.imageView);
            todayCv = (CardView) itemView.findViewById(R.id.todayCv);
        }
    }
}
