package com.example.zzk.mainpage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;
import java.util.Map;

//定义适配器Adapter类

public class MyFragmentPagerAdapter extends FragmentPagerAdapter{
    private String[] mTitles = new String[]{"详细内容", "评论"};
    private Map<String, Object> detailedInfo;
    private List<Map<String, Object>> commentList;
    private CommentFragment commentFragment;

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            commentFragment = new CommentFragment();
            commentFragment.setCommentList(commentList);
            return commentFragment;
        }
        else {
            DetailedFragment detailedFragment = new DetailedFragment();
            detailedFragment.setDetailedInfo(detailedInfo);
            return detailedFragment;
        }
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    public void setDetailedInfo(Map<String, Object> detailedInfo) {
        this.detailedInfo = detailedInfo;
    }

    public void setCommentList(List<Map<String, Object>> commentList) {
        this.commentList = commentList;
    }

    public void notifySubFragmentDataChanged() {
        commentFragment.notifyAdapterDataChanged();
    }
}
