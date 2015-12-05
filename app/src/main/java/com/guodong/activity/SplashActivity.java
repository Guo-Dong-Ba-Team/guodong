package com.guodong.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

import com.guodong.R;

/**
 * Created by yechy on 2015/12/2.
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler();
        handler.postDelayed(new splashHandler(), 1500);
    }

    class splashHandler implements Runnable {
        @Override
        public void run() {
            MainActivity.actionStart(SplashActivity.this, 0);
            SplashActivity.this.finish();
        }
    }

    //屏蔽返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return false;
    }
}
