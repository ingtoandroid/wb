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
import android.widget.TextView;


import com.example.a.app10.Adapter.NewsAdapter;
import com.example.a.app10.R;
import com.example.a.app10.bean.MessageReminder;
import com.example.a.app10.bean.NewsItem;
import com.example.a.app10.tool.KopItemDecoration;
import com.example.a.app10.tool.MyInternet;
import com.example.a.app10.tool.Net;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.QBadgeView;

public class ScienceActivity extends ToolBarBaseActivity implements View.OnClickListener {

    private RecyclerView rv;
    private List<NewsItem> list;
    private final int NUMBERBUTTONS =27;
    private int[] sideButtonsIds={R.id.btn11,R.id.btn12,R.id.btn13,R.id.btn14,R.id.btn15,R.id.btn16,R.id.btn17,R.id.btn18,R.id.btn19,
            R.id.btn21,R.id.btn22,R.id.btn23,R.id.btn24,R.id.btn25,R.id.btn26,R.id.btn27,R.id.btn28,R.id.btn29,
            R.id.btn31,R.id.btn32,R.id.btn33,R.id.btn34,R.id.btn35,R.id.btn36,R.id.btn37,R.id.btn38,R.id.btn39,};
    private Button[] sideButtons = new Button[NUMBERBUTTONS];
    private Button btnRecycle,btnSure;
    //储存选择结果的标志数组
    private boolean[] isChosen=new boolean[NUMBERBUTTONS];
    private boolean[] isShow=new boolean[NUMBERBUTTONS];
    private OkHttpClient client;
    private NewsAdapter adapter;
    private TextView tvSideTitle1,tvSideTitle2,tvSideTitle3;
    private String[] sideTitles=new String[3];
    private String[] buttonTexts,buttonCode;
    private final int EVEAY_MAX_LINE=9;
    private int[] numbers=new int[3];
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
        setRightButton(0, "筛选", new MyOnClickListener() {
            @Override
            public void onClick() {
                openDrawer();
            }
        });

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        new QBadgeView(this).bindTarget(toolbar).setBadgeNumber(Net.getMegsSize());

