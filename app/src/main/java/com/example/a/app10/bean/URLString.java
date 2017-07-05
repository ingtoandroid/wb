package com.example.a.app10.bean;

/**
 * Created by 12917 on 2017/6/13.
 */

public class URLString {


    final static public String protocol = "http://";

    final static public String hostname = "120.76.165.146";

    final static public String port = "80";
    final static public String project = "yjtyms/yjty_App/";
    final static public String super_project = "yjtyms/";
    final static public String path = protocol+hostname+"/"+project;
    final static public String path_head_image = protocol+hostname+":"+port+"/"+super_project;
    //userAPI
    final static public String login=path+"user/login";
    final static public String register=path+"user/register";
    final static public String send_code=path+"user/send_code";
    final static public String reset_pwd = path+"user/pwd_reset";
    final static public String head_image=path+"associator/modelName_associator_save";

    //myAPI
    final static public String integral_history_list = path + "jifen/jifen_history_list";
    final static public String lyhf_new_type_list = path + "ly/lyhf_new_type_list";
    final static public String xq_new_type_list = path + "xq/xq_new_type_list";
    final static public String save_question_again = path + "xq/save_question_again";
    final static public String kc_new_type_list = path + "kc/kc_new_type_list";
    final static public String order_new_type_list = path + "order/order_new_type_list";

    //视频
    final static public String shipinlist=path+"video/video_list";
    final static public String shipin_detail=path+"video/video_detail";
    final static public String comment=path+"video/comment";
    final static public String getComment=path+"video/comment_list";
    final static public String tiwen=path+"video/question";
    final static public String getTiwen=path+"video/question_list";

    //aAPI
    final static public String PERSONAL_INFO = path+"info/info_history_list";
    final static public String MODIFY_HEADIMAGE = path+"associator";
    final static public String MODIFY_INFO = path+"associator/save_associator_message";

    //courseAPI
    final static public String message_list = path+"message/message_list";
    final static public String deleteMessage = path+"message/deleateMessage";
    final static public String quickQuestion = path+"question/quickQuestion";
    final static public String question_list = path+"question/question_list";
    final static public String course_comment=path+"expert/expert_order_pingfe";

    //专家
    final static public String expert_list=path+"expert/get_expert_list";
    final static public String class_list=path+"course/courseRelease_list";
    final static public String expert_order_cancel = path + "expert/expert_order_cancel";
    final static public String expert_order_pingfe = path + "expert/expert_order_pingfe";
}

