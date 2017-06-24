package com.example.a.app10.Activity;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a.app10.R;
import com.example.a.app10.bean.MessageDetail;
import com.example.a.app10.bean.QuestionDetail;
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
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class MessageDetailedActivity extends AppCompatActivity {

    private ImageButton back;
    private EditText ed_message;
    private String questionID;
    private String messager;
    private String headImageUrl;
    private List<MessageDetail> list;
    private RecyclerView recyclerView;
    private Button bu_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detailed);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if(getIntent().hasExtra("questionID")){
            questionID = getIntent().getStringExtra("questionID");
        }

        list = new ArrayList<>();

        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bu_send = (Button)findViewById(R.id.askPurse);
        bu_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendQuestion();
            }
        });

//        MessageDetail questionDetail = new MessageDetail();
//        questionDetail.setType("追问");
//        questionDetail.setMessageContent("hahaha");
//        questionDetail.setMessageData("2017/5/7 18:00");
//        list.add(questionDetail);
//
//        MessageDetail questionDetail1 = new MessageDetail();
//        questionDetail1.setExpertname("yiyaya");
//        questionDetail1.setType("回复");
//        questionDetail1.setMessageContent("hahaha");
//        questionDetail1.setMessageData("2017/5/7 18:00");
//        list.add(questionDetail1);
        ed_message = (EditText)findViewById(R.id.edit_question);
        getMessageHistory();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter());
    }

    private void sendQuestion(){
        final String strContent = ed_message.getText().toString().trim();
        if(strContent.length() > 0) {
            Call call = Net.getInstance().getAskPurse(questionID, strContent);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String str_response = response.body().string();
                    if (str_response.length() > 0) {
                        JSONTokener jsonTokener = new JSONTokener(str_response);
                        String megs = null;
                        try {
                            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                            megs = jsonObject.getString("megs");
                            if (megs.equals("追问成功")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ed_message.setText("");
                                        MessageDetail questionDetail = new MessageDetail();
                                        questionDetail.setType("追问");
                                        questionDetail.setMessageContent(strContent);
                                        questionDetail.setHeadImage(headImageUrl);
                                        questionDetail.setMessageData("刚刚");
                                        list.add(questionDetail);
                                        recyclerView.setAdapter(new MyAdapter());
                                        recyclerView.scrollToPosition(list.size()-1);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        else{
            Toast.makeText(MessageDetailedActivity.this,"消息不能为空",Toast.LENGTH_SHORT).show();
        }
    }


    private void getMessageHistory(){
        Call call = Net.getInstance().getQuestionDatails(questionID);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String str_response = response.body().string();
                MessageDetail messageDetail = new MessageDetail();

                if(str_response.length() > 0){
                    JSONTokener jsonTokener = new JSONTokener(str_response);

                    try {
                        JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                        messager = jsonObject.getString("nickName");
                        headImageUrl = URLString.path_head_image+jsonObject.getString("filePath");
                        messageDetail.setHeadImage(URLString.path_head_image+jsonObject.getString("filePath"));
                        messageDetail.setMessageContent(jsonObject.getString("questionContent"));
                        messageDetail.setMessageData(jsonObject.getString("questionDate"));
                        messageDetail.setType("追问");
                        list.add(messageDetail);
                        JSONArray jsonArray = jsonObject.getJSONArray("dataList");
                        for(int i = 0;i<jsonArray.length();i++){
                            MessageDetail mDetail = new MessageDetail();
                            JSONObject jObject = (JSONObject)jsonArray.get(i);
                            mDetail.setExpertname(jObject.getString("expertName"));
                            mDetail.setMessageData(jObject.getString("answerDate"));
                            mDetail.setMessageContent(jObject.getString("answer"));
                            mDetail.setHeadImage(jObject.getString("filePath"));
                            mDetail.setType(jObject.getString("answerType"));
                            list.add(mDetail);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(new MyAdapter());
                            recyclerView.scrollToPosition(list.size()-1);
                        }
                    });

                }
            }
        });
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyAdapter.MyViewHolder holder = new MyAdapter.MyViewHolder(LayoutInflater.from(MessageDetailedActivity.this)
                    .inflate(R.layout.item_message_detail,parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {
            MessageDetail messageDetail = list.get(position);
            if(messageDetail.getType().equals("追问")){
                Resources resource = (Resources) getBaseContext().getResources();
                ColorStateList csl_green = (ColorStateList) resource.getColorStateList(R.color.textGreen);
                ColorStateList csl_darkBlack = (ColorStateList) resource.getColorStateList(R.color.stringDark);
                ColorStateList csl_centerBlack = (ColorStateList) resource.getColorStateList(R.color.stringCenter);

                if (csl_green != null) {
                    holder.tx_sender.setTextColor(csl_green);
                    holder.tx_reseiver.setTextColor(csl_green);
                }

                if(csl_centerBlack != null){
                    holder.tx_content.setTextColor(csl_centerBlack);
                    holder.tx_time.setTextColor(csl_centerBlack);
                }
                if(csl_darkBlack != null){
                    holder.tx_type.setTextColor(csl_darkBlack);
                }

                if(position != 0) {
                    holder.tx_sender.setText(messager);
                    holder.tx_reseiver.setText(list.get(position).getExpertname());
                    holder.tx_type.setText(list.get(position).getType());
                }else{
                    holder.tx_sender.setText(messager);
                    holder.tx_reseiver.setText("");
                    holder.tx_type.setText("");
                }

            }else if(messageDetail.getType().equals("回复")){
                Resources resource = (Resources) getBaseContext().getResources();

                ColorStateList csl_white = (ColorStateList) resource.getColorStateList(R.color.textWhiteColor);

                holder.relativeLayout.setBackgroundResource(R.drawable.backgroundcolor);
                if(csl_white != null){
                    holder.tx_sender.setTextColor(csl_white);
                    holder.tx_reseiver.setTextColor(csl_white);
                    holder.tx_time.setTextColor(csl_white);
                    holder.tx_content.setTextColor(csl_white);
                    holder.tx_type.setTextColor(csl_white);
                }

                holder.tx_sender.setText(list.get(position).getExpertname());
                holder.tx_reseiver.setText(messager);
                holder.tx_type.setText(list.get(position).getType());
            }

            Glide.with(MessageDetailedActivity.this).load(list.get(position).getHeadImage()).into(holder.head_image);
            holder.tx_content.setText(list.get(position).getMessageContent());
            holder.tx_time.setText(list.get(position).getMessageData());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            RelativeLayout relativeLayout;
            TextView tx_sender;
            TextView tx_reseiver;
            TextView tx_type;
            TextView tx_content;
            TextView tx_time;
            ImageView head_image;

            public MyViewHolder(View itemView) {
                super(itemView);
                relativeLayout = (RelativeLayout)itemView.findViewById(R.id.content_info);
                tx_sender = (TextView)itemView.findViewById(R.id.sender_username);
                tx_reseiver = (TextView)itemView.findViewById(R.id.receiver_username);
                tx_content = (TextView)itemView.findViewById(R.id.content);
                tx_type = (TextView)itemView.findViewById(R.id.type);
                tx_time = (TextView)itemView.findViewById(R.id.time);
                head_image = (ImageView)itemView.findViewById(R.id.head_image_message_item);
            }
        }
    }
}
