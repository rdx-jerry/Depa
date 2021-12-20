package com.animator.navigation.ui.latest;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.animator.navigation.R;
import com.animator.navigation.ui.latest.past.PastFragment;
import com.animator.navigation.ui.latest.upcomming.UpcommingFragment;
import com.animator.navigation.ui.todays.TodaysFragment;
import com.google.android.material.tabs.TabLayout;

public class LatestFragment extends Fragment {

    private TabAdapter tabAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static TodaysFragment newInstance() {
        return new TodaysFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.eventTab);
        viewPager = (ViewPager) view.findViewById(R.id.tabViewPager);

        tabAdapter = new TabAdapter(getFragmentManager());
        tabAdapter.addFragment(new UpcommingFragment(), "Upcomming");
        tabAdapter.addFragment(new PastFragment(), "Past");


        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}