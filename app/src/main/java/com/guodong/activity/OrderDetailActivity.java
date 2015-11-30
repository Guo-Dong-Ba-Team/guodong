package com.guodong.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guodong.R;

public class OrderDetailActivity extends Activity
{
    String gymName = "";
    String OrderTime = "";
    String ReserveTime = "";
    int ReserveField= -1;
    float price = -1;
    int status = -1;

    Button btn_wait = null;
    private TimeCount time = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_detail);

        Intent intent = getIntent();
        gymName = intent.getStringExtra("gym_name");
        OrderTime = intent.getStringExtra("order_time");
        ReserveTime = intent.getStringExtra("reserve_time");
        ReserveField = intent.getIntExtra("reserve_field", 0);
        price = intent.getFloatExtra("price", 0);
        status = intent.getIntExtra("status", 1);

        TextView tx_gym_name = (TextView) findViewById(R.id.gym_name);
        TextView tx_order_time = (TextView) findViewById(R.id.order_time);
        TextView tx_reserve_time = (TextView) findViewById(R.id.reserve_time);
        TextView tx_reserve_field = (TextView) findViewById(R.id.reserve_field);
        TextView tx_price = (TextView) findViewById(R.id.order_detail_price);

        tx_gym_name.setText("场馆名称: "+gymName);
        tx_order_time.setText("下单时间: "+OrderTime);
        tx_reserve_time.setText("预定时间: "+ReserveTime);
        tx_reserve_field.setText("预定场地: 场地"+ReserveField);
        tx_price.setText("价格: "+price+"￥");

        //判断状态，如果为0，则说明是刚下的订单，需要添加倒计时
        if (status == 0)
        {
            time = new TimeCount(120000,1000);
            btn_wait = new Button(this);
            btn_wait.setBackgroundColor(Color.GRAY);
            btn_wait.setText("订单已经提交，点击查看审核结果");
            btn_wait.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    time.start();
                }
            });

            LinearLayout main = (LinearLayout) findViewById(R.id.order_detail_wait);
            main.addView(btn_wait);
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

    public static void actionStart(Context context, String gymName, String orderTime, String reserveTime, int reserveField, float price, int status)
    {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra("gym_name", gymName);
        intent.putExtra("order_time", orderTime);
        intent.putExtra("reserve_time", reserveTime);
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
            btn_wait.setText("正在审核，请等待 "+millisUntilFinished / 1000 + "s");
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
