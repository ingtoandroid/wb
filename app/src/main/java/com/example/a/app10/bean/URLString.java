package com.example.a.app10.bean;

/**
 * Created by 12917 on 2017/6/13.
 */

public class URLString {


    final static public String protocol = "http:";
    final static public String hostname = "192.168.1.135";
    final static public String port = "8080";
    final static public String project = "yjtyms/yjty_App/";
    final static public String path = protocol+hostname+":"+port+"/"+project;

    //userAPI
    final static public String login=path+"user/login";
    final static public String register=path+"/user/register";
    final static public String send_code=path+"/user/register";

    //视频
    final static public String shipinlist=path+"video/video_list";
    final static public String shipin_detail=path+"video/video_detail";
    final static public String comment=path+"video/comment";
    final static public String getComment=path+"video/comment_list";
    final static public String tiwen=path+"video/question";
    final static public String getTiwen=path+"video/question_list";
}
