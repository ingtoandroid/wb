package com.example.a.app10.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private static int numbersButton=18;
    private OkHttpClient client;
    private int[] sideButtonsIds={R.id.btn11,R.id.btn12,R.id.btn13,R.id.btn14,R.id.btn15,R.id.btn16,
            R.id.btn21,R.id.btn22,R.id.btn23,R.id.btn24,R.id.btn25,R.id.btn26,
            R.id.btn31,R.id.btn32,R.id.btn33,R.id.btn34,R.id.btn35,R.id.btn36};
    private Button[] sideButtons = new Button[numbersButton];
    private Button btnRecycle,btnSure;
    //储存选择结果的标志数组
    private boolean[] isChosen=new boolean[numbersButton];
    private EditText et;
    private Button btnSearch;
    private boolean finish;
    private ProfessorAdapter adapter;

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
        for (int i=0;i<numbersButton;i++){
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
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        new LoadTask().execute(null,null,null);

    }

    private void getData() {
        finish=false;
        String url= MyInternet.MAIN_URL+"expert/get_expert_list";
        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                try {
                    JSONObject obj =new JSONObject(s);
                    JSONArray array=obj.getJSONArray("dataList");
                    for (int i=0;i<array.length();i++){
                        JSONObject object=array.getJSONObject(i);
                        ProfessorItem item=new ProfessorItem(object.getString("imageUrl"),
                                object.getString("expertName"),
                                "研究方向： "+object.getString("expertArea"),
                                object.getString("expertId"),5);
                        list.add(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish=true;
            }
        });
        while (!finish){

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
        for (int i=0;i<numbersButton;i++){
            if (currentId==sideButtonsIds[i]){
                if (isChosen[i]==false){
                    Log.v("tag","dwdfvdvreve");
                    Button button= (Button) view;
                    button.setBackgroundResource(R.drawable.button_side_press);
                    button.setTextColor(Color.WHITE);
                    isChosen[i]=true;
                } else {
                    Button button= (Button) view;
                    button.setBackgroundResource(R.drawable.button_side);
                    button.setTextColor(getResources().getColor(R.color.main));
                    isChosen[i]=false;
                }
            }
        }

        if (currentId==R.id.btnRecycle){
            recycle();
        }

        if (currentId==R.id.btnSure){
            reLoad();
        }

        if (currentId==R.id.btnSearch){
            search();
        }
    }

    private void search() {//搜索专家
        String s=et.getText().toString();
        if (s.length()>0){
            Toast.makeText(this,"搜索"+s,Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"不能为空",Toast.LENGTH_SHORT).show();
        }
    }

    private void recycle() {//筛选条件复原
        for (int i=0;i<numbersButton;i++){
            Button button=sideButtons[i];
            button.setBackgroundResource(R.drawable.button_side);
            button.setTextColor(getResources().getColor(R.color.main));
            isChosen[i]=false;
        }
    }

    //根据筛选条件重新加载列表
    private void reLoad() {
        recycle();//筛选条件复原
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

    //配置并显示列表
    public void showRecycler(){
        hideProgress();
        adapter.notifyDataSetChanged();//加载后刷新
        rv.setVisibility(View.VISIBLE);
    }
}
