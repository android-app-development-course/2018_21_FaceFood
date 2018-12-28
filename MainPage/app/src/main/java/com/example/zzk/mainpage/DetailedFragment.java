package com.example.zzk.mainpage;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DetailedFragment extends Fragment{
    private Map<String, Object> detailedInfo;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.show_fragment, null);
        TextView detailedUsername = view.findViewById(R.id.detailedUsername);
        TextView detailedDateTime = view.findViewById(R.id.detailedDateTime);
        TextView detailedLocation = view.findViewById(R.id.detailedLocation);
        TextView detailedContent = view.findViewById(R.id.detailedContent);
        detailedUsername.setText((String) detailedInfo.get("username"));
        detailedDateTime.setText((String) detailedInfo.get("date_time"));
        detailedLocation.setText((String) detailedInfo.get("location"));
        detailedContent.setText((String) detailedInfo.get("content"));

        ImageView imageViews[] = getAllImageViews();

        // Parse the URLs
        try {
            JSONArray jsonArray = new JSONArray((String) detailedInfo.get("images"));
            for(int i = 0; i < jsonArray.length(); i++) {
                String imageURL = jsonArray.getString(i);
                imageURL = "http://129.204.49.159/image/" + imageURL;
                Picasso.get().load(imageURL).into(imageViews[i]);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public void setDetailedInfo(Map<String, Object> detailedInfo) {
        this.detailedInfo = detailedInfo;
    }

    public ImageView[] getAllImageViews() {
        ImageView imageView[] = new ImageView[9];
        imageView[0] = view.findViewById(R.id.detailedImageItem0);
        imageView[1] = view.findViewById(R.id.detailedImageItem1);
        imageView[2] = view.findViewById(R.id.detailedImageItem2);
        imageView[3] = view.findViewById(R.id.detailedImageItem3);
        imageView[4] = view.findViewById(R.id.detailedImageItem4);
        imageView[5] = view.findViewById(R.id.detailedImageItem5);
        imageView[6] = view.findViewById(R.id.detailedImageItem6);
        imageView[7] = view.findViewById(R.id.detailedImageItem7);
        imageView[8] = view.findViewById(R.id.detailedImageItem8);
        return imageView;
    }
}