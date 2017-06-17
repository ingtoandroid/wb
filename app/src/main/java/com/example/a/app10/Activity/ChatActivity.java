package com.example.a.app10.Activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.a.app10.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.chat.adapter.message.EMAVoiceMessageBody;
import com.hyphenate.easeui.adapter.EaseMessageAdapter;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.ui.EaseBaiduMapActivity;
import com.hyphenate.easeui.ui.EaseShowVideoActivity;
import com.hyphenate.easeui.widget.EaseChatExtendMenu;
import com.hyphenate.easeui.widget.EaseChatInputMenu;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.easeui.widget.EaseVoiceRecorderView;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseChatRowVoice;
import com.hyphenate.easeui.widget.chatrow.EaseChatRowVoicePlayClickListener;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.exceptions.HyphenateException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.hyphenate.easeui.EaseConstant.MESSAGE_ATTR_IS_VIDEO_CALL;
import static com.hyphenate.easeui.EaseConstant.MESSAGE_ATTR_IS_VOICE_CALL;

public class ChatActivity extends AppCompatActivity {
    private EaseTitleBar easeTitleBar;
    private EaseChatMessageList easeChatMessageList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EaseChatInputMenu easeChatInputMenu;
    private List<EMMessage> list=new ArrayList<>();
    private EaseVoiceRecorderView e;
    private final int takePicrequestcode=0;
    private final int picrequestcode=1;
    private final int maprequestcode=2;
    private File file=null;
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
        options.setAutoLogin(false);
        options.setAcceptInvitationAlways(true);
        EMClient.getInstance().init(this, options);
        EMClient.getInstance().setDebugMode(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().login("yuanshuai1", "yuanshuai", new EMCallBack() {
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
        }).start();
        Toast.makeText(this, ""+EMClient.getInstance().getCurrentUser(), Toast.LENGTH_SHORT).show();

    }
    private void initViews(){
        easeTitleBar = (EaseTitleBar) findViewById(R.id.titlebar);
        easeTitleBar.setTitle("张建国");
        //easeTitleBar.setRightImageResource(R.drawable.ease_mm_title_remove);
        easeChatMessageList = (EaseChatMessageList) findViewById(R.id.lists);
        easeChatInputMenu=(EaseChatInputMenu)findViewById(R.id.input);
        e=(EaseVoiceRecorderView)findViewById(R.id.voice);
//        easeChatInputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.em_chat_video_selector, ITEM_VIDEO, extendMenuItemClickListener);
//
//        easeChatInputMenu.registerExtendMenuItem(R.string.attach_file, R.drawable.em_chat_file_selector, ITEM_FILE, extendMenuItemClickListener);
//
//        easeChatInputMenu.registerExtendMenuItem(R.string.attach_voice_call, R.drawable.em_chat_voice_call_selector, ITEM_VOICE_CALL, extendMenuItemClickListener);
//
//        easeChatInputMenu.registerExtendMenuItem(R.string.attach_video_call, R.drawable.em_chat_video_call_selector, ITEM_VIDEO_CALL, extendMenuItemClickListener);
//        easeChatInputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.video, ITEM_VIDEO, extendMenuItemClickListener);

        easeChatInputMenu.registerExtendMenuItem(R.string.attach_take_pic,R.drawable.ease_chat_takepic_selector,0,new EaseChatExtendMenu.EaseChatExtendMenuItemClickListener(){
            @Override
            public void onClick(int itemId, View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String path = Environment.getExternalStorageDirectory().toString()+"/pic";
                File path1=new File(path);
                if(!path1.exists()){
                    path1.mkdirs();
                }
                file=new File(path1,System.currentTimeMillis()+".jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(file));
                startActivityForResult(intent,takePicrequestcode);

            }
        });
        easeChatInputMenu.registerExtendMenuItem(R.string.attach_picture, R.drawable.ease_chat_image_selector, 1, new EaseChatExtendMenu.EaseChatExtendMenuItemClickListener() {
            @Override
            public void onClick(int itemId, View view) {
                Intent i=new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i,picrequestcode);

            }
        });
        easeChatInputMenu.registerExtendMenuItem(R.string.attach_location, R.drawable.ease_chat_location_selector, 2, new EaseChatExtendMenu.EaseChatExtendMenuItemClickListener() {
            @Override
            public void onClick(int itemId, View view) {
                Intent i=new Intent(ChatActivity.this, EaseBaiduMapActivity.class);
                startActivityForResult(i,maprequestcode);
            }
        });
        easeChatInputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.video_bg, 3, new EaseChatExtendMenu.EaseChatExtendMenuItemClickListener() {
            @Override
            public void onClick(int itemId, View view) {
                Intent i=new Intent(ChatActivity.this, EaseShowVideoActivity.class);
            }
        });
        easeChatInputMenu.registerExtendMenuItem(R.string.attach_file, R.drawable.ease_chat_item_file, 4, new EaseChatExtendMenu.EaseChatExtendMenuItemClickListener() {
            @Override
            public void onClick(int itemId, View view) {

            }
        });
        easeChatInputMenu.registerExtendMenuItem(R.string.attach_voice_call, R.drawable.ease_chat_voice_call_self, 5, new EaseChatExtendMenu.EaseChatExtendMenuItemClickListener() {
            @Override
            public void onClick(int itemId, View view) {

            }
        });
        easeChatInputMenu.registerExtendMenuItem(R.string.attach_video_call, R.drawable.ease_chat_video_call_self, 6, new EaseChatExtendMenu.EaseChatExtendMenuItemClickListener() {
            @Override
            public void onClick(int itemId, View view) {

            }
        });
        easeChatInputMenu.init();

