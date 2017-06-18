package com.example.a.app10.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private static int NUMBERBUTTONS =27;
    private OkHttpClient client;
    private int[] sideButtonsIds={R.id.btn11,R.id.btn12,R.id.btn13,R.id.btn14,R.id.btn15,R.id.btn16,R.id.btn17,R.id.btn18,R.id.btn19,
            R.id.btn21,R.id.btn22,R.id.btn23,R.id.btn24,R.id.btn25,R.id.btn26,R.id.btn27,R.id.btn28,R.id.btn29,
            R.id.btn31,R.id.btn32,R.id.btn33,R.id.btn34,R.id.btn35,R.id.btn36,R.id.btn37,R.id.btn38,R.id.btn39,};
    private Button[] sideButtons = new Button[NUMBERBUTTONS];
    private Button btnRecycle,btnSure;
    //储存选择结果的标志数组
    private boolean[] isChosen=new boolean[NUMBERBUTTONS];
    private EditText et;
    private Button btnSearch;
    private ProfessorAdapter adapter;
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
        for (int i = 0; i< NUMBERBUTTONS; i++){
            sideButtons[i]= (Button) findViewById(sideButtonsIds[i]);
            sideButtons[i].setOnClickListener(this);

            isChosen[i]=false;
        }
        btnRecycle= (Button) findViewById(R.id.btnRecycle);
        btnRecycle.setOnClickListener(this);
        btnSure= (Button) findViewById(R.id.btnSure);
        btnSure.setOnClickListener(this);
        btnSearch= (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
        et= (EditText) findViewById(R.id.et);

        list=new ArrayList<>();
        tvSideTitle1= (TextView) findViewById(R.id.tvSideTitle1);
        tvSideTitle2= (TextView) findViewById(R.id.tvSideTitle2);
        tvSideTitle3= (TextView) findViewById(R.id.tvSideTitle3);
        client=new OkHttpClient();
        buttonTexts=new String[NUMBERBUTTONS];
        buttonCode=new String[NUMBERBUTTONS];
        adapter=new ProfessorAdapter(list,this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        new LoadTask().execute(null,null,null);

    }

    private void getData() {
        String url= MyInternet.MAIN_URL+"expert/get_expert_list";
        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
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
        for (int i=0;i<3;i++){
            int length=numbers[i]<6 ? numbers[i] :6;
            for (int j=0;j<length;j++){
                if (j==5){
                    //最后一个按钮的处理
                } else {
                    int index=i*EVEAY_MAX_LINE+j;
                    sideButtons[index].setVisibility(View.VISIBLE);
                    sideButtons[index].setText(buttonTexts[index]);
                }
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
                buttonTexts[EVEAY_MAX_LINE*0+i]=object.getString("key");
                buttonCode[EVEAY_MAX_LINE*0+i]=object.getString("value");
            }
            JSONArray array2=all.getJSONArray("sclyList");
            for (int i=0;i<array2.length();i++){
                JSONObject object=array2.getJSONObject(i);
                numbers[1]++;
                buttonTexts[EVEAY_MAX_LINE*1+i]=object.getString("key");
                buttonCode[EVEAY_MAX_LINE*1+i]=object.getString("value");
            }
            JSONArray array3=all.getJSONArray("zjlxList");
            for (int i=0;i<array3.length();i++){
                JSONObject object=array3.getJSONObject(i);
                numbers[2]++;
                buttonTexts[EVEAY_MAX_LINE*2+i]=object.getString("key");
                buttonCode[EVEAY_MAX_LINE*2+i]=object.getString("value");
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

        if (currentId==R.id.btnSearch){
            search();
            return;
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
        for (int i=0;i<EVEAY_MAX_LINE;i++){//研究方向
            if (isChosen[i]){
                a+="expertArea="+buttonCode[i]+"&";
            }
        }
        for (int i=EVEAY_MAX_LINE;i<EVEAY_MAX_LINE*2;i++){//擅长领域
            if (isChosen[i]){
                a+="goodField="+buttonCode[i]+"&";
            }
        }
        for (int i=EVEAY_MAX_LINE*2;i<EVEAY_MAX_LINE*3;i++){//专家类型
            if (isChosen[i]){
                a+="expertType="+buttonCode[i];
            }
        }
        if (a.length()>1){
            showProgress("加载中");
            reGetData(a);
        }

        closeDrawer();
        recycle();//筛选条件复原
    }

    private void reGetData(String a) {
        list=new ArrayList<>();
        String url=MyInternet.MAIN_URL+"expert/get_expert_page_list?"+a;
        Log.v("tag",url);
        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                try {
                    JSONObject all=new JSONObject(s);
                    JSONArray array=all.getJSONArray("datalist");
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
        //adapter.notifyDataSetChanged();
        rv.setVisibility(View.VISIBLE);
    }
}
