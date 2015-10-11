package com.guodong.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guodong.R;

/**
 * Created by yechy on 2015/9/19.
 */
public class SportItemView extends LinearLayout{
    ImageView sportImage;
    TextView sportText;

    public SportItemView(Context context, AttributeSet attrss) {
        super(context, attrss);
        LayoutInflater.from(context).inflate(R.layout.sport_item, this);

        sportImage = (ImageView) findViewById(R.id.sport_image);
        sportText = (TextView) findViewById(R.id.sport_text);

        sportImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "启动" + sportText.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setSportImage(int resId) {
        sportImage.setImageResource(resId);
    }

    public void setSportText(String text) {
        sportText.setText(text);
    }

}
