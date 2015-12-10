package com.guodong.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.guodong.model.UpdateInfo;
import com.guodong.util.DownLoadManager;
import com.guodong.util.UpdateInfoParse;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yechy on 2015/9/25.
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    private View view;
    GlobalData globalData;
    private TextView checkUpdateBtn;
    private UpdateInfo info;
    private String localVersion;
    private final int UPDATE_NONEED = 0;
    private final int UPDATE_CLIENT = 1;
    private final int GET_UPDATEINFO_ERROR = 2;
    private final int SDCARD_NOMOUNTED = 3;
    private final int DOWN_ERROR = 4;
    private final String TAG = "APK_UPDATE";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mine_fragment, container, false);

        LinearLayout loginLayout = (LinearLayout) view.findViewById(R.id.login_layout);
        TextView loginText = (TextView) view.findViewById(R.id.login_text);
        TextView collectBtn = (TextView) view.findViewById(R.id.collect_btn);
        checkUpdateBtn = (TextView) view.findViewById(R.id.check_update_btn);

        globalData = (GlobalData) getActivity().getApplicationContext();
        if(globalData.getIsLogin()) {
            loginText.setText(globalData.getLoginAccount());
        }

        loginLayout.setOnClickListener(this);
        collectBtn.setOnClickListener(this);
        checkUpdateBtn.setOnClickListener(this);
        try {
            Log.d("YE",getVersionName());
        } catch (Exception e) {
            Log.d("YE","error");
            e.printStackTrace();
        }
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
                case R.id.check_update_btn:
                    try{
                        localVersion = getVersionName();
                        CheckVersionTask cv = new CheckVersionTask();
                        new Thread(cv).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            }
    }

    public String getVersionName() throws Exception {

        PackageManager packageManager = getActivity().getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
        return packageInfo.versionName;
    }

    public class CheckVersionTask implements Runnable {
        InputStream is;

        @Override
        public void run() {
            try {
                String path = getResources().getString(R.string.url_server);
                URL url = new URL(path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    //从服务器获得一个输入流
                    is = connection.getInputStream();
                }
                info = UpdateInfoParse.getUpdateInfo(is);
                if (info.getVersion().equals(localVersion)) {
                    Log.i(TAG, "版本号相同");
                    Message msg = new Message();
                    msg.what = UPDATE_NONEED;
                    handler.sendMessage(msg);
                } else {
                    Log.i(TAG, "版本号不同");
                    Message msg = new Message();
                    msg.what = UPDATE_CLIENT;
                    handler.sendMessage(msg);
                }
            } catch (Exception e) {
                Message msg = new Message();
                msg.what = GET_UPDATEINFO_ERROR;
                handler.sendMessage(msg);
                e.printStackTrace();
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_NONEED:
                    Toast.makeText(getActivity(), "已是最新版本", Toast.LENGTH_SHORT).show();
                    break;
                case UPDATE_CLIENT:
                    //对话框通知用户升级
                    showUpdateDialog();
                    break;
                case GET_UPDATEINFO_ERROR:
                    //服务器超时
                    Toast.makeText(getActivity(), "获取服务器更新信息失败", Toast.LENGTH_SHORT).show();
                    break;
                case DOWN_ERROR:
                    //下载apk失败
                    Toast.makeText(getActivity(), "下载新版本失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    protected void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("版本升级");
        builder.setMessage(info.getDescription());
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "下载apk，更新");
                downLoadApk();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /*
    *从服务器下载APK
     */
    protected void downLoadApk() {
        final ProgressDialog pd;
        pd = new ProgressDialog(getActivity());
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = DownLoadManager.getFileFromServer(info.getUrl(), pd);
                    sleep(3000);
                    installApk(file);
                    pd.dismiss();

                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = DOWN_ERROR;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

}
