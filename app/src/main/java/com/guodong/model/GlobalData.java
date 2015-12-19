package com.guodong.model;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yechy on 2015/10/26.
 */
public class GlobalData extends Application {
    private boolean isLogin;
    private boolean isRemember;
    private String loginAccount;
    private String password;
    private double myLongitude = 117.2725370000;
    private double myLatitude = 31.8407550000;
    private static Context context;
    private int locateNum;


    @Override
    public void onCreate() {

        context = getApplicationContext();
        locateNum =0;

        //读取登陆的用户信息
        SharedPreferences pref = getSharedPreferences("loginID", MODE_PRIVATE);
        loginAccount = pref.getString("account", "");
        password = pref.getString("password", "");
        isLogin = pref.getBoolean("islogin",false);

        super.onCreate();
    }

    public static Context getContext() {
        return context;
    }

     public boolean getIsLogin() {
         return this.isLogin;
     }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getMyLongitude() {
        return myLongitude;
    }

    public void setMyLongitude(double myLongitude) {
        this.myLongitude = myLongitude;
    }

    public double getMyLatitude() {
        return myLatitude;
    }

    public void setMyLatitude(double myLatitude) {
        this.myLatitude = myLatitude;
    }

    public int getLocateNum() {
        return locateNum;
    }

    public void setLocateNum(int locateNum) {
        this.locateNum = locateNum;
    }

    public boolean isRemember() {
        return isRemember;
    }

    public void setIsRemember(boolean isRemember) {
        this.isRemember = isRemember;
    }
}
