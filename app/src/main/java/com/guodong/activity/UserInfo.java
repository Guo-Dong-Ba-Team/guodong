package com.guodong.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guodong.R;

/**
 * Created by yechy on 2015/10/22.
 */
public class UserInfo extends Activity implements View.OnClickListener {
    private Button concelBtn;
    private LinearLayout modifyHead;
    private LinearLayout modifyUserName;
    private TextView userNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        initView();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.concel_btn:
                //回到主活动的我的界面

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
        concelBtn.setOnClickListener(this);
        modifyHead.setOnClickListener(this);
        modifyUserName.setOnClickListener(this);

    }

    public static void actionStart(Context context, String userName) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra("username", userName);
        context.startActivity(intent);
    }
}
