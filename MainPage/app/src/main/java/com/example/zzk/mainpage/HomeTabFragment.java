package com.example.zzk.mainpage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class HomeTabFragment extends Fragment {

    public HomeTabFragment() {
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_tab, null);
        ViewPager viewPager = view.findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(3);
        PagerAdapter myPagerAdapter = new PagerAdapter(getFragmentManager(), 3);

        viewPager.setAdapter(myPagerAdapter);
        TabLayout tabLayout = view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

}
