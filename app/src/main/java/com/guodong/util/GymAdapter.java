package com.guodong.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.guodong.R;
import com.guodong.model.Gym;

import java.util.List;

/**
 * Created by yechy on 2015/9/20.
 */
public class GymAdapter extends ArrayAdapter<Gym> {
    private int resourceId;
    ImageLoader imageLoader;

    public GymAdapter(Context context, int textViewResourceId, List<Gym> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(requestQueue, new BitmapCache());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Gym gym = getItem(position);
        String url = gym.getGymImageUrl();

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view = convertView;
        }
        NetworkImageView gymImage = (NetworkImageView) view.findViewById(R.id.gym_image);
        TextView gymName = (TextView) view.findViewById(R.id.gym_name);
        TextView price = (TextView) view.findViewById(R.id.price);
        TextView distance = (TextView) view.findViewById(R.id.distance);

        if(gymImage!= null) {
            //gymImage.setDefaultImageResId();
            gymImage.setImageUrl(url, imageLoader);

        }
        gymName.setText(gym.getGymName());
        price.setText("￥" + gym.getPrice() + "起");
        distance.setText(gym.getDistance() + "km");

        return view;
    }

    //创建图片缓存
    class BitmapCache implements ImageLoader.ImageCache {
        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            //获取APP最大可用内存
            int maxMemory = (int) Runtime.getRuntime().maxMemory();
            int cacheSize = maxMemory / 8;
            mCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes()*bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }
    }
}
