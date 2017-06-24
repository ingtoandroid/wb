package com.example.a.app10.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a.app10.R;
import com.example.a.app10.bean.URLString;
import com.example.a.app10.tool.CoutDownTimerUtils;
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
public class RegisterActivity extends AppCompatActivity {
    private CoutDownTimerUtils coutDownTimerUtils;

    private ImageButton back;
    private Button login;
    private EditText ed_phonenumber;
    private EditText ed_verification_code;
    private EditText ed_new_password;
    private TextView tx_send_verification;
    private TextView tx_to_login;
    private URLString strUrl = new URLString();
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initViews();
        initEvents();
    }
    private void initViews(){
        back = (ImageButton)findViewById(R.id.back);
        login=(Button)findViewById(R.id.bu_login_set_newpwd);
        ed_phonenumber = (EditText)findViewById(R.id.input_phontnumber);
        ed_verification_code = (EditText)findViewById(R.id.input_verification);
        ed_new_password = (EditText)findViewById(R.id.input_newpassword);
        tx_send_verification = (TextView)findViewById(R.id.send_verification);
        coutDownTimerUtils=new CoutDownTimerUtils(tx_send_verification,60000,1000);
        tx_to_login = (TextView)findViewById(R.id.tologin);
    }
    private void initEvents(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOutToRegister();
            }
        });
        tx_send_verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCodeForRegister();
            }
        });
        tx_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void checkOutToRegister(){
        String phoneNumber = ed_phonenumber.getText().toString().trim();
        String verificationCode = ed_verification_code.getText().toString().trim();
        String newPassword = ed_new_password.getText().toString().trim();

        if(phoneNumber.length() > 0 && verificationCode.length() > 0 && newPassword.length() > 0){

            Call call = Net.getInstance().register(phoneNumber,newPassword,verificationCode);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    ToastInfo("网络请求错误");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String str_response = response.body().string();
                    if (str_response.length() > 0) {
                        JSONTokener jsonTokener = new JSONTokener(str_response);
                        JSONObject jsonObject = null;
                        int code = -2;
                        try {
                            jsonObject = (JSONObject) jsonTokener.nextValue();
                            code = jsonObject.getInt("code");
                            String hx_pwd = jsonObject.getString("hx_pwd");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (code == 101) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, "该手机号已注册", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (code == 105) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, "验证码错误或超时", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (code == -1) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, "注册异常", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (code == 0) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }
                    }
                }
            });
        }
        else {
            Toast.makeText(RegisterActivity.this,"数据不可为空",Toast.LENGTH_LONG).show();
        }
    }


    //发送验证码
    private void getCodeForRegister(){
        String phoneNumber = ed_phonenumber.getText().toString().trim();

        if(phoneNumber.length()>0){

            Call call = Net.getInstance().getCodeForRegister(phoneNumber);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String str_response = response.body().string();
                    if(str_response.length() > 0){
                        JSONTokener jsonTokener = new JSONTokener(str_response);
                        JSONObject jsonObject = null;
                        int code = -2;
                        try {
                            jsonObject = (JSONObject)jsonTokener.nextValue();
                            code = jsonObject.getInt("code");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(code == 102){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this,"手机号已存在",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else if(code == 103)
                        {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this,"手机号不存在",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else if(code == 104){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this,"发送失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else if(code == 0){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this,"发送成功",Toast.LENGTH_SHORT).show();
                                    coutDownTimerUtils.start();
                                }
                            });
                        }

                    }
                }
            });
        }
        else{
            Toast.makeText(RegisterActivity.this,"手机号不能为空",Toast.LENGTH_SHORT).show();
        }

    }

    private void ToastInfo(final String content){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this,content,Toast.LENGTH_LONG);
            }
        });
    }
}
