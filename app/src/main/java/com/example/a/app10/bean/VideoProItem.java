package com.example.a.app10.bean;

/**
 * Created by lenovo on 2017/6/17.
 */

public class VideoProItem {
    private String videoId,videoTitle,playNum,startDate,imageUrl;

    public VideoProItem(String videoId, String videoTitle, String playNum, String startDate, String imageUrl) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.playNum = playNum;
        this.startDate = startDate;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
