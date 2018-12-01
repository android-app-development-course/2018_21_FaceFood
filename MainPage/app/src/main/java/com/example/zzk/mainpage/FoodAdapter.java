package com.example.zzk.mainpage;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.RowId;
import java.util.List;
import java.util.Map;

public class FoodAdapter extends BaseAdapter {

    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;

    public FoodAdapter(Context context, List<Map<String, Object>> data) {

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
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FoodItem item = new FoodItem();
        convertView = layoutInflater.inflate(R.layout.food_item, null);

        item.foodImage = (ImageView) convertView.findViewById(R.id.food_image);

        item.foodName = (TextView) convertView.findViewById(R.id.food_name);

        item.foodUp = (TextView) convertView.findViewById(R.id.food_up);

        item.foodDown = (TextView) convertView.findViewById(R.id.food_down);

//        item.foodDown.setText("5");
//        item.foodName.setText("大便");
//        item.foodUp.setText("100");
//        item.foodImage.setImageResource(R.drawable.ic_add_box_black_24dp);

        item.foodImage.setImageResource((Integer) data.get(position).get("food_image"));
        item.foodUp.setText((String) data.get(position).get("food_up"));
        item.foodDown.setText((String) data.get(position).get("food_down"));
        item.foodName.setText((String) data.get(position).get("food_name"));

        return convertView;
    }
}
