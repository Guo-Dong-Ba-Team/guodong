package com.guodong.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.guodong.R;
import com.guodong.activity.LoginActivity;

/**
 * Created by yechy on 2015/11/27.
 */
public class UnloginFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.unlogin_fragment, container, false);
        Button button = (Button) view.findViewById(R.id.login_bt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.actionStart(getActivity());
            }
        });

        return view;
    }
}
