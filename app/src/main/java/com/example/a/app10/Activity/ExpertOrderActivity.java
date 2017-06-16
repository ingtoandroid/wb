package com.example.a.app10.Activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.a.app10.R;
import com.example.a.app10.bean.ProfessorItem;
import com.example.a.app10.tool.MyInternet;
import com.squareup.okhttp.OkHttpClient;

import java.net.URL;

public class ExpertOrderActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ProgressDialog mProgressDialog;
    private boolean finish;
    private OkHttpClient client;
    private ProfessorItem professor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_order);

        Window window=getWindow();//设置透明状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("专家预约");
        toolbar.setNavigationIcon(R.drawable.back);//设置标题栏

        professor= (ProfessorItem) getIntent().getExtras().get("professor");
        init();
        new LoadTask().execute(null,null,null);
    }

    private void init() {
        client=new OkHttpClient();
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
