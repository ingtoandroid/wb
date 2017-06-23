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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a.app10.R;
import com.example.a.app10.bean.MessageReminder;
import com.example.a.app10.bean.MyData;
import com.example.a.app10.bean.MyMessage;
import com.example.a.app10.bean.URLString;
import com.example.a.app10.tool.Net;
import com.google.gson.Gson;
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

import q.rorbin.badgeview.QBadgeView;

public class MyMessageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<MyMessage> datas;
    private ImageView back_message,ivMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);



        datas = new ArrayList<>();
        initDatas();
//        MyMessage myMessage = new MyMessage();
//        myMessage.setUsername("nihao");
//        myMessage.setContent("hello");
//        MyMessage myMessage1 = new MyMessage();
//        myMessage1.setUsername("a");
//        myMessage1.setContent("a");
//        datas.add(myMessage);
//        datas.add(myMessage1);

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
        ivMessage= (ImageView) findViewById(R.id.ivMessage);
        new QBadgeView(this).bindTarget(ivMessage).setBadgeNumber(Net.getMegsSize());
        ivMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyMessageActivity.this, MessageReminderActivity.class));
            }
        });
    }

    private void initDatas(){
        datas = new ArrayList<>();
        Call call = Net.getInstance().getMessage(Net.getPersonID());
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
                    JSONArray jsonArray = jsonObject.getJSONArray("jifen_new_type_list");
                    for(int i = 0; i<jsonArray.length(); i++){
                        MyMessage myMessage = new MyMessage();
                        myMessage.setMessageID(((JSONObject)jsonArray.get(i)).getString("questionId"));
                        myMessage.setContent(((JSONObject)jsonArray.get(i)).getString("questionContent"));
                        myMessage.setHeadImageURL(((JSONObject)jsonArray.get(i)).getString("filePath"));
                        datas.add(myMessage);
                    }
//
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
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyAdapter.MyViewHolder holder = new MyAdapter.MyViewHolder(LayoutInflater.from(MyMessageActivity.this)
                    .inflate(R.layout.item_message,parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, final int position) {
            holder.usernameText.setText(datas.get(position).getUsername());
            holder.contentText.setText(datas.get(position).getContent());
            String str_url = URLString.path_head_image+datas.get(position).getHeadImageURL();
//            String str_url = URLString.path_head_image+"lemon/fileDownload?fileName=associator/20170615/1111.png";
            Glide.with(MyMessageActivity.this).load(str_url).into(holder.headImage);
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyMessageActivity.this,MessageDetailedActivity.class);
                    intent.putExtra("questionID",datas.get(position).getMessageID());
                    startActivity(intent);
                }
            });
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
            RelativeLayout relativeLayout;
            public MyViewHolder(View view){
                super(view);
                headImage = (ImageView)view.findViewById(R.id.head_image_message);
                usernameText = (TextView)view.findViewById(R.id.username_message);
                contentText = (TextView)view.findViewById(R.id.content_message);
                getInImage = (ImageView)view.findViewById(R.id.get_into_message);
                relativeLayout = (RelativeLayout) view.findViewById(R.id.item_messages);
            }
        }
    }
}
