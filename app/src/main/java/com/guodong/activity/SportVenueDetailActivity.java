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
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.guodong.R;
import com.guodong.model.GymDetail;
import com.guodong.util.JsonParse;
import com.guodong.util.Traffic;

import org.json.JSONException;
import org.json.JSONObject;

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
        detailUrl.append("http://182.61.8.185:8080/gym_info_detail?gym_id=3");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(detailUrl.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            gymDetail = JsonParse.ParseDetailGymInfo(response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public  void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonObjectRequest);

        initView();

        Button btnOrderNow = (Button) findViewById(R.id.btn_order_now);
        Button btnVipBuy = (Button) findViewById(R.id.btn_vip_buy);
        TextView gymNameTextview = (TextView) findViewById(R.id.detail_gym_name);
        TextView openTimeTextview = (TextView) findViewById(R.id.detail_open_time);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.detail_rating);
        TextView singlePriceTextview = (TextView) findViewById(R.id.detail_single_price);
        TextView vipPriceTextview = (TextView) findViewById(R.id.detail_vip_price);
        TextView facilityTextview = (TextView) findViewById(R.id.detail_facility);
        TextView serviceTextview = (TextView) findViewById(R.id.detail_service);

        gymNameTextview.setText(gymDetail.getName());
        openTimeTextview.setText("营业时间: " + gymDetail.getOpen_time());
        ratingBar.setRating(gymDetail.getStar_level());
        singlePriceTextview.setText("￥" + gymDetail.getSingle_price());
        vipPriceTextview.setText("￥" + gymDetail.getVip_price());
        facilityTextview.setText(gymDetail.getHardware());
        serviceTextview.setText(gymDetail.getService());

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
