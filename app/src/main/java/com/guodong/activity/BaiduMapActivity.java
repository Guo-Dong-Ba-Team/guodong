package com.guodong.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.guodong.R;

/**
 * Created by yechy on 2015/10/15.
 */
public class BaiduMapActivity extends Activity{
    private MapView mapView;
    private BaiduMap baiduMap;
    private double longitude;
    private double latitude;
    private boolean isFirstLocate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_baidumap);
        mapView = (MapView) findViewById(R.id.map_view);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);

        Intent intent = getIntent();
        longitude = intent.getDoubleExtra("longitude", 117.28);
        latitude = intent.getDoubleExtra("latitude", 31.87);

        //在地图上显示当前位置
        navigateTo(longitude, latitude);

    }

    public static void actionStart(Context context, double longitude, double latitude) {
        Intent intent = new Intent(context, BaiduMapActivity.class);
        intent.putExtra("longitude", longitude);
        intent.putExtra("latitude", latitude);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    private void navigateTo(double longitude, double latitude) {
        if (isFirstLocate) {
            LatLng latLng = new LatLng(latitude, longitude);
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
            baiduMap.animateMapStatus(mapStatusUpdate);
            mapStatusUpdate = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(mapStatusUpdate);
            isFirstLocate = false;
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(latitude);
        locationBuilder.longitude(longitude);
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
    }
}
