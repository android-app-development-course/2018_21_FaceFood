package com.example.zzk.mainpage;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
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
//    private List<Map<String, Object>> recommandList;
//    private List<Map<String, Object>> normalList;

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

            asyncHttpClient.post(getContext(), "http://www.yummmy.cn/firstNormal", entity, "application/json",
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

                                    asyncHttpClient1.post(getContext(), "http://www.yummmy.cn/firstRecommand", entity, "application/json",
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

            asyncHttpClient.post(getContext(), "http://www.yummmy.cn/firstNormal", entity, "application/json",
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

//    public void notifyUpdate() {
//
//        List<Map<String, Object>> temp = foodContentProvider.getFollowContent();
//
//        for(int i = 0; i < temp.size(); i++) {
//
//            normalList.add(temp.get(i));
//        }
//        notifyDataChanged();
//    }
//
//    private List<Map<String, Object>> getRecommandContent() {
//
//        recommandList = new ArrayList<>();
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("recommand_food_image", R.drawable.rice2);
//        map.put("recommand_food_description", "今日推荐");
//        recommandList.add(map);
//
//        Map<String, Object> map1 = new HashMap<String, Object>();
//        map1.put("recommand_food_image", R.drawable.beaf);
//        map1.put("recommand_food_description", "不存在的牛肉");
//        recommandList.add(map1);
//
//        Map<String, Object> map2 = new HashMap<String, Object>();
//        map2.put("recommand_food_image", R.drawable.shousi);
//        map2.put("recommand_food_description", "不存在的寿司");
//        recommandList.add(map2);
//
//        return recommandList;
//    }
//
//    private List<Map<String, Object>> getData() {
//        normalList = new ArrayList<>();
//
//        for(int i = 0 ; i < 4; i++) {
//            Map<String, Object> map = new HashMap<String, Object>();
//
//            map.put("food_name", "饥饿的食尸鬼");
//            map.put("food_up", "233");
//            map.put("food_down", "10");
//            switch (i) {
//                case 0:
//                    map.put("food_image", R.drawable.beaf);
//                    break;
//                case 1:
//                    map.put("food_image", R.drawable.rice);
//                    break;
//                case 2:
//                    map.put("food_image", R.drawable.shousi);
//                    break;
//                case 3:
//                    map.put("food_image", R.drawable.bd);
//                    break;
//            }
//            map.put("food_time", "12/2 10:00");
//            map.put("food_place", "雍园");
//
//            normalList.add(map);
//        }
//
//        return normalList;
//
//    }

}
