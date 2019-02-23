package com.example.zzk.mainpage;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyy.module.UserInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import com.example.cyy.util.BackEnd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Login extends AppCompatActivity {

    private EditText studentNumber;
    private EditText password;
    private Button loginButton;
    private TextView createAccount;
    private Handler handler;
    private LoginStatusKeeper loginStatusKeeper;

    // Login Status
    private final int LOGIN_SUCCESS = 1;
    private final int LOGIN_FAILED = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        {
            Context c = getApplicationContext();
            try {
                FileOutputStream outStream = this.openFileOutput("b.txt", Context.MODE_PRIVATE);
                outStream.write("test".getBytes());
                outStream.close();
                File f = new File("/data/data/com.example.zzk.mainpage/files","b.txt");
                boolean isExits = f.exists();
            }catch (FileNotFoundException e){
                ;
            }catch (IOException e) {
                ;
            }
        }

        loginStatusKeeper = new LoginStatusKeeper();
        if(loginStatusKeeper.getLoginStatus(getApplicationContext()) == loginStatusKeeper.LOGIN)
        {
            loginStatusKeeper.updateLoginStatus(getApplicationContext());
            UserInfo.initLoginedUserInfo(loginStatusKeeper.getUserID(), getApplicationContext(), null);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar

        setContentView(R.layout.activity_login);

        studentNumber = findViewById(R.id.student_number);
        password = findViewById(R.id.student_password);
        loginButton = findViewById(R.id.btn_login);
        createAccount = findViewById(R.id.link_signup);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = studentNumber.getText().toString();
                String pass = password.getText().toString();
                login(id, pass);
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this.getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });

        final Context ct = getApplicationContext();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();

                handler = new Handler() {
                    @Override
                    public void handleMessage(Message message) {

                        super.handleMessage(message);

                        if(message.what == LOGIN_SUCCESS) {
                            Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_LONG).show();
                            loginStatusKeeper.updateLoginStatus(getApplicationContext());
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            //UserInfo.initLoginedUserInfo(studentNumber.getText().toString(),ct);
                            startActivity(intent);
                        }
                        else if(message.what == LOGIN_FAILED) {
                            Toast.makeText(getApplicationContext(), "登陆失败", Toast.LENGTH_LONG).show();
                        }
                    }
                };
                Looper.loop();
            }
        }).start();

    }

    private void login(final String id, final String password) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("account", id);
            jsonObject.put("md5", password);
            StringEntity entity = new StringEntity(jsonObject.toString());

            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

            asyncHttpClient.post(getApplicationContext(), BackEnd.ip+"/account", entity, "application/json",
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {
                                String json = new String(responseBody, "utf-8");
                                JSONObject jsonObject = new JSONObject(json);
                                String loginStatus = jsonObject.getString("login");
                                Message message = new Message();
                                if(loginStatus.equals("success")) {
                                    message.what = LOGIN_SUCCESS;
                                    loginStatusKeeper.saveUserID(getApplicationContext(), id);

                                    String nickname = jsonObject.getString("nickname");
                                    loginStatusKeeper.saveNickname(getApplicationContext(), nickname);
                                    UserInfo.initLoginedUserInfo(id, getApplicationContext(),null);
                                }
                                else if(loginStatus.equals("fail")) {
                                    message.what = LOGIN_FAILED;
                                }
                                handler.sendMessage(message);
                            }
                            catch (Exception e) {

                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });

        }
        catch (Exception e) {

        }
    }

}
