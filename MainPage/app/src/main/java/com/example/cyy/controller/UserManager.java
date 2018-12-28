package com.example.cyy.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.zzk.mainpage.Login;
import com.example.zzk.mainpage.R;

public class UserManager extends Fragment {
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_logout, null);
            ((Button)view.findViewById(R.id.logout)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(),Login.class);
                    getContext().deleteSharedPreferences("loginStatus");
                    startActivity(intent);
                }
            });
            return view;
        }
}
