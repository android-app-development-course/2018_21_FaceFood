package com.example.zzk.mainpage;

import org.json.JSONObject;

public class JsonManager {

    JSONObject getAccountAndMd5(String account, String password) {

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("account", account);
            jsonObject.put("md5", password);
        }
        catch (Exception e) {

        }

        return jsonObject;
    }

    JSONObject getSingupJSON(String nickname, String student_number, String password) {

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("nickname", nickname);
            jsonObject.put("student_number", student_number);
            jsonObject.put("password", password);
        }
        catch (Exception e) {

        }

        return jsonObject;
    }

}
