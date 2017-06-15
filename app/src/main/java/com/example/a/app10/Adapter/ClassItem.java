package com.example.a.app10.Adapter;

/**
 * Created by 12917 on 2017/6/15.
 */

public class ClassItem {
    private String imgUrl;

    public ClassItem(String imgUrl, String courseId, String title, String time, String number) {
        this.imgUrl = imgUrl;
        this.courseId = courseId;
        this.title = title;
        this.time = time;
        Number = number;
    }

    private String courseId;
    private String title;
    private String time;
    private String Number;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }


}
