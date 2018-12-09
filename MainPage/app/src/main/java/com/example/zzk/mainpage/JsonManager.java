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

}
