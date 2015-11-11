package com.guodong.model;

/**
 * Created by blarrow on 2015/11/8.
 */
public class OrderInfo {
    private String user;
    private String name;
    private String time;
    private int status;
    private float money;

    public OrderInfo(String user, String name, String time, int status, float  money) {
        this.user = user;
        this.name = name;
        this.time = time;
        this.status = status;
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public String getTime() {
        return time;
    }

    public int getStatus() {
        return status;
    }

    public float getMoney() {
        return money;
    }
}



