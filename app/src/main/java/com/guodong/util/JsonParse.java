package com.guodong.util;

import com.guodong.model.GlobalData;
import com.guodong.model.Gym;
import com.guodong.model.GymDetail;
import com.guodong.model.OrderInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


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
    final static String GYM_IMAGE_URL = "main_image";
    final static String GYM_IMAGE_URL_ARRAY = "gym_image_url_array";
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

    final static String ORDER_INFO = "order_info";
    final static String ORDER_USER = "user";
    final static String ORDER_MONEY = "money";
    final static String ORDER_STATUS = "status";
    final static String ORDER_TIME = "time";
    final static String ORDER_NAME = "name";

    //Json parsing function for brief information of gyms
    //Return format: ArrayList of GymBrief objects
    public static List<Gym> ParseBriefGymInfo(String infoData)
            throws JSONException
    {
        JSONObject GymBriefInfoJson = new JSONObject(infoData);
        JSONArray infoArray = GymBriefInfoJson.getJSONArray(GYM_BRIEF);

        //Define the returned ArrayList
        ArrayList<Gym> gymBriefs = new ArrayList<>(infoArray.length());


        for (int i = 0; i < infoArray.length(); i++)
        {
            JSONObject everyGymBriefJson = infoArray.getJSONObject(i);

            String name = everyGymBriefJson.getString(GYM_NAME);
            double longitude = (double) everyGymBriefJson.getDouble(GYM_LONGITUDE);
            double latitude = (double) everyGymBriefJson.getDouble(GYM_LATITUDE);
            int gymId = (int) everyGymBriefJson.getInt("id");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://182.61.8.185/images/");
            stringBuilder.append(gymId).append("/");
            stringBuilder.append(everyGymBriefJson.getString(GYM_IMAGE_URL));
            String mainImageUrl = stringBuilder.toString();
            float single_price =  (float) everyGymBriefJson.getDouble(GYM_SINGLE_PRICE);
            float vip_price = (float) everyGymBriefJson.getDouble(GYM_VIP_PRICE);
            float discount = (float) everyGymBriefJson.getDouble(GYM_DISCOUNT);

            //distance 由百度地图计算出来
            float distance = 0;
            double myLongitude = globalData.getMyLongitude();
            double myLatitude = globalData.getMyLatitude();
            if (myLongitude != 0 && myLatitude != 0 ) {
                distance = (float) Distance(longitude, latitude, myLongitude, myLatitude);
            }

            gymBriefs.add(new Gym(name, gymId, single_price, distance, mainImageUrl));
        }

        return gymBriefs;
    }
    //Json parsing function for brief information of gyms
    //Return format: ArrayList of GymBrief objects
    public static GymDetail ParseDetailGymInfo(String infoData, int gymId)
            throws JSONException
    {
        JSONObject gymInfoJson = new JSONObject(infoData);
        JSONObject gymDetailInfoJson = gymInfoJson.getJSONObject(GYM_DETAIL);

        String name = gymDetailInfoJson.getString(GYM_NAME);
        double longitude = gymDetailInfoJson.getDouble(GYM_LONGITUDE);
        double latitude = gymDetailInfoJson.getDouble(GYM_LATITUDE);

        JSONArray imageUrlJson = gymDetailInfoJson.getJSONArray(GYM_IMAGE_URL_ARRAY);

        String[] imageUrlArray = new String[imageUrlJson.length()];
        for (int j = 0; j < imageUrlJson.length(); j++)
        {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://182.61.8.185/images/");
            stringBuilder.append(gymId).append("/");
            imageUrlArray[j] = stringBuilder.append(imageUrlJson.getString(j)).toString();
        }
        float single_price = gymDetailInfoJson.getInt(GYM_SINGLE_PRICE);
        float vip_price = gymDetailInfoJson.getInt(GYM_VIP_PRICE);
        float discount = (float) gymDetailInfoJson.getDouble(GYM_DISCOUNT);
        String address_city = gymDetailInfoJson.getString(GYM_ADDRESS_CITY);
        String address_detail = gymDetailInfoJson.getString(GYM_ADDRESS_DETAIL);
        String phone_num = gymDetailInfoJson.getString(GYM_PHONE_NUM);
        String open_time = gymDetailInfoJson.getString(GYM_OPEN_TIME);
        String hardware = gymDetailInfoJson.getString(GYM_HARDWARE);
        String service = gymDetailInfoJson.getString(GYM_SERVICE);
        float star_level = (float) gymDetailInfoJson.getDouble(GYM_STAR_LEVEL);


           GymDetail gymDetail = new GymDetail(name, imageUrlArray, single_price, vip_price, discount,
                    address_city,address_detail, longitude, latitude, phone_num, open_time, hardware, service, star_level);

        return gymDetail;
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

    //Json parsing function for order_info
    //Return format: ArrayList of OrderInfo objects

    public static ArrayList<OrderInfo> ParseorderInfos(String infoData)
            throws JSONException {
        JSONObject orderInfoJson = new JSONObject(infoData);
        JSONArray infoArray = orderInfoJson.getJSONArray(ORDER_INFO);
        ArrayList<OrderInfo> orderinfos = new ArrayList<>(infoArray.length());
        for (int i = 0; i < infoArray.length(); i++) {
            JSONObject orderinfo = infoArray.getJSONObject(i);
            String gymName = orderinfo.getString(ORDER_NAME);
            String user = orderinfo.getString(ORDER_USER);
            String bookTime = orderinfo.getString(ORDER_TIME);
            String orderTime = orderinfo.getString("order_time");
            float price = (float) orderinfo.getDouble(ORDER_MONEY);
            int status = orderinfo.getInt(ORDER_STATUS);
            orderinfos.add(new OrderInfo(gymName, bookTime, orderTime, status, price) );
        }
        return orderinfos;

    }
}
