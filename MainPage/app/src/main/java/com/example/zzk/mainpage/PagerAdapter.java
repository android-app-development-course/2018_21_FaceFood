package com.example.zzk.mainpage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {

    private int tabsNumber;
    private List<HomeFragment> homeFragments;

    public PagerAdapter(FragmentManager fm, int tabsNumber) {
        super(fm);
        this.tabsNumber = tabsNumber;
        homeFragments = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int Position) {
        if (Position < 3) {
            HomeFragment homeFragment = new HomeFragment();

            if(Position == 0) {
                homeFragment.setIsContainRecommandContent(1);
                homeFragment.setFoodContentProvideType(FoodContentProvider.ProvideType.HOME);
            }
            else {
                homeFragment.setIsContainRecommandContent(0);
                homeFragment.setFoodContentProvideType(FoodContentProvider.ProvideType.STREET);
            }
            homeFragments.add(homeFragment);
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

    public List<HomeFragment> getHomeFragments() {
        return homeFragments;
    }
}