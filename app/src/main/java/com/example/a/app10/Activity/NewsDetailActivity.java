package com.example.a.app10.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a.app10.R;
import com.example.a.app10.tool.MyInternet;
import com.example.a.app10.tool.StatusBarTool;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class NewsDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private LinearLayout llContent;
    private ProgressDialog mProgressDialog;
    private String newsId,title,subTitle,content,authorName,publishTime,totalAccess;
    private boolean finish;
    private OkHttpClient client;
    private TextView tvTitle,tvContent,tvAuthor,tvTotal,tvSubTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//原默认标题不显示
        toolbar.setNavigationIcon(R.drawable.back);

        init();


        //尝试使用透明状态栏
        if (StatusBarTool.tryLightStatus(this)){
             toolbar.setBackgroundColor(Color.WHITE);
        }

        newsId=getIntent().getStringExtra("newsId");

        new LoadTask().execute(null,null,null);
    }

    private void init() {
        llContent = (LinearLayout) findViewById(R.id.content);
        client=new OkHttpClient();
        tvTitle= (TextView) findViewById(R.id.tvTitle);
        tvContent= (TextView) findViewById(R.id.tvContent);
        tvAuthor= (TextView) findViewById(R.id.tvAuthor);
        tvTotal= (TextView) findViewById(R.id.tvTotal);
        tvSubTitle= (TextView) findViewById(R.id.tvSubTitle);
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
        llContent.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
        tvSubTitle.setText(subTitle);
        tvAuthor.setText("发布者： "+authorName+"   "+publishTime);
        tvContent.setText(content);
        tvTotal.setText("浏览"+totalAccess+"次");
    }

    private void getData() {
        finish=false;
        String url= MyInternet.MAIN_URL+"news/get_news_detail?newsId="+newsId;
        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                try {
                    JSONObject object=new JSONObject(s);
                    title=object.getString("title");
                    authorName=object.getString("authorName");
                    subTitle=object.getString("subTitle");
                    content=object.getString("content");
                    publishTime=object.getString("publishTime");
                    totalAccess=object.getString("totalAccess");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish=true;
            }

            @Override
            public void mainThread() {

            }
        },this);
        while (!finish){

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
