package com.guodong.util;

import com.guodong.model.GlobalData;
import com.guodong.model.Gym;
import com.guodong.model.GymDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by ustc on 2015/10/20.
 */
public class JsonParse
{

    //Define names for the JSON objects to parsed.
    final static String GYM_BRIEF = "gym_brief";
    final static String GYM_DETAIL = "gym_detail";
    final static String GYM_NAME = "name";
    final static String GYM_DISTANCE = "distance";
    final static String GYM_MAIN_IMAGE = "main_image";
    final static String GYM_DETAIL_IMAGE = "detail_image";
    final static String GYM_SINGLE_PRICE = "single_price";
    final static String GYM_VIP_PRICE = "vip_price";
    final static String GYM_DISCOUNT = "discount";
    final static String GYM_ADDRESS_CITY = "address_city";
    final static String GYM_ADDRESS_DETAIL = "address_detail";
    final static String GYM_LONGITUDE = "longitude";
    final static String GYM_LATITUDE = "latitude";
    final static String GYM_PHONE_NUM = "phone_num";
    final static String GYM_OPEN_TIME = "open_time";
    final static String GYM_HARDWARE = "hardware";
    final static String GYM_SERVICE = "service";
    final static String GYM_STAR_LEVEL = "star_level";

    private static GlobalData globalData = (GlobalData) GlobalData.getContext().getApplicationContext();

    //Json parsing function for brief information of gyms
    //Return format: ArrayList of GymBrief objects
    public static ArrayList<Gym> ParseBriefGymInfo(String infoData)
            throws JSONException
    {
        JSONObject GymBriefInfoJson = new JSONObject(infoData);
        JSONArray infoArray = GymBriefInfoJson.getJSONArray(GYM_BRIEF);

        //Define the returned ArrayList
        ArrayList<Gym> gymBriefs = new ArrayList<>();


        for (int i = 0; i < infoArray.length(); i++)
        {
            JSONObject everyGymBriefJson = infoArray.getJSONObject(i);

            String name = everyGymBriefJson.getString(GYM_NAME);
            double longitude = (double) everyGymBriefJson.getDouble(GYM_LONGITUDE);
            double latitude = (double) everyGymBriefJson.getDouble(GYM_LATITUDE);
            String mainImageUrl = everyGymBriefJson.getString(GYM_MAIN_IMAGE);
            float signle_price =  (float) everyGymBriefJson.getDouble(GYM_SINGLE_PRICE);
            float vip_price = (float) everyGymBriefJson.getDouble(GYM_VIP_PRICE);
            float discount = (float) everyGymBriefJson.getDouble(GYM_DISCOUNT);
            //distance 由百度地图计算出来
            float distance = 0;
            double myLongitude = globalData.getMyLongitude();
            double myLatitude = globalData.getMyLatitude();
            if (myLongitude != 0 && myLatitude != 0 ) {
                distance = (float) Distance(longitude, latitude, myLongitude, myLatitude);
            }

            gymBriefs.add(new Gym(name, signle_price, distance, mainImageUrl));
        }

        return gymBriefs;
    }
    //Json parsing function for brief information of gyms
    //Return format: ArrayList of GymBrief objects
    private static ArrayList<GymDetail> ParseDetailGymInfo(String infoData)
            throws JSONException
    {
        JSONObject GymDetailfInfoJson = new JSONObject(infoData);
        JSONArray infoArray = GymDetailfInfoJson.getJSONArray(GYM_BRIEF);

        //Define the returned ArrayList
        ArrayList<GymDetail> gymDetails= new ArrayList<>();
        int gymMainImageId = 0;
        ArrayList<Integer> detailImageIds = null;


        for (int i = 0; i < infoArray.length(); i++)
        {
            JSONObject everyGymDetailJson = infoArray.getJSONObject(i);

            String name = everyGymDetailJson.getString(GYM_NAME);
            float longitude = (float) everyGymDetailJson.getDouble(GYM_LONGITUDE);
            float latitude = (float) everyGymDetailJson.getDouble(GYM_LATITUDE);
            JSONArray detailImageArray = everyGymDetailJson.getJSONArray(GYM_DETAIL_IMAGE);

            String[] detaiImageUrl = new String[detailImageArray.length()];
            for (int j = 0; j < detailImageArray.length(); j++)
            {
                JSONObject everyDetailImage = detailImageArray.getJSONObject(j);
                detaiImageUrl[j] = everyDetailImage.getString(GYM_MAIN_IMAGE);
            }
            float signle_rice = everyGymDetailJson.getInt(GYM_SINGLE_PRICE);
            float vip_price = everyGymDetailJson.getInt(GYM_VIP_PRICE);
            float discount = (float) everyGymDetailJson.getDouble(GYM_DISCOUNT);
            String address_city = everyGymDetailJson.getString(GYM_ADDRESS_CITY);
            String address_detail = everyGymDetailJson.getString(GYM_ADDRESS_DETAIL);
            String phone_num = everyGymDetailJson.getString(GYM_PHONE_NUM);
            String open_time = everyGymDetailJson.getString(GYM_OPEN_TIME);
            String hardware = everyGymDetailJson.getString(GYM_HARDWARE);
            String service = everyGymDetailJson.getString(GYM_SERVICE);
            int star_level = everyGymDetailJson.getInt(GYM_STAR_LEVEL);

            //图片保存在本地后，获得detailImageIds;

            //获得经纬度后，由百度地图计算出距离
            int distance = 0;
            gymDetails.add(new GymDetail(name, distance, detaiImageUrl, signle_rice, vip_price, discount,
                    address_city,address_detail, longitude, latitude, phone_num, open_time, hardware, service, star_level));
        }

        return gymDetails;
    }

    /**
     * 计算地球上任意两点(经纬度)距离
     *
     * @param long1
     *            第一点经度
     * @param lat1
     *            第一点纬度
     * @param long2
     *            第二点经度
     * @param lat2
     *            第二点纬度
     * @return 返回距离 单位：km
     */
    public static double Distance(double long1, double lat1, double long2,
                                  double lat2) {
        double a, b, R;
        R = 6378137; // 地球半径
        lat1 = lat1 * Math.PI / 180.0;
        lat2 = lat2 * Math.PI / 180.0;
        a = lat1 - lat2;
        b = (long1 - long2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2
                * R
                * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)
                * Math.cos(lat2) * sb2 * sb2));
        return d/ 1000.0;
    }
}
