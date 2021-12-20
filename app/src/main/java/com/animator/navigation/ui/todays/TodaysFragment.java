package com.animator.navigation.ui.todays;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.animator.navigation.R;
import com.animator.navigation.ui.todays.anniversary.Anniversary;
import com.animator.navigation.ui.todays.birthday.BirthdatFragment;
import com.animator.navigation.ui.todays.marannondh.MaranNondhFragment;
import com.animator.navigation.ui.todays.prarthana.PrarthanaFragment;
import com.animator.navigation.ui.todays.punyatithi.PunyatithiFragment;
import com.google.android.material.tabs.TabLayout;

public class TodaysFragment extends Fragment {

    private TabAdapter tabAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static TodaysFragment newInstance() {
        return new TodaysFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todays, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.eventTab);
        viewPager = (ViewPager) view.findViewById(R.id.tabViewPager);

        tabAdapter = new TabAdapter(getFragmentManager());
        tabAdapter.addFragment(new BirthdatFragment(), "Birthday");
        tabAdapter.addFragment(new Anniversary(), "Anniversary");
        tabAdapter.addFragment(new PunyatithiFragment(), "Punyatithi");
        tabAdapter.addFragment(new MaranNondhFragment(), "Maran Nondh");
        tabAdapter.addFragment(new PrarthanaFragment(), "Prarthana");

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}