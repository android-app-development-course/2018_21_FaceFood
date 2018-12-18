package com.example.cyy.controller;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cyy.module.UserInfo;
import com.example.cyy.util.NetDoneListener;
import com.example.zzk.mainpage.R;
import com.example.cyy.util.ImageViewUrlSetter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class InfoFragment extends Fragment {
    UserInfo info=null;

    ViewPager viewPager;
    TabLayout tabLayout;
    ImageView photo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View forRet = inflater.inflate(R.layout.fragment_info, null);
        viewPager= forRet.findViewById(R.id.htab_viewpager);
        tabLayout=forRet.findViewById(R.id.htab_tabs);
        photo=forRet.findViewById(R.id.htab_header);

        UserInfo.initUserInfo("1", getContext(), new NetDoneListener() {
            @Override
            public void OnNetDone() {
                new ImageViewUrlSetter(photo).set("http://yummmy.cn/"+info.getProfilePhotoAdd());
            }
        });
        info=UserInfo.getUser();

        viewPager.setAdapter(new ViewPagerAdapter(getFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        ((ImageView)forRet.findViewById(R.id.htab_header)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProfilePhoto();
            }
        });

        return forRet;
    }

    @Override
    public void onActivityResult(int requestCode,int b,Intent data){
        Uri tmp=data.getData();
        try {
            InputStream picSelected = getContext().getContentResolver().openInputStream(tmp);
            AsyncHttpClient client=new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("student_number",info.getId());
            String newPhotoName = info.getId()+"ProPhoto.jpg";
            params.put("file",picSelected,newPhotoName);
            client.post("http://yummmy.cn/upload", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.i("cyy uploading pic done","Success");
                    Toast.makeText(getContext(), getString(R.string.SuccessUpdateProfilePhoto), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.e("cyy uploading pic error","Error");
                    Toast.makeText(getContext(), getString(R.string.ErrorCannotUploadProfilePhoto), Toast.LENGTH_LONG).show();
                }
            });
        } catch (java.io.FileNotFoundException e) {
            Toast.makeText(getContext(),getText(R.string.ErrorUnknowError),Toast.LENGTH_LONG).show();
        }
    }
    public void selectProfilePhoto(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select picture"),0);
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