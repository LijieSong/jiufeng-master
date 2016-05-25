package com.uniquedu.cemetery.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.squareup.picasso.Picasso;
import com.uniquedu.cemetery.Address;
import com.uniquedu.cemetery.BaseActivity;
import com.uniquedu.cemetery.R;
import com.uniquedu.cemetery.bean.PhotoBean;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by ZhongHang on 2016/3/27.
 */
public class ShowPhotoActivity extends BaseActivity {
    private PhotoView mPhotoView;
    private PhotoBean bean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);
        mPhotoView = (PhotoView) findViewById(R.id.iv_photo);
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
}
