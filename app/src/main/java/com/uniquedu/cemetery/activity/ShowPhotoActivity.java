package com.uniquedu.cemetery.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.uniquedu.cemetery.Address;
import com.uniquedu.cemetery.BaseActivity;
import com.uniquedu.cemetery.MainActivity;
import com.uniquedu.cemetery.R;
import com.uniquedu.cemetery.bean.PhotoBean;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by ZhongHang on 2016/3/27.
 */
public class ShowPhotoActivity extends BaseActivity {
    private PhotoView mPhotoView;
    private PhotoBean bean;
    private List<PhotoBean> list;
    private ViewPager view_pager;
    private List<View> lists = null;//存储view
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);
        mPhotoView = (PhotoView) findViewById(R.id.iv_photo);
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        Intent intent = getIntent();
        bean = intent.getParcelableExtra("photo");
        String url = bean.getPhoto();
        if (!TextUtils.isEmpty(url)) {
            url = url.trim();
            if (url.startsWith("."))
                url = url.substring(1);
            url = Address.IMAGEADDRESS + url;
            Picasso.with(this)
                    .load(url)
                    .into(mPhotoView);
        }
    }
    /**
     * viewpager的匹配器
     */
    public class ViewPagerAdapter extends PagerAdapter {
        //创建页面集合
        private List<View> pages;

        //设置有参构造
        public ViewPagerAdapter(List<View> lists) {
            this.pages = lists;
        }

        /**
         * 销毁条目的方法
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //销毁选中的条目
            ((ViewPager) container).removeView(pages.get(position));
        }

        /**
         * 加载条目的方法
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //添加条目
            ((ViewPager) container).addView(pages.get(position));
            //返回获取到的条目
            return pages.get(position);
        }

        //获取页面数量
        @Override
        public int getCount() {
            return pages.size();
        }

        //判断是来自那个页面的数据
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }
}
