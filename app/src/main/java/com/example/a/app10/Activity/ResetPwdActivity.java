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
import android.widget.TextView;
import android.widget.Toast;

import com.example.a.app10.R;
import com.example.a.app10.bean.URLString;
import com.example.a.app10.tool.Net;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

public class ResetPwdActivity extends AppCompatActivity {
    private TextView send_verification;
    private EditText ed_phonenumber;
    private EditText ed_verification_code;
    private EditText ed_new_password;
    private Button login;
    private ImageButton back;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_reset_pwd);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        init();
        initEvent();
    }

    private void init(){
        back = (ImageButton)findViewById(R.id.back);
        send_verification = (TextView)findViewById(R.id.send_verification);
        login = (Button)findViewById(R.id.bu_login_set_newpwd);
        ed_phonenumber = (EditText)findViewById(R.id.input_phontnumber);
        ed_verification_code = (EditText)findViewById(R.id.input_verification);
        ed_new_password = (EditText)findViewById(R.id.input_newpassword);
    }

    private void initEvent(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        send_verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerification();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPwdAndLogin();
            }
        });
    }

    //发送验证码
    private void sendVerification(){
        String phoneNumber = ed_phonenumber.getText().toString().trim();
        if(phoneNumber.length()>0) {
            Call call = Net.getInstance().getCodeForResetPwd(phoneNumber);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (code == 102) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ResetPwdActivity.this, "手机号已存在", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (code == 103) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ResetPwdActivity.this, "手机号不存在", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (code == 104) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ResetPwdActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (code == 0) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ResetPwdActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            });
        }
        else{
            Toast.makeText(ResetPwdActivity.this,"手机号不能为空",Toast.LENGTH_SHORT).show();
        }
    }

    private void resetPwdAndLogin() {
        String phoneNumber = ed_phonenumber.getText().toString().trim();
        String code = ed_verification_code.getText().toString().trim();
        String newPwd = ed_new_password.getText().toString().trim();

        if (phoneNumber.length() > 0 && code.length() > 0 && newPwd.length() > 0) {
            Call call = Net.getInstance().resetPwd(phoneNumber, code, newPwd);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

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

                        if (code == 105) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ResetPwdActivity.this, "验证码错误或超时", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (code == 106) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ResetPwdActivity.this, "重置失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (code == -1) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ResetPwdActivity.this, "重置异常", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (code == 0) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ResetPwdActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Intent intent = new Intent(ResetPwdActivity.this, Main1Activity.class);
                            startActivity(intent);
                        }
                    }
                }
            });
        }
        else {
            Toast.makeText(ResetPwdActivity.this, "信息不能为空", Toast.LENGTH_SHORT).show();
        }
    }
}
