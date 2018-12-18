package com.example.zzk.mainpage;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyy.module.UserInfo;

import org.json.JSONObject;

public class Login extends AppCompatActivity {

    private EditText studentNumber;
    private EditText password;
    private Button loginButton;
    private TextView createAccount;
    private NetManager netManager;
    private JsonManager jsonManager;
    private Handler handler;
    private LoginStatusKeeper loginStatusKeeper;

    // Login Status
    private final int LOGIN_SUCCESS = 1;
    private final int LOGIN_FAILED = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginStatusKeeper = new LoginStatusKeeper();
        if(loginStatusKeeper.getLoginStatus(getApplicationContext()) == loginStatusKeeper.LOGIN)
        {
            loginStatusKeeper.updateLoginStatus(getApplicationContext());
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

        netManager = new NetManager();
        jsonManager = new JsonManager();

        netManager.setPath("http://yummmy.cn/account");
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
                            //UserInfo.initUserInfo(studentNumber.getText().toString(),ct);
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

        new Thread() {

            public void run() {

                try {

                    JSONObject jsonObject = jsonManager.getAccountAndMd5(id, password);
                    JSONObject receiveJsonObject = netManager.postData(jsonObject);

                    String loginStatus = receiveJsonObject.getString("login");

                    Message message = new Message();

                    if(loginStatus.equals("success")) {
                        message.what = LOGIN_SUCCESS;
                    }
                    else if(loginStatus.equals("fail")) {
                        message.what = LOGIN_FAILED;
                    }

                    handler.sendMessage(message);
                }
                catch (Exception e) {
                    Log.i("info", "Exception");
                }

            }

        }.start();


    }

}
