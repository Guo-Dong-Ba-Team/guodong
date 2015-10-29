package com.guodong.model;

/**
 * Created by yechy on 2015/9/20.
 */
public class Gym {
    private String gymName;
    private int price;
    private float distance;
    private String gymImageUrl;

    public Gym() {

    }

    public Gym(String gymName, int price, float distance) {
        this.gymName = gymName;
        this.price = price;
        this.distance = distance;
    }

    public Gym(String gymName, int price, float distance, String gymImageUrl) {
        this.gymName = gymName;
        this.price = price;
        this.distance = distance;
        this.gymImageUrl = gymImageUrl;
    }

    public String getGymName() {
        return gymName;
    }

    public float getDistance() {
        return distance;
    }

    public int getPrice() {
        return price;
    }

    public String getGymImageUrl() {
        return gymImageUrl;
    }
}
