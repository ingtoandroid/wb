package com.example.a.app10.bean;

/**
 * Created by lenovo on 2017/6/9.
 */

import android.graphics.Bitmap;

/**
 * Created by lenovo on 2017/6/6.
 * 课程列表的课程数据模型--ZY
 */

public class ClassItem {
    private String imgUrl;
    private String courseId;//课程ID
    private String title,number,time;//题目，已参加人数，开始日期


    public String getCourseId() {

        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }


    public ClassItem(String imgUrl, String courseId, String title, String number, String time) {
        this.imgUrl = imgUrl;
        this.courseId = courseId;
        this.title = title;
        this.number = number;
        this.time = time;
    }

    public String getImgUrl() {

        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

