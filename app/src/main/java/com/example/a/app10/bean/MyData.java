package com.example.a.app10.bean;

import android.graphics.Bitmap;
import android.media.Image;

/**
 * Created by shaoqi on 2017/6/11.
 */

public class MyData {
    public String getHeadImageURL() {
        return headImageURL;
    }

    public void setHeadImageURL(String headImageURL) {
        this.headImageURL = headImageURL;
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

    private String headImageURL;
    private String username = "";
    private String content;


}
