package com.example.a.app10.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
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
    private OkHttpClient client;
    private TextView tvTitle,tvAuthor,tvTotal,tvSubTitle;
    private WebView webView;

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.WHITE);
            }
        }

        newsId=getIntent().getStringExtra("newsId");

        new LoadTask().execute(null,null,null);
    }


    private void init() {
        llContent = (LinearLayout) findViewById(R.id.content);
        client=new OkHttpClient();
        tvTitle= (TextView) findViewById(R.id.tvTitle);
        tvAuthor= (TextView) findViewById(R.id.tvAuthor);
        tvTotal= (TextView) findViewById(R.id.tvTotal);
        tvSubTitle= (TextView) findViewById(R.id.tvSubTitle);

        webView = (WebView) findViewById(R.id.wv);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
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
        tvTotal.setText("浏览"+totalAccess+"次");

        webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
    }

    private void getData() {
        String url= MyInternet.MAIN_URL+"news/get_news_detail?newsId="+newsId;
        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                try {
                    Log.v("tagSD",s);
                    JSONObject object=new JSONObject(s);
                    title=object.getString("titie");
                    authorName=object.getString("authorName");
                    subTitle=object.getString("subTitle");
                    content=object.getString("content");
                    publishTime=object.getString("publishTime");
                    totalAccess=object.getString("totalAccess");
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

    private void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(true);
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }
}
