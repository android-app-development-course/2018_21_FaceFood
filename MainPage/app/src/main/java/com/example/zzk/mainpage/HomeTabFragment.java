package com.example.zzk.mainpage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.List;
import java.util.Map;

public class HomeTabFragment extends Fragment {

    int color;

    public HomeTabFragment() {
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_tab, null);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        PagerAdapter myPagerAdapter = new PagerAdapter(getFragmentManager(), 3);
        viewPager.setAdapter(myPagerAdapter);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        return view;

    }
}
