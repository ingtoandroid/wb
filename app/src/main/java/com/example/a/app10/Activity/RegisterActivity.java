package com.example.a.app10.Activity;

import android.content.Intent;
import android.os.Handler;
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

public class RegisterActivity extends AppCompatActivity {

    private Button login;
    private EditText ed_phonenumber;
    private EditText ed_verification_code;
    private EditText ed_new_password;
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
        login=(Button)findViewById(R.id.bu_login_set_newpwd);
        ed_phonenumber = (EditText)findViewById(R.id.input_phontnumber);
        ed_verification_code = (EditText)findViewById(R.id.input_valid);
        ed_new_password = (EditText)findViewById(R.id.input_newpassword);
    }
    private void initEvents(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOutToRegister();
            }
        });
    }
    private void checkOutToRegister(){
        String phoneNumber = ed_phonenumber.getText().toString().trim();
        String verificationCode = ed_verification_code.getText().toString().trim();
        String newPassword = ed_new_password.getText().toString().trim();

        if(phoneNumber.length() > 0 && verificationCode.length() > 0 && newPassword.length() > 0){
            String url = strUrl.getProtocol()+strUrl.getDelimiter()+strUrl.getDelimiter()+strUrl.getHostname()+":"
                    +strUrl.getPort()+strUrl.getDelimiter()+strUrl.getPath1()+strUrl.getPath2()+strUrl.getParameters();
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

                }
            });
        }
        else {
            Toast.makeText(RegisterActivity.this,"数据不可为空",Toast.LENGTH_LONG).show();
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
