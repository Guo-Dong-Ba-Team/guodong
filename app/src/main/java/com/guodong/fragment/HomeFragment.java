package com.guodong.fragment;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.guodong.R;
import com.guodong.activity.BaiduMapActivity;
import com.guodong.activity.SearchActivity;
import com.guodong.activity.SportVenueDetailActivity;
import com.guodong.model.Gym;
import com.guodong.util.AdsAdapter;
import com.guodong.util.GymAdapter;
import com.guodong.util.SportItemView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yechy on 2015/9/20.
 */
public class HomeFragment extends Fragment {
    private List<Gym> gymList = new ArrayList<Gym>();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        //获取当前位置
        locateCurrentPosition();

        //初始化主页标题栏
        initTitle();

        //初始化gymList数据
        initGymData();
        GymAdapter gymAdapter = new GymAdapter(getActivity(), R.layout.gym_item, gymList);
        ListView listView = (ListView) view.findViewById(R.id.home_listview);

        headerView = LayoutInflater.from(getActivity()).inflate(R.layout.listview_header, null);
        //初始化运动项目选择区域
        initSport();
        //初始化广告区域
        initViewPager();
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
        }
    }

    private void initViewPager() {
        homeViewPager = (ViewPager) headerView.findViewById(R.id.home_viewpager);
        ViewGroup dotContainer = (ViewGroup) headerView.findViewById(R.id.dot_container);

        //创建广告页
        List<View> adsImageList = new ArrayList<View>();
        ImageView ads1 = new ImageView(getActivity());
        ads1.setImageResource(R.drawable.iss);
        adsImageList.add(ads1);
        ImageView ads2 = new ImageView(getActivity());
        ads2.setBackgroundColor(Color.RED);
        adsImageList.add(ads2);
        ImageView ads3 = new ImageView(getActivity());
        ads3.setBackgroundColor(Color.BLUE);
        adsImageList.add(ads3);
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

        sportView1.setSportImage(R.drawable.sport);
        sportView1.setSportText("羽毛球");

        sportView2.setSportImage(R.drawable.sport);
        sportView2.setSportText("乒乓球");

        sportView3.setSportImage(R.drawable.sport);
        sportView3.setSportText("网球");

        sportView4.setSportImage(R.drawable.sport);
        sportView4.setSportText("健身馆");



    }

    private void initGymData() {
        gymList.clear();
        for (int i = 0; i < 8; i++) {
            Gym gym1 = new Gym();
            gymList.add(gym1);
        }

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


private final Handler viewHandler = new Handler() {
    @Override
public void handleMessage(Message msg) {
        homeViewPager.setCurrentItem(msg.what);
        super.handleMessage(msg);
    }
};

}
