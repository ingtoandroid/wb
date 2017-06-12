package com.example.a.app10.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a.app10.R;
import com.example.a.app10.bean.MyReservation;

import java.util.ArrayList;
import java.util.List;

public class MyReservationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<MyReservation> datas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_my_reservation);
        datas = new ArrayList<>();
        MyReservation myReservation = new MyReservation();
        myReservation.setItem_content_reservation("在这里预约内容");
        myReservation.setItem_username_reservation("今天有雨");
        myReservation.setItem_time_reservation("18:00-18:30");
        datas.add(myReservation);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview_reservation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter());
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
        public void onBindViewHolder(MyViewHolder holder, int position) {
            myReservation = datas.get(position);
            holder.item_content_reservation.setText(myReservation.getItem_content_reservation());
            holder.item_username_reservation.setText(myReservation.getItem_username_reservation());
            holder.item_time_reservation.setText(myReservation.getItem_time_reservation());

        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView item_content_reservation;
            TextView item_username_reservation;
            TextView item_time_reservation;

            public MyViewHolder(View view) {
                super(view);
                item_content_reservation = (TextView) view.findViewById(R.id.item_content_reservation);
                item_username_reservation = (TextView) view.findViewById(R.id.item_username_reservation);
                item_time_reservation = (TextView) view.findViewById(R.id.item_time_reservation);
            }
        }
    }
}
