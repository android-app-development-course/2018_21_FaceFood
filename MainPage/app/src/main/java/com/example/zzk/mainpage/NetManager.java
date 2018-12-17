package com.example.zzk.mainpage;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.Map;
import java.util.Scanner;

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

    //不要用这个函数
    //to:http://129.204.49.159/getUserinfo
    private static JSONObject sendGetRequest(String to, Map<String,String> param) {
            StringBuilder msb = new StringBuilder();
            for (Map.Entry<String, String> curr : param.entrySet()) {
                msb.append(curr.getKey() + "=" + curr.getValue() + "&");
            }
            HttpURLConnection conn = null;
            try {
                String Strurl = to + "?" + msb.toString();
                URL url = new URL(Strurl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                    Log.i("PostGetUtil", "get请求成功");
                    InputStream in = conn.getInputStream();
                    Scanner scanner = new Scanner(in);
                    StringBuilder sb = new StringBuilder();
                    while (scanner.hasNext()) {
                        sb.append(scanner.nextLine());
                    }
                    String backcontent = sb.toString();
                    backcontent = URLDecoder.decode(backcontent, "UTF-8");
                    Log.i("SendGetRequest", backcontent);
                    in.close();
                    return new JSONObject(backcontent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conn.disconnect();
            }
            return null;
        }
}
