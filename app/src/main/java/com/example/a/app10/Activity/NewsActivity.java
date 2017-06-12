package com.example.a.app10.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.example.a.app10.Adapter.ScienceAdapter;
import com.example.a.app10.R;
import com.example.a.app10.bean.ScienceItem;
import com.example.a.app10.tool.KopItemDecoration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends ToolBarBaseActivity{

    private RecyclerView rv;
    private List<ScienceItem> list;
    private static int numbersButton=18;
    private int[] sideButtonsIds={R.id.btn11,R.id.btn12,R.id.btn13,R.id.btn14,R.id.btn15,R.id.btn16,
            R.id.btn21,R.id.btn22,R.id.btn23,R.id.btn24,R.id.btn25,R.id.btn26,
            R.id.btn31,R.id.btn32,R.id.btn33,R.id.btn34,R.id.btn35,R.id.btn36};


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
        setRightButton(R.drawable.message, "消息", new MyOnClickListener() {
            @Override
            public void onClick() {

            }
        });

        rv= (RecyclerView) findViewById(R.id.rv);
        hideDrawer();

        new LoadTask().execute(null,null,null);

    }

    private void getData() {
        list=new ArrayList<>();
        Bitmap bitmap1= BitmapFactory.decodeResource(getResources(),R.drawable.dance);
        Bitmap bitmap2= BitmapFactory.decodeResource(getResources(),R.drawable.run_pic);
        Bitmap bitmap3= BitmapFactory.decodeResource(getResources(),R.drawable.swim_pic);
        list.add(new ScienceItem(bitmap1,"全民健身计划2016","咨询部","刚刚"));
        list.add(new ScienceItem(bitmap2,"全民健身计划2011","咨询部","一天前"));
        list.add(new ScienceItem(bitmap3,"全民健身计划2012","咨询部","两天前"));
        list.add(new ScienceItem(bitmap1,"全民健身计划2013","咨询部","一周前"));
        list.add(new ScienceItem(bitmap2,"全民健身计划2014","咨询部","刚刚"));
        list.add(new ScienceItem(bitmap3,"全民健身计划2015","咨询部","一天前"));
        list.add(new ScienceItem(bitmap1,"全民健身计划2017","咨询部","刚刚"));
        list.add(new ScienceItem(bitmap2,"全民健身计划2018","咨询部","两天前"));

        //测试用手动延迟
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
            showRecycler();
        }
    }

    //配置并显示列表
    public void showRecycler(){
        hideProgress();
        ScienceAdapter adapter=new ScienceAdapter(list,this);
        adapter.setLisenter(new ScienceAdapter.OnItenClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(NewsActivity.this,NewsDetailActivity.class));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        rv.addItemDecoration(new KopItemDecoration(this,KopItemDecoration.VERTICAL_LIST));
        rv.setVisibility(View.VISIBLE);
    }
}
