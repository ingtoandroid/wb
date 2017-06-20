package com.example.a.app10.Activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a.app10.R;
import com.example.a.app10.bean.MyReservation;
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

public class MyReservationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<MyReservation> datas;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_my_reservation);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        back = (ImageView)findViewById(R.id.back_reservation);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        datas = new ArrayList<>();
        initData();
//        MyReservation myReservation = new MyReservation();
//        myReservation.setItem_content_reservation("在这里预约内容");
//        myReservation.setItem_username_reservation("今天有雨");
//        myReservation.setItem_time_reservation("18:00-18:30");
//        datas.add(myReservation);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview_reservation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter());
    }

    private void initData(){
        Call call = Net.getInstance().getMyReservation();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String str_response = response.body().string();
                JSONTokener jsonTokener = new JSONTokener(str_response);
                try {
                    JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                    JSONArray jsonArray = jsonObject.getJSONArray("order_new_type_list");
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject jObject = jsonArray.getJSONObject(i);
                        MyReservation myReservation = new MyReservation();
                        myReservation.setItem_content_reservation(jObject.getString("ordertitle"));
                        myReservation.setItem_username_reservation(jObject.getString("expertName"));
                        myReservation.setItem_time_reservation(jObject.getString("orderStartTime")+"-"+jObject.getString("orderEndTime"));
                        myReservation.setOrderContent(jObject.getString("orderContent"));
                        myReservation.setOrderDate(jObject.getString("orderDate"));
                        myReservation.setOrderId(jObject.getString("orderId"));
                        myReservation.setOrderType(jObject.getString("orderType"));
                        myReservation.setFilePath(jObject.getString("filePath"));
                        myReservation.setType(jObject.getInt("type"));
                        datas.add(myReservation);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(new MyAdapter());
                    }
                });
            }
        });
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private MyReservation myReservation;

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(MyReservationActivity.this)
                    .inflate(R.layout.item_reservation, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            myReservation = datas.get(position);
            holder.item_content_reservation.setText(myReservation.getItem_content_reservation());
            holder.item_username_reservation.setText(myReservation.getItem_username_reservation());
            holder.item_time_reservation.setText(myReservation.getItem_time_reservation());
            int str_type = myReservation.getType();

            if(str_type == 2){
                holder.item_consultation_reservation.setEnabled(false);
                holder.item_evaluate_reservation.setEnabled(false);

                holder.item_cancle_reservation.setBackgroundResource(R.drawable.button_reservation_disable);
                holder.item_evaluate_reservation.setBackgroundResource(R.drawable.button_reservation_disable);

//                Resources resource = (Resources) getBaseContext().getResources();
//
////                ColorStateList csl_gray = (ColorStateList) resource.getColorStateList(R.color.dividColor);
//                holder.item_consultation_reservation.setBackgroundColor(Color.rgb(225,225,225));
//                holder.item_evaluate_reservation.setBackgroundColor(Color.rgb(225,225,225));
            }
            else if(str_type == 0){
                holder.item_cancle_reservation.setEnabled(false);
                holder.item_evaluate_reservation.setEnabled(false);

                holder.item_cancle_reservation.setBackgroundResource(R.drawable.button_reservation_disable);
                holder.item_evaluate_reservation.setBackgroundResource(R.drawable.button_reservation_disable);

//                holder.item_cancle_reservation.setBackgroundColor(Color.rgb(225,225,225));
//                holder.item_evaluate_reservation.setBackgroundColor(Color.rgb(225,225,225));
            }
            else if(str_type == 1){
                holder.item_cancle_reservation.setEnabled(false);
                holder.item_consultation_reservation.setEnabled(false);

                holder.item_cancle_reservation.setBackgroundResource(R.drawable.button_reservation_disable);
                holder.item_consultation_reservation.setBackgroundResource(R.drawable.button_reservation_disable);
//                holder.item_cancle_reservation.setBackgroundColor(Color.rgb(225,225,225));
//                holder.item_consultation_reservation.setBackgroundColor(Color.rgb(225,225,225));
            }

            holder.item_consultation_reservation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyReservationActivity.this,chat.class);
                    intent.putExtra("name",datas.get(position).getItem_username_reservation());
                    intent.putExtra("filePath",datas.get(position).getFilePath());
                    startActivity(intent);
                }
            });

            holder.item_evaluate_reservation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyReservationActivity.this,Comment.class);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView item_content_reservation;
            TextView item_username_reservation;
            TextView item_time_reservation;
            Button item_cancle_reservation;
            Button item_consultation_reservation;
            Button item_evaluate_reservation;

            public MyViewHolder(View view) {
                super(view);
                item_content_reservation = (TextView) view.findViewById(R.id.item_content_reservation);
                item_username_reservation = (TextView) view.findViewById(R.id.item_username_reservation);
                item_time_reservation = (TextView) view.findViewById(R.id.item_time_reservation);
                item_cancle_reservation = (Button)view.findViewById(R.id.cancel_reservation);
                item_consultation_reservation = (Button)view.findViewById(R.id.consultation);
                item_evaluate_reservation = (Button)view.findViewById(R.id.evaluate);
            }
        }
    }
}
