package com.example.a.app10.bean;

/**
 * Created by shaoqi on 2017/6/11.
 */

public class MyPoint {

    public String getYears_and_months() {
        return years_and_months;
    }

    public void setYears_and_months(String years_and_months) {
        this.years_and_months = years_and_months;
    }

    public String getItem_content_title() {
        return item_content_title;
    }

    public void setItem_content(String item_content_title) {
        this.item_content_title = item_content_title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    private String item_content_title;
    private String years_and_months;
    private String time;
    private int point;
}
