package com.example.zzk.mainpage.UserInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.zzk.mainpage.R;

import java.util.ArrayList;
import java.util.List;

public class DummyFragment extends Fragment {
    int color;

    public DummyFragment() {
    }

    @SuppressLint("ValidFragment")
    public DummyFragment(int color) {
        this.color = color;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_dummy, container, false);

        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
        frameLayout.setBackgroundColor(color);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dummyfrag_scrollableview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        DessertAdapter adapter = new DessertAdapter(new ArrayList<InfoItem>());
        recyclerView.setAdapter(adapter);
        return view;
    }
}

class DessertAdapter extends RecyclerView.Adapter<DessertAdapter.ViewHolder>{
    private List<InfoItem> list;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView des,value;
        public ViewHolder(View view){
            super(view);
            des=view.findViewById(R.id.info_item_desc);
            value=view.findViewById(R.id.info_item_value);
        }
    }
    public DessertAdapter(List<InfoItem> list){
        this.list=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_info_viewitemlayout,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        InfoItem item = list.get(position);
        holder.des.setText(item.desc);
        holder.value.setText(item.value);
        holder.value.setOnClickListener(item.listener);
    }
}
class InfoItem{
    public String desc,value;
    public View.OnClickListener listener;
    public InfoItem(String desc,String value){
        this.desc=desc;
        this.value=value;
    }
    public InfoItem(String desc,String value,View.OnClickListener listener){
        this.desc=desc;
        this.value=value;
        this.listener=listener;
    }
}
