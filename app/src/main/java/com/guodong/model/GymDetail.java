package com.guodong.model;

/**
 * Created by ustc on 2015/10/21.
 */
public class GymDetail
{
    private String name;
    private String[] gymImageUrl;
    private float single_price;
    private float vip_price;
    private float discount;
    private String address_city;
    private String address_detail;
    private double longitude;
    private double latitude;
    private String phone_num;
    private String open_time;
    private String hardware;
    private String service;
    private int star_level;

    public GymDetail(String name, String[] gymImageUrl, float single_price, float vip_price,
                     float discount, String address_city, String address_detail, double longitude, double latitude,
                     String phone_num, String open_time, String hardware, String service, int star_level)
    {
        this.name = name;
        this.gymImageUrl = gymImageUrl;
        this.single_price = single_price;
        this.vip_price = vip_price;
        this.discount = discount;
        this.address_city = address_city;
        this.address_detail = address_detail;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phone_num = phone_num;
        this.open_time = open_time;
        this.hardware = hardware;
        this.service = service;
        this.star_level = star_level;
    }

    public String getName() {
        return name;
    }

    public String[] getGymImageUrl() {
        return gymImageUrl;
    }

    public float getSingle_price() {
        return single_price;
    }

    public float getVip_price() {
        return vip_price;
    }

    public float getDiscount() {
        return discount;
    }

    public String getAddress_city() {
        return address_city;
    }

    public String getAddress_detail() {
        return address_detail;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public String getOpen_time() {
        return open_time;
    }

    public String getHardware() {
        return hardware;
    }

    public String getService() {
        return service;
    }

    public int getStar_level() {
        return star_level;
    }


}
