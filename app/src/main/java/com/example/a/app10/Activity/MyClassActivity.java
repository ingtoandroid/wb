package com.example.a.app10.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.a.app10.Adapter.MyClassAdapter;
import com.example.a.app10.R;
import com.example.a.app10.bean.MyClassItem;
import com.example.a.app10.tool.KopItemDecoration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyClassActivity extends ToolBarBaseActivity {

    private RecyclerView rv;
    private List<MyClassItem> list;

    @Override
    protected int getSideMenu() {
        return R.layout.activity_science_side;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideDrawer();
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
            showRecycler();
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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Bitmap bitmap1= BitmapFactory.decodeResource(getResources(),R.drawable.dance);
        Bitmap bitmap2= BitmapFactory.decodeResource(getResources(),R.drawable.run_pic);
        Bitmap bitmap3= BitmapFactory.decodeResource(getResources(),R.drawable.swim_pic);
        list.add(new MyClassItem(bitmap1,false,0,0,"全民健身计划2016"));
        list.add(new MyClassItem(bitmap2,true,0,0,"全民健身计划2019"));
        list.add(new MyClassItem(bitmap3,false,0,0,"全民健身计划2018"));
        list.add(new MyClassItem(bitmap1,true,0,0,"全民健身计划2018"));
        list.add(new MyClassItem(bitmap2,false,0,0,"全民健身计划2016"));
        list.add(new MyClassItem(bitmap3,false,0,0,"全民健身计划2015"));
        list.add(new MyClassItem(bitmap1,false,0,0,"全民健身计划2017"));
        list.add(new MyClassItem(bitmap2,false,0,0,"全民健身计划2012"));
        list.add(new MyClassItem(bitmap3,false,0,0,"全民健身计划2012"));
    }

}
