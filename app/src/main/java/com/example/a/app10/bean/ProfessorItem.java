package com.example.a.app10.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/6/4.
 */

public class ProfessorItem implements Serializable{
    private String imgUrl;
    private String name,content,expertId;
    private int grade;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExpertId() {
        return expertId;
    }

    public void setExpertId(String expertId) {
        this.expertId = expertId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public ProfessorItem(String imgUrl, String name, String content, String expertId, int grade) {
        this.imgUrl = imgUrl;
        this.name = name;
        this.content = content;
        this.expertId = expertId;
        this.grade = grade;
    }
}
