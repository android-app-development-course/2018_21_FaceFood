package com.example.cyy.module;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.zzk.mainpage.JsonManager;
import com.example.zzk.mainpage.NetManager;
import com.example.zzk.mainpage.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

//暂时设计成单体类，如果以后需要查看、更改他人信息的话，再进行修改
public class UserInfo {
    static public void initUserInfo(final String id, final Context context)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("student_id", id);
        client.get("http://129.204.49.159/getUserinfo", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject ret){
                        // called when response HTTP status is "200 OK"
                        try {
                            Log.i("UserInfo","Successfully obaint user info");
                            me = new UserInfo(ret.getString("nickName"), ret.getString("address"), ret.getString("profilePhotoAdd"), ret.getString("gender"), id);
                        }catch (Exception e){
                            Toast.makeText(context,context.getResources().getText(R.string.ErrorCannotGetUserInfo),Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Toast.makeText(context,context.getResources().getText(R.string.ErrorCannotGetUserInfo),Toast.LENGTH_LONG);
                    }
                }
        );
    }
    public boolean updateUserInfo(final Context context){
        if(me==null){
            return false;
        }
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("student_id", id);
        params.put("nickName",name);
        if(profilePhotoAdd!=null)
            params.put("profilePhotoAdd",profilePhotoAdd);
        if(this.gender!=gender.UNKNOW)
            params.put("gender",String.valueOf(gender.getValue()));
        client.get("http://129.204.49.159/setUserinfo", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject ret){
                // called when response HTTP status is "200 OK"
                Toast.makeText(context,context.getResources().getText(R.string.SuccessUpdateUserInfo),Toast.LENGTH_LONG);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Toast.makeText(context,context.getResources().getText(R.string.ErrorCannotUpdateUserInfo),Toast.LENGTH_LONG);
            }
        });
        return true;
    }
    static public UserInfo getUser(){
        return me;
    }
    public enum Gender{
        MALE(0),FEMALE(1),UNKNOW(-1);
        private int value;
        private Gender(int value){
            this.value=value;
        }
        int getValue(){return value;}
        public String toString(){
            switch (value) {
                case 0:
                    return "male";
                case 1:
                    return "female";
                default:
                    return "Unknow";
            }
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getProfilePhotoAdd() {
        return profilePhotoAdd;
    }

    public void setProfilePhotoAdd(String profilePhotoAdd) {
        this.profilePhotoAdd = profilePhotoAdd;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String name;
    private String add;
    private String profilePhotoAdd;//头像文件的地址
    private Gender gender;
    private String id;
    static private UserInfo me=null;

    private UserInfo(String name,String add,String profilePhotoAdd,String gender,String id){
        this.setName(name);
        this.setAdd(add);
        this.setProfilePhotoAdd(profilePhotoAdd);
        this.setId(id);
        Gender myGender=Gender.UNKNOW;
        if(gender=="男"||gender=="boy"||gender=="male"||gender=="man"){
            myGender=Gender.MALE;
        }
        else if(gender=="女"||gender=="girl"||gender=="female"||gender=="woman"){
            myGender=Gender.FEMALE;
        }
        this.setGender(myGender);
    }
}
