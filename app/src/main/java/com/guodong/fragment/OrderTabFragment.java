package com.guodong.fragment;

import android.os.Bundle;
import android.os.Looper;
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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.guodong.R;
import com.guodong.model.GlobalData;
import com.guodong.model.OrderInfo;
import com.guodong.util.JsonParse;
import com.guodong.util.OrderAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yechy on 2015/11/27.
 */
public class OrderTabFragment extends Fragment {
    private ListView listView;
    private List<OrderInfo> orderInfoList = new ArrayList<>();
    private List<OrderInfo> orderInfoList1 = new ArrayList<>();
    private OrderInfo orderInfo;
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
                            orderInfoList.addAll(JsonParse.ParseorderInfos(response.toString()));
                            if (orderStatus == 1) {
                                orderInfoList1.clear();
                                orderInfoList1.addAll(orderInfoList);
                            }
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
            orderInfo = orderInfoList1.get(position);
            switch (item.getItemId()) {
                case 1:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HttpURLConnection connection = null;
                            try {
                                String spec = "http://182.61.8.185:8080/cancel_order?orderID=" + orderInfo.getOrderId();
                                URL orderUrl = new URL(spec);
                                connection = (HttpURLConnection) orderUrl.openConnection();
                                connection.setRequestMethod("GET");
                                connection.setConnectTimeout(8000);
                                connection.setReadTimeout(8000);
                                connection.setRequestProperty("User-Agent",
                                        "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
                                if (connection.getResponseCode() == 200) {
                                    InputStream in = connection.getInputStream();
                                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                                    StringBuilder response = new StringBuilder();
                                    String line;
                                    while ((line = bufferedReader.readLine()) != null) {
                                        response.append(line);
                                    }
                                    if (response.toString().equals("0")) {
                                        sendRequest(url, requestQueue);
                                    }
                                } else {
                                    Looper.prepare();
                                    Toast.makeText(GlobalData.getContext(), "链接失败",Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (connection != null) {
                                    connection.disconnect();
                                }
                            }
                        }
                    }).start();

                    break;
            }
        }
            return false;

    }

}
