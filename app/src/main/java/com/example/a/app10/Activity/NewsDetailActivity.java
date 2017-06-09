package com.example.a.app10.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.a.app10.R;
import com.example.a.app10.tool.StatusBarTool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;

public class NewsDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private LinearLayout content;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//原默认标题不显示

        toolbar.setNavigationIcon(R.drawable.back);

        content= (LinearLayout) findViewById(R.id.content);

        StatusBarTool.tryLightStatus(this);

        new LoadTask().execute(null,null,null);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.item1:
                Toast.makeText(this,"已点击",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
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

    private void show() {
        if (mProgressDialog!=null){
            mProgressDialog.dismiss();
        }
        content.setVisibility(View.VISIBLE);
    }

    private void getData() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(true);
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }
}
