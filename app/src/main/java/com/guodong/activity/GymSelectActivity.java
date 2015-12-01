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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class GymSelectActivity extends Activity
{
    private GlobalData globalData;
    ArrayList<Integer> orderedPosition = null;
    //用来保存每个场地的预订状态0:ordered,1:unselected,2:selected,-1:表示为顶部或左部的文字区域
    int[] orderState = null;
    String orderTime = "";
    String reserveDayStr = "";
    String gymName = "";
    String openTime = "";
    int fieldNum = 0;
    float price = -1;

    int reserveHour = -1;
    int reserveField = -1;
    int status = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        gymName = intent.getStringExtra("gym_name");
        openTime = intent.getStringExtra("open_time");
        fieldNum = intent.getIntExtra("field_num", 0);
        price = intent.getFloatExtra("price", 0.0f);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gym_select);


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
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask()
                    {
                        @Override
                        public void run()
                        {
                            LoginActivity.actionStart(GymSelectActivity.this);
                        }
                    }, 1000);
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
                            reserveDayStr, reserveHour, reserveField, price, status);
                }
            }
        });
    }

    public void setOrderState(int[] orderState)
    {
        this.orderState = orderState;
    }

    public float getPrice()
    {
        return price;
    }

    public String getOpenTime()
    {
        return openTime;
    }

    public int getFieldNum()
    {
        return fieldNum;
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
        }
        return orderedPosition.size();
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

    public static void actionStart(Context context, String gymName, String openTime, int fieldNum, float price)
    {
        Intent intent = new Intent(context, GymSelectActivity.class);
        intent.putExtra("gym_name", gymName);
        intent.putExtra("open_time", openTime);
        intent.putExtra("field_num", fieldNum);
        intent.putExtra("price", price);
        context.startActivity(intent);
    }
}