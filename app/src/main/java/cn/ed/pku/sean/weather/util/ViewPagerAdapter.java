package cn.ed.pku.sean.weather.util;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Bryce on 2015/11/4.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private List<View> views;
    private Context context;


    public ViewPagerAdapter(List<View>views,Context context){
        this.context=context;
        this.views=views;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view ==object);
    }
}
