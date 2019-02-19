package com.example.zzk.mainpage;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyy.module.UserInfo;
import com.example.cyy.util.BackEnd;
import com.example.cyy.util.ImageViewUrlSetter;
import com.example.cyy.util.NetDoneListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class DetailedFragment extends Fragment{
    private boolean hasDeleted = false;
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

        String userIDInNormal =  (String)detailedInfo.get("userID");
        final String normalID  = (String)detailedInfo.get("id");
        if(userIDInNormal.contentEquals(UserInfo.getUser().getId())){
            Button delDetail = view.findViewById(R.id.deleteNormal);
            delDetail.setVisibility(View.VISIBLE);
            delDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(hasDeleted) {
                        Toast.makeText(getContext(), getString(R.string.AlreadyDelete), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("normalID", normalID);
                    }catch (Exception er){
                        Log.e("JOSN",er.getMessage());
                    }
                    StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.post(getContext(),BackEnd.ip + "/deleteNormal", entity,"application/json", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Toast.makeText(getContext(), getString(R.string.AlreadyDelete), Toast.LENGTH_SHORT).show();
                            hasDeleted=true;
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(getContext(), "X!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }



        ImageView imageViews[] = getAllImageViews();

        // Parse the URLs
        try {
            JSONArray jsonArray = new JSONArray((String) detailedInfo.get("images"));
            for(int i = 0; i < jsonArray.length(); i++) {
                String imageURL = jsonArray.getString(i);
                imageURL = BackEnd.ip+"/image/" + imageURL;
                Picasso.get().load(imageURL).into(imageViews[i]);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }



        final ImageView profilePic = view.findViewById(R.id.imageView1);
        final UserInfo otherUser = new UserInfo(userIDInNormal);
        otherUser.downdateUserInfo(new NetDoneListener() {
            @Override
            public void OnSuccess() {
                new ImageViewUrlSetter(profilePic).set(otherUser.getProfilePhotoAdd());
            }

            @Override
            public void onFailed() {
                Toast.makeText(getContext(), getString(R.string.badNetWork), Toast.LENGTH_SHORT).show();
            }
        });
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