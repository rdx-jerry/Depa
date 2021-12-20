package com.animator.navigation.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.todays.birthday.BirthdayDetailsFragment;
import com.animator.navigation.ui.tools.BaseURL;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class FamilySplitAdapter extends RecyclerView.Adapter<FamilySplitAdapter.MyViewHolder> {

    Context context;
    ArrayList<Family> families;
    ArrayList<String> relationArray;
    private RequestQueue queue;
    private SharedPreferences share;

    ArrayList<String> checked_items= new ArrayList<>();
    ArrayList<String> member_id= new ArrayList<>();


    public FamilySplitAdapter(Context context, ArrayList<Family> families) {
        this.context = context;
        this.families = families;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_split, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Family familyObj = families.get(position);

        Picasso.with(context).load(BaseURL.getBaseUrl()+"uploads/members/"+familyObj.getImage()).into(holder.memberImage);;
        holder.memberName.setText(familyObj.getMemberName());
        holder.memberId.setText(familyObj.getMemberId());

        holder.selectM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.relationSp.setVisibility(View.VISIBLE);
                    Log.e("spin", String.valueOf(holder.relationSp.getSelectedItemId()));
                    checked_items.add(String.valueOf(holder.relationSp.getSelectedItemId()));
                    member_id.add(familyObj.getId());
                } else {
                    holder.relationSp.setVisibility(View.GONE);
                }
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
        TextView memberName, memberId;
        Spinner relationSp;

        CheckBox selectM;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            memberImage = (ImageView)itemView.findViewById(R.id.picIv);
            memberName = (TextView)itemView.findViewById(R.id.mNameTv);
            memberId = (TextView)itemView.findViewById(R.id.mIdTv);
            relationSp = (Spinner) itemView.findViewById(R.id.relationSp);
            selectM = (CheckBox) itemView.findViewById(R.id.selectCb);

            share = context.getSharedPreferences("project", MODE_PRIVATE);
            queue = Volley.newRequestQueue(context);
            relationArray = new ArrayList<>();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, BaseURL.getBaseUrl()+"Api/masters/relation", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        JSONArray jArray = jObj.getJSONArray("data");
                        relationArray = new ArrayList<>();
                        for (int i = 0; i < jArray.length(); i++) {
                            Relation re = new Relation();
                            JSONObject reJson = jArray.getJSONObject(i);
                            re.setRelation(reJson.getString("relation"));
                            relationArray.add(re.getRelation());
                        }
                        relationSp.setAdapter(new ArrayAdapter<String>(context, R.layout.spinner, relationArray));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue.add(stringRequest);
        }
    }
}
