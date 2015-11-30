package com.guodong.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.guodong.R;
import com.guodong.fragment.GymSelectFragment;
import com.guodong.model.GlobalData;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class GymSelectActivity extends Activity
{
    private GlobalData globalData;
    //用来保存每个场地的预订状态0:ordered,1:unselected,2:selected,-1:表示为顶部或左部的文字区域
    int[] orderState = null;
    String orderTime = "";
    String reserveDayStr = "";
    String gymName = "";
    int reserveHour = -1;
    int reserveField = -1;
    int status = -1;
    float price = -1;


    ArrayList<Integer> orderedPosition = null;

    public void setOrderState(int[] orderState)
    {
        this.orderState = orderState;
    }

    //由所有场地的状态得到当前预订的场地,生成订单信息
    public int generateOrderInfo()
    {
        orderedPosition = new ArrayList<>();
        if (orderState != null)
        {
            for (int i = 0; i < orderState.length; i++)
            {
                if (orderState[i] == 2)
                {
                    orderedPosition.add(i);
                }
            }
        }

        if (orderedPosition.size() == 0)
        {
            return 0;
        }

        //构造订单信息，一次下单的几个场地看作不同的订单
        for (int i = 0; i < orderedPosition.size(); i++)
        {
            reserveHour = (orderedPosition.get(i) + 1) / 7 + 7;
            reserveField = ((orderedPosition.get(i) + 1) % 7 + 6) % 7;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            orderTime = df.format(new Date());
            status = 0;
            price = 20.0f;

            //获取用户手机号
            final String loginPhone = globalData.getLoginAccount();

            //发送数据到服务器
            new Thread()
            {
                public void run()
                {
                    UploadOrderInfo(loginPhone, gymName, status, orderTime, reserveDayStr, reserveHour, reserveField, price);
                }
            }.start();
        }
        return orderedPosition.size();
    }

    public void UploadOrderInfo(String loginPhone, String gymName, int status, String orderTime, String reserveTime, int reserver_hour, int reserveField, float price)
    {
        try
        {
            String charset = "UTF-8";
            String main_url = "http://182.61.8.185:8080/order";
            String query = String.format("user=%s&gym_name=%s&status=%s&order_time=%s&reserve_day=%s&reserveHour=%s&reserveField=%s&price=%s",
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


                //订单写入数据库成功
                if (result.equals("0"))
                {
                    //跳转到订单详情页面
                    Intent intent = new Intent(GymSelectActivity.this, OrderDetailActivity.class);
                    startActivity(intent);

                } else //订单商家未通过
                {
                }
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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gym_select);

        Intent intent = getIntent();
        gymName = intent.getStringExtra("gym_name");

        final EditText orderDate = (EditText) findViewById(R.id.date);
        final TextView noticeText = (TextView) findViewById(R.id.notice_text);
        Button btnSubmitOrder = (Button) findViewById(R.id.btn_submit_order);

        globalData = (GlobalData) getApplicationContext();


        orderDate.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    Calendar c = Calendar.getInstance();
                    new DatePickerDialog(GymSelectActivity.this, new DatePickerDialog.OnDateSetListener()
                    {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                        {
                            noticeText.setText("您选择的预订日期是：");
                            orderDate.setText((year) + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");

                            //Date的构造代码里面默认将year+1900，所以这里需要减去1900
                            Date reserve_day = new Date(year - 1900, monthOfYear, dayOfMonth);
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            reserveDayStr = df.format(reserve_day);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
                    ).show();
                }
            }
        });

        orderDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(GymSelectActivity.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        noticeText.setText("您选择的预订日期是：");
                        orderDate.setText((year) + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");

                        //Date的构造代码里面默认将year+1900，所以这里需要减去1900
                        Date reserve_day = new Date(year - 1900, monthOfYear, dayOfMonth);
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        reserveDayStr = df.format(reserve_day);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });


        btnSubmitOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!globalData.getIsLogin())
                {
                    Toast.makeText(GymSelectActivity.this, "请先登录", Toast.LENGTH_LONG).show();
                    LoginActivity.actionStart(GymSelectActivity.this);
                } else
                {
                    GymSelectFragment gymSelectFragment = (GymSelectFragment) getFragmentManager().findFragmentById(R.id.gym_select_fragment);
                    gymSelectFragment.setOrderState();
                    int orderNum = generateOrderInfo();
                    if (orderNum == 0)
                    {
                        Toast.makeText(GymSelectActivity.this, "请至少选择一块场地", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (CalculateDayDiff(orderTime, reserveDayStr) > 7.0)
                    {
                        Toast.makeText(GymSelectActivity.this, "预定日期超过七天，请重新选择日期", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (CalculateDayDiff(orderTime, reserveDayStr) < 0.0)
                    {
                        Toast.makeText(GymSelectActivity.this, "预订日期已过，请重新选择日期", Toast.LENGTH_LONG).show();
                        return;
                    }

                    //跳转到订单详情页面
                    OrderDetailActivity.actionStart(GymSelectActivity.this, gymName, orderTime,
                            reserveDayStr + " " + String.valueOf(reserveHour) + ":00:00",
                            reserveField, price, status);
                }
            }
        });
    }

    double CalculateDayDiff(String orderTime, String reserveTime)
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date orderDate = null;
        Date reserveDate = null;

        try
        {
            orderDate = df.parse(orderTime);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        try
        {
            reserveDate = df.parse(reserveTime);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        GregorianCalendar orderCanlendar = new GregorianCalendar();
        GregorianCalendar reserveCanlendar = new GregorianCalendar();
        orderCanlendar.setTime(orderDate);
        reserveCanlendar.setTime(reserveDate);
        int a = (int) (reserveCanlendar.getTimeInMillis() - orderCanlendar.getTimeInMillis()) / (1000 * 3600 * 24);
        return (reserveCanlendar.getTimeInMillis() - orderCanlendar.getTimeInMillis()) / (1000 * 3600 * 24);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gym_select, menu);
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

    public static void actionStart(Context context, String gymName)
    {
        Intent intent = new Intent(context, GymSelectActivity.class);
        intent.putExtra("gym_name", gymName);
        context.startActivity(intent);
    }
}