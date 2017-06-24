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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a.app10.R;
import com.example.a.app10.tool.Net;

import java.util.Set;

public class SettingActivity extends AppCompatActivity {
    private ImageButton back;
    private LinearLayout about_us_item;
    private LinearLayout username_item;
    private TextView tx_usernameSetting;
    private Button exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_setting);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        init();
        initEvent();
    }

    private void init(){
        back = (ImageButton) findViewById(R.id.back);
        about_us_item = (LinearLayout)findViewById(R.id.about_us_item);
        username_item = (LinearLayout)findViewById(R.id.username_item);
        tx_usernameSetting = (TextView)findViewById(R.id.username_setting);
        tx_usernameSetting.setText(Net.getPersonName());
        exit = (Button)findViewById(R.id.exit);
    }

    private void initEvent(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        about_us_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,AboutUsActivity.class);
                startActivity(intent);
            }
        });
        username_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Net.getPersonID().equals("")){
                    Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(SettingActivity.this, ModifyDataActivity.class);
                    startActivity(intent);
                }
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tx_usernameSetting.setText(Net.getPersonName());
    }

    private void exit(){
        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("id", "-1");
        editor.putString("pwd", "-1");
        editor.commit();
        Net.setUserUUID("");
        Net.setUsername("");
        Net.setHx_pwd("");
        Net.setPersonID("");
        Net.setPersonName("");
        Net.setPhotoUrl("");
        Net.setMegsSize(0);
        Intent intent = new Intent(SettingActivity.this,SplashActivity.class);
        startActivity(intent);
        finish();
    }
}

