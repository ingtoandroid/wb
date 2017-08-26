package com.example.a.app10.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.example.a.app10.R;
import com.example.a.app10.bean.MyData;
import com.example.a.app10.bean.MyMessage;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class QuestionActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<MyData> datas;
    private ImageButton back_question;
    private LinearLayout line_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_question);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        datas = new ArrayList<>();
        initData();
//        MyData myData = new MyData();
//        myData.setContent("123");
//        myData.setHeadImageURL("233");
//        myData.setUsername("333");
//        datas.add(myData);
        recyclerView = (RecyclerView)findViewById(R.id.question_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter());
        back_question = (ImageButton) findViewById(R.id.back_question);
        back_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        line_back = (LinearLayout)findViewById(R.id.line_back);
        line_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData(){

        Call call = Net.getInstance().getQuestion(Net.getPersonID());
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
                        MyData myData = new MyData();
                        myData.setContent(((JSONObject)jsonArray.get(i)).getString("questionContent"));
                        myData.setHeadImageURL(((JSONObject)jsonArray.get(i)).getString("filePath"));
                        myData.setQuestionID(((JSONObject) jsonArray.get(i)).getString("questionId"));
                        datas.add(myData);
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
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(QuestionActivity.this)
                    .inflate(R.layout.item_question,parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.usernameText.setText(datas.get(position).getUsername());
            holder.contentText.setText(datas.get(position).getContent());
            String str_url = datas.get(position).getHeadImageURL();
//            String str_url = datas.get(position).getHeadImageURL();
//            String str_url = URLString.path_head_image+"lemon/fileDownload?fileName=associator/20170615/1111.png";
            Glide.with(QuestionActivity.this).load(str_url).into(holder.headImage);
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(QuestionActivity.this,QuestionDetailedActivity.class);
                    intent.putExtra("questionID",datas.get(position).getQuestionID());
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
//            CircleImageView circleImageView;
            public MyViewHolder(View view){
                super(view);
//                circleImageView = (CircleImageView)findViewById(R.id.head_image_questions);
                headImage = (ImageView)view.findViewById(R.id.head_image_questions);
                usernameText = (TextView)view.findViewById(R.id.username_questions);
                contentText = (TextView)view.findViewById(R.id.content_questions);
                getInImage = (ImageView)view.findViewById(R.id.get_into_question);
                relativeLayout = (RelativeLayout) view.findViewById(R.id.item_questions);
            }
        }
    }


}
