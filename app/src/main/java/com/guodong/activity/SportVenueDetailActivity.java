package com.guodong.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private TextView textView;
    private Button btnVipBuy;
    private Button btnOrderNow;
    private TextView gymNameTextview;
    private TextView openTimeTextview;
    private RatingBar ratingBar;
    private TextView singlePriceTextview;
    private TextView vipPriceTextview;
    private TextView facilityTextview;
    private TextView serviceTextview;
    private TextView addressTextview;
    private TextView telephoneTextview;
    private NetworkImageView imageView;
    private int gymId;

    private String gymName;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sport_venue_detail);

        Intent intent = getIntent();
        gymId = intent.getIntExtra("gym_id", 0);
        gymName = intent.getStringExtra("gym_name");

        initView();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        //加载场馆详细信息
        StringBuilder detailUrl = new StringBuilder();
        detailUrl.append("http://182.61.8.185:8080/gym_info_detail?gym_id=").append(gymId);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(detailUrl.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("YE","debug");
                            gymDetail = JsonParse.ParseDetailGymInfo(response.toString(), gymId);
                            Log.d("YE",gymDetail.getName());
                            Log.d("YE",gymDetail.getGymImageUrl()[0]);

                            //显示图片数
                            Log.d("YE","debug 2");
                            textView.setText("" + gymDetail.getGymImageUrl().length);

                            //显示封面图片
                            Log.d("YE","debug 3");
                            Traffic.showNetworkImage(requestQueue, gymDetail.getGymImageUrl()[0], imageView);


                            gymNameTextview.setText(gymDetail.getName());
                            openTimeTextview.setText("营业时间: " + gymDetail.getOpen_time());
                            ratingBar.setRating(gymDetail.getStar_level());
                            singlePriceTextview.setText("￥" + gymDetail.getSingle_price());
//                            vipPriceTextview.setText("￥" + gymDetail.getVip_price());
                            facilityTextview.setText(gymDetail.getHardware());
                            serviceTextview.setText(gymDetail.getService());
                            addressTextview.setText(gymDetail.getAddress_detail());
                            telephoneTextview.setText(gymDetail.getPhone_num());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public  void onErrorResponse(VolleyError error) {
                Log.d("YE", "gymdetail 返回错误信息" + error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);

        btnOrderNow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {                
            	GymSelectActivity.actionStart(SportVenueDetailActivity.this,gymName, gymDetail.getOpen_time(), 6, gymDetail.getSingle_price());
            }
        });

        //浏览所有图片
       imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gymDetail.getGymImageUrl().length > 0) {
                final String[] imageIntro = new String[gymDetail.getGymImageUrl().length];
                    for(int i = 0; i < gymDetail.getGymImageUrl().length; i++) {
                        imageIntro[i] = "intro";
                    }
                    DisplayImageActivity.actionStart(SportVenueDetailActivity.this, gymDetail.getGymImageUrl(), imageIntro);
                }
            }
        });

    }

    private void initView() {
        btnOrderNow = (Button) findViewById(R.id.btn_order_now);
        btnVipBuy = (Button) findViewById(R.id.btn_vip_buy);
        gymNameTextview = (TextView) findViewById(R.id.detail_gym_name);
        openTimeTextview = (TextView) findViewById(R.id.detail_open_time);
        ratingBar = (RatingBar) findViewById(R.id.detail_rating);
        singlePriceTextview = (TextView) findViewById(R.id.detail_single_price);
       // vipPriceTextview = (TextView) findViewById(R.id.detail_vip_price);
        facilityTextview = (TextView) findViewById(R.id.detail_facility);
        serviceTextview = (TextView) findViewById(R.id.detail_service);
        imageView = (NetworkImageView) findViewById(R.id.detail_show_image);
        textView = (TextView) findViewById(R.id.detail_image_num);
        addressTextview = (TextView) findViewById(R.id.detail_address);
        telephoneTextview = (TextView) findViewById(R.id.detail_telephone);
        //

    }

    public static void actionStart(Context context, int gymId, String gymName) {
        Intent intent = new Intent(context, SportVenueDetailActivity.class);
        intent.putExtra("gym_id", gymId);
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
