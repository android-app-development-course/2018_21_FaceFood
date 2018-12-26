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

public class FoodAdapter extends BaseAdapter {

    private List<Map<String, Object>> recommand_food;
    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;

    private int TYPE_COUNT = 2;


    public FoodAdapter(Context context, List<Map<String, Object>> data, List<Map<String, Object>> recommand_food) {

        this.context = context;
//        this.data = data;
//        this.recommand_food = recommand_food;
        this.layoutInflater = LayoutInflater.from(context);

        this.data = new ArrayList<>();
        this.recommand_food = new ArrayList<>();

        for(int i = 0; i < data.size(); i++) {
            this.data.add(data.get(i));
        }

        for(int i = 0; i < recommand_food.size(); i++) {
            this.recommand_food.add(recommand_food.get(i));
        }

    }

    public void addMoreDate(List<Map<String, Object>> updateContent) {

        for(int i = 0; i < updateContent.size(); i++) {

            data.add(updateContent.get(i));

        }

    }

    @Override
    public Object getItem(int position) {
        if(position < 1) {
            return recommand_food.get(position);
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

            for(int i = 0; i < recommand_food.size(); i++) {

                SliderView sliderView = new SliderView(context);

                String description = (String)recommand_food.get(i).get("recommand_food_description");
                String imagePath = (String) recommand_food.get(i).get("recommand_food_image");
                imagePath = "http://129.204.49.159/" + imagePath;

//                int imageID = (int) recommand_food.get(i).get("recommand_food_image");

//                sliderView.setImageDrawable(imageID);
                sliderView.setImageUrl(imagePath);
                sliderView.setDescription(description);

                sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                final int finalI = position;

                sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(SliderView sliderView) {
                        Toast.makeText(context, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(context, DetailedActivity.class);
//                        startActivity(intent);
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
            imageURL = "http://129.204.49.159/" + imageURL;
//
//            try {
//                loadImage.get(position - 1);
//            }
//            catch (IndexOutOfBoundsException e) {
//                loadImage.add(1);
//                new LoadNetImageView(item.foodImage).execute(imageURL);
//            }

            Picasso.get().load(imageURL).into(item.foodImage);

//            item.foodImage.setImageResource((Integer) data.get(position - 1).get("food_image"));
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
