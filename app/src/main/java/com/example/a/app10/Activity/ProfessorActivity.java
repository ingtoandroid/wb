package com.example.a.app10.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.a.app10.Adapter.ProfessorAdapter;
import com.example.a.app10.R;
import com.example.a.app10.bean.ProfessorItem;
import com.example.a.app10.tool.MyInternet;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProfessorActivity extends ToolBarBaseActivity implements View.OnClickListener {
    private RecyclerView rv;
    private List<ProfessorItem> list;
    private OkHttpClient client;
    private List<Button> listButton;
    private Button btnRecycle,btnSure;
    //储存选择结果的标志数组
    private List<Boolean> isChosen1;
    private EditText et;
    private GridLayout grid1,grid2,grid3;
    private Button btnSearch;
    private ProfessorAdapter adapter;
    private TextView tvSideTitle1,tvSideTitle2,tvSideTitle3;
    private String[] sideTitles=new String[3];
    private List<String> buttonTexts,buttonCode;
    private String expertArea="";
    private int[] numbers=new int[3];//每个组的数据数目
    private LinearLayout llLoading;
    private int pageIndex=0;
    private int currentPosition=0;
    private boolean isShaiXuan=false;
    private boolean end=false;
    private String aa=null;//筛选列表用到的参数
    private int butonWidth,buttonHeight;
    private Resources resources;

    @Override
    protected int getSideMenu() {
        return R.layout.activity_professor_side;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setMyTitle("专家列表");
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

        rv= (RecyclerView) findViewById(R.id.rv);
        llLoading= (LinearLayout) findViewById(R.id.llLoading);

        buttonHeight=(int) getResources().getDimension(R.dimen.side_button_height);
        butonWidth=(int) getResources().getDimension(R.dimen.side_button_width);
        resources=getResources();

        btnRecycle= (Button) findViewById(R.id.btnRecycle);
        btnRecycle.setOnClickListener(this);
        btnSure= (Button) findViewById(R.id.btnSure);
        btnSure.setOnClickListener(this);
        btnSearch= (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
        et= (EditText) findViewById(R.id.et);
        grid1= (GridLayout) findViewById(R.id.grid1);
        grid2= (GridLayout) findViewById(R.id.grid2);
        grid3= (GridLayout) findViewById(R.id.grid3);

        listButton=new ArrayList<>();
        isChosen1=new ArrayList<>();
        buttonTexts=new ArrayList<>();
        buttonCode=new ArrayList<>();
        list=new ArrayList<>();
        tvSideTitle1= (TextView) findViewById(R.id.tvSideTitle1);
        tvSideTitle2= (TextView) findViewById(R.id.tvSideTitle2);
        tvSideTitle3= (TextView) findViewById(R.id.tvSideTitle3);
        client=new OkHttpClient();
        adapter=new ProfessorAdapter(list,this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        if (getIntent().getStringExtra("expertArea")!=null){
            expertArea=getIntent().getStringExtra("expertArea");
        }

        new LoadTask().execute(null,null,null);

    }

    private void getData() {
        pageIndex++;
        String url= MyInternet.MAIN_URL+"expert/get_expert_page_list?expertArea="+expertArea+"&pageIndex="+pageIndex;
        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                try {
                    JSONObject obj =new JSONObject(s);
                    JSONArray array=obj.getJSONArray("datalist");
                    if (array.length()<=0){
                        end=true;
                        return;
                    }
                    for (int i=0;i<array.length();i++){
                        JSONObject object=array.getJSONObject(i);
                        ProfessorItem item=new ProfessorItem(object.getString("imageUrl"),
                                object.getString("expertName"),
                                "研究方向： "+object.getString("expertArea"),
                                object.getString("expertId"),object.getInt("expertGrade"));
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


        String listUrl=MyInternet.MAIN_URL+"expert/get_expert_search_list";
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
            Button button=new Button(ProfessorActivity.this);
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
        grid1.removeAllViews();
        grid2.removeAllViews();
        grid3.removeAllViews();
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
            sideTitles[0]=all.getString("yjfx");
            sideTitles[1]=all.getString("scly");
            sideTitles[2]=all.getString("zjlx");
            JSONArray array1=all.getJSONArray("yjfxList");
            for (int i=0;i<array1.length();i++){
                JSONObject object=array1.getJSONObject(i);
                numbers[0]++;
                buttonTexts.add(object.getString("key"));
                buttonCode.add(object.getString("value"));
            }
            JSONArray array2=all.getJSONArray("sclyList");
            for (int i=0;i<array2.length();i++){
                JSONObject object=array2.getJSONObject(i);
                numbers[1]++;
                buttonTexts.add(object.getString("key"));
                buttonCode.add(object.getString("value"));
            }
            JSONArray array3=all.getJSONArray("zjlxList");
            for (int i=0;i<array3.length();i++){
                JSONObject object=array3.getJSONObject(i);
                numbers[2]++;
                buttonTexts.add(object.getString("key"));
                buttonCode.add(object.getString("value"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_professor;
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
            reLoad();
            return;
        }

        if (currentId==R.id.btnSearch){
            search();
            return;
        }
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

    private void search() {//搜索专家
        String s=et.getText().toString();
        if (s.length()>0){
            showProgress("加载中");
            String url=MyInternet.MAIN_URL+"expert/get_expert_search_page_list?expertMegs="+s;
            MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
                @Override
                public void handle(String s) {
                    list=new ArrayList<>();
                    try {
                        JSONObject obj =new JSONObject(s);
                        JSONArray array=obj.getJSONArray("datalist");
                        for (int i=0;i<array.length();i++){
                            JSONObject object=array.getJSONObject(i);
                            ProfessorItem item=new ProfessorItem(object.getString("imageUrl"),
                                    object.getString("expertName"),
                                    "研究方向： "+object.getString("expertArea"),
                                    object.getString("expertId"),object.getInt("expertGrade"));
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
        } else {
            Toast.makeText(this,"不能为空",Toast.LENGTH_SHORT).show();
        }
    }

    private void recycle() {//筛选条件复原
        for (int i = 0; i< listButton.size(); i++){
            Button button=listButton.get(i);
            button.setBackgroundResource(R.drawable.button_side);
            button.setTextColor(getResources().getColor(R.color.main));
            isChosen1.set(i,false);
        }
    }

    //根据筛选条件重新加载列表
    private void reLoad() {
        isShaiXuan=true;
        pageIndex=0;
        currentPosition=0;
        String a="";
        for (int i=0;i<numbers[0];i++){//研究方向
            if (isChosen1.get(i)){
                a+="expertArea="+buttonCode.get(i)+"&";
            }
        }
        for (int i=numbers[0];i<numbers[0]+numbers[1];i++){//擅长领域
            if (isChosen1.get(i)){
                a+="goodField="+buttonCode.get(i)+"&";
            }
        }
        for (int i=numbers[0]+numbers[1];i<listButton.size();i++){//专家类型
            if (isChosen1.get(i)){
                a+="expertType="+buttonCode.get(i);
            }
        }
        if (a.length()>1){
            showProgress("加载中");
            aa=a;
            reGetData();
        }

        closeDrawer();
        recycle();//筛选条件复原
    }

    private void reGetData() {
        if (pageIndex==0){
            list=new ArrayList<>();//首次加载，清空数组
        }
        pageIndex++;
        String url=MyInternet.MAIN_URL+"expert/get_expert_page_list?"+aa+"pageIndex="+pageIndex;
        Log.v("tag",url);
        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                try {
                    JSONObject all=new JSONObject(s);
                    JSONArray array=all.getJSONArray("datalist");
                    if (array.length()<=0){
                        end=true;
                        return;
                    }
                    for (int i=0;i<array.length();i++){
                        JSONObject object=array.getJSONObject(i);
                        ProfessorItem item=new ProfessorItem(object.getString("imageUrl"),
                                object.getString("expertName"),
                                "研究方向： "+object.getString("expertArea"),
                                object.getString("expertId"),object.getInt("expertGrade"));
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
        }
    }

    //配置并显示列表
    public void showRecycler(){
        hideProgress();
        hideBottomProgress();
        adapter=new ProfessorAdapter(list,this);
        adapter.setLisenter(new ProfessorAdapter.OnItenClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(ProfessorActivity.this,ProfessorDetailActivity.class);
                intent.putExtra("expertId",list.get(position).getExpertId());
                intent.putExtra("imageUrl",list.get(position).getImgUrl());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        rv.setAdapter(adapter);
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
        rv.scrollToPosition(currentPosition);
        //adapter.notifyDataSetChanged();
        rv.setVisibility(View.VISIBLE);
    }

    private class MyListener implements View.OnClickListener{//更多的点击事件

        private int line;//组号，从零开始

        @Override
        public void onClick(View view) {
            view.setOnClickListener(ProfessorActivity.this);//修改监听器
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
        if (list.size()<5||end){
            return;
        }
        showBottomProgress();
        currentPosition=list.size()-5;
        if (isShaiXuan){
            reGetData();
        } else {
            getData();
        }
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
