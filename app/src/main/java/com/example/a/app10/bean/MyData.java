package com.example.a.app10.bean;

import android.graphics.Bitmap;
import android.media.Image;

/**
 * Created by shaoqi on 2017/6/11.
 */

public class MyData {
    public Bitmap getHeadImage() {
        return headImage;
    }

    public void setHeadImage(Bitmap headImage) {
        this.headImage = headImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private Bitmap headImage;
    private String username;
    private String content;


}
