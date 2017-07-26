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

import com.example.a.app10.Adapter.ClassAdapter;
import com.example.a.app10.Adapter.ClassItem;
import com.example.a.app10.Adapter.VideoAdapter;
import com.example.a.app10.R;
import com.example.a.app10.bean.MessageReminder;
import com.example.a.app10.bean.ShipinItem;
import com.example.a.app10.tool.MyInternet;
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
import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.QBadgeView;

public class ClassActivity extends ToolBarBaseActivity implements View.OnClickListener {

    private RecyclerView rv;
    private List<ClassItem> list;
    private LinearLayout llLoading;
    private Button btnClub, btnPersonal;
    private boolean isClub= true;
    private int pageIndex=0;
    private int currentPosition=0;
    private boolean change=false;
    @Override
    protected int getSideMenu() {
        return R.layout.activity_science_side;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideDrawer();
        setMyTitle("课程列表");
        setLeftButton(R.drawable.back, new MyOnClickListener() {
            @Override
            public void onClick() {
                onBackPressed();
            }
        });

        rv= (RecyclerView) findViewById(R.id.rv);
        list=new ArrayList<>();
        llLoading= (LinearLayout) findViewById(R.id.llLoading);
        btnClub= (Button) findViewById(R.id.btnClub);
        btnClub.setOnClickListener(this);
        btnPersonal = (Button) findViewById(R.id.btnPersonal);
        btnPersonal.setOnClickListener(this);

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        new QBadgeView(this).bindTarget(toolbar).setBadgeNumber(Net.getMegsSize());

        new LoadTask().execute(1,null,null);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_class;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnClub:
                if (!isClub){
                    btnClub.setTextColor(getResources().getColor(R.color.textGreen));
                    btnPersonal.setTextColor(Color.rgb(50,50,50));
                    isClub=true;
                    change=true;
                    loadClub();
                }
                break;
            case R.id.btnPersonal:
                if (isClub){
                    btnPersonal.setTextColor(getResources().getColor(R.color.textGreen));
                    btnClub.setTextColor(Color.rgb(50,50,50));
                    isClub=false;
                    change=true;
                    loadOrder();
                }
                break;
        }
    }

    private void loadOrder() {
        pageIndex=1;
        new LoadTask().execute(2,null,null);
    }

    private void loadClub() {
        pageIndex=1;
        new LoadTask().execute(1,null,null);
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
        ClassAdapter adapter=new ClassAdapter(list,this);
        adapter.setLisenter(new ClassAdapter.OnItenClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(ClassActivity.this,ClassDetailActivity.class);
                intent.putExtra("courseId",list.get(position).getCourseId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        rv.setAdapter(adapter);
        rv.setVisibility(View.VISIBLE);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isSlideToBottom(recyclerView)) {
                    loadMore();
                }
            }
        });
        if (!change){
            rv.scrollToPosition(currentPosition);
        }
    }

    private void getData(int i) {
        if (change){
            list=new ArrayList<>();
            pageIndex=1;
        } else {//加载更多
            pageIndex++;
        }

        Call call= Net.getInstance().getClassList("?pageIndex="+pageIndex+"&courseType="+i);
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
                    if (jsonArray.length()<=0){//无法加载更多
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideBottomProgress();
                            }
                        });
                        return;
                    }
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object=jsonArray.getJSONObject(i);
                        list.add(new ClassItem(object.getString("imageUrl"),
                                object.getString("courseId"),
                                object.getString("courseTitle"),
                                object.getString("startDate"),
                                object.getString("entereNum")));
                    }
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


    }

    private void loadMore() {//加载更多
        showBottomProgress();
        change=false;
        currentPosition=list.size()-4;
        getData(isClub? 1:2);
    }


    private boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    private void showBottomProgress() {
        llLoading.setVisibility(View.VISIBLE);
    }

    private void hideBottomProgress(){
        llLoading.setVisibility(View.GONE);
    }
}