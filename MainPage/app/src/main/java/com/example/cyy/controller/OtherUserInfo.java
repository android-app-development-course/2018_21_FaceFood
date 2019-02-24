package com.example.cyy.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cyy.util.BackEnd;
import com.example.cyy.util.ImageViewUrlSetter;
import com.example.zzk.mainpage.R;

public class OtherUserInfo extends AppCompatActivity {
    TextView name,add;
    ImageView pp;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_info);

        name=findViewById(R.id.otherUserInfoName);
        add=findViewById(R.id.otherUserInfoAddress);
        pp=findViewById(R.id.OtherprofilePhoto);

        Bundle bundle = this.getIntent().getExtras();
        name.setText(bundle.getString("name"));
        add.setText(bundle.getString("add"));
        String ppUrl = bundle.getString("pp");
        if(!ppUrl.isEmpty()) new ImageViewUrlSetter(pp,getApplicationContext()).set(BackEnd.ip+'/'+ppUrl);
    }
}
