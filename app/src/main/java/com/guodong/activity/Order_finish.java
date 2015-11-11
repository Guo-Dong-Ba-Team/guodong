package com.guodong.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.String ;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.guodong.R;
import com.guodong.model.OrderInfo;
import com.guodong.util.JsonParse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by blarrow on 2015/10/10.
 */
public class Order_finish extends Activity {
    private Context mContext;
    private ListView mlistview;
    private RequestQueue requestQueue;
    private ArrayList<OrderInfo> morderInfo;

    //自定义适配器
    private class mAdapter extends BaseAdapter {
        public int getCount() {
            return morderInfo.size();//数据总数
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(Order_finish.this, R.layout.order_no_item, null);
            TextView tv_purchasetime = (TextView) view.findViewById(R.id.tv_purchasetime);
            TextView tv_sportsname = (TextView) view.findViewById(R.id.tv_sportsname);
            TextView tv_money = (TextView) view.findViewById(R.id.tv_money);
            OrderInfo orderInfo = morderInfo.get(position);
            tv_purchasetime.setText(orderInfo.getTime());
            tv_sportsname.setText(orderInfo.getName());
            tv_money.setText((int) orderInfo.getMoney()) ;

            return view;
        }
        //条目对象
        public Object getItem(int position) {
            return null;
        }
        //条目id
        public long getItemId(int position) {
            return 0;
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.order_no_item);
        mlistview= (ListView) findViewById(R.id.lv_order_no);
        mContext = this;

        Intent intent = getIntent();
        //final String orderInfo = intent.getStringExtra("response");
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringBuilder infolUrl = new StringBuilder();
        infolUrl.append("http://182.61.8.185:8080/order_info?status=1");
        //infolUrl.append("http://10.0.3.2/order_info?status=0");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(infolUrl.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("-----------------执行请求-----------------");
                            morderInfo = JsonParse.ParseorderInfos(response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("-----------------请求失败-----------------");
            }
        });
        requestQueue.add(jsonObjectRequest);


    }



}
