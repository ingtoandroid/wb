package com.example.a.app10.tool;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;

/**
 * Created by 12917 on 2017/6/16.
 */

public class MyApplication extends Application {
    public MyApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    public void init(){
        EMOptions options = new EMOptions();
        options.setAutoLogin(false);
        options.setAcceptInvitationAlways(true);
        EMClient.getInstance().init(getApplicationContext(), options);
        EMClient.getInstance().setDebugMode(true);
        EaseUI.getInstance().init(getApplicationContext(),options);

    }
}
