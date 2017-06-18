package com.example.a.app10.tool;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by lenovo on 2017/6/13.
 */

public class MyInternet {

    public static final String MAIN_URL="http://192.168.1.129:8080/yjtyms/yjty_App/";

    public static void getMessage(String url, OkHttpClient client, final MyInterface myInterface, final Activity context){
        Request request=new Request.Builder()
                .url(url)
                .build();
        Call call=client.newCall(request);//将请求封装成任务
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.v("tag","failed");
                //myInterface.failed();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result=response.body().string();
                myInterface.handle(result);
                (context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myInterface.mainThread();
                    }
                });
            }
        });
    }


    public interface MyInterface{
        void handle(String s);
        //void failed();
        void mainThread();
    }
}
