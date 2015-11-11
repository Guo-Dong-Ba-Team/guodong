package com.guodong.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.guodong.R;
import com.guodong.model.Gym;
import com.guodong.util.GymAdapter;
import com.guodong.util.JsonParse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GymListActivity extends Activity
{
    private List<Gym> gymList = new ArrayList<>();
    private RequestQueue requestQueue;
    private Context context = GymListActivity.this;
    private GymAdapter gymAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sport_venue_lists);

        TextView sportNameText = (TextView) findViewById(R.id.sport_name);
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        sportNameText.setText(category);

        StringBuilder detailUrl = new StringBuilder();
        switch (category) {
            case "羽毛球场":
                detailUrl.append(this.getString(R.string.badmintonUrl));
                break;
            case "乒乓球馆":
                detailUrl.append(this.getString(R.string.pingpongUrl));
                break;
            case "游泳馆":
                detailUrl.append(this.getString(R.string.swimmingUrl));
                break;
            case "健身馆":
                detailUrl.append(this.getString(R.string.gymUrl));
                break;
            case "台球厅":
                detailUrl.append(this.getString(R.string.billiardUrl));
                break;
        }
        requestQueue = Volley.newRequestQueue(context);
        sendRequest(detailUrl.toString(), requestQueue);

        gymAdapter = new GymAdapter(GymListActivity.this, R.layout.sport_venue_lists_item, gymList);

        ListView gym_list = (ListView) findViewById(R.id.sport_venue_listView);
        gym_list.setAdapter(gymAdapter);
        gym_list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Gym gym = gymList.get(position);
                SportVenueDetailActivity.actionStart(context, gym.getGymId());
            }
        });

    }


    private void initGyms()
    {
        Gym gym1 = new Gym("合肥市奥体中心", 12, 3.3f);
        gymList.add(gym1);

        Gym gym2 = new Gym("合肥市奥体中心", 12, 3.3f);
        gymList.add(gym2);

        Gym gym3 = new Gym("合肥市奥体中心", 12, 3.3f);
        gymList.add(gym3);

        Gym gym4 = new Gym("合肥市奥体中心", 12, 3.3f);
        gymList.add(gym4);

        Gym gym5 = new Gym("合肥市奥体中心", 12, 3.3f);
        gymList.add(gym5);
    }

    public static void actionStart(Context context, String category) {
        Intent intent = new Intent(context, GymListActivity.class);
        intent.putExtra("category", category);
        context.startActivity(intent);
    }

    private void sendRequest(String url, RequestQueue requestQueue) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            gymList.clear();
                            Log.d("YE", "01 " + response.toString());
                            gymList.addAll(JsonParse.ParseBriefGymInfo(response.toString()));
                            gymAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //
                        /*Message message = new Message();
                        message.what = Constant.SHOW_RESPONSE;
                        message.obj = response.toString();
                        viewHandler.sendMessage(message);*/
                    }
                }, new Response.ErrorListener() {
            @Override
            public  void onErrorResponse(VolleyError error) {
                Log.d("YE", "gymlist 返回错误信息" + error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gym_list, menu);
        return true;
    }*/

/*    @Override
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
    }*/
}
