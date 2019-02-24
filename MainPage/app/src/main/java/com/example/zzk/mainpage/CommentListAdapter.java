package com.example.zzk.mainpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cyy.module.UserInfo;

import java.util.List;
import java.util.Map;

public class CommentListAdapter extends BaseAdapter {
    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;

    public CommentListAdapter(Context context, List<Map<String, Object>> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.comment_item, null);
        TextView username = convertView.findViewById(R.id.comment_list_name);
        TextView commentContent = convertView.findViewById(R.id.comment_list_content);
        username.setText((String) data.get(position).get("username"));
        commentContent.setText((String) data.get(position).get("commentContent"));

////        final String myuserid=UserInfo.getLoginedUser(context).getId();//查看的人的id
        final String commenter_id=(String)data.get(position).get("user_id");//评论的人的id

        if(commenter_id.contentEquals(UserInfo.getLoginedUser(context).getId())){//判断是不是自己的评论
            TextView delDetail = convertView.findViewById(R.id.comment_list_delete);
            delDetail.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

}
