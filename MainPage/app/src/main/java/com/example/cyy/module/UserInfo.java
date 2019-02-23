package com.example.cyy.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.cyy.util.BackEnd;
import com.example.cyy.util.NetDoneListener;
import com.example.zzk.mainpage.Login;
import com.example.zzk.mainpage.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class UserInfo {
    public enum InfoType{
        name,id,address,gender,photo
    };

    static public void initLoginedUserInfo(final String id, final Context context, final NetDoneListener netDoneListener)
    {
        final UserInfo me =new UserInfo();
        me.setId(id);
        me.downdateUserInfo(new NetDoneListener() {
            @Override
            public void OnSuccess() {
                SharedPreferences LoginedUser = context.getSharedPreferences("LoginedUser",context.MODE_PRIVATE);
                SharedPreferences.Editor editor = LoginedUser.edit();
                editor.putString("id",me.getId());
                editor.putString("name",me.getName());
                editor.putString("add",me.getAdd());
                editor.putString("profilePhotoAdd",me.getProfilePhotoAdd());
                editor.putString("gender",me.getGender().toString());
                editor.commit();
                if(netDoneListener!=null)netDoneListener.OnSuccess();
            }

            @Override
            public void onFailed() {
                if(netDoneListener!=null)netDoneListener.onFailed();
            }
        });
    }
    static public void Logout(Context context){
        context.getSharedPreferences("LoginedUser",context.MODE_PRIVATE).edit().clear();
    }
    static public UserInfo getLoginedUser(Context context){
        SharedPreferences l = context.getSharedPreferences("LoginedUser",Context.MODE_PRIVATE);
        if(l.contains("id")&&l.contains("name")&&l.contains("add")&&l.contains("profilePhotoAdd")&&l.contains("gender")) {
            return new UserInfo(
                    l.getString("name", ""),
                    l.getString("add",""),
                    l.getString("profilePhotoAdd",""),
                    l.getString("gender",""),
                    l.getString("id","")
            );
        }
        else throw new NullPointerException("当前登录状态不完整或者存在");
    }

    private UserInfo(){}
    public UserInfo(String id){this.id=id;}

    public boolean updateUserInfo(final Context context){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("student_id", id);
        params.put("nickName",name);
        if(profilePhotoAdd!=null)
            params.put("profilePhotoAdd",profilePhotoAdd);
            params.put("gender",gender);
        if(this.add!=null)
            params.put("address",this.add);
        client.get(BackEnd.ip+"/setUserinfo", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response){
                // called when response HTTP status is "200 OK"
                Toast.makeText(context,context.getResources().getText(R.string.SuccessUpdateUserInfo),Toast.LENGTH_LONG);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] res, Throwable t) {
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
                                netDoneListener.OnSuccess();
                            _this.setInitComplete();
                            Log.i("UserInfo","Successfully obaint user info");
                        }catch (Exception e){
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        if(netDoneListener!=null)
                            netDoneListener.onFailed();
                    }
                });
        return;
    }

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

    public String getGender(){
        return this.gender;
    }

    public void setGender(String gender){
        this.gender=gender;
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
    private String gender;
    private String id;
    private boolean isInit=false;

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
}
