package com.guodong.model;

/**
 * Created by yechy on 2015/9/20.
 */
public class Gym {
    private String gymName;
    private int price;
    private float distance;
    private int gymImageId;

    public Gym() {

    }

    public Gym(String gymName, int price, float distance) {
        this.gymName = gymName;
        this.price = price;
        this.distance = distance;
    }

    public Gym(String gymName, int price, float distance, int gymImageId) {
        this.gymName = gymName;
        this.price = price;
        this.distance = distance;
        this.gymImageId = gymImageId;
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

    public int getGymImageId() {
        return gymImageId;
    }
}
