package com.guodong.model;

/**
 * Created by yechy on 2015/9/20.
 */
public class Gym implements Comparable {
    private String gymName;
    private float price;
    private float distance;
    private String gymImageUrl;
    private int gymId;

    public Gym() {

    }

    public Gym(String gymName, float price, float distance) {
        this.gymName = gymName;
        this.price = price;
        this.distance = distance;
    }

    public Gym(String gymName, int gymId, float price, float distance, String gymImageUrl) {
        this.gymName = gymName;
        this.gymId = gymId;
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

    public float getPrice() {
        return price;
    }

    public String getGymImageUrl() {
        return gymImageUrl;
    }

    public int getGymId() {
        return gymId;
    }

    @Override
    public int compareTo(Object o) {
        Gym anotherGym = (Gym) o;
        if(this.distance > anotherGym.getDistance()) {
            return 1;
        } else if (this.distance < anotherGym.getDistance()) {
            return -1;
        } else {
            return 0;
        }
    }
}
