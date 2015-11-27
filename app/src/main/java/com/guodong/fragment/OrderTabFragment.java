package com.guodong.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private OrderAdapter orderAdapter;
    private String url;
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        Log.d("YE","DEBUG1");
        url = args.getString("url", " ");
        Log.d("YE","DEBUG2" + url);
        View view = inflater.inflate(R.layout.order_tab_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.order_listview);
        orderAdapter = new OrderAdapter(getActivity(), R.layout.order_item, orderInfoList);

        initOrderList();
        listView.setAdapter(orderAdapter);
        return view;
    }

    private void initOrderList() {
        orderInfoList.clear();
        requestQueue = Volley.newRequestQueue(getActivity());
        //sendRequest(url, requestQueue);

        OrderInfo orderInfo = new OrderInfo("天河体育场","12月11日12:00", "12345", 1, 20);
        orderInfoList.add(orderInfo);
        orderInfoList.add(orderInfo);
        orderInfoList.add(orderInfo);

    }

    private void sendRequest(String url, RequestQueue requestQueue) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            orderInfoList.clear();
                            orderInfoList.addAll(JsonParse.ParseorderInfos(response.toString()));
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
}
