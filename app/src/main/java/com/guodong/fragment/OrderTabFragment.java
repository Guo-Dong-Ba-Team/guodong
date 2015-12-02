package com.guodong.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.guodong.R;
import com.guodong.model.OrderInfo;
import com.guodong.util.JsonParse;
import com.guodong.util.OrderAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yechy on 2015/11/27.
 */
public class OrderTabFragment extends Fragment {
    private ListView listView;
    private List<OrderInfo> orderInfoList = new ArrayList<>();
    private List<OrderInfo> orderInfoList1 = new ArrayList<>();
    private OrderAdapter orderAdapter;
    private String url;
    private int orderStatus;
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        url = args.getString("url", " ");
        orderStatus = args.getInt("status");
        View view = inflater.inflate(R.layout.order_tab_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.order_listview);
        initOrderList();
        if (orderStatus == 1) {
            listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
                    menu.setHeaderTitle("确定取消订单？");
                    menu.add(0, 1, Menu.NONE, "确定");
                }
            });
        }
        orderAdapter = new OrderAdapter(getActivity(), R.layout.order_item, orderInfoList);
        listView.setAdapter(orderAdapter);
        return view;
    }

    private void initOrderList() {
        requestQueue = Volley.newRequestQueue(getActivity());
        sendRequest(url, requestQueue);

        //OrderInfo orderInfo = new OrderInfo("天河体育场", 12, "12月11日12:00", "12345", 1, 20);
        //orderInfoList.add(orderInfo);
        //orderInfoList.add(orderInfo);
        //orderInfoList.add(orderInfo);

    }

    private void sendRequest(String url, RequestQueue requestQueue) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            orderInfoList.clear();
                            Log.d("YE", "orderStatus " + orderStatus);
                            orderInfoList.addAll(JsonParse.ParseorderInfos(response.toString()));
                            if (orderStatus == 1) {
                                orderInfoList1.clear();
                                orderInfoList1.addAll(orderInfoList);
                            }
                            Log.d("YE", "orderInfoList " + orderInfoList.size());
                            orderAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public  void onErrorResponse(VolleyError error) {
                Log.d("YE", "orderInfoList 返回错误信息" + error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (orderStatus == 1) {
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int position = menuInfo.position;
            Log.d("YE", "orderStatus2 " + orderStatus);
            Log.d("YE", "orderInfoList2 " + orderInfoList1.size());
            OrderInfo orderInfo = orderInfoList1.get(position);
            switch (item.getItemId()) {
                case 1:

                    break;
            }
        }
            return false;

    }
}
