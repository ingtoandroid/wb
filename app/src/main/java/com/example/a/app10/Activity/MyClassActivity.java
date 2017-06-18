package com.example.a.app10.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.a.app10.Adapter.MyClassAdapter;
import com.example.a.app10.R;
import com.example.a.app10.bean.MyClassItem;
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

public class MyClassActivity extends ToolBarBaseActivity {

    private RecyclerView rv;
    private List<MyClassItem> list;
    private String userId;
    private OkHttpClient client;

    @Override
    protected int getSideMenu() {
        return R.layout.activity_science_side;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideDrawer();
        userId= Net.getPersonID();
        setMyTitle("我的课程");
        setLeftButton(R.drawable.back, new MyOnClickListener() {
            @Override
            public void onClick() {
                onBackPressed();
            }
        });
        setRightButton(R.drawable.message, "消息", new MyOnClickListener() {
            @Override
            public void onClick() {

            }
        });

        rv= (RecyclerView) findViewById(R.id.rv);
        list=new ArrayList<>();
        client=new OkHttpClient();

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        new QBadgeView(this).bindTarget(toolbar).setBadgeNumber(Net.getMegsSize());

        new LoadTask().execute(null,null,null);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_my_class;
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

    private void showRecycler() {
        hideProgress();
        rv.setLayoutManager(new LinearLayoutManager(this));
        MyClassAdapter adapter=new MyClassAdapter(list,this);
        rv.setAdapter(adapter);
        rv.addItemDecoration(new KopItemDecoration(this,KopItemDecoration.VERTICAL_LIST));
        rv.setVisibility(View.VISIBLE);
    }

    private void getData() {
        String url= MyInternet.MAIN_URL+"kc/kc_new_type_list?infoId="+userId;
        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                try {
                    JSONObject all = new JSONObject(s);
                    JSONArray array=all.getJSONArray("dataList");
                    for (int i=0;i<array.length();i++){
                        JSONObject object=array.getJSONObject(i);
                        MyClassItem item=new MyClassItem(object.getString("modelName"),
                                object.getString("courseId"),object.getString("courseTitle"),
                                object.getString("startDate"),object.getString("entereId"),object.getString("state"));
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

}
