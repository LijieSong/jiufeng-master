package com.uniquedu.cemetery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.uniquedu.cemetery.BaseActivity;
import com.uniquedu.cemetery.MainActivity;
import com.uniquedu.cemetery.R;
import com.uniquedu.cemetery.utils.APKDownload;

/**
 * Created by ZhongHang on 2016/3/27.
 */
public class SplashActivity extends BaseActivity {
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 500);
    }
}
