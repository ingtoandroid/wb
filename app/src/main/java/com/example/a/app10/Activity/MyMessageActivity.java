package com.example.a.app10.Activity;

import android.content.Intent;
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
import com.example.a.app10.bean.MyData;
import com.example.a.app10.bean.MyMessage;

import java.util.ArrayList;
import java.util.List;

public class MyMessageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<MyMessage> datas;
    private ImageView back_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        datas = new ArrayList<>();
        MyMessage myMessage = new MyMessage();
        myMessage.setUsername("nihao");
        myMessage.setContent("hello");
        MyMessage myMessage1 = new MyMessage();
        myMessage1.setUsername("a");
        myMessage1.setContent("a");
        datas.add(myMessage);
        datas.add(myMessage1);
        recyclerView = (RecyclerView)findViewById(R.id.message_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter());
        back_message = (ImageView)findViewById(R.id.back_messaage);
        back_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyAdapter.MyViewHolder holder = new MyAdapter.MyViewHolder(LayoutInflater.from(MyMessageActivity.this)
                    .inflate(R.layout.item_message,parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {
            holder.usernameText.setText(datas.get(position).getUsername());
            holder.contentText.setText(datas.get(position).getContent());
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView headImage;
            TextView usernameText;
            TextView contentText;
            ImageView getInImage;

            public MyViewHolder(View view){
                super(view);
                headImage = (ImageView)view.findViewById(R.id.head_image_message);
                usernameText = (TextView)view.findViewById(R.id.username_message);
                contentText = (TextView)view.findViewById(R.id.content_message);
                getInImage = (ImageView)view.findViewById(R.id.get_into_message);
            }
        }
    }
}
