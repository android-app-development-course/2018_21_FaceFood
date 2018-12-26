package com.example.zzk.mainpage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.example.zzk.mainpage.CommentDialog;
import com.example.zzk.mainpage.MyFragmentPagerAdapter;


public class DetailedActivity extends AppCompatActivity implements CommentDialog.OnCommitListener{

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    private TabLayout.Tab one;
    private TabLayout.Tab two;

    private int flag1=0,flag2=0;
    private MenuItem it1,it2;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        Menu menu;
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home1:
                    it1=item;
                    if(flag1==0){
                        item.setIcon(R.drawable.ic_favorite_black_24dp);
                        flag1=1;
                        if(flag2==1){
                            it2.setIcon(R.drawable.ic_mood_bad_black_24dp);
                            flag2=0;
                        }
                    }
                    else if(flag1==1){
                        item.setIcon(R.drawable.ic_mood_black_24dp);
                        flag1=0;
                    }
                    return true;
                case R.id.navigation_dashboard1:
                    CommentDialog cd=new CommentDialog(DetailedActivity.this);
                    cd.setOnCommitListener(DetailedActivity.this);
                    cd.show();

                    return true;
                case R.id.navigation_notifications1:
                    it2=item;
                    if(flag2==0) {
                        item.setIcon(R.drawable.ic_clear_black_24dp);
                        flag2 = 1;
                        if(flag1==1){
                            it1.setIcon(R.drawable.ic_mood_black_24dp);
                            flag1=0;
                        }
                    }
                    else if(flag2==1){
                        item.setIcon(R.drawable.ic_mood_bad_black_24dp);
                        flag2 = 0;
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar

        setContentView(R.layout.activity_detailed);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);

        //初始化视图
        initViews();
    }

    //添加评论的函数
    @Override
    public void onCommit(EditText et, View v){
        LayoutInflater inflater = LayoutInflater.from(DetailedActivity.this);
        //姓名
        TextView tv1 = new TextView(this);
        tv1.setText("不愿意透露姓名的用户：");

        //评论
        TextView tv2 = new TextView(this);
        tv2.setText(et.getText().toString());

        //加入到水平layout中
        LinearLayout layout = (LinearLayout) inflater.inflate(
               R.layout.comment_item, null).findViewById(R.id.LinearLayout_item);
        layout.addView(tv1);
        layout.addView(tv2);

        // 获取需要被添加控件的布局
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.comment_layout_main);

        // 加入到评论页面
        linearLayout.addView(layout);
    }

    private void initViews() {

        //使用适配器将ViewPager与Fragment绑定在一起
        mViewPager= (ViewPager) findViewById(R.id.detailedViewPager);
        mViewPager.setOffscreenPageLimit(2);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(myFragmentPagerAdapter);

        //将TabLayout与ViewPager绑定在一起
        mTabLayout = (TabLayout) findViewById(R.id.detailedTabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

        //指定Tab的位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);

        //设置Tab的图标，假如不需要则把下面的代码删去
      //  one.setIcon(R.mipmap.ic_launcher);
        //two.setIcon(R.mipmap.ic_launcher);
    }



}
