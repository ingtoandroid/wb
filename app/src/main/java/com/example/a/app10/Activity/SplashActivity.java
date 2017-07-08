package com.example.a.app10.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.a.app10.R;
import com.example.a.app10.tool.Net;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

public class SplashActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private boolean isFinish=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Window window=getWindow();//设置透明状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    //isFinish=true;
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void init(){
        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String name=sharedPreferences.getString("id","-1");
        String pwd=sharedPreferences.getString("pwd","-1");
        if((!name.equals("-1"))&&(!pwd.equals("-1"))){
            /*登陆*/
            Call call = Net.getInstance().login(name,pwd);
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
                                Net.setPhotoUrl(jsonObject.getString("filePath"));
                                Net.setRemark(jsonObject.getString("remark"));
                                Net.setPersonName(jsonObject.getString("personName"));
                                Net.setPersonID(jsonObject.getString("personId"));
                                Net.setHx_pwd(jsonObject.getString("hx_pwd"));
                                Net.setUsername(jsonObject.getString("username"));
                                Net.setMegsSize(jsonObject.getInt("megsSize"));
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                            while (!isFinish);
                            Intent intent = new Intent(SplashActivity.this,Main1Activity.class);
                            startActivity(intent);
                            finish();
                        }
                        else if(code == 100){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SplashActivity.this,"用户名或密码错误",Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                        else if(code == -1){
                            ToastInfo("登录异常");
                            Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            });
        }
        else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intent=new Intent(SplashActivity.this,Main1Activity.class);
                    startActivity(intent);
                    finish();
                }
            }).start();

        }
    }
    private void checkOutToLogin(String username,String password){
        if(username.length() > 0 && password.length() > 0){

            Call call = Net.getInstance().login(username,password);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    ToastInfo("网络请求错误");
                    Intent intent=new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
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
                            Intent intent=new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        if(code == 0){
                            try {
                                Net.setUserUUID(jsonObject.getString("userUUID"));
                                Net.setPhotoUrl(jsonObject.getString("filePath"));
                                Net.setRemark(jsonObject.getString("remark"));
                                Net.setPersonName(jsonObject.getString("personName"));
                                Net.setPersonID(jsonObject.getString("personId"));
                                Net.setHx_pwd(jsonObject.getString("hx_pwd"));
                                Net.setUsername(jsonObject.getString("username"));
                                Net.setMegsSize(jsonObject.getInt("megsSize"));
                            }catch (JSONException e) {
                                e.printStackTrace();
                                Intent intent=new Intent(SplashActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }

                            Intent intent = new Intent(SplashActivity.this,Main1Activity.class);
                            startActivity(intent);
                        }
                        else if(code == 100){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SplashActivity.this,"用户名或密码错误",Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(SplashActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                        else if(code == -1){
                            ToastInfo("登录异常");
                            Intent intent=new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            });

        }
        else {
            Toast.makeText(SplashActivity.this,"账号密码不能为空",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void ToastInfo(final String content){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SplashActivity.this,content,Toast.LENGTH_LONG);
            }
        });
    }
}
