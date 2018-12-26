package com.example.zzk.mainpage;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class HomeFragment extends Fragment {

    private ListView foodList;
    private Adapter foodAdapter;
    private View view;
    private int preLast;
    public FoodContentProvider foodContentProvider;
    // 0 no, 1 yes
    private int isContainRecommandContent = 1;

    public HomeFragment() {
        foodContentProvider = new FoodContentProvider();
    }

    void setIsContainRecommandContent(int isContainRecommandContent) {
        this.isContainRecommandContent = isContainRecommandContent;
    }

    void setFoodContentProvideType(FoodContentProvider.ProvideType provideType) {
        foodContentProvider.setProvideType(provideType);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, null);
        foodList = view.findViewById(R.id.list_view);
        preLast = 0;
        foodList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final int lastItem = firstVisibleItem + visibleItemCount;
                if(lastItem == totalItemCount) {
                    if(lastItem != preLast) {
                        preLast = lastItem;
                        Toast.makeText(getContext(), "加载更多", Toast.LENGTH_SHORT).show();
                        fillMoreListView();
                    }
                }
            }
        });
        foodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DetailedActivity.class);
                startActivity(intent);
            }
        });
        view.setVisibility(View.INVISIBLE);
        fillListView();
        return view;
    }

    private void fillListView() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("normal", "first");
            StringEntity entity = new StringEntity(jsonObject.toString());
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.post(getContext(), "http://129.204.49.159/firstNormal", entity, "application/json",
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {

                                String json = new String(responseBody, "utf-8");
                                JSONObject jsonObject = new JSONObject(json);

                                JSONArray normalJsonArray = jsonObject.getJSONArray("normal");
                                foodContentProvider.processNormalContent(normalJsonArray);

                                final List<Map<String, Object>> normalContent = foodContentProvider.getNormalContent();

                                if(isContainRecommandContent == 1) {

                                    JSONObject jsonObject1 = new JSONObject();
                                    jsonObject.put("recommand", "first");
                                    StringEntity entity = new StringEntity(jsonObject1.toString());

                                    AsyncHttpClient asyncHttpClient1 = new AsyncHttpClient();

                                    asyncHttpClient1.post(getContext(), "http://129.204.49.159/firstRecommand", entity, "application/json",
                                            new AsyncHttpResponseHandler() {
                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                                                    try {
                                                        String json = new String(responseBody, "utf-8");
                                                        JSONObject jsonObject = new JSONObject(json);

                                                        JSONArray recommandJsonArray = jsonObject.getJSONArray("recommand");
                                                        foodContentProvider.processRecommandContent(recommandJsonArray);

                                                        List<Map<String, Object>> recommandContent = foodContentProvider.getRecommandContent();

                                                        foodAdapter = new FoodAdapter(getActivity(), normalContent, recommandContent);
                                                        foodList.setAdapter((FoodAdapter)foodAdapter);
                                                        view.setVisibility(View.VISIBLE);

                                                    }
                                                    catch (Exception e) {

                                                    }

                                                }

                                                @Override
                                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                                }
                                            });

                                }
                                else {
                                    foodAdapter = new FoodListAdapter(getActivity(), normalContent);
                                    foodList.setAdapter((FoodListAdapter)foodAdapter);
                                    view.setVisibility(View.VISIBLE);
                                    notifyDataChanged();
                                }


                            }
                            catch (Exception e) {

                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });

        }
        catch (Exception e) {

        }

    }

    public void fillMoreListView() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("normal", "follow");
            StringEntity entity = new StringEntity(jsonObject.toString());

            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

            asyncHttpClient.post(getContext(), "http://129.204.49.159/firstNormal", entity, "application/json",
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {

                                String json = new String(responseBody, "utf-8");
                                JSONObject jsonObject = new JSONObject(json);

                                JSONArray normalJsonArray = jsonObject.getJSONArray("normal");
                                foodContentProvider.processNormalContent(normalJsonArray);

                                final List<Map<String, Object>> normalContent = foodContentProvider.getNormalContent();
                                notifyUpdateMore(normalContent);
                            }
                            catch (Exception e) {

                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });

        }
        catch (Exception e) {

        }
    }

    public void notifyDataChanged() {

        if(isContainRecommandContent == 1) {
            ((FoodAdapter)foodAdapter).notifyDataSetChanged();
        }
        else {
            ((FoodListAdapter)foodAdapter).notifyDataSetChanged();
        }
    }

    public void notifyUpdateMore(List<Map<String, Object>> updateContent) {

        if(isContainRecommandContent == 1) {
            ((FoodAdapter)foodAdapter).addMoreDate(updateContent);
        }
        else {
            ((FoodListAdapter)foodAdapter).addMoreDate(updateContent);
        }

        notifyDataChanged();
    }
}
