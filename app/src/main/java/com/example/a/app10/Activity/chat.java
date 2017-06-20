package com.example.a.app10.Activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.a.app10.R;
import com.example.a.app10.tool.MyApplication;
import com.example.a.app10.tool.Net;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EasyUtils;
import com.hyphenate.util.HanziToPinyin;

import java.security.Permission;

public class chat extends AppCompatActivity {

    public static chat activityInstance;
    private EaseChatFragment chatFragment;
    private String toChatUsername;
    private String headurl;
    private String[] paths={"",""};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activityInstance = this;
        if(getIntent().hasExtra("name"))
        toChatUsername = getIntent().getExtras().getString("name");
        //得到对方账号
        if(getIntent().hasExtra("filePath"))
            headurl=getIntent().getStringExtra("filePath");
        paths[0]=Net.getPhotoUrl();
        paths[1]=headurl;

        ((MyApplication) getApplication()).init();
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

        chatFragment = new EaseChatFragment();
        Bundle b=new Bundle();
        b.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        b.putString(EaseConstant.EXTRA_USER_ID,toChatUsername);
        b.putStringArray("paths",paths);
        chatFragment.setArguments(b);
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
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
    private void init(){
        /*登陆*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().login(Net.getInstance().getUsername(), Net.getHx_pwd(), new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(chat.this,"success",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(int code, String error) {
                        Toast.makeText(chat.this,""+error,Toast.LENGTH_LONG).show();
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

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
//    }

