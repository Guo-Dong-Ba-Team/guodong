package com.guodong.model;

/**
 * Created by blarrow on 2015/11/8.
 */
public class OrderInfo {
    private String user;
    private String gymName;
    private String bookTime;
    private String orderTime;
    private int reverseField;
    private int status;
    private float price;

    public OrderInfo(String gymName, int reverseField, String bookTime, String orderTime, int status, float  price) {
        this.gymName = gymName;
        this.reverseField = reverseField;
        this.bookTime = bookTime;
        this.orderTime = orderTime;
        this.status = status;
        this.price = price;
    }

    public String getUser() {
        return user;
    }

    public int getReverseField() {
        return reverseField;
    }

    public String getGymName() {
        return gymName;
    }

    public String getBookTime() {
        return bookTime;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public int getStatus() {
        return status;
    }

    public float getPrice() {
        return price;
    }
}



