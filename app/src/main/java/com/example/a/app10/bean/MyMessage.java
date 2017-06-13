package com.example.a.app10.bean;

import android.media.Image;

/**
 * Created by 12917 on 2017/6/13.
 */

public class MyMessage {
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
