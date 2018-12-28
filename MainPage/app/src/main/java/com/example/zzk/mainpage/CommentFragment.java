package com.example.zzk.mainpage;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CommentFragment extends Fragment{
    private View view;
    private ListView commentList;
    private List<Map<String, Object>> commentListData;
    private CommentListAdapter commentListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.comment_fragment, null);
        // 设置适配器
        commentList = view.findViewById(R.id.comment_list);
        commentListAdapter = new CommentListAdapter(getContext(), commentListData);
        commentList.setAdapter(commentListAdapter);
        return view;
    }

    public void setCommentList(List<Map<String, Object>> commentListData) {
        this.commentListData = commentListData;
    }

    public void notifyAdapterDataChanged() {
        this.commentListAdapter.notifyDataSetChanged();
    }

}
