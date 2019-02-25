package com.example.zzk.mainpage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyy.module.UserInfo;
import com.example.cyy.util.BackEnd;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class CommentListAdapter extends BaseAdapter {
    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private boolean hasDeleted = false;

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
        final String commenter_id=(String)data.get(position).get("student_number");//评论的人的id
        final String commentID  = String.valueOf( data.get(position).get("commentID"));

        if(commenter_id.contentEquals(UserInfo.getLoginedUser(context).getId())){//判断是不是自己的评论
            TextView delDetail = convertView.findViewById(R.id.comment_list_delete);
            delDetail.setVisibility(View.VISIBLE);

            delDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(hasDeleted) {
                        Toast.makeText(context, R.string.CommentAlreadyDelete, Toast.LENGTH_SHORT).show();
                        return;
                    }
//
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("commentID", commentID);
//                        Toast.makeText(context,  R.string.CommentAlreadyDelete, Toast.LENGTH_SHORT).show();
                    }catch (Exception er){
                        Log.e("JOSN",er.getMessage());
                    }
                    StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.post(context,BackEnd.ip + "/deleteComment", entity,"application/json", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Toast.makeText(context, R.string.CommentAlreadyDelete, Toast.LENGTH_SHORT).show();
                            hasDeleted=true;
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(context, "X!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }

        return convertView;
    }

}
