package com.example.a.app10.Activity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.a.app10.Adapter.VideoRecycleAdapter;
import com.example.a.app10.R;
import com.example.a.app10.tool.Net;
import com.example.a.app10.tool.VideoControllerView;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

public class VideoDetail extends AppCompatActivity implements SurfaceHolder.Callback ,View.OnClickListener {

    private SurfaceView videoSurface;
    private TextView pinglun;
    private TextView tiwen;
    private Button comment;
    private MediaPlayer player;
    private VideoControllerView controller;
    private int screenWidth;
    private int screenHeight;
    private int width;
    private int height;
    private boolean isFull=false;
    private LinearLayout linearLayout;
    private FrameLayout container;
    private RecyclerView recyclerView;
    private VideoRecycleAdapter adapter;
    private ScrollView scrollView;
    private String  id;
    private String uri=null;
    private VideoControllerView.MediaPlayerControl mediaPlayerControl=new VideoControllerView.MediaPlayerControl() {
        @Override
        public boolean canPause() {
            return true;
        }

        @Override
        public boolean canSeekBackward() {
            return true;
        }

        @Override
        public boolean canSeekForward() {
            return true;
        }

        @Override
        public int getBufferPercentage() {
            return 0;
        }

        @Override
        public int getCurrentPosition() {
            return player.getCurrentPosition();
        }

        @Override
        public int getDuration() {
            return player.getDuration();
        }

        @Override
        public boolean isPlaying() {
            return player.isPlaying();
        }

        @Override
        public void pause() {
            player.pause();
        }

        @Override
        public void seekTo(int i) {
            player.seekTo(i);
        }

        @Override
        public void start() {
            player.start();
        }

        @Override
        public boolean isFullScreen() {
            return isFull;
        }

        @Override
        public void toggleFullScreen() {
            if(!isFull)
                full();
            else
                quitFull();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        id=getIntent().getStringExtra("id");
        initView();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initEvent();
    }
    private void initView(){
        pinglun=(TextView)findViewById(R.id.pinglun);
        tiwen=(TextView)findViewById(R.id.tiwen);
        comment=(Button)findViewById(R.id.comment);
        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        recyclerView=(RecyclerView)findViewById(R.id.video_pinglun);
        scrollView=(ScrollView)findViewById(R.id.scroll);
        adapter=new VideoRecycleAdapter(this);
        SurfaceHolder videoHolder = videoSurface.getHolder();
        container=(FrameLayout)findViewById(R.id.videoSurfaceContainer);
        linearLayout=(LinearLayout)findViewById(R.id.line);
        videoHolder.addCallback(this);
        player = new MediaPlayer();
        controller = new VideoControllerView(this);
        controller.setMediaPlayer(mediaPlayerControl);
        screenWidth=getWindowManager().getDefaultDisplay().getWidth();
        screenHeight=getWindowManager().getDefaultDisplay().getHeight();

    }
    private void initEvent(){
        Call call=Net.getInstance().shipinDetail(id);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String string=response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    uri=jsonObject.getString("filePath");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                player.setDataSource(VideoDetail.this, Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
                                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        controller.setMediaPlayer(mediaPlayerControl);
                                        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
                                        player.start();
                                    }
                                });


                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (SecurityException e) {
                                e.printStackTrace();
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
        videoSurface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.show();
            }
        });
        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(this,LinearLayout.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager1);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(holder);
        //player.prepareAsync();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    /*全屏*/
    public void full(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        scrollView.scrollTo(0,300);
        setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE);
        width=container.getWidth();
        height=container.getHeight();
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(screenHeight,screenWidth);
        container.setLayoutParams(layoutParams);

        scrollView.fullScroll(View.FOCUS_UP);

        isFull=true;
    }
    /*推出全屏*/
    public void quitFull(){
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attrs);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(width,height);
        container.setLayoutParams(layoutParams);
        
        isFull=false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pinglun:
                break;
            case R.id.tiwen:
                break;
            case R.id.comment:
                break;

        }
    }
    private void showView(int type){
        if(type==0){
            comment.setText("发表评论");
            showRecyclerView(type);
            pinglun.setTextColor(getResources().getColor(R.color.main));
            tiwen.setTextColor(Color.BLACK);

        }
        else{
            comment.setText("提问");
            showRecyclerView(type);
            pinglun.setTextColor(Color.BLACK);
            tiwen.setTextColor(getResources().getColor(R.color.main));
        }
    }
    private void showRecyclerView(int type){
        Call call=Net.getInstance().getComment(id);
    }
}
