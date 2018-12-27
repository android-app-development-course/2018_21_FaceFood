package com.example.chenyuyang.notebook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenyuyang.notebook.module.note;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    final Context context;
    private List<note> list;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView des,date;
        LinearLayout ll ;
        public ViewHolder(View view){
            super(view);
            des=view.findViewById(R.id.rec_item_desc);
            date=view.findViewById(R.id.rec_date);
            ll=view.findViewById(R.id.itemLL);
        }
    }
    public RecyclerAdapter(List<note> list,Context context){
        this.list=list;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitemlayout,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        final note item = list.get(position);
        holder.des.setText((item).getTitle());
        holder.date.setText((item).getSimpleDate());
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,noteEditor.class);
                intent.putExtra("note",item);
                context.startActivity(intent);
            }
        });
    }
}
