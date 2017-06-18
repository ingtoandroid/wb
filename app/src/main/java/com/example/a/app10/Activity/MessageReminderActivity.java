package com.example.a.app10.Activity;

import android.provider.ContactsContract;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a.app10.R;
import com.example.a.app10.bean.MessageReminder;
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

public class MessageReminderActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<MessageReminder> list;
    TextView deleteText;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_reminder);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        Net.setMegsSize(0);

        deleteText = (TextView)findViewById(R.id.deleteMessage);
        deleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMessageList();
            }
        });

        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list = new ArrayList<>();
        getMessageList();
//        MessageReminder messageReminder = new MessageReminder();
//        messageReminder.setCreateTime_sys("2017.6.7 12:00");
//        messageReminder.setRemindCount("nihao");
//        list.add(messageReminder);
//        MessageReminder messageReminder1 = new MessageReminder();
//        messageReminder1.setCreateTime_sys("2017.6.7 12:00");
//        messageReminder1.setRemindCount("nihao");
//        list.add(messageReminder1);
        recyclerView = (RecyclerView)findViewById(R.id.message_reminder_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter());
//        deleteMessageList();

    }

    private void getMessageList(){
        Call call = Net.getInstance().getMessageList(Net.getPersonID());
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
                    JSONArray jsonArray = jsonObject.getJSONArray("datalist");
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject item = jsonArray.getJSONObject(i);
                        MessageReminder messageReminder = new MessageReminder();
                        messageReminder.setRemindCount(item.getString("remindCount"));
                        messageReminder.setCreateTime_sys(item.getString("createTime_sys"));
                        list.add(messageReminder);
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

    private void deleteMessageList(){
        final Call call = Net.getInstance().deleteMessageList(Net.getPersonID());
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
                    int code = jsonObject.getInt("code");
                    if(code == 1){
                        //清空成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MessageReminderActivity.this,"清空成功",Toast.LENGTH_SHORT).show();
                                list = new ArrayList<MessageReminder>();
                                recyclerView.setAdapter(new MyAdapter());
                            }
                        });
                    }
                    else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MessageReminderActivity.this,"清空失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyAdapter.MyViewHolder holder = new MyAdapter.MyViewHolder(LayoutInflater.from(MessageReminderActivity.this)
                    .inflate(R.layout.item_message_reminder,parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {
            holder.time.setText(list.get(position).getCreateTime_sys());
            holder.content.setText(list.get(position).getRemindCount());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView time;
            TextView content;
            public MyViewHolder(View view){
                super(view);
                time = (TextView)view.findViewById(R.id.message_time);
                content = (TextView)view.findViewById(R.id.message_detail_content);
            }
        }
    }
}
