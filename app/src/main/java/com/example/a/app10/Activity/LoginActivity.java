package com.example.a.app10.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a.app10.R;
import com.example.a.app10.bean.URLString;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import android.os.Handler;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private Button reg;
    private TextView forget_pwd;
    private EditText ed_username;
    private EditText ed_password;
    private URLString strURL = new URLString();
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
//                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initViews();
        initEvents();

    }
    private void initViews(){
        login=(Button)findViewById(R.id.bu_login);
        reg=(Button)findViewById(R.id.bu_reg);
        forget_pwd = (TextView)findViewById(R.id.forget_pwd);
        ed_username = (EditText)findViewById(R.id.ed_username);
        ed_password = (EditText)findViewById(R.id.ed_pwd);

    }
    private void initEvents(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOutToLogin();
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ResetPwdActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkOutToLogin(){
        String username = ed_username.getText().toString().trim();
        String password = ed_password.getText().toString().trim();

        if(username.length() > 0 && password.length() > 0){
            strURL.setPath2("user/login");
            strURL.setParameters("username="+username+"&password="+password);

            String url = strURL.getProtocol() + strURL.getDelimiter()+strURL.getDelimiter()
                    +strURL.getHostname()+":"+strURL.getPort() +strURL.getDelimiter()
                    +strURL.getPath1()+strURL.getPath2()+"?"+strURL.getParameters();

            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder().url(url).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    ToastInfo("网络请求错误");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String result = response.body().string();
                    if(result != ""){
                        int code = -2;

                        try {
                            JSONTokener jsonTokener = new JSONTokener(result);
                            JSONObject jsonObject = (JSONObject)jsonTokener.nextValue();
                            code = jsonObject.getInt("code");
                        }catch (JSONException ex){
                            //处理结果
                        }

                        if(code == 0){
                            Intent intent = new Intent(LoginActivity.this,Main1Activity.class);
                            startActivity(intent);
                        }
                        else if(code == 100){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_LONG).show();
                                    ed_username.setText("");
                                    ed_password.setText("");
                                }
                            });
                        }
                        else if(code == -1){
                            ToastInfo("登录异常");
                        }
                    }
                }
            });

        }
        else {
            Toast.makeText(LoginActivity.this,"账号密码不能为空",Toast.LENGTH_LONG).show();
        }
    }

    private void ToastInfo(final String content){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this,content,Toast.LENGTH_LONG);
            }
        });
    }
}
