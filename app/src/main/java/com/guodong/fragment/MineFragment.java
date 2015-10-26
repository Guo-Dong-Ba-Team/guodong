package com.guodong.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guodong.R;
import com.guodong.activity.LoginActivity;
import com.guodong.activity.UserInfo;
import com.guodong.model.GlobalData;

/**
 * Created by yechy on 2015/9/25.
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    private View view;
    GlobalData globalData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mine_fragment, container, false);

        LinearLayout loginLayout = (LinearLayout) view.findViewById(R.id.login_layout);
        TextView loginText = (TextView) view.findViewById(R.id.login_text);
        TextView collectBtn = (TextView) view.findViewById(R.id.collect_btn);
        TextView moreBtn = (TextView) view.findViewById(R.id.more_btn);

        globalData = (GlobalData) getActivity().getApplicationContext();
        if(globalData.getIsLogin()) {
            loginText.setText(globalData.getLoginAccount());
        }

        loginLayout.setOnClickListener(this);
        collectBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_layout:
                    if (!globalData.getIsLogin()) {
                        LoginActivity.actionStart(getActivity());
                    } else {
                        UserInfo.actionStart(getActivity(), globalData.getLoginAccount());
                    }
                    break;
                case R.id.collect_btn:
                    if (globalData.getIsLogin()) {
                        Toast.makeText(getActivity(), "我的收藏", Toast.LENGTH_SHORT).show();
                    } else {
                        LoginActivity.actionStart(getActivity());
                    }
                    break;
                case R.id.more_btn:
                    break;
            }
    }

}
