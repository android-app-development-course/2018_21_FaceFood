package com.example.chenyuyang.notebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.chenyuyang.notebook.module.note;

import java.util.ArrayList;
import java.util.Date;

public class searchResult extends Activity {
    RecyclerView recyclerView=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        recyclerView = (RecyclerView)findViewById(R.id.dummyfrag_scrollableview2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
    }

    public void onStart(){
        super.onStart();
        Intent intent = getIntent();
        if(!intent.hasExtra("fuzzy")){
            finish();
            return;
        }
        ArrayList<note> list = note.select(intent.getStringExtra("fuzzy"));
        if(list.size()==0){
            finish();
            return;
        }
        RecyclerAdapter adapter = new RecyclerAdapter(list,searchResult.this);
        recyclerView.setAdapter(adapter);
    }


}
