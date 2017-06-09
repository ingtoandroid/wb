package com.example.a.app10.bean;

import android.graphics.Bitmap;

/**
 * Created by lenovo on 2017/6/7.
 */

public class MyClassItem {
    private Bitmap bitmap;
    private boolean ifFinish;
    private int commentNumber,questionNumber;
    private String title;

    public MyClassItem(Bitmap bitmap, boolean ifFinish, int commentNumber, int questionNumber, String title) {
        this.bitmap = bitmap;
        this.ifFinish = ifFinish;
        this.commentNumber = commentNumber;
        this.questionNumber = questionNumber;
        this.title = title;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isIfFinish() {
        return ifFinish;
    }

    public void setIfFinish(boolean ifFinish) {
        this.ifFinish = ifFinish;
    }

    public int getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
