package com.guodong.activity;

/**
 * Created by blarrow on 2015/10/21.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.guodong.R;
import com.guodong.model.GlobalData;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends Activity {
    private EditText et_password;
    private EditText loginPhone;
    private EditText et_username;
    private Button bt_login;
    private Button bt_register;
    private CheckBox rememberPass;
    private ProgressDialog Dialog;
    private Context mContext;
    private GlobalData globalData;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private String phone;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = getSharedPreferences("loginID", MODE_PRIVATE);
        mContext = this;
        bt_login = (Button) findViewById(R.id.login);
        bt_register = (Button) findViewById(R.id.register);
        loginPhone = (EditText) findViewById(R.id.loginPhone);
        et_password = (EditText) findViewById(R.id.password);
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        globalData = (GlobalData) getApplicationContext();

        phone = pref.getString("account","");
        if (phone != "") {
            loginPhone.setText(phone);
        }
    }
    public void registerInfo(View v){
        Intent intent=new Intent(mContext, RegisterActivity.class);
        startActivity(intent);
    }
    public void login(View v){

        password = et_password.getText().toString();
        phone = loginPhone.getText().toString();

        if ("".equals(phone.trim()) || "".equals(password.trim())) {
            Toast.makeText(mContext, "手机号或密码为空...", Toast.LENGTH_SHORT).show();
        } else if (phone.trim().length() != 11  || password.trim().length() > 16) {
            Toast.makeText(mContext, "手机号或密码格式错误", Toast.LENGTH_SHORT).show();
        } else {
            Dialog = new ProgressDialog(LoginActivity.this);
            Dialog.setTitle("提示");
            Dialog.setMessage("正在请求服务器，请稍候...");
            Dialog.show();
        }
        new Thread()
        {
            public void run()
            {
                loginByGet(phone, password );
            };
        }.start();
    }
    public void loginByGet(String loginPhone, String userPass) {

        try {

            System.out.println("======================================================");
            
            //genymotion 模拟器可以通过10.0.3.2连接到电脑localhost上
            String spec = "http://10.0.3.2:8080/login" + "?phone="+loginPhone+"&password="+userPass;
         
            // 根据地址创建URL对象(网络访问的url)
            URL url = new URL(spec);
            // url.openConnection()打开网络链接
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("GET");// 设置请求的方式
            urlConnection.setReadTimeout(5000);// 设置超时的时间
            urlConnection.setConnectTimeout(5000);// 设置链接超时的时间
            // 设置请求的头
            urlConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
            // 获取响应的状态码 404 200 505 302
            System.out.println(urlConnection.getResponseCode());
            if (urlConnection.getResponseCode() == 200) {
                // 获取响应的输入流对象
                InputStream is = urlConnection.getInputStream();

                // 创建字节输出流对象
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    os.write(buffer, 0, len);
                }
                // 释放资源
                is.close();
                os.close();
                // 返回字符串
                String result = new String(os.toByteArray());
                System.out.println("***************" + result
                        + "******************");

                if(result == "0") {

                    //保存登陆的用户信息
                    globalData.setIsLogin(true);
                    globalData.setLoginAccount(loginPhone);
                    globalData.setPassword(userPass);
                    editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    editor.putBoolean("islogin", true);
                    editor.putString("account",loginPhone);
                    editor.putString("password",userPass);

                    editor.commit();

                    MainActivity.actionStart(LoginActivity.this, 3);

                }
                else if (result == "1")
                {
                    //这个手机号还注册

                }
                else if (result == "2")
                {
                    //手机号或密码不正确

                }

            } else {
                System.out.println("------------------链接失败-----------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
