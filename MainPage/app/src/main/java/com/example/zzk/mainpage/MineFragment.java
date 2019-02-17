package com.example.zzk.mainpage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import com.example.cyy.util.BackEnd;

public class MineFragment extends Fragment {
    private View view;
    private ListView foodListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FoodListAdapter foodAdapter;
    private int preLast; // use for check if reach to the bottom
    private List<Map<String, Object>> normalItems;

    public MineFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        view.setVisibility(View.INVISIBLE);
        initView();

        normalItems = new ArrayList<>();
        getNormalItems();

        return view;
    }

    private void initView() {
        preLast = 0;
        foodListView = view.findViewById(R.id.list_view);
        foodListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final int lastItem = firstVisibleItem + visibleItemCount;
                if (lastItem == totalItemCount) {
                    if (lastItem != preLast) {
                        preLast = lastItem;
                        getNormalItems();
                    }
                }
            }
        });

        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DetailedActivity.class);
                intent.putExtra("username", (String) normalItems.get(position).get("food_name"));
                intent.putExtra("date_time", (String) normalItems.get(position).get("food_time"));
                intent.putExtra("location", (String) normalItems.get(position).get("food_place"));
                intent.putExtra("content", (String) normalItems.get(position).get("content"));
                intent.putExtra("images", (String) normalItems.get(position).get("images"));
                startActivity(intent);
            }
        });

        swipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                normalItems.clear();
                getNormalItems();
            }
        });
    }

    private void getNormalItems() {
        try {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("nickname", "undefined");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("offset", normalItems.size());
            jsonObject.put("username", username);
            StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.post(getContext(), BackEnd.ip+"/myNormal", entity, "application/json",
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {
                                String json = new String(responseBody, "utf-8");
                                JSONObject response = new JSONObject(json);
                                String hasNormal = response.getString("hasNormal");
                                if(hasNormal.equals("true")) {
                                    JSONArray normalJsonArray = response.getJSONArray("normal");
                                    for(int i = 0; i < normalJsonArray.length(); i++) {
                                        Map<String, Object> map = new HashMap<>();
                                        JSONObject jsonObject = (JSONObject)normalJsonArray.get(i);
                                        map.put("id", jsonObject.getString("id"));
                                        map.put("food_name", jsonObject.getString("nickname"));
                                        map.put("food_image", jsonObject.getString("image_path"));
                                        map.put("food_time", jsonObject.getString("date_time"));
                                        map.put("food_place", jsonObject.getString("eat_place"));
                                        map.put("food_up", jsonObject.getString("upup"));
                                        map.put("food_down", jsonObject.getString("downdown"));
                                        map.put("content", jsonObject.getString("content"));
                                        map.put("images", jsonObject.getString("images"));
                                        normalItems.add(map);
                                    }
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }

                            // --- 第一次初始化adapter，后来的就通知adapter发生了变化
                            if(foodAdapter == null) {
                                foodAdapter = new FoodListAdapter(getActivity(), normalItems);
                                foodListView.setAdapter(foodAdapter);
                                view.setVisibility(View.VISIBLE);
                            }
                            else {
                                foodAdapter.notifyDataSetChanged();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(getContext(), "无法连接网络", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}