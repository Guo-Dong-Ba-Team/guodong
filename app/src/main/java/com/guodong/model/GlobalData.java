package com.guodong.model;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by yechy on 2015/10/26.
 */
public class GlobalData extends Application {
    private boolean isLogin;
    private String loginAccount;
    private String password;


    @Override
    public void onCreate() {

        //读取登陆的用户信息
        SharedPreferences pref = getSharedPreferences("loginID", MODE_PRIVATE);
        loginAccount = pref.getString("account", "");
        password = pref.getString("password", "");
        isLogin = pref.getBoolean("islogin",false);

        super.onCreate();
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
}
