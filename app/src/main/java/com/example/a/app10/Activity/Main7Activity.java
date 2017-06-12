package com.example.a.app10.Activity;

import android.media.Image;
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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Main7Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<MyData> datas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main7);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        datas = new ArrayList<>();
        MyData myData = new MyData();
        myData.setUsername("nihao");
        myData.setContent("hello");
        MyData myData1 = new MyData();
        myData1.setUsername("a");
        myData1.setContent("a");
        datas.add(myData);
        datas.add(myData1);
        recyclerView = (RecyclerView)findViewById(R.id.question_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter());
       // recyclerView.addItemDecoration();
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(Main7Activity.this)
                    .inflate(R.layout.item_question,parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
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
                headImage = (ImageView)view.findViewById(R.id.head_image_questions);
                usernameText = (TextView)view.findViewById(R.id.username_questions);
                contentText = (TextView)view.findViewById(R.id.content_questions);
                getInImage = (ImageView)view.findViewById(R.id.get_into_question);
            }
        }
    }
}
