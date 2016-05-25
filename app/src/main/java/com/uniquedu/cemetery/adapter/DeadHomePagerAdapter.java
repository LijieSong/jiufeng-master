package com.uniquedu.cemetery.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.uniquedu.cemetery.BaseFragment;

import java.util.List;

/**
 * Created by ZhongHang on 2016/3/4.
 */
public class DeadHomePagerAdapter extends FragmentPagerAdapter{
    private List<BaseFragment> mPages;
    public DeadHomePagerAdapter(FragmentManager fm,List<BaseFragment> pages) {
        super(fm);
        mPages=pages;
    }

    @Override
    public Fragment getItem(int position) {
        return mPages.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mPages.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return mPages.size();
    }
}
