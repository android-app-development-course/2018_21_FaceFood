package com.example.zzk.mainpage;

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
import android.widget.Toast;

import org.json.JSONObject;
import com.example.cyy.util.BackEnd;

public class SignUp extends AppCompatActivity {

    private EditText mnickname;
    private EditText mstudent_number;
    private EditText mpassword;
    private Button mButton;
    private Handler handler;
    private NetManager netManager;
    private JsonManager jsonManager;

    // Constant
    private final int SIGNUP_SUCCESS = 1;
    private final int SIGNUP_FAILED = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar

        setContentView(R.layout.activity_sign_up);

        mnickname = findViewById(R.id.input_nickname);
        mstudent_number = findViewById(R.id.input_number);
        mpassword = findViewById(R.id.input_password);
        mButton = findViewById(R.id.btn_signup);

        new Thread(new Runnable() {
            @Override
            public void run() {

                Looper.prepare();

                handler = new Handler() {
                    @Override
                    public void handleMessage(Message message) {
                        super.handleMessage(message);
                        if(message.what == SIGNUP_SUCCESS) {
                            Toast.makeText(getApplicationContext(), "注册成功，欢迎使用！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                        }
                        else if(message.what == SIGNUP_FAILED) {
                            Toast.makeText(getApplicationContext(), "注册失败, 学号已经被注册", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                Looper.loop();
            }
        }).start();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s_nickname = mnickname.getText().toString();
                String s_student_number = mstudent_number.getText().toString();
                String s_password = mpassword.getText().toString();

                signup(s_nickname, s_student_number, s_password);
            }
        });

        netManager = new NetManager();
        jsonManager = new JsonManager();

        netManager.setPath(BackEnd.ip+"/createAccount");
    }

    private void signup(final String nickname, final String student_number, final String password) {

        new Thread() {

            @Override
            public void run() {

                try {
                    JSONObject jsonObject = jsonManager.getSingupJSON(nickname, student_number, password);
                    JSONObject jsonObject1 = netManager.postData(jsonObject);

                    String signupStatus = jsonObject1.getString("signup");

                    Message message = new Message();

                    if(signupStatus.equals("success")) {
                        message.what = SIGNUP_SUCCESS;
                    }
                    else if(signupStatus.equals("failed")) {
                        message.what = SIGNUP_FAILED;
                    }

                    handler.sendMessage(message);
                }
                catch (Exception e) {

                }

            }

        }.start();
    }

}
