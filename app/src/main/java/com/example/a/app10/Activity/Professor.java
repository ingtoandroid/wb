package com.example.a.app10.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.a.app10.Adapter.ExpertAdapter;
import com.example.a.app10.R;
import com.example.a.app10.bean.ExpertSearchItem;
import com.example.a.app10.bean.MessageReminder;
import com.example.a.app10.tool.MyInternet;
import com.example.a.app10.tool.Net;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.QBadgeView;

public class Professor extends AppCompatActivity {
    private RecyclerView professor;
    private ExpertAdapter expertAdapter;
    private List<ExpertSearchItem> list;
    private OkHttpClient client;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor2);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initViews();
        initEvents();
        getData();
    }
    private void initViews(){
        professor=(RecyclerView)findViewById(R.id.professor_recyclerview);
        list=new ArrayList<>();
        client=new OkHttpClient();
    }
    private void initEvents(){
        expertAdapter=new ExpertAdapter(Professor.this,list);
        professor.setLayoutManager(new GridLayoutManager(Professor.this,4));
        professor.setAdapter(expertAdapter);
    }

    private void getData() {
        String url= MyInternet.MAIN_URL+"expert/get_expert_area";
        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                try {
                    JSONObject all=new JSONObject(s);
                    JSONArray array=all.getJSONArray("yjfxList");
                    for (int i=0;i<array.length();i++){
                        JSONObject obj=array.getJSONObject(i);
                        list.add(new ExpertSearchItem(obj.getString("key"),
                                obj.getString("value"),obj.getString("filePath")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void mainThread() {
                show();
            }
        },this);
    }

    private void show() {
        hideProgress();
        expertAdapter=new ExpertAdapter(Professor.this,list);
        expertAdapter.setItemOnClickListener(new ExpertAdapter.ItemOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent=new Intent(Professor.this,ProfessorActivity.class);
                intent.putExtra("expertArea",list.get(position).getValue());
                startActivity(intent);
            }
        });
        professor.setAdapter(expertAdapter);
    }

    //显示进度对话框
    protected void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(true);
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }
    protected void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
