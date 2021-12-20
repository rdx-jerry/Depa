package com.animator.navigation.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;
import com.animator.navigation.ui.tools.BaseURL;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class FamilyDeathAdapter extends RecyclerView.Adapter<FamilyDeathAdapter.MyViewHolder> {

    Context context;
    ArrayList<Family> families;
    ArrayList<String> relationArray;
    private RequestQueue queue;
    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    String client, ip;

    ArrayList<String> checked_items= new ArrayList<>();
    ArrayList<String> member_id= new ArrayList<>();

    public FamilyDeathAdapter(Context context, ArrayList<Family> families) {
        this.context = context;
        this.families = families;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_family_death, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final MemberDeathFragment mdf = new MemberDeathFragment();

        final Family familyObj = families.get(position);
        Picasso.with(context).load(BaseURL.getBaseUrl()+"uploads/members/"+familyObj.getImage()).into(holder.memberImage);;
        holder.memberName.setText(familyObj.getMemberName());
        holder.memberId.setText(familyObj.getMemberId());

        if (familyObj.getLive_type().equals("Death")) {
            holder.memberName.setText(familyObj.getMemberName() + " " + "(Late)");
        } else {
            holder.memberName.setText(familyObj.getMemberName());
        }

        holder.relationSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                checked_items.add(String.valueOf(holder.relationSp.getSelectedItemId()));
                member_id.add(familyObj.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
        LinearLayout mainLL;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            memberImage = (ImageView)itemView.findViewById(R.id.picIv);
            memberName = (TextView)itemView.findViewById(R.id.mNameTv);
            memberId = (TextView)itemView.findViewById(R.id.mIdTv);
            relationSp = (Spinner) itemView.findViewById(R.id.relationSp);
            mainLL = (LinearLayout) itemView.findViewById(R.id.mainLL);
            share = context.getSharedPreferences("project", MODE_PRIVATE);
            queue = Volley.newRequestQueue(context);

            client = share.getString("client", "");
            ip = share.getString("IP", "");
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