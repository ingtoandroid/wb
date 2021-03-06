package com.example.a.app10.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.example.a.app10.R;
import com.example.a.app10.tool.Net;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.DemoHelper;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.ui.ChatFragment;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EasyUtils;
import com.hyphenate.util.HanziToPinyin;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

public class chat extends AppCompatActivity {
    private EaseChatFragment chatFragment;
    private String toChatUsername;
    private String headurl;
    private String[] paths = {"", ""};
    private static String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.LOCATION_HARDWARE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
    };
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (getIntent().hasExtra("name"))
            toChatUsername = getIntent().getExtras().getString("name");
        //得到对方账号
        if (getIntent().hasExtra("filePath"))
            headurl = getIntent().getStringExtra("filePath");
        paths[0] = Net.getPhotoUrl();
        paths[1] = headurl;
        getPermission();
        init();
        EaseUI.getInstance().setSettingsProvider(new EaseUI.EaseSettingsProvider() {
            @Override
            public boolean isMsgNotifyAllowed(EMMessage message) {
                return false;
            }

            @Override
            public boolean isMsgSoundAllowed(EMMessage message) {
                return false;
            }

            @Override
            public boolean isMsgVibrateAllowed(EMMessage message) {
                return false;
            }

            @Override
            public boolean isSpeakerOpened() {
                return false;
            }
        });

        chatFragment = new ChatFragment();
        Bundle b = new Bundle();
        b.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        b.putString(EaseConstant.EXTRA_USER_ID, toChatUsername);
        b.putStringArray("paths", paths);
        chatFragment.setArguments(b);
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EMClient.getInstance().logout(true);

    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        String username = intent.getStringExtra("userId");
//        if (toChatUsername.equals(username))
//            super.onNewIntent(intent);
//        else {
//            finish();
//            startActivity(intent);
//        }
//​
//
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }
    }


    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
    }

    private void init() {
        DemoHelper.getInstance().init(this);
        /*登陆*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().login(Net.getUsername(), Net.getHx_pwd(), new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        //Toast.makeText(chat.this,"success",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(int code, String error) {
                        //Toast.makeText(chat.this,""+error,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }
                });
//                try {
//                    EMClient.getInstance().contactManager().addContact("text", "hello");
//                }
//                catch (HyphenateException e){
//                    e.printStackTrace();
//                }
            }
        }).start();
    }

    private void getPermission() {
        List<String> list = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(chat.this, PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED) {
            list.add(PERMISSIONS[0]);
        }
        if (ContextCompat.checkSelfPermission(chat.this, PERMISSIONS[1]) != PackageManager.PERMISSION_GRANTED) {
            list.add(PERMISSIONS[1]);
        }
        if (ContextCompat.checkSelfPermission(chat.this, PERMISSIONS[2]) != PackageManager.PERMISSION_GRANTED) {
            list.add(PERMISSIONS[2]);
        }
        if (ContextCompat.checkSelfPermission(chat.this, PERMISSIONS[3]) != PackageManager.PERMISSION_GRANTED) {
            list.add(PERMISSIONS[3]);
        }
        String[] strings = new String[list.size()];
        list.toArray(strings);
        Log.e("list",""+strings.length);
        ActivityCompat.requestPermissions(chat.this,
                strings, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
    }
}

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
//    }

