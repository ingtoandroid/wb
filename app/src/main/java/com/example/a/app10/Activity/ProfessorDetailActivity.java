package com.example.a.app10.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;


import com.example.a.app10.Adapter.ScienceAdapter;
import com.example.a.app10.R;
import com.example.a.app10.bean.ScienceItem;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDetailActivity extends ToolBarBaseActivity implements View.OnClickListener {

    private LinearLayout llContent;
    private RecyclerView rvVideo,rvCourse;
    private List<ScienceItem> list;
    private Button btnComment,btnLeave,btnOrder;

    @Override
    protected int getSideMenu() {
        return R.layout.activity_science_side;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideDrawer();//禁用原侧滑菜单
        setMyTitle("专家详情");
        setLeftButton(R.drawable.back, new MyOnClickListener() {
            @Override
            public void onClick() {
                onBackPressed();
            }
        });

        llContent= (LinearLayout) findViewById(R.id.llContent);
        rvVideo= (RecyclerView) findViewById(R.id.rvVideo);
        rvCourse= (RecyclerView) findViewById(R.id.rvCourse);
        btnComment= (Button) findViewById(R.id.btnComment);
        btnComment.setOnClickListener(this);
        btnLeave= (Button) findViewById(R.id.btnLeave);
        btnLeave.setOnClickListener(this);
        btnOrder= (Button) findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(this);

        list=new ArrayList<>();

        new LoadTask().execute(null,null,null);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_professor_detail;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnComment:

                break;
            case R.id.btnLeave:
                break;
            case R.id.btnOrder:
                break;
        }
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
            hideProgress();
            show();
        }
    }

    private void show() {
        rvVideo.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        ScienceAdapter adapter1=new ScienceAdapter(list,this);
        adapter1.setLisenter(new ScienceAdapter.OnItenClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(ProfessorDetailActivity.this,NewsDetailActivity.class));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        ScienceAdapter adapter2=new ScienceAdapter(list,this);
        adapter2.setLisenter(new ScienceAdapter.OnItenClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(ProfessorDetailActivity.this,NewsDetailActivity.class));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        rvVideo.setAdapter(adapter1);
        rvCourse.setLayoutManager(new LinearLayoutManager(this));//默认竖直列表
        rvCourse.setAdapter(adapter2);
        llContent.setVisibility(View.VISIBLE);
    }

    private void getData() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
    }
}
