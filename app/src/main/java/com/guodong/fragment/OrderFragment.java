package com.guodong.fragment;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.guodong.R;
import com.guodong.activity.LoginActivity;
import com.guodong.activity.MainActivity;
import com.guodong.activity.Order_cancel;
import com.guodong.activity.Order_no;
import com.guodong.model.GlobalData;

import java.util.ArrayList;
import java.util.HashMap;


public class OrderFragment extends Fragment {

    public OrderFragment() {
    }

    private MainActivity mMainActivity;
    private ListView mListView;
    private SimpleAdapter myAdapter;
    private FragmentManager mFragmentManager;
    private GlobalData globalData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View orderLayout = inflater.inflate(R.layout.fragment_main, container,
                false);
        mMainActivity = (MainActivity) getActivity();
        mFragmentManager = getActivity().getFragmentManager();
        mListView = (ListView) orderLayout.findViewById(R.id.listview_order);
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        String strOrders[] = {"未消费订单", "已取消订单", "已完成订单"};
        for (int i = 0; i < 3; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("order_text", strOrders[i]);
            mylist.add(map);
        }
        myAdapter = new SimpleAdapter(mMainActivity,
                mylist,
                R.layout.order_item,
                new String[]{"order_text"},
                new int[]{R.id.order_text});
        mListView.setAdapter(myAdapter);

        mListView.setOnItemClickListener(onItemClickListener);
        return orderLayout;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //MainActivity.currFragTag = com.guodong.model.Constant.FRAGMENT_FLAG_Order;
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            if (new GlobalData().getIsLogin()) {
                if (position == 0) {
                    Intent intent = new Intent(mMainActivity, Order_no.class);
                    startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(mMainActivity, Order_cancel.class);
                    startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent(mMainActivity, Order_no.class);
                    startActivity(intent);
                }
            } else {
                LoginActivity.actionStart(getActivity());
            }
        }

    };
}
