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

        FoodAdapter foodAdapter = new FoodAdapter(getActivity(), list);

        foodList.setAdapter(foodAdapter);

        return view;

    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for(int i = 0 ; i < 18; i++) {
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("food_name", "大便");
            map.put("food_up", "233");
            map.put("food_down", "10");
            map.put("food_image", R.drawable.bd);

            list.add(map);
        }


        return list;

    }

}
