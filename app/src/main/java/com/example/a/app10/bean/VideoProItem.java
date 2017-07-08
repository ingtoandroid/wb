package com.example.a.app10.bean;

/**
 * Created by lenovo on 2017/6/17.
 */

public class VideoProItem {
    private String videoId,videoTitle,playNum, videoType,imageUrl;

    public VideoProItem(String videoId, String videoTitle, String playNum, String videoType, String imageUrl) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.playNum = playNum;
        this.videoType = videoType;
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

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
