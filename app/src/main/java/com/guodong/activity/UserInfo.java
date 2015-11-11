package com.guodong.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guodong.R;
import com.guodong.model.GlobalData;

/**
 * Created by yechy on 2015/10/22.
 */
public class UserInfo extends Activity implements View.OnClickListener {
    private Button concelBtn;
    private LinearLayout modifyHead;
    private LinearLayout modifyUserName;
    private TextView userNameText;
    GlobalData globalData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        globalData = (GlobalData) getApplicationContext();

        initView();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.concel_btn:
                //将登陆状态改为false，账户信息设为空
                globalData.setIsLogin(false);
                SharedPreferences.Editor editor = getSharedPreferences("loginID",MODE_PRIVATE).edit();
                editor.remove("islogin");
                editor.commit();
                editor.putBoolean("islogin",false);
                editor.commit();
                //回到主活动的我的界面
                MainActivity.actionStart(UserInfo.this, 3);
                finish();
                break;
            case R.id.modify_head:
                Toast.makeText(UserInfo.this, "修改头像", Toast.LENGTH_SHORT).show();
                break;
            case R.id.modify_username:
                Toast.makeText(UserInfo.this, "修改用户名", Toast.LENGTH_SHORT).show();
                break;
            }
        }

    private void initView() {
        concelBtn = (Button) findViewById(R.id.concel_btn);
        modifyHead = (LinearLayout) findViewById(R.id.modify_head);
        modifyUserName = (LinearLayout) findViewById(R.id.modify_username);
        userNameText = (TextView) findViewById(R.id.username_text);
        Intent intent = getIntent();
        String userName = intent.getStringExtra("username");
        userNameText.setText(userName);

        concelBtn.setOnClickListener(this);
        modifyHead.setOnClickListener(this);
        modifyUserName.setOnClickListener(this);

    }

    public static void actionStart(Context context, String userName) {
        Intent intent = new Intent(context, UserInfo.class);
        intent.putExtra("username", userName);
        context.startActivity(intent);
    }
}
