package com.example.a.app10.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a.app10.R;
import com.example.a.app10.tool.Net;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

public class QuickQuestionActivity extends ToolBarBaseActivity {

    private EditText et;

    @Override
    protected int getSideMenu() {
        return R.layout.activity_science_side;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        et= (EditText) findViewById(R.id.et);
        hideDrawer();
        setMyTitle("快速提问");
        setLeftButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setRightButton(0, "发送", new MyOnClickListener() {
            @Override
            public void onClick() {
                sendQuestion();
            }
        });
    }

    private void sendQuestion(){
        Call call = Net.getInstance().quickQuestion(Net.getPersonID(),et.getText().toString());
        et.setText("");
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(QuickQuestionActivity.this,"连接失败",Toast.LENGTH_SHORT).show();
                onBackPressed();
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(QuickQuestionActivity.this,"发送成功",Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(QuickQuestionActivity.this,"发送失败",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_quick_question;
    }
}
