package com.example.a.app10.bean;

import android.media.Image;

/**
 * Created by shaoqi on 2017/6/11.
 */

public class MyData {
    public Image getHeadImage() {
        return headImage;
    }

    public void setHeadImage(Image headImage) {
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

    private Image headImage;
    private String username;
    private String content;


}
