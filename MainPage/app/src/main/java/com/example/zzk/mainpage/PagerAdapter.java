package com.example.zzk.mainpage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    int tabsNumber;

    public PagerAdapter(FragmentManager fm, int tabsNumber) {
        super(fm);
        this.tabsNumber = tabsNumber;
    }


    @Override
    public Fragment getItem(int Position) {
        if (Position < 3) {
            HomeFragment homeFragment = new HomeFragment();
            return homeFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "推荐";
            case 1:
                return "大街";
            case 2:
                return "关注";
            default:
                return null;
        }
    }
}