package com.example.a.app10.Activity;

import android.icu.text.IDNA;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a.app10.R;
import com.example.a.app10.bean.MyPoint;
import com.example.a.app10.bean.URLString;
import com.example.a.app10.tool.Net;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyPointsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<MyPoint> datas;
    private ImageButton back_image;
    private Handler handler = new Handler();
    private TextView tx_point;
    private String integral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_my_points2);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        tx_point = (TextView)findViewById(R.id.point_tx);
        initDatas();
//        datas = new ArrayList<>();
//        MyPoint myPoint = new MyPoint();
//        myPoint.setItem_content_title("成功登录悦健体育");
//        myPoint.setYears_and_months("2017.04.14 18:30:26");
//        datas.add(myPoint);
//
//        MyPoint myPoint1 = new MyPoint();
//        myPoint1.setItem_content_title("成功登录悦健体");
//        myPoint1.setYears_and_months("2017.04.14 18:30:26");
//        datas.add(myPoint1);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview_point);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter());

        back_image = (ImageButton) findViewById(R.id.back);
        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initDatas(){
        datas = new ArrayList<>();
        getHistory();
    }

    private void getHistory(){
        Call call = Net.getInstance().getIntegralHistory(Net.getPersonID());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String strResponse = response.body().string();
                JSONTokener jsonTokener = new JSONTokener(strResponse);
                try {
                    JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                    integral = jsonObject.getString("integral");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tx_point.setText(integral);
                        }
                    });
                    JSONArray jsonArray = jsonObject.getJSONArray("jifenHistoryList");
                    for(int i = 0;i<jsonArray.length();i++){
                        MyPoint myPoint = new MyPoint();
                        myPoint.setItem_content_title(((JSONObject)jsonArray.get(i)).getString("busName"));
                        myPoint.setPoint(((JSONObject)jsonArray.get(i)).getString("jifen"));
                        myPoint.setYears_and_months(((JSONObject)jsonArray.get(i)).getString("createTime_sys"));
                        datas.add(myPoint);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(new MyAdapter());
                    }
                });

            }
        });
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private MyPoint myPoint;

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(MyPointsActivity.this)
                    .inflate(R.layout.item_point, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            myPoint = datas.get(position);
            if(!myPoint.getItem_content_title().equals("null")) {
                holder.item_content_title.setText(myPoint.getItem_content_title());
            }
            if(!myPoint.getYears_and_months().equals("null")) {
                holder.years_and_months.setText(myPoint.getYears_and_months());
            }
            holder.point.setText(myPoint.getPoint());
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView item_content_title;
            TextView years_and_months;
            TextView point;

            public MyViewHolder(View view) {
                super(view);
                item_content_title = (TextView) view.findViewById(R.id.itme_content_title);
                years_and_months = (TextView) view.findViewById(R.id.years_and_months_and_time);
                point = (TextView)view.findViewById(R.id.point);
            }
        }
    }
}
