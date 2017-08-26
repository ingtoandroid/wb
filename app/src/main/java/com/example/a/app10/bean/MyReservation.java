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

    public String getItem_expert_userName() {
        return item_expert_userName;
    }

    public void setItem_expert_userName(String item_expert_userName) {
        this.item_expert_userName = item_expert_userName;
    }

    private String item_expert_userName;

    public String getOrderContent() {
        return orderContent;
    }

    public void setOrderContent(String orderContent) {
        this.orderContent = orderContent;
    }

    private String orderContent;
    private String orderDate;

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStartTime() {
        return orderStartTime;
    }

    public void setOrderStartTime(String orderStartTime) {
        this.orderStartTime = orderStartTime;
    }

    public String getOrderEndTime() {
        return orderEndTime;
    }

    public void setOrderEndTime(String orderEndTime) {
        this.orderEndTime = orderEndTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private String orderStartTime;
    private String orderEndTime;
    private String orderType;
    private String orderId;
    private String filePath;
    public int type;
}
