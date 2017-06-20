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
    private Button btnClub, btnPersonal;
    private boolean isClub= true;
    private View item1;
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
        setRightButton(R.drawable.message_reminder, "消息", new MyOnClickListener() {
            @Override
            public void onClick() {
                startActivity(new Intent(ClassActivity.this, MessageReminder.class));
            }
        });

        rv= (RecyclerView) findViewById(R.id.rv);
        list=new ArrayList<>();

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
                    loadClub();
                }
                break;
            case R.id.btnPersonal:
                if (isClub){
                    btnPersonal.setTextColor(getResources().getColor(R.color.textGreen));
                    btnClub.setTextColor(Color.rgb(50,50,50));
                    isClub=false;
                    loadOrder();
                }
                break;
        }
    }

    private void loadOrder() {
        new LoadTask().execute(2,null,null);
    }

    private void loadClub() {
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
    }

    private void getData(int i) {
        list=new ArrayList<>();

//        Call call= Net.getInstance().get(MyInternet.MAIN_URL+"course/courseRelease_list?pageIndex=1&courseType="+i);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                String string=response.body().string();
//                Log.v("tagS",string);
//                try {
//                    JSONObject jsonObject = new JSONObject(string);
//                    JSONArray jsonArray=jsonObject.getJSONArray("datalist");
//                    for (int i=0;i<jsonArray.length();i++){
//                        JSONObject object=jsonArray.getJSONObject(i);
//                        list.add(new ClassItem(object.getString("imageUrl"),
//                                object.getString("courseId"),
//                                object.getString("courseTitle"),
//                                object.getString("startDate"),
//                                object.getString("entereNum")));
//                    }
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            showRecycler();
//                        }
//                    });
//                }
//                catch (JSONException e){
//                    e.printStackTrace();
//                }
//            }
//        });

        //测试用手动延迟
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}