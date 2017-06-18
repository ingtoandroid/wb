package com.example.a.app10.Activity;

import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    private String location = "";
    private String telephone = "";
    private String sex = "";
    private String signature = "";
    private String headImageUrl = "";

    private ImageView back;
    private ImageView im_headImage;
    private EditText ed_username;
    private TextView tx_phoneNumber;
    private EditText ed_sex;
    private EditText ed_location;
    private EditText ed_signature;
    private TextView tx_saveInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_modify_data);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        init();
        initEvent();

        getPersonalInfo();
    }

    private void init(){
        back = (ImageView)findViewById(R.id.back);
        im_headImage = (ImageView)findViewById(R.id.head_image);
        ed_username = (EditText)findViewById(R.id.username);
        tx_phoneNumber = (TextView)findViewById(R.id.phone_number);
        ed_sex = (EditText)findViewById(R.id.sex);
        ed_location = (EditText)findViewById(R.id.location);
        ed_signature = (EditText)findViewById(R.id.signature);
        tx_saveInfo = (TextView)findViewById(R.id.save_info);
    }

    private void initEvent(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tx_saveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_username = ed_username.getText().toString().trim();
                String str_signature = ed_signature.getText().toString().trim();
                String str_sex = ed_sex.getText().toString().trim();
                modifyInfo(str_username,str_signature,str_sex);
            }
        });
    }
    //获取用户信息
    private void getPersonalInfo(){
        Call call = Net.getInstance().getPersonalInfo(Net.getPersonID());
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
                    headImageUrl = jsonObject.getString("modelName");//图片路径，绝对
                    telephone = jsonObject.getString("telephone");
                    sex = jsonObject.getString("sex");
                    signature = jsonObject.getString("signature");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ed_username.setText(nickName);
                        tx_phoneNumber.setText(telephone);
                        ed_sex.setText(sex);
                        Glide.with(ModifyDataActivity.this).load(headImageUrl).into(im_headImage);
                        //头像设置
//                        ed_location.setText();
                        ed_signature.setText(signature);
                    }
                });
            }
        });
    }

    //修改会员信息
    public void modifyInfo(String username,String signature,String sex){
        Call call = Net.getInstance().modifyInfo(Net.getPersonID(),username,signature,sex);
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
                    if(megs.equals("修改成功")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ModifyDataActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ModifyDataActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
