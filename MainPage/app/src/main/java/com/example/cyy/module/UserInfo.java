package com.example.cyy.module;

import android.content.Context;
import android.net.sip.SipSession;
import android.util.Log;
import android.widget.Toast;

import com.example.cyy.util.BackEnd;
import com.example.cyy.util.NetDoneListener;
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
    public enum InfoType{
        name,id,address,gender,photo
    };
    static public void initUserInfo(final String id, final Context context, NetDoneListener netDoneListener)
    {
        me=new UserInfo();
        me.setId(id);
        me.downdateUserInfo(netDoneListener);
    }
    static public void logOut(){
        me=null;
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
        if(this.add!=null)
            params.put("address",this.add);
        client.get(BackEnd.ip+"/setUserinfo", params, new JsonHttpResponseHandler() {
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
    public void downdateUserInfo(final NetDoneListener netDoneListener){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("student_id", id);
        final UserInfo _this=this;
        client.get(BackEnd.ip+"/getUserinfo", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject ret){
                        // called when response HTTP status is "200 OK"
                        try {
                            ret=ret.getJSONArray("data").getJSONObject(0);
                            _this.setName(ret.getString("nickName"));
                            _this.setAdd(ret.getString("address"));
                            _this.setProfilePhotoAdd(ret.getString("profilePhotoAdd"));
                            _this.setGender(ret.getString("gender"));
                            if(netDoneListener!=null)
                                netDoneListener.OnNetDone();
                            _this.setInitComplete();
                            Log.i("UserInfo","Successfully obaint user info");
                        }catch (Exception e){
                            UserInfo.logOut();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    }
                });
        return;
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
    public void setGender(String gender){
        Gender myGender=Gender.UNKNOW;
        if(gender=="男"||gender=="boy"||gender=="male"||gender=="man"||gender==String.valueOf(Gender.MALE.value)){
            myGender=Gender.MALE;
        }
        else if(gender=="女"||gender=="girl"||gender=="female"||gender=="woman"||gender==String.valueOf(Gender.MALE.value)){
            myGender=Gender.FEMALE;
        }
        this.gender=myGender;
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
    private boolean isInit=false;
    static private UserInfo me=null;

    public void setInitComplete(){
        isInit=true;
    }
    public boolean getIsInit(){
        return isInit;
    }

    private UserInfo(String name,String add,String profilePhotoAdd,String gender,String id){
        this.setName(name);
        this.setAdd(add);
        this.setProfilePhotoAdd(profilePhotoAdd);
        this.setId(id);
        this.setGender(gender);
    }
    private UserInfo(){
        ;
    }
}
