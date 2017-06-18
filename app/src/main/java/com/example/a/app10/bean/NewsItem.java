package com.example.a.app10.bean;

import android.graphics.Bitmap;

/**
 * Created by lenovo on 2017/5/31.
 */

public class NewsItem {
    private String newsId,title,publishTime,authorName,imageUrl;

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public NewsItem(String newsId, String title, String publishTime, String authorName, String imageUrl) {
        this.newsId = newsId;
        this.title = title;
        this.publishTime = publishTime;
        this.authorName = authorName;
        this.imageUrl = imageUrl;
    }
}
