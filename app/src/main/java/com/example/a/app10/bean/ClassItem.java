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
    private Bitmap bitmap;
    private String title,number,time;

    public ClassItem(Bitmap bitmap, String title, String number, String time) {
        this.bitmap = bitmap;
        this.title = title;
        this.number = number;
        this.time = time;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
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

