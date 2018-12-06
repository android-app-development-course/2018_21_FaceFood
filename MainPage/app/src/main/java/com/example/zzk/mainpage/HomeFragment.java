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
import java.util.Random;


public class HomeFragment extends Fragment {

    private ListView foodList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, null);

        foodList = (ListView)view.findViewById(R.id.list_view);

        List<Map<String, Object>> list = getData();

        List<Map<String, Object>> recommand_list = getRecommandContent();

        FoodAdapter foodAdapter = new FoodAdapter(getActivity(), list, recommand_list);

        foodList.setAdapter(foodAdapter);

        return view;
    }

    private List<Map<String, Object>> getRecommandContent() {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("recommand_food_image", R.drawable.rice2);

        map.put("recommand_food_description", "今日推荐");

        list.add(map);

        Map<String, Object> map1 = new HashMap<String, Object>();

        map1.put("recommand_food_image", R.drawable.beaf);

        map1.put("recommand_food_description", "不存在的牛肉");

        list.add(map1);

        Map<String, Object> map2 = new HashMap<String, Object>();

        map2.put("recommand_food_image", R.drawable.shousi);

        map2.put("recommand_food_description", "不存在的寿司");

        list.add(map2);

        return list;
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for(int i = 0 ; i < 4; i++) {
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("food_name", "饥饿的食尸鬼");
            map.put("food_up", "233");
            map.put("food_down", "10");
            switch (i) {
                case 0:
                    map.put("food_image", R.drawable.beaf);
                    break;
                case 1:
                    map.put("food_image", R.drawable.rice);
                    break;
                case 2:
                    map.put("food_image", R.drawable.shousi);
                    break;
                case 3:
                    map.put("food_image", R.drawable.bd);
                    break;
            }
            map.put("food_time", "12/2 10:00");
            map.put("food_place", "雍园");

            list.add(map);
        }

        return list;

    }

}
