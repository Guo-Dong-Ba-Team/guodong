package com.guodong.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.guodong.R;

/**
 * Created by yechy on 2015/10/29.
 */
public class Traffic {

    public static void showNetworkImage(RequestQueue requestQueue, String url,
                                        NetworkImageView networkImageView) {

        //RequestQueue requestQueue = Volley.newRequestQueue(context);
        ImageLoader imageLoader = new ImageLoader(requestQueue, new BitmapCache());
        networkImageView.setDefaultImageResId(R.drawable.bg);
        networkImageView.setErrorImageResId(R.drawable.bg);
        networkImageView.setImageUrl(url, imageLoader);
    }

    private static class BitmapCache implements ImageLoader.ImageCache{
        private LruCache<String, Bitmap> mCache;

        private BitmapCache() {
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
