package com.animator.navigation.ui.matrimonial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.animator.navigation.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MatrimonialFragment extends Fragment {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;

    RecyclerView marriageRv;
    MarriageAdapter marriageAdapter;
    ArrayList<Marriage> marriageArrayList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matrimonial, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Matrimony");
        queue= Volley.newRequestQueue(getActivity());
        share=this.getActivity().getSharedPreferences("project", MODE_PRIVATE);

        marriageRv = (RecyclerView)view.findViewById(R.id.marriageRv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        marriageRv.setLayoutManager(linearLayoutManager);
        marriageRv.setItemAnimator(new DefaultItemAnimator());

        marriageArrayList = new ArrayList<Marriage>();
        for (int i = 0; i < MarriageData.name.length; i++) {
            marriageArrayList.add(new Marriage(
                    MarriageData.name[i],
                    MarriageData.age[i]
            ));
        }

        marriageAdapter = new MarriageAdapter(getContext(), marriageArrayList);
        marriageRv.setAdapter(marriageAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
