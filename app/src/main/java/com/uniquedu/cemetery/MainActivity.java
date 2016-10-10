package com.uniquedu.cemetery;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.uniquedu.cemetery.adapter.MainPagerAdapter;
import com.uniquedu.cemetery.fragment.CenterFragment;
import com.uniquedu.cemetery.fragment.DeadGridFragment;
import com.uniquedu.cemetery.fragment.InfomationFragment;
import com.uniquedu.cemetery.service.UpdateInfoService;
import com.uniquedu.cemetery.utils.GetXMLfromInternet;
import com.uniquedu.cemetery.utils.UpdataInfo;
import com.uniquedu.cemetery.utils.UpdateManager;
import com.uniquedu.cemetery.zbar.CameraTestActivity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final int STORAGE_PERMISSION = 0X23;
    private ViewPager mViewPager;
    private TextView mTextViewInfomation;
    private TextView mTextViewWorship;
    private TextView mTextViewCenter;
    private TextView mTextViewScan;
    public static final int INFOMATION = 0;
    public static final int WORSHIP = 1;
    public static final int CENTER = 2;
    public static final int SCAN = 3;
    private List<Fragment> mPages;
    // 更新版本要用到的一些信息
    private UpdataInfo info;
    private ProgressDialog progressDialog;
    private UpdateInfoService updateInfoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请Camera权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION);
        }
        initView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTextViewInfomation = (TextView) findViewById(R.id.textview_infomation);
        mTextViewWorship = (TextView) findViewById(R.id.textview_worship);
        mTextViewCenter = (TextView) findViewById(R.id.textview_center);
        mTextViewScan = (TextView) findViewById(R.id.textview_scan);
        mTextViewInfomation.setOnClickListener(this);
        mTextViewWorship.setOnClickListener(this);
        mTextViewCenter.setOnClickListener(this);
        mTextViewScan.setOnClickListener(this);
        mPages = new ArrayList<>();
        mPages.add(new InfomationFragment());
        mPages.add(new DeadGridFragment());
        mPages.add(new CenterFragment());
        mViewPager.setOffscreenPageLimit(3);
        FragmentManager manager = getSupportFragmentManager();
        MainPagerAdapter pagerAdapter = new MainPagerAdapter(manager, mPages);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectTab();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        checkUpdate();//检查更新
//        UpdateManager manager1 = new UpdateManager(MainActivity.this);
////        // 检查软件更新
//        manager1.checkUpdate();
//        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("软件更新").setMessage("更新提示").setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        }).setNeutralButton("后台更新", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                APKDownload.download(getApplicationContext(), "http://www.whjfs.com/app/whjfs.apk");
//                dialog.dismiss();
//            }
//        }).show();
    }

    public void selectTab() {
        mTextViewInfomation.setSelected(false);
        mTextViewWorship.setSelected(false);
        mTextViewCenter.setSelected(false);
        mTextViewScan.setSelected(false);
        switch (mViewPager.getCurrentItem()) {
            case INFOMATION:
                mTextViewInfomation.setSelected(true);
                break;
            case WORSHIP:
                mTextViewWorship.setSelected(true);
                break;
            case CENTER:
                mTextViewCenter.setSelected(true);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textview_infomation:
                mViewPager.setCurrentItem(INFOMATION, false);
                break;
            case R.id.textview_worship:
                mViewPager.setCurrentItem(WORSHIP, false);
                break;
            case R.id.textview_center:
                mViewPager.setCurrentItem(CENTER, false);
                break;
            case R.id.textview_scan:
                //只有点击扫描的时候是启动一个新的界面去扫描
                Intent intent = new Intent(getApplicationContext(), CameraTestActivity.class);
                Log.d("MainActivity", "启动二维码扫描");
                startActivity(intent);
                break;
        }
    }

    private long mExitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void checkUpdate(){
        Toast.makeText(MainActivity.this, "正在检查版本更新..", Toast.LENGTH_SHORT).show();
        // 自动检查有没有新版本 如果有新版本就提示更新
        new Thread() {
            public void run() {
                try {
                    updateInfoService = new UpdateInfoService(MainActivity.this);
                    info = updateInfoService.getUpDateInfo();
                    handler1.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }

    private Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {
            // 如果有更新就提示
            if (updateInfoService.isNeedUpdate()) {
                showUpdateDialog();
            }
        };
    };

    //显示是否要更新的对话框
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("请升级APP至版本" + info.getVersion());
        builder.setMessage(info.getDescription());
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    downFile(info.getUrl());
                } else {
                    Toast.makeText(MainActivity.this, "SD卡不可用，请插入SD卡",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    void downFile(final String url) {
        progressDialog = new ProgressDialog(MainActivity.this);    //进度条，在下载的时候实时更新进度，提高用户友好度
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("正在下载");
        progressDialog.setMessage("请稍候...");
        progressDialog.setProgress(0);
        progressDialog.show();
        updateInfoService.downLoadFile(url, progressDialog,handler1);
    }
}
