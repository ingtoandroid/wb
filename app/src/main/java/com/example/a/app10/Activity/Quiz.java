package com.example.a.app10.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.baidu.platform.comapi.map.N;
import com.example.a.app10.R;
import com.example.a.app10.bean.QuestionDetail;
import com.example.a.app10.bean.QuestionItem;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Quiz extends AppCompatActivity {
    private Toolbar toolbar;
    private List<QuestionItem> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        init();
//        getQuestionList();
        sendQuestion();
    }
    private void init(){
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        list = new ArrayList<>();
    }

    //快速提问
    private void sendQuestion(){
        Call call = Net.getInstance().quickQuestion("562889f7-a858-4a4b-9ed1-70d23a100e95","吃了么");
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
                    if(code == 1) {
                        //成功
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //获取提问内容
    private void getQuestionList(){
        Call call = Net.getInstance().getQuestionList("562889f7-a858-4a4b-9ed1-70d23a100e95");
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
                    for(int i = 0 ;i<jsonArray.length();i++){
                        JSONObject item = jsonArray.getJSONObject(i);
                        QuestionItem questionItem = new QuestionItem();
                        questionItem.setQuestionID(item.getString("questionId"));
                        questionItem.setQuestionTitle(item.getString("questionTitle"));
                        questionItem.setUsername(item.getString("userName"));
                        questionItem.setCreateTime_sys(item.getString("createTime_sys"));
                        questionItem.setPhotoUrl(item.getString("photoUrl"));
                        list.add(questionItem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                list.get(0);
            }
        });
    }
}
