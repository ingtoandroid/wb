package com.example.a.app10.bean;

import android.graphics.Bitmap;

/**
 * Created by lenovo on 2017/6/7.
 */

public class MyClassItem {
    private String modelName,courseId,courseTitle,startDate,entereId,state;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEntereId() {
        return entereId;
    }

    public void setEntereId(String entereId) {
        this.entereId = entereId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public MyClassItem(String modelName, String courseId, String courseTitle, String startDate, String entereId, String state) {
        this.modelName = modelName;

        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.startDate = startDate;
        this.entereId = entereId;
        this.state = state;
    }
}
