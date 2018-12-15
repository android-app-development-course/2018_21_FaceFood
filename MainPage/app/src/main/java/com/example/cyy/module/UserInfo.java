package com.example.cyy.module;

import com.example.zzk.mainpage.JsonManager;
import com.example.zzk.mainpage.NetManager;

import org.json.JSONException;
import org.json.JSONObject;

//单体类
public class UserInfo {
    static public UserInfo getTmpUsedUserInfo(){
        return new UserInfo("陈舞阳","西三",null,"男","20161380241");
    }
    static public void initUserInfo(String id) throws Exception {
        NetManager nm = new NetManager();
        nm.setPath("http://yummmy.cn/getUserInfo");
        JSONObject ret = nm.postData(JsonManager.getAccount(id));
        me=new UserInfo(ret.getString("name"),ret.getString("add"),ret.getString("profilePhotoAdd"),ret.getString("gender"),id);
    }
    static public UserInfo getUser(){
        return me;
    }
    public enum Gender{
        MALE,FEMALE,UNKNOW
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
