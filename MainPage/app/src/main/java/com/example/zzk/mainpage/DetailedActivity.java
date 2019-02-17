package com.example.zzk.mainpage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import android.widget.Toast;

import com.example.cyy.util.BackEnd;
import com.example.zzk.mainpage.CommentDialog;
import com.example.zzk.mainpage.MyFragmentPagerAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class DetailedActivity extends AppCompatActivity implements CommentDialog.OnCommitListener{

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    private TabLayout.Tab one;
    private TabLayout.Tab two;

    private Map<String, Object> detailedInfo;
    private List<Map<String, Object>> commentListData;

    private int flag1=0,flag2=0;
    private MenuItem it1,it2;
    private BottomNavigationView navigation;
    private int entryExist = 0;
    private String isFirst = "no";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home1:
                    it1=item;
                    if(flag1==0){
                        item.setIcon(R.drawable.ic_favorite_black_24dp);
                        postUpDown(1);
                        flag1=1;
                        if(flag2==1){
                            it2.setIcon(R.drawable.ic_mood_bad_black_24dp);
                            flag2=0;
                        }
                    }
                    else if(flag1==1){
                        item.setIcon(R.drawable.ic_mood_black_24dp);
                        postUpDown(2);
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
                        postUpDown(0);
                        flag2 = 1;
                        if(flag1==1){
                            it1.setIcon(R.drawable.ic_mood_black_24dp);
                            flag1=0;
                        }
                    }
                    else if(flag2==1){
                        item.setIcon(R.drawable.ic_mood_bad_black_24dp);
                        postUpDown(3);
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

        navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String date_time = intent.getStringExtra("date_time");
        String location = intent.getStringExtra("location");
        String content = intent.getStringExtra("content");
        String images = intent.getStringExtra("images");
        String id = intent.getStringExtra("id");
        detailedInfo = new HashMap<>();
        detailedInfo.put("username", username);
        detailedInfo.put("date_time", date_time);
        detailedInfo.put("location", location);
        detailedInfo.put("content", content);
        detailedInfo.put("images", images);
        detailedInfo.put("id", id);

        commentListData = new ArrayList<>();
        //初始化视图
        initViews();

        // 获取用户是否点赞点踩，如有反映到图标上
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("normalID", detailedInfo.get("id"));
            SharedPreferences sharedPreferences = DetailedActivity.this.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
            jsonObject.put("userID", sharedPreferences.getString("userID", ""));
            StringEntity entity = new StringEntity(jsonObject.toString());
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.post(DetailedActivity.this, BackEnd.ip+"/getUpDown", entity, "application/json",
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {
                                String json = new String(responseBody, "utf-8");
                                JSONObject response = new JSONObject(json);
                                String hasRecord = response.getString("hasRecord");
                                if(hasRecord.equals("true")) {
                                    Integer upOrDown = response.getInt("upOrDown");
                                    entryExist = 1;
                                    isFirst = "yes";
                                    if(upOrDown == 1) {
                                        navigation.setSelectedItemId(R.id.navigation_home1);
                                    }
                                    else {
                                        navigation.setSelectedItemId(R.id.navigation_notifications1);
                                    }
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(DetailedActivity.this, "无法连接网络", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //添加评论的函数
    @Override
    public void onCommit(EditText et, View v){

        myFragmentPagerAdapter.setCommentList(commentListData);
        SharedPreferences sharedPreferences = DetailedActivity.this.getSharedPreferences("loginStatus", MODE_PRIVATE);
        String student_number = sharedPreferences.getString("userID", "1");
        String commentContent = et.getText().toString();
        String username = sharedPreferences.getString("nickname", "error");
        Map<String, Object> oneComment = new HashMap<>();
        oneComment.put("username", username);
        oneComment.put("commentContent", commentContent);
        commentListData.add(oneComment);

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("normalID", detailedInfo.get("id"));
            jsonObject.put("student_number", student_number);
            jsonObject.put("commentContent", commentContent);
            StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.post(DetailedActivity.this, BackEnd.ip+"/postComment", entity, "application/json",
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(DetailedActivity.this, "无法连接网络", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews() {

        //使用适配器将ViewPager与Fragment绑定在一起
        mViewPager= (ViewPager) findViewById(R.id.detailedViewPager);
        mViewPager.setOffscreenPageLimit(2);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        myFragmentPagerAdapter.setDetailedInfo(detailedInfo);

        // 获取信息
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("normalID", detailedInfo.get("id"));
            StringEntity entity = new StringEntity(jsonObject.toString());
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.post(DetailedActivity.this, BackEnd.ip+"/getComment", entity, "application/json",
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {
                                String json = new String(responseBody, "utf-8");
                                JSONObject response = new JSONObject(json);
                                String getStatus = response.getString("get");
                                if(getStatus.equals("success")) {
                                    JSONArray commentList = response.getJSONArray("content");
                                    for(int i = 0; i < commentList.length(); i++) {
                                        JSONObject oneComment = commentList.getJSONObject(i);
                                        Map<String, Object> oneCommentInfo = new HashMap<>();
                                        oneCommentInfo.put("username", oneComment.getString("username"));
                                        oneCommentInfo.put("commentContent", oneComment.get("commentContent"));
                                        commentListData.add(oneCommentInfo);
                                    }
                                    myFragmentPagerAdapter.setCommentList(commentListData);
                                    myFragmentPagerAdapter.notifySubFragmentDataChanged();
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(DetailedActivity.this, "无法连接网络", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        myFragmentPagerAdapter.setCommentList(commentListData);
        mViewPager.setAdapter(myFragmentPagerAdapter);

        //将TabLayout与ViewPager绑定在一起
        mTabLayout = (TabLayout) findViewById(R.id.detailedTabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

        //指定Tab的位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);


//        upItem = findViewById(R.id.navigation_home1);
//        downItem = findViewById(R.id.navigation_notifications1);
        //设置Tab的图标，假如不需要则把下面的代码删去
        //  one.setIcon(R.mipmap.ic_launcher);
        //two.setIcon(R.mipmap.ic_launcher);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        return false;
    }

    private void postUpDown(int upOrDown) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("normalID", detailedInfo.get("id"));
            SharedPreferences sharedPreferences = DetailedActivity.this.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
            jsonObject.put("userID", sharedPreferences.getString("userID", ""));
            jsonObject.put("upOrDown", upOrDown);
            jsonObject.put("entryExist", entryExist);
            jsonObject.put("isFirst", isFirst);
            isFirst = "no";
            StringEntity entity = new StringEntity(jsonObject.toString());
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.post(DetailedActivity.this, BackEnd.ip+"/postUpDown", entity, "application/json",
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Log.i("DetailedActivity", "发送成功");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(DetailedActivity.this, "无法连接网络", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
