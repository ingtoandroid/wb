package com.example.a.app10.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a.app10.Adapter.VideoRecycleAdapter;
import com.example.a.app10.bean.CommentItem;
import com.example.a.app10.tool.Net;
import com.example.a.app10.tool.VideoControllerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.a.app10.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class VideoDetail extends AppCompatActivity implements SurfaceHolder.Callback  {

    private  SurfaceView videoSurface;
    private TextView pinglun;
    private TextView tiwen;
    private Button comment;
    public static MediaPlayer player;
    public  VideoControllerView controller;
    private int screenWidth;
    private int screenHeight;
    private int width;
    private int height;
    private boolean isFull=false;
    private LinearLayout linearLayout;
    private FrameLayout container;
    private RecyclerView recyclerView;
    private VideoRecycleAdapter adapter;
    private TextView count;
    private ScrollView scrollView;
    private String  id;
    private String uri=null;
    private EditText ed;
    private boolean isPrepare=false;
    private int per=0;
    private ProgressBar progressBar;
    private AlertDialog dialog;
    List<CommentItem> list;
    private int type=0;
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
            return per;
        }

        @Override
        public int getCurrentPosition() {
            return player.getCurrentPosition();
        }

        @Override
        public int getDuration() {
            if(!isPrepare)
                return 0;
            else
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
        if(getIntent().hasExtra("id"))
            id=getIntent().getStringExtra("id");
        initView();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        screenWidth=this.getWindowManager().getDefaultDisplay().getWidth();
        screenHeight=this.getWindowManager().getDefaultDisplay().getHeight();
        initEvent();
    }
    private void initView(){
        pinglun=(TextView)findViewById(R.id.pinglun);
        tiwen=(TextView)findViewById(R.id.tiwen);
        comment=(Button)findViewById(R.id.comment);
        ed=(EditText)findViewById(R.id.ed);
        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        recyclerView=(RecyclerView)findViewById(R.id.video_pinglun);
        scrollView=(ScrollView)findViewById(R.id.scroll);
        count=(TextView)findViewById(R.id.text_count);
        progressBar=(ProgressBar)findViewById(R.id.progress);
        SurfaceHolder videoHolder = videoSurface.getHolder();
        container=(FrameLayout)findViewById(R.id.videoSurfaceContainer);
        linearLayout=(LinearLayout)findViewById(R.id.line);
        videoHolder.addCallback(this);
        player = new MediaPlayer();
        controller = new VideoControllerView(this,2);
        controller.setMediaPlayer(mediaPlayerControl);
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                player.start();
                isPrepare=true;
                player.setLooping(true);
                progressBar.setVisibility(View.GONE);
                //player.pause();
            }
        });
        player.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                player.start();


            }
        });
        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e("error",""+what+""+extra);
                return false;
            }
        });
        player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                per=percent;
            }
        });
        screenWidth=getWindowManager().getDefaultDisplay().getWidth();
        screenHeight=getWindowManager().getDefaultDisplay().getHeight();
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Net.getPersonID().equals("")){
                    Intent intent = new Intent(VideoDetail.this,LoginActivity.class);
                    startActivity(intent);
                }else {
                    showPrograssBar();
                    commit();
                }
            }
        });
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                count.setText(ed.getText().length()+"/300");
            }
        });
        showView(0);
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
                    uri = jsonObject.getString("filePath");

                    Log.e("path",""+uri);
                            try {
                                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                player.setDataSource(uri);
                                player.prepare();
                                player.setLooping(true);


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


                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
        );
        videoSurface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.show();
            }
        });
        pinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showView(0);
                type=0;
            }
        });
        tiwen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showView(1);
                type=1;
            }
        });

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
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        scrollView.scrollTo(0,300);
//        setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE);
//        width=container.getWidth();
//        height=container.getHeight();
//        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(screenHeight,screenWidth);
//        container.setLayoutParams(layoutParams);
//
//        scrollView.fullScroll(View.FOCUS_UP);
//
//        isFull=true;
        controller.setmShowing(false);
        container.removeView(controller);
        Intent intent=new Intent(VideoDetail.this,FullscreenVideo.class);
        intent.putExtra("id",id);
        startActivity(intent);
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

    private void showView(int type){
        list=new ArrayList<>();
        if(type==0){
            comment.setText("发表评论");
            showRecyclerView(type);
            pinglun.setTextColor(getResources().getColor(R.color.textGreen));
            tiwen.setTextColor(getResources().getColor(R.color.stringDark));

        }
        else{
            comment.setText("提问");
            showRecyclerView(type);
            pinglun.setTextColor(getResources().getColor(R.color.stringDark));
            tiwen.setTextColor(getResources().getColor(R.color.textGreen));
        }
    }
    private void showRecyclerView(int type){
        if(type==0){
            Call call=Net.getInstance().getComment(id);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String string=response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(new JSONObject(string).getJSONArray("datalist").toString());
                        Gson gson=new Gson();
                        list=gson.fromJson(jsonArray.toString(),new TypeToken<List<CommentItem>>(){}.getType());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter=new VideoRecycleAdapter(VideoDetail.this,list);
                                LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(VideoDetail.this,LinearLayout.VERTICAL,false);
                                recyclerView.setLayoutManager(linearLayoutManager1);
                                recyclerView.setAdapter(adapter);
                                Log.e("aaa","s");
                            }
                        });
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
        }
        else
        {
            Call call=Net.getInstance().getTiwen(id);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String string=response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(new JSONObject(string).getJSONArray("datalist").toString());
                        Gson gson=new Gson();
                        list=gson.fromJson(jsonArray.toString(),new TypeToken<List<CommentItem>>(){}.getType());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter=new VideoRecycleAdapter(VideoDetail.this,list);
                                LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(VideoDetail.this,LinearLayout.VERTICAL,false);
                                recyclerView.setLayoutManager(linearLayoutManager1);
                                recyclerView.setAdapter(adapter);

                            }
                        });
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });}
    }
    private void commit(){
        if(type==0){
            Call call=Net.getInstance().commitPinglun(ed.getText().toString(),id);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {


                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String s=response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if(("1").equals(jsonObject.getString("code"))){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dismiss();
                                    Toast.makeText(VideoDetail.this, "评论成功", Toast.LENGTH_SHORT).show();
                                    ed.setText("");
                                    showRecyclerView(0);
                                }
                            });
                        }
                        else
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(VideoDetail.this, "评论失败", Toast.LENGTH_SHORT).show();
                                    ed.setText("");
                                }
                            });
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            });
        }
        else{
            Call call =Net.getInstance().commitTiwen(ed.getText().toString(),id);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String s=response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if(("1").equals(jsonObject.getString("code"))){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(VideoDetail.this, "提问成功", Toast.LENGTH_SHORT).show();
                                    ed.setText("");
                                    showRecyclerView(1);
                                }
                            });
                        }
                        else
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(VideoDetail.this, "提问失败", Toast.LENGTH_SHORT).show();
                                    ed.setText("");
                                }
                            });
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.hide();
        player.release();
    }
    private void showPrograssBar(){
        if(dialog==null){
            AlertDialog.Builder builder=new AlertDialog.Builder(VideoDetail.this);
            builder.setView(LayoutInflater.from(VideoDetail.this).inflate(R.layout.dialog3,null));

            dialog=builder.create();
        }
        dialog.show();
    }
    private void dismiss(){
        dialog.dismiss();
    }
}