        rv= (RecyclerView) findViewById(R.id.rv);
        for (int i = 0; i< NUMBERBUTTONS; i++){
            sideButtons[i]= (Button) findViewById(sideButtonsIds[i]);
            sideButtons[i].setOnClickListener(this);

            isChosen[i]=false;
            isShow[i]=false;
        }
        btnRecycle= (Button) findViewById(R.id.btnRecycle);
        btnRecycle.setOnClickListener(this);
        btnSure= (Button) findViewById(R.id.btnSure);
        btnSure.setOnClickListener(this);
        tvSideTitle1= (TextView) findViewById(R.id.tvSideTitle1);
        tvSideTitle2= (TextView) findViewById(R.id.tvSideTitle2);
        tvSideTitle3= (TextView) findViewById(R.id.tvSideTitle3);
        client=new OkHttpClient();
        list=new ArrayList<>();
        buttonTexts=new String[NUMBERBUTTONS];
        buttonCode=new String[NUMBERBUTTONS];

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new KopItemDecoration(this,KopItemDecoration.VERTICAL_LIST));

        new LoadTask().execute(null,null,null);

    }

    private void getData() {
        String url= MyInternet.MAIN_URL+"sports/get_sports_list";
        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                try {
                    JSONObject all=new JSONObject(s);
                    JSONArray array=all.getJSONArray("dataList");
                    int index=array.length();
                    for (int i=0;i<index;i++){
                        JSONObject object=array.getJSONObject(i);
                        NewsItem item=new NewsItem(object.getString("newsId"),
                                object.getString("title"),object.getString("publishTime"),
                                object.getString("authorName"),object.getString("filePath"));
                        list.add(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void mainThread() {
                showRecycler();
            }
        },this);

        String listUrl=MyInternet.MAIN_URL+"sports/get_sports_search_list";
        MyInternet.getMessage(listUrl, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                handleSide(s);
            }

            @Override
            public void mainThread() {
                showSide();
            }
        },this);
    }

    private void showSide() {
        tvSideTitle1.setText(sideTitles[0]);
        tvSideTitle2.setText(sideTitles[1]);
        tvSideTitle3.setText(sideTitles[2]);
        for (int i=0;i<3;i++){
            int length=numbers[i]<6 ? numbers[i] :6;
            for (int j=0;j<length;j++){
                int index=i*EVEAY_MAX_LINE+j;
                if (j==5){
                    //最后一个按钮的处理
                    //最后一个按钮的处理
                    sideButtons[index].setVisibility(View.VISIBLE);
                    sideButtons[index].setText("更多");
                    sideButtons[index].setOnClickListener(new MyListener(i));
                } else {
                    sideButtons[index].setVisibility(View.VISIBLE);
                    sideButtons[index].setText(buttonTexts[index]);
                }
            }
        }
    }

    private void handleSide(String s) {//解析侧滑菜单的API
        try {
            JSONObject all=new JSONObject(s);
            JSONArray array=all.getJSONArray("dataList");
            for (int i=0;i<3;i++){
                JSONObject obj=array.getJSONObject(i);
                sideTitles[i]=obj.getString("catalogName");
                Log.v("tagSc",sideTitles[i]);
                JSONArray array1=obj.getJSONArray("childList");
                for (int j=0;j<array1.length();j++){
                    JSONObject object=array1.getJSONObject(j);
                    buttonTexts[i*EVEAY_MAX_LINE+j]=object.getString("catalogName");
                    buttonCode[i*EVEAY_MAX_LINE+j]=object.getString("catalogCode");
                    numbers[i]++;
                }
            }
        } catch (JSONException e) {
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
        for (int i = 0; i< NUMBERBUTTONS; i++){
            if (currentId==sideButtonsIds[i]){
                if (isChosen[i]==false){
                    int group=i/EVEAY_MAX_LINE;
                    for (int j=group*EVEAY_MAX_LINE;j<(group+1)*EVEAY_MAX_LINE;j++){//去除原有其他标记
                        isChosen[j]=false;
                        sideButtons[j].setBackgroundResource(R.drawable.button_side);
                        sideButtons[j].setTextColor(getResources().getColor(R.color.main));
                    }
                    sideButtons[i].setBackgroundResource(R.drawable.button_side_press);
                    sideButtons[i].setTextColor(Color.WHITE);
                    isChosen[i]=true;
                } else {
                    Button button= (Button) view;
                    button.setBackgroundResource(R.drawable.button_side);
                    button.setTextColor(getResources().getColor(R.color.main));
                    isChosen[i]=false;
                }
                return;
            }
        }

        if (currentId==R.id.btnRecycle){
            recycle();
            return;
        }

        if (currentId==R.id.btnSure){
            reLoad();
            return;
        }

    }

    private void recycle() {
        for (int i = 0; i< NUMBERBUTTONS; i++){
            Button button=sideButtons[i];
            button.setBackgroundResource(R.drawable.button_side);
            button.setTextColor(getResources().getColor(R.color.main));
            isChosen[i]=false;
        }
    }

    //根据筛选条件重新加载列表
    private void reLoad() {
        String a="";
        for (int i=0;i<NUMBERBUTTONS;i++){
            if (isChosen[i]){
                a+=buttonCode[i]+",";
            }
        }
        if (a.length()>1){
            reGetData(a);
        }

        closeDrawer();
        recycle();//筛选条件复原
    }

    private void reGetData(String a) {
        String url=MyInternet.MAIN_URL+"sports/get_sports_list?catalogCode="+a;
        Log.v("aaaaa",a);
        showProgress("加载中");
        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {

                try {
                    list=new ArrayList<>();
                    JSONObject all=new JSONObject(s);
                    JSONArray array=all.getJSONArray("dataList");
                    for (int i=0;i<array.length();i++){
                        JSONObject object=array.getJSONObject(i);
                        NewsItem item=new NewsItem(object.getString("newsId"),
                                object.getString("title"),object.getString("publishTime"),
                                object.getString("authorName"),object.getString("filePath"));
                        list.add(item);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void mainThread() {
                showRecycler();
            }
        },this);
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
        }
    }

    //配置并显示列表
    public void showRecycler(){
        hideProgress();
        adapter=new NewsAdapter(list,this);
        adapter.setLisenter(new NewsAdapter.OnItenClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(ScienceActivity.this,NewsDetailActivity.class);
                intent.putExtra("newsId",list.get(position).getNewsId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        rv.setAdapter(adapter);

        rv.setVisibility(View.VISIBLE);
    }

    private class MyListener implements View.OnClickListener{//更多的点击事件

        private int line;//组号，从零开始

        @Override
        public void onClick(View view) {
            view.setOnClickListener(this);//修改监听器
            for (int i=5;i<9;i++){
                int index=line*EVEAY_MAX_LINE+i;
                sideButtons[index].setVisibility(View.VISIBLE);
                sideButtons[index].setText(buttonTexts[index]);
            }
        }

        public MyListener(int line){
            this.line=line;
        }
    }
}
