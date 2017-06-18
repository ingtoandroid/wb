package com.example.a.app10.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a.app10.Adapter.ClassItem;
import com.example.a.app10.R;
import com.example.a.app10.bean.MyClassItem;
import com.example.a.app10.bean.MyCourse;
import com.example.a.app10.bean.QuestionDetail;
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
    private EditText ed;
    private String questionID;
    private String questioner;
    private List<QuestionDetail> list;
    private List<MyCourse> courses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detailed);

        if(getIntent().hasExtra("questionID")){
            questionID = getIntent().getStringExtra("questionID");
        }

        list = new ArrayList<>();
        courses = new ArrayList<>();
        askPursue();
//        getMyCourse();
//        getQuestionHistory();
    }

    private void getQuestionHistory(){
        Call call = Net.getInstance().getQuestionDatails("85d3864a-4d58-4ba7-a6fb-8c22c1697e05");
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
                        questionDetail.setHeadImage(jsonObject.getString("filePath"));
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
                            qDetail.setHeadImage(jObject.getString("filePath"));
                            qDetail.setType(jObject.getString("answerType"));
                            list.add(qDetail);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    list.get(0);
                }
            }
        });
    }

    private void getMyCourse(){
        Call call = Net.getInstance().getMyCourse("9629e659-b37a-417f-90cd-1e3ffea7057b");
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
                    JSONArray jsonArray = jsonObject.getJSONArray("dataList");
                    for(int i = 0; i < jsonArray.length();i++){
                        JSONObject item = (JSONObject) jsonArray.get(i);
                        MyCourse myCourse = new MyCourse();
                        myCourse.setModelName(item.getString("modelName"));
                        myCourse.setCourseId(item.getString("courseId"));
                        myCourse.setCourseTitle(item.getString("courseTitle"));
                        myCourse.setStartData(item.getString("startDate"));
                        myCourse.setEnterId(item.getString("entereId"));
                        myCourse.setState(item.getString("state"));
                        courses.add(myCourse);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                courses.get(0);
            }
        });
    }

    private void askPursue(){
        Call call = Net.getInstance().getAskPurse("85d3864a-4d58-4ba7-a6fb-8c22c1697e05","haha");
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
                    String megs = jsonObject.getString("megs");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void sendQuestion(){
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
                                Toast.makeText(QuestionDetailedActivity.this,"追问成功",Toast.LENGTH_SHORT);
                            }
                        });
                    }
                }
            }
        });
    }
}
