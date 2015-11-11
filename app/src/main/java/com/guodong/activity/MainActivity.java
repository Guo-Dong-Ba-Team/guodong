package com.guodong.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.guodong.R;
import com.guodong.fragment.HomeFragment;
import com.guodong.fragment.MineFragment;
import com.guodong.fragment.OrderFragment;
import com.guodong.fragment.OtherFragment;

/**
 * Created by yechy on 2015/9/21.
 */
public class MainActivity extends FragmentActivity {

    private FragmentTabHost tabHost;
    private String tabNames[] = {"主页", "订单", "动吧", "我的"};
    private int tabImages[] = {R.drawable.tab_home_selector, R.drawable.tab_order_selector,
            R.drawable.tab_dongba_selector, R.drawable.tab_mine_selector};
    private Class fragmentArray[] = {HomeFragment.class, OrderFragment.class, OtherFragment.class,
            MineFragment.class};

    private int tabIndex;
    private int mBackKeyPressedTimes = 0;

    protected void onCreate(Bundle savedInsatnceState) {
        super.onCreate(savedInsatnceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        tabIndex = intent.getIntExtra("tabindex",0);

        initView();
    }

    private void initView() {

        tabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.maincontent);

        for (int i = 0; i < tabNames.length; i++ ) {
            TabHost.TabSpec spec = tabHost.newTabSpec(tabNames[i]).setIndicator(getView(i));
            tabHost.addTab(spec, fragmentArray[i], null);
            //tabHost.getTabWidget().getChildAt(i).setBackgroundColor();

        }

        tabHost.setCurrentTab(tabIndex);
    }

    private View getView(int i) {
        View myView = View.inflate(MainActivity.this, R.layout.tab_item, null);
        ImageView imageView = (ImageView) myView.findViewById(R.id.tab_image);
        TextView textView = (TextView) myView.findViewById(R.id.tab_text);
        imageView.setImageResource(tabImages[i]);
        textView.setText(tabNames[i]);
        return  myView;
    }

    public static void actionStart(Context context, int tabIndex) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("tabindex", tabIndex);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (mBackKeyPressedTimes == 0) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mBackKeyPressedTimes = 1;
            new Thread() {
                @Override
            public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        mBackKeyPressedTimes = 0;
                    }
                }
            }.start();
            return;
        } else {
            finish();
            System.exit(0);
        }
        super.onBackPressed();
    }
}
