package com.example.a.app10.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.a.app10.Adapter.NewsAdapter;
import com.example.a.app10.R;
import com.example.a.app10.bean.NewsItem;
import com.example.a.app10.tool.MyInternet;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDetailActivity extends ToolBarBaseActivity implements View.OnClickListener {

    private LinearLayout llContent;
    private RecyclerView rvVideo,rvCourse;
    private List<NewsItem> list;
    private Button btnComment,btnLeave,btnOrder;
    private String expertId,name,content,indroduction,imageUrl;
    private OkHttpClient client;
    private ImageView image;
    private TextView tvName,tvContent,tvIntroduction;


    @Override
    protected int getSideMenu() {
        return R.layout.activity_science_side;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideDrawer();//禁用原侧滑菜单
        setMyTitle("专家详情");
        setLeftButton(R.drawable.back, new MyOnClickListener() {
            @Override
            public void onClick() {
                onBackPressed();
            }
        });

        llContent= (LinearLayout) findViewById(R.id.llContent);
        rvVideo= (RecyclerView) findViewById(R.id.rvVideo);
        rvCourse= (RecyclerView) findViewById(R.id.rvCourse);
        btnComment= (Button) findViewById(R.id.btnComment);
        btnComment.setOnClickListener(this);
        btnLeave= (Button) findViewById(R.id.btnLeave);
        btnLeave.setOnClickListener(this);
        btnOrder= (Button) findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(this);
        image= (ImageView) findViewById(R.id.image);
        tvContent= (TextView) findViewById(R.id.tvContent);
        tvName= (TextView) findViewById(R.id.tvName);
        tvIntroduction= (TextView) findViewById(R.id.tvIndroduction);
        client=new OkHttpClient();

        list=new ArrayList<>();

        expertId=getIntent().getStringExtra("expertId");
        imageUrl=getIntent().getStringExtra("imageUrl");

        new LoadTask().execute(null,null,null);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_professor_detail;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnComment:

                break;
            case R.id.btnLeave:
                break;
            case R.id.btnOrder:
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
            hideProgress();
            show();
        }
    }

    private void show() {
        rvVideo.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        NewsAdapter adapter1=new NewsAdapter(list,this);
        adapter1.setLisenter(new NewsAdapter.OnItenClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(ProfessorDetailActivity.this,NewsDetailActivity.class));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        NewsAdapter adapter2=new NewsAdapter(list,this);
        adapter2.setLisenter(new NewsAdapter.OnItenClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(ProfessorDetailActivity.this,NewsDetailActivity.class));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        rvVideo.setAdapter(adapter1);
        rvCourse.setLayoutManager(new LinearLayoutManager(this));//默认竖直列表
        rvCourse.setAdapter(adapter2);
        llContent.setVisibility(View.VISIBLE);

        Glide.with(this).load(imageUrl).into(image);
        tvName.setText(name);
        tvContent.setText("研究方向： "+content);
        tvIntroduction.setText(indroduction);
    }

    private void getData() {
        String url= MyInternet.MAIN_URL+"expert/get_expert_info?expertId="+expertId;
        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                try {
                    JSONObject object=new JSONObject(s);
                    name=object.getString("expertName");
                    content=object.getString("expertArea");
                    indroduction=object.getString("introduction");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
