package com.guodong.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.guodong.R;
import com.guodong.fragment.DisplayImageFragment;
import com.guodong.util.ImageFragAdapter;

import java.util.ArrayList;

/**
 * Created by yechy on 2015/10/30.
 */
public class DisplayImageActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_display_image);

        Intent intent = getIntent();
        String[] imageUrl = intent.getStringArrayExtra("imageUrl");
        String[] imageIntro = intent.getStringArrayExtra("imageIntro");

        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
        for (int i = 0; i < imageUrl.length; i++) {
            DisplayImageFragment fragment = new DisplayImageFragment();
            Bundle args = new Bundle();
            args.putString("imageUrl", imageUrl[i]);
            args.putString("imageIntro", imageIntro[i]);
            fragment.setArguments(args);
            fragmentList.add(fragment);

        }
        ImageFragAdapter adapter = new ImageFragAdapter(getSupportFragmentManager(), fragmentList);

        ViewPager viewPager = (ViewPager) findViewById(R.id.image_viewpager);
        viewPager.setAdapter(adapter);
    }

    public static void actionStart(Context context, String[] imageUrl, String[] imageIntro) {
        Intent intent = new Intent(context, DisplayImageActivity.class);
        intent.putExtra("imageUrl", imageUrl);
        intent.putExtra("imageIntro", imageIntro);
        context.startActivity(intent);
    }
}
