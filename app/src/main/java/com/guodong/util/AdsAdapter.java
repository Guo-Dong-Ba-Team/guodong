package com.guodong.util;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by yechy on 2015/9/21.
 */
public class AdsAdapter extends PagerAdapter {

        private List<View> adsList;

        public AdsAdapter(List<View> adsList) {
            this.adsList = adsList;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(adsList.get(position));
        }

    @Override
    public void finishUpdate(View container) {

    }

        @Override
        public int getCount() {
            return adsList.size();
        }

    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(adsList.get(position), 0);
        return adsList.get(position);
    }

    @Override
    public boolean isViewFromObject(View container, Object object) {
        return  container == object;
    }

}
