package com.example.a.app10.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.a.app10.tool.Net;
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
//                checkOutToLogin();
                Intent intent = new Intent(LoginActivity.this,Main1Activity.class);
                startActivity(intent);
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
        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String name=sharedPreferences.getString("id","-1");
        String pwd=sharedPreferences.getString("pwd","-1");
        if(pwd.equals("-1")){
            ed_password.setText("");
        }
        else
        {
            if(name.equals("-1")){
                ed_username.setText("");
            }
            else
                ed_username.setText(name);
        }

    }

    private void checkOutToLogin(){
        final String username = ed_username.getText().toString().trim();
        final String password = ed_password.getText().toString().trim();

        if(username.length() > 0 && password.length() > 0){

            Call call = Net.getInstance().login(username,password);
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
                        JSONTokener jsonTokener = null;
                        JSONObject jsonObject = null;
                        try {
                            jsonTokener = new JSONTokener(result);
                            jsonObject = (JSONObject)jsonTokener.nextValue();
                            code = jsonObject.getInt("code");
                        }catch (JSONException ex){
                            //异常处理
                        }

                        if(code == 0){
                            try {
                                Net.setUserUUID(jsonObject.getString("userUUID"));
                                Net.setPhotoUrl(jsonObject.getString("photoUrl"));
                                Net.setRemark(jsonObject.getString("remark"));
                                Net.setPersonName(jsonObject.getString("personName"));
                                Net.setPersonID(jsonObject.getString("personId"));
                                Net.setHx_pwd(jsonObject.getString("hx_pwd"));
                                Net.setUsername(jsonObject.getString("username"));
                                Net.setMegsSize(jsonObject.getInt("megsSize"));
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                            SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("id", username);
                            editor.putString("pwd", password);
                            editor.commit();
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
