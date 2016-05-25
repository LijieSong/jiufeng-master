package com.uniquedu.cemetery;

import android.support.v4.app.Fragment;

/**
 * Created by ZhongHang on 2016/3/3.
 */
public class BaseFragment extends Fragment {
    private String mTitle;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
