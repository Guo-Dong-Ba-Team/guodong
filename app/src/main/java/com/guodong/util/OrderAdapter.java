package com.guodong.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.guodong.R;
import com.guodong.model.OrderInfo;

import java.util.List;

/**
 * Created by yechy on 2015/11/27.
 */
public class OrderAdapter extends ArrayAdapter<OrderInfo> {
    private int resourceId;

    public OrderAdapter(Context context, int textViewResourceId, List<OrderInfo> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderInfo orderInfo = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view = convertView;
        }
        TextView gymNameTV = (TextView) view.findViewById(R.id.order_gymname);
        TextView bookTimeTV = (TextView) view.findViewById(R.id.order_booktime);
        TextView orderTimeTV = (TextView) view.findViewById(R.id.order_ordertime);
        TextView priceTV = (TextView) view.findViewById(R.id.order_price);

        gymNameTV.setText(orderInfo.getGymName());
        bookTimeTV.setText("预订时间： " + orderInfo.getBookTime());
        orderTimeTV.setText(orderInfo.getOrderTime() + " 下单");
        priceTV.setText("￥" + orderInfo.getPrice());

        return view;
    }
}
