package com.example.a.app10.bean;

import android.graphics.Bitmap;

/**
 * Created by lenovo on 2017/6/4.
 */

public class ProfessorItem {
    private Bitmap image;
    private String name,content;
    private int grade;

    public ProfessorItem(Bitmap image, String name, String content, int grade) {
        this.image = image;
        this.name = name;
        this.content = content;
        this.grade = grade;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
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

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
