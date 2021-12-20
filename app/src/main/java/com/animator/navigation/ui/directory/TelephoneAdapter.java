package com.animator.navigation.ui.directory;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.animator.navigation.R;
import com.animator.navigation.ui.jobs.JobDetailsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TelephoneAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    Context context;
    ArrayList<Telephone> telephoneArrayList;
    ArrayList<Telephone> telephoneArrayListFilterd;
    Dialog layoutCall;

    public TelephoneAdapter(Context context, ArrayList<Telephone> telephoneArrayList) {
        this.context = context;
        this.telephoneArrayList = telephoneArrayList;
        this.telephoneArrayListFilterd = telephoneArrayList;
    }

    @Override
    public int getItemViewType(int position) {
        return telephoneArrayListFilterd.get(position).getIndex() != null ? 0 : 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        return getItemViewType(position) == 0 ? new IndexViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.index_item, viewGroup, false)) : new DataViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_telephone, viewGroup, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Telephone telObj = telephoneArrayListFilterd.get(position);
        final String id;
        final String family;
        if (getItemViewType(position) == 0) {
            IndexViewHolder viewHolder = (IndexViewHolder) holder;

            viewHolder.tvIndex.setText(telObj.getIndex());
        }else {
            final DataViewHolder viewHolder = (DataViewHolder) holder;
            id = telObj.getId();
            family = telObj.getFamily();
            Log.e("memberIdDirectory", id);
            Log.e("familyIdDirectory", family);
            Picasso.with(context).load(telObj.getProfileImg()).error(R.drawable.ic_menu_profile).into(viewHolder.profileImg);
            viewHolder.conName.setText("  " + telObj.getName());
//            viewHolder.conNum.setText(telObj.getContact());

            viewHolder.conName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = new DetailsFragment();
                    final Bundle bundle = new Bundle();
                    myFragment.setArguments(bundle);
                    bundle.putString("memberId", id);
                    bundle.putString("family_no", family);
                    Log.e("send to Detail", family);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
//                    layoutCall = new Dialog(context);
//                    layoutCall.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    layoutCall.setContentView(R.layout.layout_call);
//                    layoutCall.show();
//
//                    TextView whatsapp = (TextView)layoutCall.findViewById(R.id.whatsappTv);
//                    whatsapp.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
////                            Uri uri = Uri.parse("smsto:" + viewHolder.conNum.getText().toString());
////                            Intent i = new Intent(Intent.ACTION_SENDTO, uri);
////                            i.setPackage("com.whatsapp");
////                            Uri uri = Uri.parse("whatsapp://send?phone=+91" + mob_num);
//                            Uri uri = Uri.parse("smsto:" + "8779087052");
//                            Intent sendIntent = new Intent(Intent.ACTION_SEND, uri);
////                            sendIntent.setAction(Intent.ACTION_SEND, uri);
////                            sendIntent.putExtra(Intent.EXTRA_TEXT, sendString);
//                            sendIntent.setType("text/plain");
//                            sendIntent.setPackage("com.whatsapp");
//                            context.startActivity(sendIntent);
//                        }
//                    });
//
//                    TextView call = (TextView)layoutCall.findViewById(R.id.callTv);
//                    call.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                        }
//                    });
//                    call.setText(viewHolder.conName.getText().toString().trim());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return telephoneArrayListFilterd.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    telephoneArrayListFilterd = telephoneArrayList;
                } else {
                    ArrayList<Telephone> filteredList = new ArrayList<>();

                    for (Telephone row : telephoneArrayList) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    telephoneArrayListFilterd = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = telephoneArrayListFilterd;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                telephoneArrayListFilterd = (ArrayList<Telephone>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
