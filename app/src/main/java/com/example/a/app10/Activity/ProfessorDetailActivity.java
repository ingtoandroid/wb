package com.example.a.app10.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.a.app10.Adapter.ClassAdapter;
import com.example.a.app10.Adapter.ClassItem;
import com.example.a.app10.Adapter.VideoProAdapter;
import com.example.a.app10.R;
import com.example.a.app10.bean.ProfessorItem;
import com.example.a.app10.bean.VideoProItem;
import com.example.a.app10.tool.MyInternet;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDetailActivity extends ToolBarBaseActivity implements View.OnClickListener {

    private LinearLayout llContent;
    private RecyclerView rvVideo,rvCourse;
    private List<ClassItem> listClass;
    private List<VideoProItem> listVideo;
    private Button btnOrder;
    private String expertId,name,content,indroduction,imageUrl;
    private OkHttpClient client;
    private ImageView image,ivGrade;
    private TextView tvName,tvContent,tvIntroduction;
    private int expertGrade;
    private int[] grades={R.drawable.star0,R.drawable.star1,R.drawable.star2,R.drawable.star3,R.drawable.star4,R.drawable.star5};


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
        btnOrder= (Button) findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(this);
        image= (ImageView) findViewById(R.id.image);
//        ivGrade= (ImageView) findViewById(R.id.ivGrade);
        tvContent= (TextView) findViewById(R.id.tvContent);
        tvName= (TextView) findViewById(R.id.tvName);
        tvIntroduction= (TextView) findViewById(R.id.tvIndroduction);
        client=new OkHttpClient();

        listClass =new ArrayList<>();
        listVideo=new ArrayList<>();

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
            case R.id.btnOrder:
                Intent intent=new Intent(ProfessorDetailActivity.this,ExpertOrderActivity.class);
                ProfessorItem item=new ProfessorItem(imageUrl,name,content,expertId,expertGrade);
                Bundle bundle=new Bundle();
                bundle.putSerializable("professor",item);
                intent.putExtras(bundle);
                startActivity(intent);
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
        }
    }

    private void show() {
        hideProgress();
        rvVideo.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        VideoProAdapter adapter1=new VideoProAdapter(listVideo,this);
        adapter1.setLisenter(new VideoProAdapter.OnItenClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(ProfessorDetailActivity.this,VideoDetail.class);
                intent.putExtra("id",listVideo.get(position).getVideoId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        ClassAdapter adapter2=new ClassAdapter(listClass,this);
        adapter2.setLisenter(new ClassAdapter.OnItenClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(ProfessorDetailActivity.this,ClassDetailActivity.class);
                intent.putExtra("courseId",listClass.get(position).getCourseId());
                startActivity(intent);
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
//        ivGrade.setImageResource(grades[expertGrade]);
    }

    private void getData() {
        //获取专家信息
        String url= MyInternet.MAIN_URL+"expert/get_expert_info?expertId="+expertId;
        MyInternet.getMessage(url, client, new MyInternet.MyInterface() {
            @Override
            public void handle(String s) {
                handleJson(s);
            }

            @Override
            public void mainThread() {
                show();
            }
        },this);
    }

    private void handleJson(String s) {
        try {
            JSONObject object=new JSONObject(s);
            //专家信息
            name=object.getString("expertName");
            content=object.getString("expertArea");
            indroduction=object.getString("introduction");
            expertGrade=object.getInt("expertGrade");

            //课程列表
            JSONArray arrayList=object.getJSONArray("courseList");
            for (int i=0;i<arrayList.length();i++){
                JSONObject obj=arrayList.getJSONObject(i);
                ClassItem item=new ClassItem(obj.getString("imageUrl"),
                        obj.getString("courseId"),obj.getString("courseTitle"),
                        obj.getString("startDate"),obj.getString("entereNum"));
                listClass.add(item);
            }
            //视频列表
            JSONArray array=object.getJSONArray("videoList");
            for (int i=0;i<array.length();i++){
                JSONObject obj=array.getJSONObject(i);
                Log.v("tag",array.toString());
                VideoProItem item=new VideoProItem(obj.getString("videoId"),
                        obj.getString("videoTitle"), obj.getString("playNum"),
                        obj.getString("videoType"),obj.getString("imageUrl"));
                listVideo.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
