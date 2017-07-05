package com.example.a.app10.Activity;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
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
import com.example.a.app10.Adapter.ClassItem;
import com.example.a.app10.R;
import com.example.a.app10.bean.MyClassItem;
import com.example.a.app10.bean.MyCourse;
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
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CheckedOutputStream;

public class QuestionDetailedActivity extends AppCompatActivity {
    private EditText ed_question;
    private String questionID;
    private String questioner = "我";
    private String headImageUrl;
    private List<QuestionDetail> list;
    private List<MyCourse> courses;
    private RecyclerView recyclerView;
    private ImageButton imageView;
    private Button askPurse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detailed);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if(getIntent().hasExtra("questionID")){
            questionID = getIntent().getStringExtra("questionID");
        }

        imageView = (ImageButton)findViewById(R.id.back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ed_question = (EditText)findViewById(R.id.edit_question);

        askPurse = (Button)findViewById(R.id.askPurse);
        askPurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendQuestion();
            }
        });
        list = new ArrayList<>();
        getQuestionHistory();

//        courses = new ArrayList<>();



//        QuestionDetail questionDetail = new QuestionDetail();
//        questionDetail.setType("追问");
//        questionDetail.setQuestionContent("hahaha");
//        questionDetail.setQuestionData("2017/5/7 18:00");
//        list.add(questionDetail);
//
//        QuestionDetail questionDetail1 = new QuestionDetail();
//        questionDetail1.setExpertname("yiyaya");
//        questionDetail1.setType("回复");
//        questionDetail1.setQuestionContent("hahaha");
//        questionDetail1.setQuestionData("2017/5/7 18:00");
//        list.add(questionDetail1);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter());
//        askPursue();
//        getMyCourse();

    }

    private void getQuestionHistory(){
        Call call = Net.getInstance().getQuestionDatails(questionID);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String str_response = response.body().string();
                QuestionDetail questionDetail = new QuestionDetail();

                if(str_response.length() > 0){
                    JSONTokener jsonTokener = new JSONTokener(str_response);

                    try {
                        JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                        questioner = jsonObject.getString("nickName");
                        headImageUrl = URLString.path_head_image+jsonObject.getString("filePath");
                        questionDetail.setHeadImage(URLString.path_head_image+jsonObject.getString("filePath"));
                        questionDetail.setQuestionContent(jsonObject.getString("questionContent"));
                        questionDetail.setQuestionData(jsonObject.getString("questionDate"));
                        questionDetail.setType("追问");
                        list.add(questionDetail);
                        JSONArray jsonArray = jsonObject.getJSONArray("dataList");
                        for(int i = 0;i<jsonArray.length();i++){
                            QuestionDetail qDetail = new QuestionDetail();
                            JSONObject jObject = (JSONObject)jsonArray.get(i);
                            qDetail.setExpertname(jObject.getString("expertName"));
                            qDetail.setQuestionData(jObject.getString("answerDate"));
                            qDetail.setQuestionContent(jObject.getString("answer"));
                            qDetail.setHeadImage(URLString.path_head_image+jObject.getString("filePath"));
                            qDetail.setType(jObject.getString("answerType"));
                            list.add(qDetail);
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


    private void sendQuestion(){
        final String strContent = ed_question.getText().toString().trim();
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
                                        ed_question.setText("");
                                        QuestionDetail questionDetail = new QuestionDetail();
                                        questionDetail.setType("追问");
                                        questionDetail.setQuestionContent(strContent);
                                        questionDetail.setHeadImage(headImageUrl);
                                        questionDetail.setQuestionData("刚刚");
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
            Toast.makeText(QuestionDetailedActivity.this,"消息不能为空",Toast.LENGTH_SHORT).show();
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyAdapter.MyViewHolder holder = new MyAdapter.MyViewHolder(LayoutInflater.from(QuestionDetailedActivity.this)
                    .inflate(R.layout.item_message_detail,parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {
            QuestionDetail questionDetail = list.get(position);
            if(questionDetail.getType().equals("追问")){
                Resources resource = (Resources) getBaseContext().getResources();
                ColorStateList csl_green = (ColorStateList) resource.getColorStateList(R.color.textGreen);
                ColorStateList csl_darkBlack = (ColorStateList) resource.getColorStateList(R.color.stringDark);
                ColorStateList csl_centerBlack = (ColorStateList) resource.getColorStateList(R.color.stringCenter);

                holder.relativeLayout.setBackgroundResource(R.drawable.tx_background);
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
                    holder.tx_sender.setText(questioner);
                    holder.tx_reseiver.setText(list.get(position).getExpertname());
                    holder.tx_type.setText(list.get(position).getType());
                }else{
                    holder.tx_sender.setText(questioner);
                    holder.tx_reseiver.setText("");
                    holder.tx_type.setText("");
                }

                TextPaint paint = holder.tx_sender.getPaint();
                paint.setFakeBoldText(true);

                paint = holder.tx_type.getPaint();
                paint.setFakeBoldText(true);

                paint = holder.tx_reseiver.getPaint();
                paint.setFakeBoldText(true);

            }else if(questionDetail.getType().equals("回复")){
                Resources resource = (Resources) getBaseContext().getResources();

                ColorStateList csl_white = (ColorStateList) resource.getColorStateList(R.color.textWhiteColor);

                holder.relativeLayout.setBackgroundResource(R.drawable.button_reservation);

                if(csl_white != null){
                    holder.tx_sender.setTextColor(csl_white);
                    holder.tx_reseiver.setTextColor(csl_white);
                    holder.tx_time.setTextColor(csl_white);
                    holder.tx_content.setTextColor(csl_white);
                    holder.tx_type.setTextColor(csl_white);
                }

                holder.tx_sender.setText(list.get(position).getExpertname());
                holder.tx_reseiver.setText(questioner);
                holder.tx_type.setText(list.get(position).getType());

            }

            Glide.with(QuestionDetailedActivity.this).load(list.get(position).getHeadImage()).into(holder.head_image);
            holder.tx_content.setText(list.get(position).getQuestionContent());
            holder.tx_time.setText(list.get(position).getQuestionData());

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
