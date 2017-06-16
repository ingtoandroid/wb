package com.example.a.app10.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


import com.example.a.app10.Adapter.ClassAdapter;
import com.example.a.app10.Adapter.ClassItem;
import com.example.a.app10.R;
import com.example.a.app10.bean.ShipinItem;
import com.example.a.app10.tool.MyInternet;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ClassActivity extends ToolBarBaseActivity implements View.OnClickListener {

    private RecyclerView rv;
    private List<ClassItem> list1,list2,list;//两个列表和要显示的数组
    private Handler handler=new Handler();
    private Button btnClub, btnPersonal;
    private boolean isClub= true;//标记显示类型
    private OkHttpClient client;
    private boolean finish;//非常重要，控制任务顺序
    private ClassAdapter adapter;
    private LinearLayout llLoading;
    private int pageIndex1,pageIndex2,maxIndex1,maxIndex2;

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
        setRightButton(R.drawable.message, "消息", new MyOnClickListener() {
            @Override
            public void onClick() {

            }
        });

        rv= (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int endPosition=((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                if (endPosition>=list.size()-1){
                    if (isClub){
                        loadMore(1);
                    } else {
                        loadMore(2);
                    }
                }
            }
        });
        list1 =new ArrayList<>();
        list2 =new ArrayList<>();
        list=new ArrayList<>();
        pageIndex1=1;
        pageIndex2=1;

        btnClub= (Button) findViewById(R.id.btnClub);
        btnClub.setOnClickListener(this);
        btnPersonal = (Button) findViewById(R.id.btnPersonal);
        btnPersonal.setOnClickListener(this);
        llLoading= (LinearLayout) findViewById(R.id.llLoading);

        client=new OkHttpClient();

        new LoadTask().execute(null,null,null);
    }

    private void loadMore(final int type) {
        finish=false;
        int pageIndex=0;
        if (type==1){
            pageIndex1++;
            pageIndex=pageIndex1;
        }
        if (type==2){
            pageIndex2++;
            pageIndex=pageIndex2;
        }
        llLoading.setVisibility(View.VISIBLE);
        MyInternet.MyInterface myInterface=new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                //handleJson(s,type);
            }
        };
        String url=MyInternet.MAIN_URL+"course/courseRelease_list?pageIndex="+pageIndex+"&courseType="+type;
        MyInternet.getMessage(url,client,myInterface);
        while (!finish){//等待列表加载完毕
        }
        adapter.notifyDataSetChanged();
        llLoading.setVisibility(View.GONE);
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
                    //new LoadTask().execute(null,null,null);
                    btnClub.setTextColor(getResources().getColor(R.color.main));
                    btnPersonal.setTextColor(Color.BLACK);
                    isClub=true;
                    changeRecycler(1);
                }
                break;
            case R.id.btnOrder:
                if (isClub){
                    //new LoadTask().execute(null,null,null);
                    btnPersonal.setTextColor(getResources().getColor(R.color.main));
                    btnClub.setTextColor(Color.BLACK);
                    isClub=false;
                    changeRecycler(2);
                }
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
            showRecycler();
        }
    }

    private void showRecycler() {
        hideProgress();
        list=list1;
        adapter=new ClassAdapter(list,this);
        adapter.setLisenter(new ClassAdapter.OnItenClickListener() {
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

    public void changeRecycler(int type){
        if (type==1){
            list=list1;
        }
        if (type==2){
            list=list2;
        }
        adapter.notifyDataSetChanged();
    }

    private void getData() {
        finish=false;
        //获取俱乐部课程列表
        MyInternet.MyInterface myInterface=new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                handleJson(s,1);
            }
        };
        String url=MyInternet.MAIN_URL+"course/courseRelease_list?pageIndex=1&courseType=1";
        MyInternet.getMessage(url,client,myInterface);
        //获取私人课程列表
        MyInternet.MyInterface myInterface2=new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                handleJson(s,2);
            }
        };
        String url2=MyInternet.MAIN_URL+"course/courseRelease_list?pageIndex=1&courseType=2";
        MyInternet.getMessage(url2,client,myInterface2);

        while (!finish){
            //控制加载流程的结束
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleJson(String s,int type) {
        try {
            JSONObject object2=new JSONObject(s);
            JSONArray array=object2.getJSONArray("dataList");
            int length=array.length();
            for (int i=0;i<length;i++){
                JSONObject object=array.getJSONObject(i);
                ClassItem item=new ClassItem(object.getString("imageUrl"),
                        object.getString("courseId"),object.getString("courseTitle"),
                        object.getString("entereNum")+"人已参加",
                        "开课时间：  "+object.getString("startDate"));
                if (type==1){
                    list1.add(item);
                }
                if (type==2){
                    list2.add(item);
                }
            }
            finish=true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
