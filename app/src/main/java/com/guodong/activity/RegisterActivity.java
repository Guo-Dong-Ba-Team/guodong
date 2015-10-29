package com.guodong.activity;

/**
 * Created by blarrow on 2015/10/21.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.guodong.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends Activity {
    private Context mContext;
    private EditText regisname;
    private EditText regisphone;
    private  EditText pswd;
    private  EditText pswdconfirm;
    private  Button btn_complete;
    private ProgressDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        mContext =this;
        regisname = (EditText)findViewById(R.id.username);
        regisphone = (EditText)findViewById(R.id.et_phone);
        pswd = (EditText)findViewById(R.id.pswd);
        pswdconfirm =(EditText)findViewById(R.id.pswdconfirm);
        btn_complete =(Button) findViewById(R.id.submit);
    }


    public void register(View v) {
        final String username = regisname.getText().toString();
        final String password = pswd.getText().toString();
        final String phone = regisphone.getText().toString();
        final String confirmPswd = pswdconfirm.getText().toString();
        if("".equals(phone.trim())||"".equals(username.trim()) || "".equals(password.trim()) || "".equals(confirmPswd.trim())){
            Toast.makeText(mContext, "信息请填写完整", Toast.LENGTH_SHORT).show();
        }else if(username.trim().length()>10||password.trim().length()<3||password.trim().length()>10){
            Toast.makeText(mContext, "用户名或密码位数不正确", Toast.LENGTH_SHORT).show();
        }else if(phone.trim().length()>11) {
            Toast.makeText(mContext, "手机号码过长", Toast.LENGTH_SHORT).show();
        }
        else if(!confirmPswd.equals(password) ){
            Toast.makeText(mContext, "两次密码不一致", Toast.LENGTH_SHORT).show();
        }
        else{
            mDialog = new ProgressDialog(RegisterActivity.this);
            mDialog.setTitle("提交");
            mDialog.setMessage("正在提交注册信息，请稍候...");
            mDialog.show();
            new Thread()
            {
                public void run()
                {
                    //System.out.println("======================================================");
                    registerByGet(username, password, phone);

                };
            }.start();
        }
    }
    public void  registerByGet(String regisname, String password, String regisphone ) {
        try
        {
            //genymotion 模拟器可以通过10.0.3.2连接到电脑localhost上
            String spec = "http://10.0.3.2:8080/register"+"?username="+regisname +"&password="+password  + "&phone="+regisphone;
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
            if (urlConnection.getResponseCode() == 200)
            {
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

                if (result == "0")
                {
                    //注册成功，跳转到登录页面
                    Intent intent=new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                }
                else if (result == "1")
                {
                    //这个手机号已经被注册，弹出提示：去登录或忘记密码

                }
                else if (result == "2")
                {
                    //这个昵称已经被使用，弹出提示：修改昵称

                }
            }






        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
