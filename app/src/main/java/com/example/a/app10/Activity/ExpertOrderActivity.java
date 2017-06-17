package com.example.a.app10.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.a.app10.R;
import com.example.a.app10.bean.ProfessorItem;
import com.example.a.app10.tool.MyInternet;
import com.example.a.app10.view.MyOrderList;
import com.squareup.okhttp.OkHttpClient;

import java.net.URL;

public class ExpertOrderActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ProgressDialog mProgressDialog;
    private boolean finish;
    private OkHttpClient client;
    private ProfessorItem professor;
    private View head;
    private MyOrderList form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_order);

        Window window=getWindow();//设置透明状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.back);//设置标题栏

        professor= (ProfessorItem) getIntent().getExtras().get("professor");
        init();
        //new LoadTask().execute(null,null,null);
    }

    private void init() {
        client=new OkHttpClient();
        head=findViewById(R.id.head);
        head.setBackgroundColor(Color.TRANSPARENT);
        form= (MyOrderList) findViewById(R.id.form);
        form.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()!=MotionEvent.ACTION_UP){//只要拿起事件
                    return true;
                }
                float x=motionEvent.getX();
                float y=motionEvent.getY();
                float singleWidth=form.getWidth()/7;
                float singleHeight=form.getHeight()/3;
                int index=(int)(y/singleHeight)*7+(int) (x/singleWidth);
                Log.v("tag","index:"+index);
                if (form.getState(index)==MyOrderList.FREE){
                    form.setState(index,MyOrderList.CHOSEN);
                    form.invalidate();//强制刷新
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
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
        finish=false;
        String url= MyInternet.MAIN_URL+"expert/get_expert_order_service?expertId="+professor.getExpertId();
        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {

            }

            @Override
            public void mainThread() {

            }
        },this);
        while (!finish){

        }
    }

    private void show() {
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
