package com.example.a.app10.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.example.a.app10.Activity.NewsDetailActivity;
import com.example.a.app10.Activity.ToolBarBaseActivity;
import com.example.a.app10.Adapter.ScienceAdapter;
import com.example.a.app10.R;
import com.example.a.app10.bean.ScienceItem;
import com.example.a.app10.tool.KopItemDecoration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ScienceActivity extends ToolBarBaseActivity implements View.OnClickListener {

    private RecyclerView rv;
    private List<ScienceItem> list;
    private static int numbersButton=18;
    private int[] sideButtonsIds={R.id.btn11,R.id.btn12,R.id.btn13,R.id.btn14,R.id.btn15,R.id.btn16,
            R.id.btn21,R.id.btn22,R.id.btn23,R.id.btn24,R.id.btn25,R.id.btn26,
            R.id.btn31,R.id.btn32,R.id.btn33,R.id.btn34,R.id.btn35,R.id.btn36};
    private Button[] sideButtons = new Button[numbersButton];
    private Button btnRecycle,btnSure;
    //储存选择结果的标志数组
    private boolean[] isChosen=new boolean[numbersButton];

    @Override
    protected int getSideMenu() {
        return R.layout.activity_science_side;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setMyTitle("科普处方");
        setLeftButton(R.drawable.back, new MyOnClickListener() {
            @Override
            public void onClick() {
                onBackPressed();
            }
        });
        setRightButton(R.drawable.message, "消息", new MyOnClickListener() {
            @Override
            public void onClick() {
                //openDrawer();
            }
        });

        rv= (RecyclerView) findViewById(R.id.rv);
        for (int i=0;i<numbersButton;i++){
            sideButtons[i]= (Button) findViewById(sideButtonsIds[i]);
            sideButtons[i].setOnClickListener(this);

            isChosen[i]=false;
        }
        btnRecycle= (Button) findViewById(R.id.btnRecycle);
        btnRecycle.setOnClickListener(this);
        btnSure= (Button) findViewById(R.id.btnSure);
        btnSure.setOnClickListener(this);


        new LoadTask().execute(null,null,null);

    }

    private void getData() {
        list=new ArrayList<>();
        Bitmap bitmap1= BitmapFactory.decodeResource(getResources(),R.drawable.dance);
        Bitmap bitmap2= BitmapFactory.decodeResource(getResources(),R.drawable.run);
        Bitmap bitmap3= BitmapFactory.decodeResource(getResources(),R.drawable.swim);
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

    @Override
    public void onClick(View view) {
        int currentId=view.getId();
        //判断是否是菜单上的按钮
        for (int i=0;i<numbersButton;i++){
            if (currentId==sideButtonsIds[i]){
                if (isChosen[i]==false){
                    Log.v("tag","dwdfvdvreve");
                    Button button= (Button) view;
                    button.setBackgroundResource(R.drawable.button_side_press);
                    button.setTextColor(Color.WHITE);
                    isChosen[i]=true;
                } else {
                    Button button= (Button) view;
                    button.setBackgroundResource(R.drawable.button_side);
                    button.setTextColor(getResources().getColor(R.color.main));
                    isChosen[i]=false;
                }
            }
        }

        if (currentId==R.id.btnRecycle){
            recycle();
        }

        if (currentId==R.id.btnSure){
            reLoad();
        }
    }

    private void recycle() {
        for (int i=0;i<numbersButton;i++){
            Button button=sideButtons[i];
            button.setBackgroundResource(R.drawable.button_side);
            button.setTextColor(getResources().getColor(R.color.main));
            isChosen[i]=false;
        }
    }

    //根据筛选条件重新加载列表
    private void reLoad() {
        recycle();//筛选条件复原
    }

    private class LoadTask extends AsyncTask<URL,Integer,Void>{
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
                startActivity(new Intent(ScienceActivity.this,NewsDetailActivity.class));
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
