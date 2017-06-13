package com.example.a.app10.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a.app10.R;
import com.example.a.app10.bean.MyPoint;

import java.util.ArrayList;
import java.util.List;

public class MyPointsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<MyPoint> datas;
    private ImageView back_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_my_points2);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        datas = new ArrayList();
        MyPoint myPoint = new MyPoint();
        myPoint.setItem_content("成功登录悦健体育");
        myPoint.setYears_and_months("2017.04.14");
        myPoint.setTime("18:30:26");
        datas.add(myPoint);

        MyPoint myPoint1 = new MyPoint();
        myPoint1.setItem_content("成功登录悦健体");
        myPoint1.setYears_and_months("2017.04.14");
        myPoint1.setTime("18:30:26");
        datas.add(myPoint1);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview_point);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter());

        back_image = (ImageView)findViewById(R.id.back);
        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
            holder.item_content_title.setText(myPoint.getItem_content_title());
            holder.years_and_months.setText(myPoint.getYears_and_months());
            holder.times.setText(myPoint.getTime());
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView item_content_title;
            TextView years_and_months;
            TextView times;
            TextView point;

            public MyViewHolder(View view) {
                super(view);
                item_content_title = (TextView) view.findViewById(R.id.itme_content_title);
                years_and_months = (TextView) view.findViewById(R.id.years_and_months);
                times = (TextView) view.findViewById(R.id.times);
            }
        }
    }
}