        easeChatInputMenu.setChatInputMenuListener(new EaseChatInputMenu.ChatInputMenuListener() {
            @Override
            public void onSendMessage(String content) {
                EMMessage message = EMMessage.createTxtSendMessage(content, "yuanshuai");
                list.add(message);
                EMClient.getInstance().chatManager().sendMessage(message);
                easeChatMessageList.refreshSeekTo(0);

            }

            @Override
            public void onBigExpressionClicked(EaseEmojicon emojicon) {

            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                e.onPressToSpeakBtnTouch(v, event, new EaseVoiceRecorderView.EaseVoiceRecorderCallback() {
                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        EMMessage mes=EMMessage.createVoiceSendMessage(voiceFilePath,voiceTimeLength,"yuanshuai");
                        list.add(mes);
                        EMClient.getInstance().chatManager().sendMessage(mes);
                        easeChatMessageList.refreshSeekTo(0);
                    }
                });
                Toast.makeText(ChatActivity.this, ""+event.getAction(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
//初始化messagelist
        easeChatMessageList.init("yuanshuai", 2, new EaseCustomChatRowProvider() {
            public int getCustomChatRowTypeCount() {
                //音、视频通话发送、接收共4种
                return 4;
            }

            @Override
            public int getCustomChatRowType(EMMessage message) {
                if(message.getType() == EMMessage.Type.TXT){
                    //语音通话类型
                    if (message.getBooleanAttribute(MESSAGE_ATTR_IS_VOICE_CALL, false)){
                        return message.direct() == EMMessage.Direct.RECEIVE ? 7: 6;
                    }else if (message.getBooleanAttribute(MESSAGE_ATTR_IS_VIDEO_CALL, false)){
                        //视频通话
                        return message.direct() == EMMessage.Direct.RECEIVE ? 9: 8;
                    }
                }
                return 0;
            }

            @Override
            public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
                if(message.getType() == EMMessage.Type.TXT){
                    // 语音通话、视频通话
                    if (message.getBooleanAttribute(MESSAGE_ATTR_IS_VOICE_CALL, false) ||
                            message.getBooleanAttribute(MESSAGE_ATTR_IS_VIDEO_CALL, false)){
                        //ChatRowVoiceCall为一个继承自EaseChatRow的类
                        return new EaseChatRowVoice(ChatActivity.this, message, position, adapter);
                    }
                }
                return null;
            }
        });
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                easeChatMessageList.refreshSelectLast();
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                easeChatMessageList.refreshSelectLast();
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                easeChatMessageList.refreshSelectLast();
            }

            @Override
            public void onMessageDelivered(List<EMMessage> messages) {
                easeChatMessageList.refreshSelectLast();
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                easeChatMessageList.refreshSelectLast();
            }
        });

//设置item里的控件的点击事件
        easeChatMessageList.setItemClickListener(new EaseChatMessageList.MessageListItemClickListener() {
            @Override
            public void onResendClick(EMMessage message) {

            }

            @Override
            public boolean onBubbleClick(EMMessage message) {
                if(message.getType()== EMMessage.Type.VOICE){
                    EaseChatRowVoice e=new EaseChatRowVoice(ChatActivity.this,message,0,null);
                    e.onBubbleClick();


                }

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
        //easeChatMessageList.refreshSeekTo(0);
//        easeChatMessageList.refreshSelectLast();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().importMessages(list);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==takePicrequestcode){
                EMMessage ee=EMMessage.createImageSendMessage(file.getPath(),false,"yuanshuai");
                EMClient.getInstance().chatManager().sendMessage(ee);

        }
        else if(requestCode==picrequestcode){
            Uri u=data.getData();
            if(u!=null) {
                EMMessage ee = EMMessage.createImageSendMessage(uriToPath(u), false, "yuanshuai");
                EMClient.getInstance().chatManager().sendMessage(ee);
                easeChatMessageList.refreshSelectLast();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public String uriToPath(Uri uri){
        //通过uri找到图片地址并得到图片
        Cursor cursor=this.getContentResolver().query(uri,null,null,null,null);
        cursor.moveToFirst();
        int index=cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        String path=cursor.getString(index);
        cursor.close();
        return path;
    }
}
