package com.guodong.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.guodong.R;

/**
 * Created by blarrow on 2015/10/10.
 */
public class Order_no extends Activity {
    private Context mContext;
    private ListView order_no;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_no);
        order_no=(ListView)findViewById(R.id.lv_order_no);
        mContext=this;


    }
}
