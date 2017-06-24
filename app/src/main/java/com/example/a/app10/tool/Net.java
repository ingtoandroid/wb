package com.example.a.app10.tool;

import com.example.a.app10.bean.URLString;
import com.google.android.gms.common.internal.GetServiceRequest;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.net.URL;

/**
 * Created by 12917 on 2017/6/15.
 */

public class Net {
    private static Net net;
    private OkHttpClient okHttpClient;

    private static String userUUID = "";
    private static String photoUrl = "";
    private static String remark = "";
    private static String personName = "";
    private static String personID = "";
    private static String hx_pwd = "";
    private static String username = "";
    private static String signature = "";
    private static String sex = "";
    private static int megsSize = 0;




    //single
    private Net(){
        okHttpClient=new OkHttpClient();
    }
    public static synchronized Net getInstance(){
        if(net==null)
            net=new Net();
        return net;
    }

    //Getter and Setter
    public static Net getNet() {
        return net;
    }

    public static void setNet(Net net) {
        Net.net = net;
    }

    public static String getUserUUID() {
        return userUUID;
    }

    public static void setUserUUID(String userUUID) {
        Net.userUUID = userUUID;
    }

    public static String getPhotoUrl() {
        return photoUrl;
    }

    public static void setPhotoUrl(String photoUrl) {
        Net.photoUrl = photoUrl;
    }

    public static String getRemark() {
        return remark;
    }

    public static void setRemark(String remark) {
        Net.remark = remark;
    }

    public static String getPersonName() {
        return personName;
    }

    public static void setPersonName(String personName) {
        Net.personName = personName;
    }

    public static String getPersonID() {
        return personID;
    }

    public static void setPersonID(String personID) {
        Net.personID = personID;
    }

    public static String getHx_pwd() {
        return hx_pwd;
    }

    public static void setHx_pwd(String hx_pwd) {
        Net.hx_pwd = hx_pwd;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Net.username = username;
    }

    public static String getSex() {
        return sex;
    }

    public static void setSex(String sex) {
        Net.sex = sex;
    }

    public static String getSignature() {
        return signature;
    }

    public static void setSignature(String signature) {
        Net.signature = signature;
    }

    public static int getMegsSize() {
        return megsSize;
    }

    public static void setMegsSize(int megsSize) {
        Net.megsSize = megsSize;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }


    /*
    * usrAPI
    * */
    public Call login(String name,String password){
        String url= URLString.login+"?"+"username="+name+"&"+"password="+password;
        return get(url);
    }

    public Call register(String phone,String password,String checkCode){
        String url = URLString.register+"?"+"phone="+phone+"&"+"password="+password
                +"&"+"code="+checkCode;
        return get(url);
    }

    public Call resetPwd(String phone,String password,String checkCode){
        String url = URLString.reset_pwd+"?"+"phone="+phone+"&"+"password="+password
                +"&code="+checkCode;
        return get(url);
    }
    public Call getCodeForRegister(String phone){
        String url = URLString.send_code+"?"+"phone="+phone+"&"+"fs=0";
        return get(url);
    }

    public Call getCodeForResetPwd(String phone){
        String url = URLString.send_code+"?"+"phone="+phone+"&"+"fs=1";
        return get(url);
    }
    public Call setHeadImage(File file){
        String url=URLString.head_image;
        RequestBody requestBody=new MultipartBuilder().addFormDataPart("infoId",personID).addFormDataPart("avatar",file.getName(),RequestBody.create(MediaType.parse("application/jpg"),file)).build();
        return post(url,requestBody);
    }

    /*myAPI
     */
    public Call getIntegralHistory(String infoId){
        String url = URLString.integral_history_list+"?"+"infoId="+infoId;
        return get(url);
    }

    public Call getMessage(String infoId){
        String url = URLString.lyhf_new_type_list+"?"+"infoId="+infoId+"&"+"type=0";
        return get(url);
    }

    public Call getQuestion(String infoId){
        String url = URLString.lyhf_new_type_list+"?"+"infoId="+infoId+"&"+"type=1";
        return get(url);
    }

    public Call getQuestionDatails(String questionID){
        String url = URLString.xq_new_type_list+"?"+"questionId="+questionID;
        return get(url);
    }

    public Call getAskPurse(String questionID,String questionContent){
        String url = URLString.save_question_again+"?"+"questionId="+questionID+"&"
                +"questionContent="+questionContent;
        return get(url);
    }

    public Call getMyCourse(String infoID){
        String url = URLString.kc_new_type_list+"?"+"infoId="+infoID;
        return get(url);
    }

    public Call getMyReservation() {
        String url = URLString.order_new_type_list + "?" + "infoid=" + personID;
//        String url = URLString.order_new_type_list + "?" + "infoid=" + "6b8c6112-305e-4bb2-991b-b00805669fe0";
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
    public Call commitPinglun(String comment,String videoId){
        String url=URLString.comment+"?personId="+personID+"&conmment="+comment+"&userName="+username+"&videoId="+videoId;
        return get(url);
    }
    public Call commitTiwen(String tiwen,String videoId){
        String url=URLString.tiwen+"?personId="+personID+"&question="+tiwen+"&userName="+username+"&videoId="+videoId;
        return get(url);
    }

    /*
    AAPI
     */
    public Call getPersonalInfo(String personID){
        String url = URLString.PERSONAL_INFO+"?"+"infoId="+personID;
        return get(url);
    }

//    public Call modifyHeadImage(){
//        String url = URLString.MODIFY_HEADIMAGE;
//
//    }

    public Call modifyInfo(String personID,String username,String signature,String sex){
        String url = URLString.MODIFY_INFO+"?"+"infoId="+personID+"&"+"nickName="+username
                +"&"+"signature="+signature+"&"+"sex="+sex;
        return get(url);
    }


    /*
    courseAPI
     */

    public Call getMessageList(String personID){
        String url = URLString.message_list+"?"+"pageIndex=1"+"&"+"infoId="+personID;
        return get(url);
    }

    public Call deleteMessageList(String personID){
        String url =  URLString.deleteMessage+"?"+"infoId="+personID;
        return get(url);
    }

    public Call quickQuestion(String personID,String question){
        String url = URLString.quickQuestion+"?"+"personId="+personID+"&"
                +"question=" +question;
        return get(url);
    }

    public Call getQuestionList(){
        String url = URLString.question_list+"?"+"personId="+personID;
        return get(url);
    }


    /*专家*/
    public Call getExpertList(){
        String url= URLString.expert_list;
        return get(url);
    }

    public Call getClassList(String s){
        String url=URLString.class_list+s;
        return get(url);
    }

    public Call cancelOrder(String ordId){
        String url = URLString.expert_order_cancel +"?"+"orderId=" + ordId;
        return  get(url);
    }

    //  get/post请求
    private Call get(String url){
        Request request = new Request.Builder().url(url).build();
        return  okHttpClient.newCall(request);
    }

    private Call post(String url, RequestBody requestBody){
        Request request = new Request.Builder().url(url).method("POST",requestBody).build();
        return okHttpClient.newCall(request);
    }

    //



}
