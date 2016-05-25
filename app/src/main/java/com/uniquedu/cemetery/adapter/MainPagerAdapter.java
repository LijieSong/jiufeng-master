package com.uniquedu.cemetery.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by ZhongHang on 2016/3/4.
 * 主界面的适配器
 */
public class MainPagerAdapter extends FragmentPagerAdapter{
    private List<Fragment> mPages;
    public MainPagerAdapter(FragmentManager fm,List<Fragment> pages) {
        super(fm);
        mPages=pages;
    }

    @Override
    public Fragment getItem(int position) {
        return mPages.get(position);
    }

    @Override
    public int getCount() {
        return mPages.size();
    }
}
