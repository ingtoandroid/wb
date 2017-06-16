package com.example.a.app10.tool;

import com.example.a.app10.bean.URLString;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

/**
 * Created by 12917 on 2017/6/15.
 */

public class Net {
    private static Net net;
    private OkHttpClient okHttpClient;
    private Net(){
        okHttpClient=new OkHttpClient();
    }
    public static synchronized Net getInstance(){
        if(net==null)
            net=new Net();
        return net;
    }

    /*
    * usrAPI*/
    public Call login(String name,String password){
        String url= URLString.login+"?"+"username="+name+"&"+"password="+password;
        return get(url);
    }



    /*
    *视频*/
    public Call shipinList(int index,int type){
        /*
        * index表示第几页
        * type 1表示视频，2表示直播*/

        String url=URLString.shipinlist+"?"+"pageIndex="+index+"&"+"videoType="+type;
        return get(url);
    }
    public Call shipinDetail(String  id){
        String url=URLString.shipin_detail+"?"+"videoId="+id;
        return get(url);
    }
    public Call getComment(String id){
        String url=URLString.getComment+"?videoId="+id;
        return get(url);
    }
    public Call getTiwen(String id){
        String url=URLString.getTiwen+"?videoId="+id;
        return get(url);
    }


    public Call get(String url){
        Request request=new Request.Builder().url(url).build();
        return  okHttpClient.newCall(request);
    }
    private Call post(String url, RequestBody requestBody){
        Request request=new Request.Builder().url(url).method("POST",requestBody).build();
        return okHttpClient.newCall(request);
    }

}
