package com.example.a.app10.bean;

/**
 * Created by 12917 on 2017/6/13.
 */

public class URLString {


    final static public String protocol = "http:";
    final static public String hostname = "192.168.1.150";
    final static public String port = "8080";
    final static public String project = "yjtyms/yjty_App/";
    final static public String path = protocol+hostname+":"+port+"/"+project;

    //userAPI
    final static public String login=path+"user/login";
    final static public String register=path+"user/register";
    final static public String send_code=path+"user/send_code";
    final static public String reset_pwd = path+"user/pwd_reset";

    //myAPI
    final static public String integral_history_list = path + "jifen/jifen_history_list";
    final static public String lyhf_new_type_list = path + "ly/lyhf_new_type_list";
    final static public String xq_new_type_list = path + "xq/xq_new_type_list";
    final static public String save_question_again = path + "xq/save_question_again";
    final static public String kc_new_type_list = path + "kc/kc_new_type_list";
    final static public String order_new_type_list = path + "order/order_new_type_list";
}
