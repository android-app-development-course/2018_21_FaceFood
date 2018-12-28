package com.example.chenyuyang.notebook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chenyuyang.notebook.module.note;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class publicFragment extends Fragment {
    RecyclerView recyclerView=null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View forRet = inflater.inflate(R.layout.public_fragment,null);

        recyclerView = (RecyclerView) forRet.findViewById(R.id.dummyfrag_scrollableview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        return forRet;
    }

    @Override
    public void onStart() {
        super.onStart();
        List<note> list = note.getNotes();
        RecyclerAdapter adapter = new RecyclerAdapter(list,getContext());
        recyclerView.setAdapter(adapter);
    }
}
