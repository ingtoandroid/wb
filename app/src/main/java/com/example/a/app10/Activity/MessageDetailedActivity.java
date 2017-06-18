package com.example.a.app10.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a.app10.R;
import com.example.a.app10.bean.MessageDetail;
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

    private EditText ed;
    private String questionID;
    private String messager;
    private List<MessageDetail> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detailed);

        if(getIntent().hasExtra("questionID")){
            questionID = getIntent().getStringExtra("questionID");
        }

        list = new ArrayList<>();

    }

    private void sendMessage(){
        String strContent = ed.getText().toString().trim();
        Call call = Net.getInstance().getAskPurse(questionID,strContent);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String str_response = response.body().string();
                if(str_response.length() > 0){
                    JSONTokener jsonTokener = new JSONTokener(str_response);
                    String megs = null;
                    try {
                        JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                        megs = jsonObject.getString("megs");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(megs != null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MessageDetailedActivity.this,"追问成功",Toast.LENGTH_SHORT);
                            }
                        });
                    }
                }
            }
        });
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
                MessageDetail messageDetail = new MessageDetail();

                if(str_response.length() > 0){
                    JSONTokener jsonTokener = new JSONTokener(str_response);

                    try {
                        JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                        messager = jsonObject.getString("nickName");
                        messageDetail.setHeadImage(jsonObject.getString("filePath"));
                        messageDetail.setMessageContent(jsonObject.getString("questionContent"));
                        messageDetail.setMessageData(jsonObject.getString("questionData"));
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

                }
            }
        });
    }
}
