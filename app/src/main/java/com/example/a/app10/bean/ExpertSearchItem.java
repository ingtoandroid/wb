package com.example.a.app10.bean;

/**
 * Created by lenovo on 2017/6/24.
 */

public class ExpertSearchItem {
    private String key,value,imageUrl;

    public ExpertSearchItem(String key, String value, String imageUrl) {
        this.key = key;
        this.value = value;
        this.imageUrl = imageUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
