package com.animator.navigation.ui.directory;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.animator.navigation.R;

public class IndexViewHolder extends RecyclerView.ViewHolder {
    public TextView tvIndex;
    public IndexViewHolder(View inflate) {
        super(inflate);
        tvIndex=inflate.findViewById(R.id.tvIndex);
    }
}
