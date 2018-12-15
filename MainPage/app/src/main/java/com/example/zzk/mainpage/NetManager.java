package com.example.zzk.mainpage;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.*;

public class NetManager {

    String path;

    public void setPath(String path) {
        this.path = path;
    }

    public JSONObject postData(JSONObject jsonObject) throws Exception {

        // throws Exception
        URL url = new URL(path);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        setPostRequestContent(conn, jsonObject);

        conn.connect();

        int code = conn.getResponseCode();

        if (code == 200) {
            JSONObject jsonObject1 = receiveJson(conn);
            return jsonObject1;
        }
        else {
            return null;
        }

    }

    private void setPostRequestContent(HttpURLConnection conn,
                                       JSONObject jsonObject) throws IOException {

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();
    }

    private JSONObject receiveJson(HttpURLConnection conn) throws Exception {

        InputStream is = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

        char [] buffer = new char[255];
        reader.read(buffer);

        String json = new String(buffer);

        return new JSONObject(json);
    }

}
