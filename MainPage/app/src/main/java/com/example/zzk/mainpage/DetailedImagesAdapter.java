package com.example.zzk.mainpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.cyy.util.BackEnd;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailedImagesAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<String> imageURLs;
    private int TYPE_COUNT = 1;

    public DetailedImagesAdapter(Context context, List<String> imageURLs) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.imageURLs = imageURLs;
    }

    @Override
    public Object getItem(int position) {
        return imageURLs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return imageURLs.size();
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.detailed_images_item, null);
        ImageView imageView = convertView.findViewById(R.id.detailedImages_item);
        String imageURL = imageURLs.get(position);
        imageURL = BackEnd.ip+"/image/" + imageURL;
        Picasso.get().load(imageURL).into(imageView);
        return imageView;
    }
}
