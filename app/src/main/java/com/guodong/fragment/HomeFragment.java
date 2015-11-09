package com.guodong.fragment;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.guodong.R;
import com.guodong.activity.BaiduMapActivity;
import com.guodong.activity.SearchActivity;
import com.guodong.activity.SportVenueDetailActivity;
import com.guodong.model.Constant;
import com.guodong.model.GlobalData;
import com.guodong.model.Gym;
import com.guodong.util.AdsAdapter;
import com.guodong.util.GymAdapter;
import com.guodong.util.JsonParse;
import com.guodong.util.SportItemView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yechy on 2015/9/20.
 */
public class HomeFragment extends Fragment {
    private List<Gym> gymList = new ArrayList<>();
    private View view;
    private View headerView;
    private ViewPager homeViewPager;
    private ImageView[] dotImageViews;
    private ImageView dotImageView;
    private AtomicInteger what = new AtomicInteger(0);
    private boolean isContinue = true;
    private LocationManager locationManager;
    private String provider;
    private double longitude;
    private double latitude;
    private RequestQueue requestQueue;
    private GlobalData globalData;
    private GymAdapter gymAdapter;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        //判断网络是否连接
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isAvailable()) {
            Toast.makeText(getActivity(), "网络连接不可用", Toast.LENGTH_LONG).show();
        }
        requestQueue = Volley.newRequestQueue(getActivity());

        //获取当前位置
        locateCurrentPosition();

        //初始化主页标题栏
        initTitle();

        //初始化gymList数据
        initGymData();
        gymAdapter = new GymAdapter(getActivity(), R.layout.gym_item, gymList);
        ListView listView = (ListView) view.findViewById(R.id.home_listview);

        headerView = LayoutInflater.from(getActivity()).inflate(R.layout.listview_header, null);
        //初始化运动项目选择区域
        initSport();
        //初始化广告区域
        initViewPager();
        progressBar = (ProgressBar) view.findViewById(R.id.home_progress_bar);
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }

        listView.addHeaderView(headerView, null, false);
        listView.setAdapter(gymAdapter);
        //设置listView点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Gym gym = gymList.get(position);
                SportVenueDetailActivity.actionStart(getActivity(), gym.getGymName());
            }
        });


        return view;
    }

    private void initTitle() {
        TextView cityBtn = (TextView) view.findViewById(R.id.city);
        Button inputGym = (Button) view.findViewById(R.id.input_gym_btn);
        Button openMap = (Button) view.findViewById(R.id.open_map);

        //设置城市
        cityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //查询场馆
        inputGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.actionStart(getActivity(), "beijing");
            }
        });

        //打开地图
        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    BaiduMapActivity.actionStart(getActivity(), longitude, latitude);
            }
        });
    }

    private void locateCurrentPosition() {
        locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(getActivity(), "没有可用的位置提供器",Toast.LENGTH_SHORT).show();
            return;
        }
        Location myLocation = locationManager.getLastKnownLocation(provider);
        if (myLocation != null) {
            longitude = myLocation.getLongitude();
            latitude = myLocation.getLatitude();
            //经纬度保存到全局变量中
            globalData = (GlobalData) getActivity().getApplicationContext();
            globalData.setMyLongitude(longitude);
            globalData.setMyLatitude(latitude);
        }
    }

    private void initViewPager() {
        homeViewPager = (ViewPager) headerView.findViewById(R.id.home_viewpager);
        ViewGroup dotContainer = (ViewGroup) headerView.findViewById(R.id.dot_container);

        //创建广告页
        List<View> adsImageList = new ArrayList<View>();
        NetworkImageView ads1 = new NetworkImageView(getActivity());

        ads1.setBackgroundColor(Color.TRANSPARENT);
        adsImageList.add(ads1);
        NetworkImageView ads2 = new NetworkImageView(getActivity());
        ads2.setBackgroundColor(Color.TRANSPARENT);
        adsImageList.add(ads2);
        NetworkImageView ads3 = new NetworkImageView(getActivity());
        ads3.setBackgroundColor(Color.TRANSPARENT);
        adsImageList.add(ads3);
        //从服务器加载广告图片并显示

        //Traffic.showNetworkImage(getActivity(),requestQueue, adUrl, ads1);

        //对dotImageViews进行填充
        dotImageViews = new ImageView[adsImageList.size()];
        //导航圆点
        for (int i = 0; i < adsImageList.size(); i++) {
            dotImageView = new ImageView(getActivity());
            dotImageView.setLayoutParams(new LinearLayout.LayoutParams(20, 20));
            dotImageView.setPadding(5,5,5,5);
            dotImageViews[i] = dotImageView;
            if (i == 0) {
                dotImageViews[i].setImageResource(R.drawable.dot_selected);
            } else {
                dotImageViews[i].setImageResource(R.drawable.dot);
            }
            dotContainer.addView(dotImageViews[i]);
        }

        homeViewPager.setAdapter(new AdsAdapter(adsImageList));
        homeViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                what.getAndSet(position);
                for (int i = 0; i < dotImageViews.length; i++) {
                    dotImageViews[position].setImageResource(R.drawable.dot_selected);
                    if (position != i) {
                        dotImageViews[i].setImageResource(R.drawable.dot);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        homeViewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        isContinue = false;
                    case MotionEvent.ACTION_UP:
                        isContinue = true;
                        break;
                    default:
                        isContinue = true;
                        break;
                }
                return false;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (isContinue) {
                        viewHandler.sendEmptyMessage(what.get());
                        whatOpition();
                    }
                }
            }
        }).start();

        homeViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动广告活动
            }
        });
    }

    private void initSport() {
        SportItemView sportView1 = (SportItemView) headerView.findViewById(R.id.sport1);
        SportItemView sportView2 = (SportItemView) headerView.findViewById(R.id.sport2);
        SportItemView sportView3 = (SportItemView) headerView.findViewById(R.id.sport3);
        SportItemView sportView4 = (SportItemView) headerView.findViewById(R.id.sport4);
        SportItemView spotrView5 = (SportItemView) headerView.findViewById(R.id.sport5);

        sportView1.setSportImage(R.drawable.yumaoqiu);
        sportView1.setSportText("羽毛球场");

        sportView2.setSportImage(R.drawable.pingpangqiu);
        sportView2.setSportText("乒乓球馆");

        sportView3.setSportImage(R.drawable.youyongguan);
        sportView3.setSportText("游泳馆");

        sportView4.setSportImage(R.drawable.jianshenfang);
        sportView4.setSportText("健身馆");

        spotrView5.setSportImage(R.drawable.taiqiu);
        spotrView5.setSportText("台球厅");
    }

    private void initGymData() {
        gymList.clear();
        //下载场馆列表数据
        StringBuilder gymListUrl = new StringBuilder();
        gymListUrl.append("http://182.61.8.185:8080/gym_info_brief");
        sendRequest(gymListUrl.toString(), requestQueue);

    }

private void whatOpition() {
    what.incrementAndGet();
    if (what.get() > dotImageViews.length - 1) {
        what.getAndAdd(-3);
    }

    try {
        Thread.sleep(8000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}


private Handler viewHandler = new Handler() {
    @Override
public void handleMessage(Message msg) {
        switch (msg.what) {
            case Constant.SHOW_RESPONSE:
                String response = (String) msg.obj;
                try {
                    gymList.clear();
                    gymList.addAll(JsonParse.ParseBriefGymInfo(response));
                    gymAdapter.notifyDataSetChanged();

                    if (progressBar.getVisibility() == View.VISIBLE) {
                        progressBar.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    }

                break;
            default:
            homeViewPager.setCurrentItem(msg.what);
            super.handleMessage(msg);
        }
    }
};


    public void sendRequest(String url, RequestQueue requestQueue) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //
                        Message message = new Message();
                        message.what = Constant.SHOW_RESPONSE;
                        message.obj = response.toString();
                        viewHandler.sendMessage(message);
                    }
                }, new Response.ErrorListener() {
            @Override
            public  void onErrorResponse(VolleyError error) {
                Log.d("YE","home 返回错误信息" + error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
