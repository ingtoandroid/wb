package com.example.a.app10.Activity;

import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.a.app10.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.widget.EaseChatInputMenu;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.exceptions.HyphenateException;

import java.util.Iterator;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private EaseTitleBar easeTitleBar;
    private EaseChatMessageList easeChatMessageList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EaseChatInputMenu easeChatInputMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        init();
        initViews();
    }
    private void init(){
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);
        int pid = android.os.Process.myPid();
        final String processAppName = getAppName(pid);
        if (processAppName == null ||!processAppName.equalsIgnoreCase(this.getPackageName())) {
            return;
        }
        EMClient.getInstance().init(this, options);
        EMClient.getInstance().setDebugMode(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
//                try {
//                    EMClient.getInstance().createAccount("yuanshuai1", "2236674122");
//                }
//                catch (HyphenateException e){
//                    e.printStackTrace();
//                }
                EMClient.getInstance().login("yuanshuai", "yuanshuai", new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(ChatActivity.this, "success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(int code, String error) {
                        Toast.makeText(ChatActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        Toast.makeText(ChatActivity.this, ""+progress+status, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
    private void initViews(){
        easeTitleBar = (EaseTitleBar) findViewById(R.id.titlebar);
        easeTitleBar.setTitle("张建国");
        //easeTitleBar.setRightImageResource(R.drawable.ease_mm_title_remove);
        easeChatMessageList = (EaseChatMessageList) findViewById(R.id.lists);
        easeChatInputMenu=(EaseChatInputMenu)findViewById(R.id.input);
        easeChatInputMenu.init();
        easeChatInputMenu.setChatInputMenuListener(new EaseChatInputMenu.ChatInputMenuListener() {
            @Override
            public void onSendMessage(String content) {
                onSendMessage(content);
            }

            @Override
            public void onBigExpressionClicked(EaseEmojicon emojicon) {

            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                return false;
            }
        });
//初始化messagelist
        easeChatMessageList.init("yuanshuai1", 0, null);
//设置item里的控件的点击事件
        easeChatMessageList.setItemClickListener(new EaseChatMessageList.MessageListItemClickListener() {
            @Override
            public void onResendClick(EMMessage message) {

            }

            @Override
            public boolean onBubbleClick(EMMessage message) {
                return false;
            }

            @Override
            public void onBubbleLongClick(EMMessage message) {

            }

            @Override
            public void onUserAvatarClick(String username) {

            }

            @Override
            public void onUserAvatarLongClick(String username) {

            }
        });
//获取下拉刷新控件
        swipeRefreshLayout = easeChatMessageList.getSwipeRefreshLayout();
//刷新消息列表
        easeChatMessageList.refresh();
        easeChatMessageList.refreshSeekTo(0);
        easeChatMessageList.refreshSelectLast();

    }
}
