package com.animator.navigation.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.animator.navigation.R;

import java.util.ArrayList;

public class SpinAdapter extends ArrayAdapter<String> {
    ArrayList<Family> memberList;


    public SpinAdapter(@NonNull Context context, int resource, ArrayList<Family> memberList) {
        super(context, resource);
        this.memberList = memberList;
    }

    @Override
    public int getCount() {
        return memberList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        Family family = memberList.get(position);
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.spinnerid, parent, false);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.memName);
//            mViewHolder.mId = (TextView) convertView.findViewById(R.id.memId);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mName.setText(family.getMemberName());
//        mViewHolder.mId.setText(family.getMemberId());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getDropDownView(position, convertView, parent);
        return getView(position, convertView, parent);
    }

    private static class ViewHolder {
        TextView mName;
//        TextView mId;
    }
}
