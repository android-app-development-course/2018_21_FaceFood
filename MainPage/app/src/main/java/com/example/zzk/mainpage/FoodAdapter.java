package com.example.zzk.mainpage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.cyy.util.BackEnd;

public class FoodAdapter extends BaseAdapter {
    private List<Map<String, Object>> recommend_food;
    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private int TYPE_COUNT = 2;

    public FoodAdapter(Context context, List<Map<String, Object>> data, List<Map<String, Object>> recommend_food) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.recommend_food = recommend_food;
    }

    public void freshAll(List<Map<String,Object>> newData){
        data=newData;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        if(position < 1) {
            return recommend_food.get(position);
        }
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return data.size() + 1;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 1) {
            return 0;
        }
        else {
            return 1;
        }
    }

    private class FoodItem {
        public ImageView foodImage;
        public TextView foodName;
        public TextView foodUp;
        public TextView foodDown;
        public TextView foodPlace;
        public TextView foodTime;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if(type == 0) {
            convertView = layoutInflater.inflate(R.layout.recommand_item, null);
            SliderLayout sliderLayout = convertView.findViewById(R.id.imageSlider);
            sliderLayout.setIndicatorAnimation(SliderLayout.Animations.DROP);
            sliderLayout.setScrollTimeInSec(1);
            for(int i = 0; i < recommend_food.size(); i++) {
                SliderView sliderView = new SliderView(context);
                String description = (String)recommend_food.get(i).get("recommend_food_description");
                String imagePath = (String) recommend_food.get(i).get("recommend_food_image");
                imagePath = BackEnd.ip+"/" + imagePath;
                sliderView.setImageUrl(imagePath);
                sliderView.setDescription(description);
                sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                final int finalI = position;
                sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(SliderView sliderView) {
                        Toast.makeText(context, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
                    }
                });
                sliderLayout.addSliderView(sliderView);
            }
            return convertView;
        }
        else if(type == 1){
            FoodItem item = new FoodItem();
            convertView = layoutInflater.inflate(R.layout.food_item, null);
            item.foodImage = convertView.findViewById(R.id.food_image);
            item.foodName = convertView.findViewById(R.id.food_name);
            item.foodUp = convertView.findViewById(R.id.food_up);
            item.foodDown = convertView.findViewById(R.id.food_down);
            item.foodTime = convertView.findViewById(R.id.food_time);
            item.foodPlace = convertView.findViewById(R.id.food_place);

            String imageURL = (String)data.get(position - 1).get("food_image");
            imageURL = BackEnd.ip+"/" + imageURL;
            Picasso.get().load(imageURL).into(item.foodImage);
            item.foodUp.setText((String) data.get(position - 1).get("food_up"));
            item.foodDown.setText((String) data.get(position - 1).get("food_down"));
            item.foodName.setText((String) data.get(position - 1).get("food_name"));
            item.foodTime.setText((String) data.get(position - 1).get("food_time"));
            item.foodPlace.setText((String) data.get(position -1).get("food_place"));
            return convertView;
        }
        return convertView;
    }
}
