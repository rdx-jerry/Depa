package com.animator.navigation.ui.directory;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.animator.navigation.R;

import java.util.ArrayList;
import java.util.List;

public class IndexAdapter extends RecyclerView.Adapter<IndexViewHolder> {

    private List<String> dataList;
    private SelectListner selectLisner;

    IndexAdapter(ArrayList<String> dataList, SelectListner selectListner) {
        this.dataList = dataList;
        this.selectLisner = selectListner;
    }

    @NonNull
    @Override
    public IndexViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        return new IndexViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.index_item, viewGroup, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final IndexViewHolder holder, final int position) {
        holder.tvIndex.setText(dataList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLisner.selectedIndex(holder.tvIndex);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface SelectListner {
        void selectedIndex(View view);
    }
}