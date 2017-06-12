package com.example.a.app10.bean;

/**
 * Created by shaoqi on 2017/6/11.
 */

public class MyReservation {
    public String getItem_content_reservation() {
        return item_content_reservation;
    }

    public void setItem_content_reservation(String item_content_reservation) {
        this.item_content_reservation = item_content_reservation;
    }

    public String getItem_username_reservation() {
        return item_username_reservation;
    }

    public void setItem_username_reservation(String item_username_reservation) {
        this.item_username_reservation = item_username_reservation;
    }

    public String getItem_time_reservation() {
        return item_time_reservation;
    }

    public void setItem_time_reservation(String item_time_reservation) {
        this.item_time_reservation = item_time_reservation;
    }

    private String item_content_reservation;
    private String item_username_reservation;
    private String item_time_reservation;
}
