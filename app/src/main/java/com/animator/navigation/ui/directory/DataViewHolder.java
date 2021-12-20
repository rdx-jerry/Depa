package com.animator.navigation.ui.directory;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.animator.navigation.R;

public class DataViewHolder extends RecyclerView.ViewHolder {
    ImageView profileImg;
    TextView conName;
//    , conNum;

    public DataViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImg = (ImageView)itemView.findViewById(R.id.profileImg);
        conName = (TextView)itemView.findViewById(R.id.conNameTv);
//        conNum = (TextView)itemView.findViewById(R.id.conNumTv);
    }
}
