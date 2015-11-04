package com.guodong.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.guodong.R;
import com.guodong.model.Gym;
import com.guodong.util.GymAdapter;

import java.util.ArrayList;
import java.util.List;

public class GymListActivity extends Activity
{
    private List<Gym> gymList = new ArrayList<>();
    private RequestQueue requestQueue;
    private Context context = GymListActivity.this;

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
                detailUrl.append(R.string.badmintonUrl);
                break;
            case "乒乓球馆":
                detailUrl.append(R.string.pingpongUrl);
                break;
            case "游泳馆":
                detailUrl.append(R.string.swimmingUrl);
                break;
            case "健身馆":
                detailUrl.append(R.string.gymUrl);
                break;
            case "台球厅":
                detailUrl.append(R.string.billiardUrl);
                break;
        }

        initGyms();
/*      requestQueue = Volley.newRequestQueue(context);
        String response = Traffic.sendRequest(detailUrl.toString(), requestQueue);
        try {
            gymList = JsonParse.ParseBriefGymInfo(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/


        GymAdapter gymAdapter = new GymAdapter(GymListActivity.this, R.layout.sport_venue_lists_item, gymList);

        ListView gym_list = (ListView) findViewById(R.id.sport_venue_listView);
        gym_list.setAdapter(gymAdapter);
        gym_list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Gym gym = gymList.get(position);
                SportVenueDetailActivity.actionStart(context, gym.getGymName());
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
