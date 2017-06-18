package com.example.a.app10.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;


import com.example.a.app10.Adapter.NewsAdapter;
import com.example.a.app10.R;
import com.example.a.app10.bean.MessageReminder;
import com.example.a.app10.bean.NewsItem;
import com.example.a.app10.tool.KopItemDecoration;
import com.example.a.app10.tool.MyInternet;
import com.example.a.app10.tool.Net;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.QBadgeView;

public class NewsActivity extends ToolBarBaseActivity{

    private RecyclerView rv;
    private List<NewsItem> list;

    private OkHttpClient client;


    @Override
    protected int getSideMenu() {
        return R.layout.activity_science_side;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setMyTitle("资讯");
        setLeftButton(R.drawable.back, new MyOnClickListener() {
            @Override
            public void onClick() {
                onBackPressed();
            }
        });
        setRightButton(R.drawable.message_reminder, "消息", new MyOnClickListener() {
            @Override
            public void onClick() {
                startActivity(new Intent(NewsActivity.this, MessageReminder.class));
            }
        });

        list=new ArrayList<>();

        rv= (RecyclerView) findViewById(R.id.rv);
        client=new OkHttpClient();
        hideDrawer();

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        new QBadgeView(this).bindTarget(toolbar).setBadgeNumber(Net.getMegsSize());

        new LoadTask().execute(null,null,null);

    }

    private void getData() {
        String url= MyInternet.MAIN_URL+"news/get_news_list";
        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                try {
                    JSONObject object2=new JSONObject(s);
                    JSONArray array=object2.getJSONArray("dataList");
                    for (int i=0;i<array.length();i++){
                        JSONObject object=array.getJSONObject(i);
                        NewsItem item=new NewsItem(object.getString("newsId"),
                                object.getString("title"),object.getString("publishTime"),
                                object.getString("authorName"),object.getString("filePath"));
                        list.add(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void mainThread() {
                showRecycler();
            }
        },this);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_science;
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

    //配置并显示列表
    public void showRecycler(){
        hideProgress();
        rv.setVisibility(View.VISIBLE);
        NewsAdapter adapter=new NewsAdapter(list,this);
        adapter.setLisenter(new NewsAdapter.OnItenClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(NewsActivity.this,NewsDetailActivity.class);
                intent.putExtra("newsId",list.get(position).getNewsId());
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        rv.addItemDecoration(new KopItemDecoration(this,KopItemDecoration.VERTICAL_LIST));
    }
}
