package com.guodong.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guodong.R;
import com.guodong.model.GlobalData;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;


public class OrderDetailActivity extends Activity
{
    private GlobalData globalData;

    String gymName = "";
    String orderTime = "";
    String reserveDay = "";
    int reserveField = -1;
    int reserveHour = -1;
    float price = -1;
    int status = -1;

    Button btn_wait = null;
    private ProgressDialog Dialog = null;

    Handler handler = new Handler()
    {
        public void handleMessage(Message message)
        {
            switch (message.what)
            {
                //订单提交成功
                case 0:
                {
                    Dialog.dismiss();
                    Toast.makeText(OrderDetailActivity.this, "场地预订成功！请在未消费记录里面查看您的订单", Toast.LENGTH_LONG).show();

                    Timer timer = new Timer();
                    timer.schedule(new TimerTask()
                    {
                        @Override
                        public void run()
                        {
                            //转到未消费订单页面
                            MainActivity.actionStart(OrderDetailActivity.this, 1);
                            finish();
                        }
                    }, 1000);

                    break;
                }
                case -1:
                {
                    //商家端未通过该订单
                    //服务器端在知道该请求未通过后，自动将其放到数据库中的已取消记录里面，所以客户端不需要进行任何操作
                    Dialog.dismiss();
                    Toast.makeText(OrderDetailActivity.this, "很抱歉商家用户太多，暂时没有空场地,请看看别的场馆吧", Toast.LENGTH_LONG).show();

                    Timer timer = new Timer();
                    timer.schedule(new TimerTask()
                    {
                        @Override
                        public void run()
                        {
                            //跳转到主页
                            Intent intent = new Intent(OrderDetailActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }, 1000);

                    break;
                }
                default:
                {
                    //未知错误
                    Dialog.dismiss();
                    Toast.makeText(OrderDetailActivity.this, "发生了未知错误，请稍后重试", Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_detail);

        Intent intent = getIntent();
        gymName = intent.getStringExtra("gym_name");
        orderTime = intent.getStringExtra("order_time");
        reserveDay = intent.getStringExtra("reserve_day");
        reserveHour = intent.getIntExtra("reserve_hour", 0);
        reserveField = intent.getIntExtra("reserve_field", 0);
        price = intent.getFloatExtra("price", 0);
        status = intent.getIntExtra("status", 1);

        globalData = (GlobalData) getApplicationContext();

        TextView tx_gym_name = (TextView) findViewById(R.id.gym_name);
        TextView tx_order_time = (TextView) findViewById(R.id.order_time);
        TextView tx_reserve_time = (TextView) findViewById(R.id.reserve_time);
        TextView tx_reserve_field = (TextView) findViewById(R.id.reserve_field);
        TextView tx_price = (TextView) findViewById(R.id.order_detail_price);

        tx_gym_name.setText("场馆名称: " + gymName);
        tx_order_time.setText("下单时间: " + orderTime);
        tx_reserve_time.setText("预定时间: " + reserveDay + "日" + reserveHour + ":00");
        tx_reserve_field.setText("预定场地: 场地" + reserveField);
        tx_price.setText("价格: " + price + "￥");

        //判断状态，如果为0，则说明是刚下的订单，需要添加倒计时
        if (status == 0)
        {
            btn_wait = new Button(this);
            btn_wait.setBackgroundColor(0xffffb100);
            btn_wait.setTextSize(17);
            btn_wait.setText("提交订单");
            btn_wait.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Dialog = new ProgressDialog(OrderDetailActivity.this);
                    Dialog.setTitle("提示");
                    Dialog.setMessage("正在等待商家审核，请稍候...");
                    Dialog.show();

                    //发送数据到服务器
                    new Thread()
                    {
                        public void run()
                        {
                            //获取用户手机号
                            final String loginPhone = globalData.getLoginAccount();
                            UploadOrderInfo(loginPhone, gymName, status, orderTime, reserveDay, reserveHour, reserveField, price);
                        }
                    }.start();
                }

            });

            LinearLayout main = (LinearLayout) findViewById(R.id.order_detail_wait);
            main.addView(btn_wait);
        }
    }

    public void UploadOrderInfo(String loginPhone, String gymName, int status, String orderTime, String reserveTime, int reserver_hour, int reserveField, float price)
    {
        try
        {
            String charset = "UTF-8";
            String main_url = "http://182.61.8.185:8080/order";
            String query = String.format("user=%s&gym_name=%s&status=%s&order_time=%s&reserve_day=%s&reserve_hour=%s&reserve_field=%s&price=%s",
                    URLEncoder.encode(loginPhone, charset),
                    URLEncoder.encode(gymName, charset),
                    URLEncoder.encode(String.valueOf(status), charset),
                    URLEncoder.encode(orderTime, charset),
                    URLEncoder.encode(reserveTime, charset),
                    URLEncoder.encode(String.valueOf(reserver_hour), charset),
                    URLEncoder.encode(String.valueOf(reserveField), charset),
                    URLEncoder.encode(String.valueOf(price), charset));

            // 根据地址创建URL对象(网络访问的url)
            URL url = new URL(main_url + "?" + query);
            // url.openConnection()打开网络链接
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("GET");// 设置请求的方式
            urlConnection.setReadTimeout(5000);// 设置超时的时间
            urlConnection.setConnectTimeout(5000);// 设置链接超时的时间
            urlConnection.setRequestProperty("Accept-Charset", charset);
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
                while ((len = is.read(buffer)) != -1)
                {
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

                Message message = new Message();
                message.what = Integer.parseInt(result);
                handler.sendMessage(message);

            } else
            {
                System.out.println("------------------链接失败-----------------");
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void actionStart(Context context, String gymName, String orderTime, String reserveDay, int reserveHour, int reserveField, float price, int status)
    {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra("gym_name", gymName);
        intent.putExtra("order_time", orderTime);
        intent.putExtra("reserve_day", reserveDay);
        intent.putExtra("reserve_hour", reserveHour);
        intent.putExtra("reserve_field", reserveField);
        intent.putExtra("price", price);
        intent.putExtra("status", status);
        context.startActivity(intent);
    }

    class TimeCount extends CountDownTimer
    {
        public TimeCount(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }

        @Override

        public void onTick(long millisUntilFinished)
        {
            btn_wait.setClickable(false);
            btn_wait.setText("正在审核，请等待 " + millisUntilFinished / 1000 + "s");
            btn_wait.setTextColor(Color.WHITE);
        }

        @Override
        public void onFinish()
        {
            btn_wait.setText("查看商家审核结果");
            btn_wait.setClickable(true);
        }
    }
}
