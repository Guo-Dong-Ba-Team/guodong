package com.guodong.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guodong.R;
import com.guodong.model.Gym;

import java.util.List;

/**
 * Created by yechy on 2015/9/20.
 */
public class GymAdapter extends ArrayAdapter<Gym> {
    private int resourceId;
    public GymAdapter(Context context, int textViewResourceId, List<Gym> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Gym gym = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view = convertView;
        }
        ImageView gymImage = (ImageView) view.findViewById(R.id.gym_image);
        TextView gymName = (TextView) view.findViewById(R.id.gym_name);
        TextView price = (TextView) view.findViewById(R.id.price);
        TextView distance = (TextView) view.findViewById(R.id.distance);

        if(gymImage!= null) {
            gymImage.setImageResource(gym.getGymImageId());
        }
        gymName.setText(gym.getGymName());
        price.setText("￥" + gym.getPrice() + "起");
        distance.setText(gym.getDistance() + "km");

        return view;
    }
}
