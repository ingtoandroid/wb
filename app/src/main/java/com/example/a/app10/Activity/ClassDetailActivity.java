package com.example.a.app10.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a.app10.R;
import com.example.a.app10.tool.MyInternet;
import com.example.a.app10.tool.Net;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;


public class ClassDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvClassContent, tvFee, tvRisk,tvOpen1,
            tvOpen2,tvOpen3,tvTitle,tvStartDate,tvEnterNum,tvClassroom;
    private final int CURRENT_LINES= 3;
    private boolean[] isOpen={false,false,false};//标记三个文本是否展开
    private Button btnBack,btnJoin;
    private ProgressDialog mProgressDialog;
    private String courseId,userid;
    private String courseTitle,startDate,entereNum,classroom,courseContent,feeExplain,risk;
    private boolean isEntere;
    private OkHttpClient client;
    private final int ORDER=10;
    private final int CANCEL=20;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==ORDER){
                switch (msg.arg1){
                    case 0:
                        Toast.makeText(ClassDetailActivity.this,"报名时间已结束",Toast.LENGTH_SHORT).show();
                        btnJoin.setText("无法报名");
                        btnJoin.setClickable(false);
                        btnJoin.setBackgroundResource(R.drawable.not_click_button);
                        break;
                    case 1:
                        Toast.makeText(ClassDetailActivity.this,"报名成功",Toast.LENGTH_SHORT).show();
                        btnJoin.setText("取消报名");
                        btnJoin.setBackgroundResource(R.drawable.redbutton);
                        isEntere=true;
                        break;
                    case 3:
                        break;
                }
            }
            if (msg.what==CANCEL){
                switch (msg.arg2){
                    case 0:
                        Toast.makeText(ClassDetailActivity.this,"报名时间已结束",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(ClassDetailActivity.this,"取消报名成功",Toast.LENGTH_SHORT).show();
                        btnJoin.setText("我要报名");
                        btnJoin.setBackgroundResource(R.drawable.button_side_press);
                        break;
                    case 3:
                        Toast.makeText(ClassDetailActivity.this,"您未报名此课程",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail);

        Window window=getWindow();//设置透明状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        courseId =getIntent().getStringExtra("courseId");
        userid=Net.getPersonID();
        Log.v("tagUserId","%%%%%%%%%%%%%%%%%%%%%%%%%"+userid);

        init();
    }

    private void init() {
        tvClassContent = (TextView) findViewById(R.id.tvCourseContent);
        tvClassContent.setHeight(tvClassContent.getLineHeight()*CURRENT_LINES);
        tvFee = (TextView) findViewById(R.id.tvFee);
        tvFee.setHeight(tvFee.getLineHeight()*CURRENT_LINES);
        tvRisk = (TextView) findViewById(R.id.tvRisk);
        tvRisk.setHeight(tvRisk.getLineHeight()*CURRENT_LINES);
        tvOpen1= (TextView) findViewById(R.id.tvOpen1);
        tvOpen2= (TextView) findViewById(R.id.tvOpen2);
        tvOpen3= (TextView) findViewById(R.id.tvOpen3);
        tvTitle= (TextView) findViewById(R.id.tvTitle);
        tvStartDate= (TextView) findViewById(R.id.tvStartDate);
        tvEnterNum= (TextView) findViewById(R.id.tvEnterNum);
        tvClassroom= (TextView) findViewById(R.id.tvClassroom);
        btnBack= (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnJoin= (Button) findViewById(R.id.btnJoin);
        btnJoin.setOnClickListener(this);
        client=new OkHttpClient();

        tvClassContent.setMaxLines(CURRENT_LINES);
        tvFee.setMaxLines(CURRENT_LINES);
        tvRisk.setMaxLines(CURRENT_LINES);

        tvOpen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen[0]==false){
                    if (tvClassContent.getLineCount()>CURRENT_LINES)
                        tvClassContent.setHeight(tvClassContent.getLineHeight()* tvClassContent.getLineCount());
                    isOpen[0]=true;
                    tvOpen1.setText("点击收回");
                } else {
                    if (tvClassContent.getLineCount()>CURRENT_LINES)
                        tvClassContent.setHeight(tvClassContent.getLineHeight()*CURRENT_LINES);
                    isOpen[0]=false;
                    tvOpen1.setText("展开全部");
                }}
        });
        tvOpen2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen[1]==false){
                    if (tvClassContent.getLineCount()>CURRENT_LINES)
                        tvFee.setHeight(tvFee.getLineHeight()* tvFee.getLineCount());
                    isOpen[1]=true;
                    tvOpen2.setText("点击收回");
                } else {
                    tvFee.setHeight(tvFee.getLineHeight()*CURRENT_LINES);
                    isOpen[1]=false;
                    tvOpen2.setText("展开全部");
                }}
        });
        tvOpen3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen[2]==false){
                    tvRisk.setHeight(tvRisk.getLineHeight()* tvRisk.getLineCount());
                    isOpen[2]=true;
                    tvOpen3.setText("点击收回");
                } else {
                    tvRisk.setHeight(tvRisk.getLineHeight()*CURRENT_LINES);
                    isOpen[2]=false;
                    tvOpen3.setText("展开全部");
                }}
        });

        new LoadTask().execute(null,null,null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.btnJoin:
                if(Net.getPersonID().equals("")){
                    Intent intent = new Intent(ClassDetailActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                if (!isEntere){
                    join();
                } else{
                    cancelEntere();
                }
                break;
        }
    }

    private void cancelEntere() {
        String url= MyInternet.MAIN_URL+"course/cancelCourseEntere?courseId="
                +courseId+"&userid="+userid;
        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                try {
                    Log.v("tagRe",s);
                    JSONObject object=new JSONObject(s);
                    int result=object.getInt("code");
                    Message message=new Message();
                    message.arg2=result;
                    message.what=CANCEL;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void mainThread() {

            }
        },this);
    }

    private void join() {//课程报名
        new AlertDialog.Builder(this).setMessage("是否确定报名课程："+courseTitle)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String url= MyInternet.MAIN_URL+"course/courseEntere?courseId="
                                +courseId+"&userid="+userid;
                        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
                            @Override
                            public void handle(String s) {
                                try {
                                    JSONObject object=new JSONObject(s);
                                    int result=object.getInt("code");
                                    Message message=new Message();
                                    message.arg1=result;
                                    message.what=ORDER;
                                    handler.sendMessage(message);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void mainThread() {

                            }
                        },ClassDetailActivity.this);
                    }
                }).setNegativeButton("取消", null).create().show();
    }

    private class LoadTask extends AsyncTask<URL,Integer,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress("加载中");
        }

        @Override
        protected Void doInBackground(URL... urls) {
            getData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private void getData() {
        String url= MyInternet.MAIN_URL+"course/courseRelease_detail?courseId="
                +courseId+"&userid="+userid;
        Log.v("tagUser",url);
        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                try {
                    Log.v("tagAll",s);
                    JSONObject object=new JSONObject(s);
                    courseTitle=object.getString("courseTitle");
                    startDate=object.getString("startDate");
                    entereNum=object.getString("entereNum");
                    classroom=object.getString("classroom");
                    courseContent=object.getString("courseContent");
                    feeExplain=object.getString("feeExplain");
                    risk=object.getString("risk");
                    int i=object.getInt("isEntere");
                    if (i==1){
                        isEntere=true;
                    } else {
                        isEntere=false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void mainThread() {
                show();
            }
        },this);

    }

    private void show() {
        hideProgress();
        tvTitle.setText(courseTitle);
        tvStartDate.setText("开课时间"+"\n"+startDate);
        tvEnterNum.setText("报名人数"+"\n"+entereNum);
        tvClassroom.setText("场地"+"\n"+classroom);
        tvClassContent.setText(courseContent);
        tvFee.setText(feeExplain);
        tvRisk.setText(risk);
//        tvClassContent.setHeight(CURRENT_LINES*tvClassContent.getLineHeight());
//        tvRisk.setHeight(CURRENT_LINES*tvRisk.getLineHeight());
//        tvFee.setHeight(CURRENT_LINES*tvFee.getLineHeight());

        if (isEntere){//判断是否已报名
            btnJoin.setText("取消报名");
            btnJoin.setBackgroundResource(R.drawable.redbutton);
        } else {

        }
    }

    //显示进度对话框
    protected void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(true);
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }
    protected void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
