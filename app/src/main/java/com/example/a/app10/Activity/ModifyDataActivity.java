package com.example.a.app10.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

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

public class ModifyDataActivity extends AppCompatActivity {

    private String nickName = "";
    private String modelName = "";
    private String telephone = "";
    private String sex = "";
    private String signature = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_modify_data);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

//        modifyInfo();
        getPersonalInfo();
    }

    //获取用户信息
    private void getPersonalInfo(){
        Call call = Net.getInstance().getPersonalInfo("10a9477c-ca1d-4ecc-ab19-ac27d0cafe53");
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String str_response = response.body().string();
                JSONTokener jsonTokener = new JSONTokener(str_response);
                try {
                    JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                    nickName = jsonObject.getString("nickName");
                    modelName = jsonObject.getString("modelName");
                    telephone = jsonObject.getString("telephone");
                    sex = jsonObject.getString("sex");
                    signature = jsonObject.getString("signature");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //修改会员信息
    public void modifyInfo(){
        Call call = Net.getInstance().modifyInfo("10a9477c-ca1d-4ecc-ab19-ac27d0cafe53","haha",signature,sex);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String str_response = response.body().string();
                JSONTokener jsonTokener = new JSONTokener(str_response);
                try {
                    JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                    String megs = jsonObject.getString("megs");
                    //修改成功
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
