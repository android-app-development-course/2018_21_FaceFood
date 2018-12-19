package com.example.cyy.controller;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class PrivateInfoFragment extends Fragment {
    int color=0x9fefebeb;
    UserInfo info=UserInfo.getUser();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_info_public, null);

        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
        frameLayout.setBackgroundColor(color);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dummyfrag_scrollableview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        ArrayList<UserInfoRecyclerAdapter.InfoItem> list = new ArrayList<>();
        list.add(UserInfoRecyclerAdapter.makeTextInputItem(getContext(), info, UserInfo.InfoType.id));
        list.add(UserInfoRecyclerAdapter.makeTextInputItem(getContext(),info,UserInfo.InfoType.gender));

        UserInfoRecyclerAdapter adapter = new UserInfoRecyclerAdapter(list);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
