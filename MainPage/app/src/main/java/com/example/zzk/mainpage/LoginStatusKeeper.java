package com.example.zzk.mainpage;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.cyy.module.UserInfo;

import java.util.Calendar;
import java.util.Date;

public class LoginStatusKeeper {
    private String userID;
    private String nickname;

    // Login status
    public final int LOGIN = 1;
    public final int LOGIN_EXPIRED = 2;

    public LoginStatusKeeper() {

    }

    public int getLoginStatus(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);

        int year = sharedPreferences.getInt("year", -1);
        int month = sharedPreferences.getInt("month", -1);
        int day = sharedPreferences.getInt("day", -1);
        userID = sharedPreferences.getString("userID", "error");

        // Get current time
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        if(year != currentYear || currentMonth != month || ((currentDay - day) > 1)) {
            return LOGIN_EXPIRED;
        }
        else {
            return LOGIN;
        }

    }

    public void updateLoginStatus(Context context) {

        // Get current time
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        SharedPreferences sharedPreferences = context.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("year", currentYear);
        editor.putInt("month", currentMonth);
        editor.putInt("day", currentDay);

        editor.commit();
    }

    public void saveUserID(Context context, String userID) {
        this.userID = userID;

        SharedPreferences sharedPreferences = context.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userID", userID);
        editor.commit();
    }

    public String getUserID() {
        return this.userID;
    }

    public void saveNickname(Context context, String nickname) {
        this.nickname = nickname;

        SharedPreferences sharedPreferences = context.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nickname", nickname);
        editor.commit();
    }

    public String getNickname() {
        return this.nickname;
    }
}
