package com.animator.navigation.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
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

public class FamilyMergeAdapter extends RecyclerView.Adapter<FamilyMergeAdapter.MyViewHolder> {

    Context context;
    ArrayList<Family> families;
    ArrayList<String> member_id= new ArrayList<>();


    public FamilyMergeAdapter(Context context, ArrayList<Family> families) {
        this.context = context;
        this.families = families;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_merge, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Family familyObj = families.get(position);


        Picasso.with(context).load(BaseURL.getBaseUrl()+"uploads/members/"+familyObj.getImage()).into(holder.memberImage);;
        holder.memberName.setText(familyObj.getMemberName());
        holder.memberId.setText(familyObj.getMemberId());

        holder.selectM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.e("member", familyObj.getId());
                    member_id.add(familyObj.getId());
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
        CheckBox selectM;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            memberImage = (ImageView)itemView.findViewById(R.id.picIv);
            memberName = (TextView)itemView.findViewById(R.id.mNameTv);
            memberId = (TextView)itemView.findViewById(R.id.mIdTv);
            selectM = (CheckBox) itemView.findViewById(R.id.selectCb);
        }
    }
}
