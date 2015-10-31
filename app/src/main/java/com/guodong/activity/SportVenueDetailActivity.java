package com.guodong.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.guodong.R;
import com.guodong.model.GymDetail;
import com.guodong.util.JsonParse;
import com.guodong.util.Traffic;

import org.json.JSONException;

public class SportVenueDetailActivity extends Activity
{

    private RequestQueue requestQueue;
    private GymDetail gymDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sport_venue_detail);

        Intent intent = getIntent();
        String gymName = intent.getStringExtra("gym_name");

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        //加载场馆详细信息
        StringBuilder detailUrl = new StringBuilder();
        String response = Traffic.sendRequest(detailUrl.toString(), requestQueue );
        try {
            gymDetail = JsonParse.ParseDetailGymInfo(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        initView();

        Button btnOrderNow = (Button) findViewById(R.id.btn_order_now);
        Button btnVipBuy = (Button) findViewById(R.id.btn_vip_buy);

        btnOrderNow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SportVenueDetailActivity.this, GymSelectActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initView() {
        NetworkImageView imageView = (NetworkImageView) findViewById(R.id.detail_show_image);
        TextView textView = (TextView) findViewById(R.id.detail_image_num);
        //
        final String[] imageIntro = null;
        //显示图片数
        textView.setText("" + gymDetail.getGymImageUrl().length);
        //显示封面图片
        Traffic.showNetworkImage(getApplicationContext(), requestQueue, gymDetail.getGymImageUrl()[0], imageView);
        //浏览所有图片
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gymDetail.getGymImageUrl().length > 0) {
                    DisplayImageActivity.actionStart(getApplicationContext(), gymDetail.getGymImageUrl(), imageIntro);
                }
            }
        });
    }

    public static void actionStart(Context context, String gymName) {
        Intent intent = new Intent(context, SportVenueDetailActivity.class);
        intent.putExtra("gym_name", gymName);
        context.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sport_venue_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
