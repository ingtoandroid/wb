package com.example.a.app10.Activity;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.a.app10.R;
import com.example.a.app10.tool.VideoControllerView;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenVideo extends AppCompatActivity {
    private SurfaceView video;
    private MediaPlayer player;
    private VideoControllerView controller;
    private String id;
    private float ox,oy;
    private float nx,ny;
    private AudioManager audioManager;
    private int  max;
    private final float f=40f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_video);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE);
        id=getIntent().getStringExtra("id");
        audioManager = (AudioManager) this.getSystemService(AUDIO_SERVICE);
        max=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        player=VideoDetail.player;
        controller=new VideoControllerView(this,1);
        controller.setId(id);
        controller.setMediaPlayer(new VideoControllerView.MediaPlayerControl() {
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
                return true;
            }

            @Override
            public void toggleFullScreen() {
                finish();
            }
        });
        controller.setAnchorView((FrameLayout)findViewById(R.id.full_container));
        video=(SurfaceView)findViewById(R.id.video);
        video.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                player.setDisplay(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
//        video.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText(FullscreenVideo.this, "aaa", Toast.LENGTH_SHORT).show();
//                controller.show();
//            }
//        });
//        video.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        ox=event.getX();
//                        oy=event.getY();
//                        Toast.makeText(FullscreenVideo.this, "down", Toast.LENGTH_SHORT).show();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        nx=event.getX();
//                        ny=event.getY();
//                        Toast.makeText(FullscreenVideo.this, "move", Toast.LENGTH_SHORT).show();
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        Toast.makeText(FullscreenVideo.this, "up ", Toast.LENGTH_SHORT).show();
//                        break;
//                    default:
//                        break;
//                }
//                float dx=Math.abs(nx-ox);
//                float dy=Math.abs(ny-oy);
//                if(dy>dx){
//                    if(ny>oy){
//                /*降低音量*/
//                        subVoice();
//
//                    }
//                    else{
//                /*提高音量*/
//                        addvoice();
//                    }
//                }
//                return false;
//            }
//        });

    }

    private void addvoice(){
        int duration=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int  n=++duration;
        if(n>max)
            n=max;
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,n,AudioManager.FLAG_SHOW_UI);
    }
    private void subVoice(){
        int duration=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int n=--duration;
        if(n<0)
            n=0;
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,n,AudioManager.FLAG_SHOW_UI);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                ox=event.getX();
                oy=event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                nx=event.getX();
                ny=event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if(Math.abs(event.getY()-oy)<f)
                    controller.show();
                break;
            default:
                break;
        }
        float dx=Math.abs(nx-ox);
        float dy=Math.abs(ny-oy);
        if(dy>f){
            if(dy>dx){
                if(ny>oy){
                /*降低音量*/
                    subVoice();

                }
                else{
                /*提高音量*/
                    addvoice();
                }
            }
        }

        return false;
    }
    }

