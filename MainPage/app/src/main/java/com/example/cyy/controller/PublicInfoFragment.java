package com.example.cyy.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zzk.mainpage.R;

public class PublicInfoFragment extends Fragment {
    View fraView=null;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fraView= inflater.inflate(R.layout.fragment_info_public, null);
        return fraView;
    }
}
