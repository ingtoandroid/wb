package com.example.a.app10.Activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.a.app10.R;
import com.example.a.app10.tool.MyInternet;
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
    private String url;
    private String courseTitle,startDate,entereNum,classroom,courseContent,feeExplain,risk;
    private boolean isEntere;
    private OkHttpClient client;
    private boolean isFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail);

        Window window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        url=getIntent().getStringExtra("courseId");

        init();
    }

    private void init() {
        tvClassContent = (TextView) findViewById(R.id.tvCourseContent);
        tvClassContent.setHeight(tvClassContent.getLineHeight()*CURRENT_LINES);
        tvFee = (TextView) findViewById(R.id.tvFee);
        tvFee.setHeight(tvFee.getLineHeight()*CURRENT_LINES);
        tvRisk = (TextView) findViewById(R.id.tvFee);
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

        tvOpen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen[0]==false){
                    tvClassContent.setHeight(tvClassContent.getLineHeight()* tvClassContent.getLineCount());
                    isOpen[0]=true;
                    tvOpen1.setText("点击收回");
                } else {
                    tvClassContent.setHeight(tvClassContent.getLineHeight()*CURRENT_LINES);
                    isOpen[0]=false;
                    tvOpen1.setText("展开全部");
                }}
        });
        tvOpen2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen[1]==false){
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
                break;
        }
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
            show();
        }
    }

    private void getData() {
        isFinish=false;
        String url= MyInternet.MAIN_URL+"course/courseRelease_detail?courseId="
                +""+"&userid=admin";
        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                try {
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
                    isFinish=true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        while (!isFinish){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
        if (isEntere){//判断是否已报名
            btnJoin.setText("已报名");
            btnJoin.setBackgroundResource(R.drawable.not_click_button);
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
