package com.guodong.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.guodong.R;
import com.guodong.util.ClearEditText;

/**
 * Created by yechy on 2015/9/25.
 */
public class SearchActivity extends Activity {
    ClearEditText inputGymText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
    }

    private void initView() {
        inputGymText = (ClearEditText) findViewById(R.id.input_gym_name);
        Button searchBtn = (Button) findViewById(R.id.search_gym_btn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputGymName = inputGymText.getText().toString();
                if (!"".equals(inputGymName)) {
                    Intent intent = getIntent();
                    String city = intent.getStringExtra("city");
                    //搜索场馆
                    Toast.makeText(SearchActivity.this, "正在查询场馆...", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public static void actionStart(Context context, String city) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra("city", city);
        context.startActivity(intent);
    }
}
