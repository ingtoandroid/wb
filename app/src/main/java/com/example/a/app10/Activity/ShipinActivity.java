package com.example.a.app10.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

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

import q.rorbin.badgeview.QBadgeView;

public class ShipinActivity extends ToolBarBaseActivity implements View.OnClickListener {

    private RecyclerView rv;
    private List<ShipinItem> list;
    private Button btnClub,btnOrder;
    private LinearLayout llLoading;
    private boolean isClub= true;
    private int totalPages=0;
    private int currentPages=0;
    private int type=0;
    private int currentCount=0;
    @Override
    protected int getSideMenu() {
        return R.layout.activity_science_side;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideDrawer();
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        new QBadgeView(this).bindTarget(toolbar).setBadgeNumber(Net.getMegsSize());
        setMyTitle("视频列表");
        setLeftButton(R.drawable.back, new MyOnClickListener() {
            @Override
            public void onClick() {
                onBackPressed();
            }
        });

        rv= (RecyclerView) findViewById(R.id.rv);
        llLoading= (LinearLayout) findViewById(R.id.llLoading);
        list=new ArrayList<>();

        btnClub= (Button) findViewById(R.id.btnClub);
        btnClub.setOnClickListener(this);
        btnOrder= (Button) findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(this);
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isSlidingToLast = false;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        //加载更多功能的代码
                        currentCount=manager.getItemCount();
                        getData2(type);
                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0)
                    isSlidingToLast=true;
            }
        });
        new LoadTask().execute(1,null,null);
        type=1;
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
        type=2;
    }

    private void loadClub() {
        new ShipinActivity.LoadTask().execute(1,null,null);
        type=1;
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
        hideBottomProgress();
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
        currentPages=0;
        totalPages=0;
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
                    totalPages=jsonObject.getInt("totalPages");
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
    }
    private void getData2(int i) {
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        showBottomProgress();
        if(++currentPages<totalPages){
        Call call= Net.getInstance().shipinList(currentPages,i);
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
                    List list2=new ArrayList<ShipinItem>();
                    list2=gson.fromJson(jsonArray.toString(),new TypeToken<List<ShipinItem>>(){}.getType());
                    list.addAll(list2);
                    Log.e("size",""+list.size());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showRecycler();
                            rv.scrollToPosition(currentCount);
                        }
                    });
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

        //测试用手动延迟
    }
    else
        {
            Toast.makeText(this, "没有更多", Toast.LENGTH_SHORT).show();
        }}

    private void showBottomProgress() {
        llLoading.setVisibility(View.VISIBLE);
    }

    private void hideBottomProgress(){
        llLoading.setVisibility(View.GONE);
    }
}