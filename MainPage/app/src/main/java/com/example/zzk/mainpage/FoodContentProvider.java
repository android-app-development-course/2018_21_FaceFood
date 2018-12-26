package com.example.zzk.mainpage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodContentProvider {

    List<Map<String, Object>> recommandContent;
    List<Map<String, Object>> normalContent;

    public enum ProvideType {
        HOME, STREET, CARE
    }

    private ProvideType provideType;

    public void setProvideType(ProvideType provideType) {
        this.provideType = provideType;
    }

    public FoodContentProvider() {
        recommandContent = new ArrayList<>();
        normalContent = new ArrayList<>();
    }

    public void processRecommandContent(JSONArray jsonArray) {

        recommandContent.clear();
        try {

            for(int i = 0; i < jsonArray.length(); i++) {
                Map<String, Object> map = new HashMap<>();
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                map.put("recommand_food_image", jsonObject.getString("image_path"));
                map.put("recommand_food_description", jsonObject.getString("image_description"));
                recommandContent.add(map);
            }
        }
        catch (Exception e) {

        }

    }

    public void processNormalContent(JSONArray jsonArray) {

        normalContent.clear();
        try {

            for(int i = 0; i < jsonArray.length(); i++) {
                Map<String, Object> map = new HashMap<>();
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);

                map.put("food_name", jsonObject.getString("nickname"));
                map.put("food_image", jsonObject.getString("image_path"));
                map.put("food_time", jsonObject.getString("date_time"));
                map.put("food_place", jsonObject.getString("eat_place"));
                map.put("food_up", jsonObject.getString("upup"));
                map.put("food_down", jsonObject.getString("downdown"));

                normalContent.add(map);
            }
        }
        catch (Exception e) {

        }

    }

    public List<Map<String, Object>> getRecommandContent() {

        return recommandContent;
    }

    public List<Map<String, Object>> getNormalContent() {

        return normalContent;
    }

    public List<Map<String, Object>> getFollowContent() {

        return normalContent;
    }

}
