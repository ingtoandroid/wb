package com.example.a.app10.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
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

    private boolean end=false;
    private RecyclerView rv;
    private LinearLayout llLoading;
    private boolean isShaiXuan=false;
    private List<NewsItem> list;
    private Button btnRecycle,btnSure;
    //储存选择结果的标志数组
    private List<Button> listButton;
    private List<Boolean> isChosen1;
    private List<String> buttonTexts,buttonCode;
    private GridLayout grid1,grid2,grid3;
    private OkHttpClient client;
    private NewsAdapter adapter;
    private TextView tvSideTitle1,tvSideTitle2,tvSideTitle3;
    private String[] sideTitles=new String[3];
    private int[] numbers=new int[3];
    private int pageIndex=0;
    private int currentPosition=0;
    private String catalogCode=null;
    private boolean loading=false;
    private int butonWidth,buttonHeight;
    private Resources resources;
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
        grid1= (GridLayout) findViewById(R.id.grid1);
        grid2= (GridLayout) findViewById(R.id.grid2);
        grid3= (GridLayout) findViewById(R.id.grid3);
        buttonHeight=(int) getResources().getDimension(R.dimen.side_button_height);
        butonWidth=(int) getResources().getDimension(R.dimen.side_button_width);
        resources=getResources();
        listButton=new ArrayList<>();
        isChosen1=new ArrayList<>();
        buttonTexts=new ArrayList<>();
        buttonCode=new ArrayList<>();
        list=new ArrayList<>();
        btnRecycle= (Button) findViewById(R.id.btnRecycle);
        btnRecycle.setOnClickListener(this);
        btnSure= (Button) findViewById(R.id.btnSure);
        btnSure.setOnClickListener(this);
        tvSideTitle1= (TextView) findViewById(R.id.tvSideTitle1);
        tvSideTitle2= (TextView) findViewById(R.id.tvSideTitle2);
        tvSideTitle3= (TextView) findViewById(R.id.tvSideTitle3);
        client=new OkHttpClient();
        list=new ArrayList<>();
        llLoading= (LinearLayout) findViewById(R.id.llLoading);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new KopItemDecoration(this,KopItemDecoration.VERTICAL_LIST));

        new LoadTask().execute(null,null,null);

    }

    private void getData() {
        pageIndex++;
        String url= MyInternet.MAIN_URL+"sports/get_sports_list?pageIndex="+pageIndex;
        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                try {
                    JSONObject all=new JSONObject(s);
                    JSONArray array=all.getJSONArray("dataList");
                    int index=array.length();
                    if (index<=0){
                        end=true;
                        return;
                    }
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
        int count=numbers[0]+numbers[1]+numbers[2];
        for (int i=0;i<count;i++){
            LinearLayout.LayoutParams params1=new LinearLayout.LayoutParams(butonWidth,buttonHeight);
            GridLayout.LayoutParams params=new GridLayout.LayoutParams(params1);
            params.rightMargin=20;params.bottomMargin=10;
            Button button=new Button(ScienceActivity.this);
            button.setText(buttonTexts.get(i));
            button.setTextColor(resources.getColor(R.color.main));
            button.setBackgroundResource(R.drawable.button_side);
            button.setMaxLines(1);
            button.setOnClickListener(this);
            button.setVisibility(View.GONE);
            button.setLayoutParams(params);
            listButton.add(button);
            isChosen1.add(false);
        }
        int index1=6>numbers[0]?numbers[0]:6;
        for (int i=0;i<numbers[0];i++){
            grid1.addView(listButton.get(i));
        }
        for (int a=0;a<index1;a++){
            listButton.get(a).setVisibility(View.VISIBLE);
            if(a==5){
                listButton.get(a).setText("更多");
                listButton.get(a).setOnClickListener(new MyListener(0));
            }
        }

        int index2=6>numbers[1]?numbers[1]:6;
        for (int i=numbers[0];i<numbers[0]+numbers[1];i++){
            grid2.addView(listButton.get(i));
        }
        for (int a=numbers[0];a<numbers[0]+index2;a++){
            listButton.get(a).setVisibility(View.VISIBLE);
            if(a==numbers[0]+5){
                listButton.get(a).setText("更多");
                listButton.get(a).setOnClickListener(new MyListener(1));
            }
        }

        int index3=6>numbers[2]?numbers[2]:6;
        for (int i=numbers[0]+numbers[1];i<numbers[0]+numbers[1]+numbers[2];i++){
            grid3.addView(listButton.get(i));
        }
        for (int a=numbers[0]+numbers[1];a<numbers[0]+numbers[1]+index3;a++){
            listButton.get(a).setVisibility(View.VISIBLE);
            if(a==numbers[0]+numbers[1]+5){
                listButton.get(a).setText("更多");
                listButton.get(a).setOnClickListener(new MyListener(2));
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
                JSONArray array1=obj.getJSONArray("childList");
                for (int j=0;j<array1.length();j++){
                    JSONObject object=array1.getJSONObject(j);
                    buttonTexts.add(object.getString("catalogName"));
                    buttonCode.add(object.getString("catalogCode"));
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
        for (int i = 0; i< listButton.size(); i++){
            if (view.equals(listButton.get(i))){
                if (!isChosen1.get(i)){
                    clearGroup(i);
                    view.setBackgroundResource(R.drawable.button_side_press);
                    listButton.get(i).setTextColor(Color.WHITE);
                    isChosen1.set(i,true);
                } else {
                    Button button= (Button) view;
                    button.setBackgroundResource(R.drawable.button_side);
                    button.setTextColor(getResources().getColor(R.color.main));
                    isChosen1.set(i,false);
                }
                return;
            }
        }

        if (currentId==R.id.btnRecycle){
            recycle();
            return;
        }

        if (currentId==R.id.btnSure){
            isShaiXuan=true;
            pageIndex=0;//页号初始化
            list=new ArrayList<>();//列表清空
            reLoad();
            return;
        }

    }

    private void recycle() {
        for (int i = 0; i< listButton.size(); i++){
            Button button=listButton.get(i);
            button.setBackgroundResource(R.drawable.button_side);
            button.setTextColor(getResources().getColor(R.color.main));
            isChosen1.set(i,false);
        }
    }

    //根据筛选条件重新加载列表
    private void reLoad() {
        String a="";
        for (int i=0;i<listButton.size();i++){
            if (isChosen1.get(i)){
                a+=buttonCode.get(i)+",";
            }
        }
        if (a.length()>1){
            catalogCode=a;
            reGetData();
        }

        closeDrawer();
        recycle();//筛选条件复原
    }

    private void reGetData() {
        pageIndex++;
        String url=MyInternet.MAIN_URL+"sports/get_sports_list?catalogCode="+catalogCode+"&pageIndex="+pageIndex;
        showProgress("加载中");
        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                try {
                    JSONObject all=new JSONObject(s);
                    JSONArray array=all.getJSONArray("dataList");
                    if (array.length()<=0){
                        end=true;
                        return;
                    }
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
        hideBottomProgress();
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
        rv.setAdapter(adapter);
        rv.scrollToPosition(currentPosition);
        rv.setVisibility(View.VISIBLE);
        loading=false;
    }

    private class MyListener implements View.OnClickListener{//更多的点击事件

        private int line;//组号，从零开始

        @Override
        public void onClick(View view) {
            view.setOnClickListener(ScienceActivity.this);//修改监听器
            switch (line){
                case 0:
                    for (int i=5;i<numbers[0];i++){
                        listButton.get(i).setVisibility(View.VISIBLE);
                        listButton.get(i).setText(buttonTexts.get(i));
                    }
                    break;
                case 1:
                    for (int i=numbers[0]+5;i<numbers[0]+numbers[1];i++){
                        listButton.get(i).setVisibility(View.VISIBLE);
                        listButton.get(i).setText(buttonTexts.get(i));
                    }
                    break;
                case 2:
                    for (int i=numbers[0]+numbers[1]+5;i<numbers[0]+numbers[1]+numbers[2];i++){
                        listButton.get(i).setVisibility(View.VISIBLE);
                        listButton.get(i).setText(buttonTexts.get(i));
                    }
                    break;
            }
        }

        public MyListener(int line){
            this.line=line;
        }
    }

    private void loadMore() {//加载更多
        if (list.size()<6){
            return;
        }
        if (loading||end){
            return;
        }
        loading=true;
        showBottomProgress();
        currentPosition=list.size()-6;
        Log.v("tag","loadmore");
        if (isShaiXuan){
            reGetData();
        } else {
            getData();
        }
    }

    private void showBottomProgress() {
        llLoading.setVisibility(View.VISIBLE);
    }

    private void hideBottomProgress(){
        llLoading.setVisibility(View.GONE);
    }

    private boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    private void clearGroup(int index) {//清除同组标记
        int group;
        if (index<numbers[0]+numbers[1]){
            if (index<numbers[0]){//一组
                for (int i=0;i<numbers[0];i++){
                    isChosen1.set(i,false);
                    listButton.get(i).setBackgroundResource(R.drawable.button_side);
                    listButton.get(i).setTextColor(getResources().getColor(R.color.main));
                }
            } else {//二组
                for (int i=numbers[0];i<numbers[0]+numbers[1];i++){
                    isChosen1.set(i,false);
                    listButton.get(i).setBackgroundResource(R.drawable.button_side);
                    listButton.get(i).setTextColor(getResources().getColor(R.color.main));
                }
            }
        } else {
            for (int i=numbers[0]+numbers[1];i<numbers[0]+numbers[1]+numbers[2];i++){
                isChosen1.set(i,false);
                listButton.get(i).setBackgroundResource(R.drawable.button_side);
                listButton.get(i).setTextColor(getResources().getColor(R.color.main));
            }
        }

    }
}
