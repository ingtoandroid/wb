package com.example.a.app10.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.a.app10.Adapter.ClassAdapter;
import com.example.a.app10.Adapter.VideoAdapter;
import com.example.a.app10.R;
import com.example.a.app10.bean.ShipinItem;
import com.example.a.app10.tool.Net;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ShipinActivity extends ToolBarBaseActivity implements View.OnClickListener {

    private RecyclerView rv;
    private List<ShipinItem> list;
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

        new ShipinActivity.LoadTask().execute(1,null,null);
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
        new ShipinActivity.LoadTask().execute(2,null,null);
    }

    private void loadClub() {
        new ShipinActivity.LoadTask().execute(1,null,null);
    }

    private class LoadTask extends AsyncTask<Integer,Integer,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress("加载中");
        }

        @Override
        protected Void doInBackground(Integer... i) {
            getData(i[0]);
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
        VideoAdapter adapter=new VideoAdapter(list,this);
        adapter.setListener(new VideoAdapter.OnItenClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(ShipinActivity.this,VideoDetail.class);
                intent.putExtra("id",list.get(position).getVideoId().toString());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        rv.setAdapter(adapter);
        rv.setVisibility(View.VISIBLE);
    }

    private void getData(int i) {
        list=new ArrayList<>();

        Call call= Net.getInstance().shipinList(0,i);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String string=response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    JSONArray jsonArray=jsonObject.getJSONArray("datalist");
                    Gson gson=new Gson();
                    list=gson.fromJson(jsonArray.toString(),new TypeToken<List<ShipinItem>>(){}.getType());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showRecycler();
                        }
                    });
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

        //测试用手动延迟
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}