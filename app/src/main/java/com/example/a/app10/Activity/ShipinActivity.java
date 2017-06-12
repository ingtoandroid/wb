package com.example.a.app10.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.a.app10.Adapter.ClassAdapter;
import com.example.a.app10.R;
import com.example.a.app10.bean.ClassItem;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ShipinActivity extends ToolBarBaseActivity implements View.OnClickListener {

    private RecyclerView rv;
    private List<ClassItem> list;
    private Button btnClub,btnOrder;
    private boolean isClub= true;
    @Override
    protected int getSideMenu() {
        return R.layout.activity_science_side;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideDrawer();
        setMyTitle("视频列表");
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

        btnClub= (Button) findViewById(R.id.btnClub);
        btnClub.setOnClickListener(this);
        btnOrder= (Button) findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(this);

        new ShipinActivity.LoadTask().execute(null,null,null);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_shipin;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnClub:
                if (!isClub){
                    btnClub.setTextColor(getResources().getColor(R.color.main));
                    btnOrder.setTextColor(Color.BLACK);
                    isClub=true;
                    loadClub();
                }
                break;
            case R.id.btnOrder:
                if (isClub){
                    btnOrder.setTextColor(getResources().getColor(R.color.main));
                    btnClub.setTextColor(Color.BLACK);
                    isClub=false;
                    loadOrder();
                }
                break;
        }
    }

    private void loadOrder() {
    }

    private void loadClub() {
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
        ClassAdapter adapter=new ClassAdapter(list,this);
        adapter.setLisenter(new ClassAdapter.OnItenClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(ShipinActivity.this,VideoDetail.class));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        rv.setAdapter(adapter);
        rv.setVisibility(View.VISIBLE);
    }

    private void getData() {
        list=new ArrayList<>();
        Bitmap bitmap1= BitmapFactory.decodeResource(getResources(),R.drawable.dance);
        Bitmap bitmap2= BitmapFactory.decodeResource(getResources(),R.drawable.run_pic);
        Bitmap bitmap3= BitmapFactory.decodeResource(getResources(),R.drawable.swim_pic);
        list.add(new ClassItem(bitmap1,"HIIT适应性训练","5416574684人已参加","开课时间：    20150202"));
        list.add(new ClassItem(bitmap2,"HIIT适应性训练","5416574684人已参加","开课时间：    20150202"));
        list.add(new ClassItem(bitmap3,"HIIT适应性训练","5416574684人已参加","开课时间：    20150202"));
        list.add(new ClassItem(bitmap1,"HIIT适应性训练","5416574684人已参加","开课时间：    20150202"));
        list.add(new ClassItem(bitmap2,"HIIT适应性训练","5416574684人已参加","开课时间：    20150202"));
        list.add(new ClassItem(bitmap3,"HIIT适应性训练","5416574684人已参加","开课时间：    20150202"));
        list.add(new ClassItem(bitmap1,"HIIT适应性训练","5416574684人已参加","开课时间：    20150202"));
        list.add(new ClassItem(bitmap2,"HIIT适应性训练","5416574684人已参加","开课时间：    20150202"));
        list.add(new ClassItem(bitmap3,"HIIT适应性训练","5416574684人已参加","开课时间：    20150202"));


        //测试用手动延迟
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}