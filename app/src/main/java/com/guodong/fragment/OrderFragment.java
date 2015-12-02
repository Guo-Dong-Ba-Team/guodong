package com.guodong.fragment;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.guodong.R;
import com.guodong.activity.MainActivity;
import com.guodong.model.GlobalData;
import com.guodong.util.ImageFragAdapter;

import java.util.ArrayList;


public class OrderFragment extends Fragment {

    private MainActivity mMainActivity;
    private View orderView;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragmentArrayList;
    private ImageView cursorImage;
    private TextView orderTab1, orderTab2, orderTab3;
    private int currIndex;//当前页卡编号
    private int bmpW;//横线图片宽度
    private int offset;//图片移动的偏移量
    private ArrayList<TextView> textViewList;
    private GlobalData globalData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        orderView = inflater.inflate(R.layout.order_fragment, container,
                false);
        mMainActivity = (MainActivity) getActivity();
        globalData = (GlobalData) mMainActivity.getApplicationContext();

        initTextView();
        initImage();
        initViewPager();

        return orderView;
    }

    private void initTextView() {
        orderTab1 = (TextView) orderView.findViewById(R.id.order_tab1);
        orderTab2 = (TextView) orderView.findViewById(R.id.order_tab2);
        orderTab3 = (TextView) orderView.findViewById(R.id.order_tab3);
        textViewList = new ArrayList<TextView>();
        textViewList.add(orderTab1);
        textViewList.add(orderTab2);
        textViewList.add(orderTab3);

        orderTab1.setOnClickListener(new txListener(0));
        orderTab2.setOnClickListener(new txListener(1));
        orderTab3.setOnClickListener(new txListener(2));
    }

    public class txListener implements View.OnClickListener{
        private int index = 0;

        public txListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(index);
        }
    }

    /*
    * 初始化图片的位移像素
    */
    private void initImage() {
        cursorImage = (ImageView) orderView.findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.cursor).getWidth();
        DisplayMetrics dm = new DisplayMetrics();
        mMainActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (screenW/3 - bmpW)/2;

        //cursorimage设置平移，使下划线平移到初始位置
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursorImage.setImageMatrix(matrix);
    }

    private void initViewPager() {
        viewPager = (ViewPager) orderView.findViewById(R.id.order_viewpager);
        fragmentArrayList = new ArrayList<>();
        Fragment fragment1 = new OrderTabFragment();
        Bundle args1 = new Bundle();
        args1.putString("url", "http://182.61.8.185:8080/order_info?user=" +globalData.getLoginAccount() + "&status=1");
        args1.putInt("status", 1);
        fragment1.setArguments(args1);
        Fragment fragment2 = new OrderTabFragment();
        Bundle args2 = new Bundle();
        args2.putString("url", "http://182.61.8.185:8080/order_info?user=" +globalData.getLoginAccount() + "&status=2");
        args2.putInt("status",2);
        fragment2.setArguments(args2);
        Fragment fragment3 = new OrderTabFragment();
        Bundle args3 = new Bundle();
        args3.putString("url", "http://182.61.8.185:8080/order_info?user=" +globalData.getLoginAccount() + "&status=3");
        args3.putInt("status",3);
        fragment3.setArguments(args3);
        fragmentArrayList.add(fragment1);
        fragmentArrayList.add(fragment2);
        fragmentArrayList.add(fragment3);

        viewPager.setAdapter(new ImageFragAdapter(mMainActivity.getSupportFragmentManager(), fragmentArrayList));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{
        private int one = offset * 2 + bmpW;//俩个相邻页面的偏移量

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public void onPageSelected(int position) {
            Animation animation = new TranslateAnimation(currIndex*one, position*one, 0 ,0);//平移动画
            currIndex = position;
            animation.setFillAfter(true);//动画终止时停留在最后一帧，不然会回到没有执行前的状态
            animation.setDuration(200);
            cursorImage.startAnimation(animation);//用imageview来显示动画

            for(int i = 0; i < textViewList.size(); i++) {
                if (i != position) {
                    textViewList.get(i).setTextColor(Color.GRAY);
                }
                textViewList.get(position).setTextColor(Color.RED);

            }
        }
    }
}
