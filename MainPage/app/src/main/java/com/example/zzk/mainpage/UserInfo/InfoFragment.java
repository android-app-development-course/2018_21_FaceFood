package com.example.zzk.mainpage.UserInfo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cyy.module.UserInfo;
import com.example.zzk.mainpage.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class InfoFragment extends Fragment {
    UserInfo info=null;

    ViewPager viewPager;
    TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        UserInfo.initUserInfo("1",getContext());
        info=UserInfo.getUser();
        View forRet = inflater.inflate(R.layout.fragment_info, null);

        viewPager= forRet.findViewById(R.id.htab_viewpager);
        tabLayout=forRet.findViewById(R.id.htab_tabs);

        viewPager.setAdapter(new ViewPagerAdapter(getFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        return forRet;
    }
}

class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
        mFragmentTitleList.add("公开信息");
        mFragmentTitleList.add("隐私信息");
        mFragmentList.add( new PublicInfoFragment());
        mFragmentList.add(new PrivateInfoFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
