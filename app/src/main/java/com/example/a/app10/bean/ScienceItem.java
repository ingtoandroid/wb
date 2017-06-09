package com.example.a.app10.bean;

import android.graphics.Bitmap;

/**
 * Created by lenovo on 2017/5/31.
 */

public class ScienceItem {
    private Bitmap bitmap;

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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ScienceItem(Bitmap bitmap, String title, String department, String time) {
        this.bitmap = bitmap;
        this.title = title;
        this.department = department;
        this.time = time;
    }

    private String title,department,time;
}
