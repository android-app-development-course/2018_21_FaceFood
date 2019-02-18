package com.example.cyy.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyy.module.UserInfo;
import com.example.zzk.mainpage.R;

import java.util.ArrayList;
import java.util.List;

public class UserInfoRecyclerAdapter extends RecyclerView.Adapter<UserInfoRecyclerAdapter.ViewHolder>{
    private List<InfoItem> list;
    static public class InfoItem {
        public String desc, value;
        public View.OnClickListener listener;

        public InfoItem(String desc, String value) {
            this.desc = desc;
            this.value = value;
        }

        public InfoItem(String desc, String value, View.OnClickListener listener) {
            this.desc = desc;
            this.value = value;
            this.listener = listener;
        }
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView des;
        TextView value;
        public ViewHolder(View view){
            super(view);
            des=view.findViewById(R.id.info_item_desc);
            value=view.findViewById(R.id.info_item_value);
        }
    }
    public UserInfoRecyclerAdapter(List<InfoItem> list){
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
    public static  UserInfoRecyclerAdapter.InfoItem makeTextInputItem(final Context context,final UserInfo info,final UserInfo.InfoType type) {
        String _name,_deValue;
        switch (type){
            case name:
                _name=context.getString(R.string.name);
                _deValue=info.getName();
                break;
            case address:
                _name=context.getString(R.string.address);
                _deValue=info.getAdd();
                break;
            case id:
                _name=context.getString(R.string.ID);
                _deValue=info.getId();
                break;
            case gender:
                _name=context.getString(R.string.gender);
                _deValue=info.getGender().toString();
                break;
                default:
                    Log.e("错误使用","其他数据不要使用本方法产生InfoItem");
                    return null;
        }
        final String name=_name,deValue=_deValue;
        if(type==UserInfo.InfoType.id){
            return new UserInfoRecyclerAdapter.InfoItem(name,deValue,new View.OnClickListener(){
                public void onClick(View v){;}
            });
        }
        else if(type==UserInfo.InfoType.gender) {
            return new UserInfoRecyclerAdapter.InfoItem(
                    name, deValue, new View.OnClickListener() {
                        public void onClick(View v) {
                            final TextView tv = (TextView)v;
                            final Spinner spinner = new Spinner(context);
                            ArrayList<String> chose = new ArrayList<>();
                            chose.add(context.getString(R.string.GENDER_MALE));
                            chose.add(context.getString(R.string.GENDER_FEMALE));
                            chose.add(context.getString(R.string.GENDER_UNKNOW));
                            ArrayAdapter<String> adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, chose);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if(position==0){
                                        info.setGender(UserInfo.Gender.MALE);
                                        tv.setText(UserInfo.Gender.MALE.toString());
                                    }
                                    else if(position==1){
                                        info.setGender(UserInfo.Gender.FEMALE);
                                        tv.setText(UserInfo.Gender.FEMALE.toString());
                                    }
                                    else{
                                        info.setGender(UserInfo.Gender.UNKNOW);
                                        tv.setText(UserInfo.Gender.UNKNOW.toString());
                                    }
                                    info.updateUserInfo(context);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    Toast.makeText(context, context.getString(R.string.NoChangeHappen), Toast.LENGTH_LONG).show();
                                }
                            });
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle(name);
                            builder.setView(spinner);
                            builder.show();
                        }
            });
        }
        UserInfoRecyclerAdapter.InfoItem forRet = new UserInfoRecyclerAdapter.InfoItem(
                name, deValue, new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final TextView from = (TextView)v;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(name);
                final EditText edit = new EditText(context);
                    edit.setText(deValue);
                    builder.setView(edit);
                builder.setPositiveButton(context.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newValue = edit.getText().toString();
                        from.setText(newValue);
                        if (newValue == deValue) {
                            Toast.makeText(context, context.getString(R.string.NoChangeHappen), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        switch (type){
                            case address:
                                info.setAdd(newValue);
                                ((TextView) v).setText(newValue);
                                break;
                            case name:
                                info.setName(newValue);
                                ((TextView) v).setText(newValue);
                                break;
                        }
                        info.updateUserInfo(context);
                    }
                });
                builder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, context.getString(R.string.NoChangeHappen), Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
        return forRet;
    }
}
