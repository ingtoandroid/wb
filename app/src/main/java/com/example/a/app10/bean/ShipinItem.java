package com.example.a.app10.bean;

/**
 * Created by lenovo on 2017/6/9.
 */

import android.graphics.Bitmap;

/**
 * Created by lenovo on 2017/6/6.
 * 课程列表的课程数据模型--ZY
 */

public class ShipinItem {
    private String imageUrl;
    private String videoId;//课程ID
    private String videoTitle;
    private String playNum;

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    private String videoType;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getPlayNum() {
        return playNum;
    }

    public void setPlayNum(String playNum) {
        this.playNum = playNum;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    private String startDate;//题目，已参加人数，开始日期

}

