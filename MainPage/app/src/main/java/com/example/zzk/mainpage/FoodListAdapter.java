package com.example.zzk.mainpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.example.cyy.util.BackEnd;

public class FoodListAdapter extends BaseAdapter {
    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;

    public FoodListAdapter(Context context, List<Map<String, Object>> data) {
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
        FoodItem item = new FoodItem();
        convertView = layoutInflater.inflate(R.layout.food_item, null);
        item.foodImage = convertView.findViewById(R.id.food_image);
        item.foodName = convertView.findViewById(R.id.food_name);
        item.foodUp = convertView.findViewById(R.id.food_up);
        item.foodDown = convertView.findViewById(R.id.food_down);
        item.foodTime = convertView.findViewById(R.id.food_time);
        item.foodPlace = convertView.findViewById(R.id.food_place);
        String imageURL = (String)data.get(position).get("food_image");
        imageURL = BackEnd.ip+"/" + imageURL;
        Picasso.get().load(imageURL).into(item.foodImage);

        item.foodUp.setText((String) data.get(position).get("food_up"));
        item.foodDown.setText((String) data.get(position).get("food_down"));
        item.foodName.setText((String) data.get(position).get("food_name"));
        item.foodTime.setText((String) data.get(position).get("food_time"));
        item.foodPlace.setText((String) data.get(position).get("food_place"));

        return convertView;

    }

}
