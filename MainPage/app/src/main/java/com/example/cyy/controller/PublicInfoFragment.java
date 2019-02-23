package com.example.cyy.controller;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.cyy.module.UserInfo;
import com.example.cyy.util.UserInfoRecyclerAdapter;
import com.example.zzk.mainpage.R;

import java.util.ArrayList;

public class PublicInfoFragment extends Fragment {
    int color=Color.WHITE;
    UserInfo info=null;
    public void setColor(int color){this.color =color;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(info==null)info=UserInfo.getLoginedUser(getContext());
        View view= inflater.inflate(R.layout.fragment_info_public, null);

        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
        frameLayout.setBackgroundColor(color);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dummyfrag_scrollableview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        ArrayList<UserInfoRecyclerAdapter.InfoItem> list = new ArrayList<>();
        try {
            list.add(UserInfoRecyclerAdapter.makeTextInputItem(getContext(), info, UserInfo.InfoType.name));
        }catch (Exception e){
            ;
        }
        list.add(UserInfoRecyclerAdapter.makeTextInputItem(getContext(),info,UserInfo.InfoType.address));

        UserInfoRecyclerAdapter adapter = new UserInfoRecyclerAdapter(list);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
