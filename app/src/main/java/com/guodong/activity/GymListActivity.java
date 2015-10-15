package com.guodong.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.guodong.R;
import com.guodong.model.Gym;
import com.guodong.util.GymAdapter;

import java.util.ArrayList;
import java.util.List;

public class GymListActivity extends AppCompatActivity
{
    private List<Gym> gymList = new ArrayList<Gym>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_venue_lists);

        initGyms();

        GymAdapter gymAdapter = new GymAdapter(GymListActivity.this, R.layout.sport_venue_lists_item, gymList);

        ListView gym_list = (ListView) findViewById(R.id.sport_venue_listView);
        gym_list.setAdapter(gymAdapter);
        gym_list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(view.getContext(), com.guodong.activity.SportVenueDetailActivity.class);
                startActivity(intent);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gym_list, menu);
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