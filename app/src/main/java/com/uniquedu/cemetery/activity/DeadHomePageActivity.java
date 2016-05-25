package com.uniquedu.cemetery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uniquedu.cemetery.Address;
import com.uniquedu.cemetery.BaseActivity;
import com.uniquedu.cemetery.BaseFragment;
import com.uniquedu.cemetery.R;
import com.uniquedu.cemetery.adapter.DeadHomePagerAdapter;
import com.uniquedu.cemetery.bean.DeadCallBack;
import com.uniquedu.cemetery.fragment.AnthologyFragment;
import com.uniquedu.cemetery.fragment.DeadInfomationFragment;
import com.uniquedu.cemetery.fragment.DeadWorshipFragment;
import com.uniquedu.cemetery.fragment.PhotoFragment;
import com.uniquedu.cemetery.fragment.WorshipDailyFragment;
import com.uniquedu.cemetery.service.MusicService;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhongHang on 2016/3/4.
 * 表示逝者的界面
 */
public class DeadHomePageActivity extends BaseActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<BaseFragment> mPages;
    private DeadHomePagerAdapter mAdapter;
    public String mDeadId;
    private DeadInfomationFragment deadInfoFragment;
    private DeadWorshipFragment deadFragment;
    private WorshipDailyFragment worshipDailyFragment;
    private PhotoFragment photoFragment;
    public DeadCallBack callBack;
    private boolean isFirst = true;
    public static final String EXTRA_DEAID ="deadId";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        initView();
    }

    public void toInfomation() {
        mViewPager.setCurrentItem(1, false);
    }

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        FragmentManager manager = getSupportFragmentManager();
        mPages = new ArrayList<>();
        deadFragment = new DeadWorshipFragment();
        deadFragment.setTitle("在线祭扫");
        mPages.add(deadFragment);
        deadInfoFragment = new DeadInfomationFragment();
        deadInfoFragment.setTitle("逝者资料");
        mPages.add(deadInfoFragment);
        worshipDailyFragment = new WorshipDailyFragment();
        worshipDailyFragment.setTitle("祭扫日志");
        mPages.add(worshipDailyFragment);
        photoFragment = new PhotoFragment();
        photoFragment.setTitle("历史相册");
        mPages.add(photoFragment);
        AnthologyFragment anthologyFragment = new AnthologyFragment();
        anthologyFragment.setTitle("纪念文选");
        mPages.add(anthologyFragment);
        mAdapter = new DeadHomePagerAdapter(manager, mPages);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInfomation();
    }

    /**
     * 请求到灵堂信息，以及逝者的详细信息，刷新灵堂界面以及详细信息界面
     */
    private void getInfomation() {
        mDeadId = getIntent().getStringExtra(EXTRA_DEAID);
        StringRequest request = new StringRequest(Request.Method.POST, Address.INFOMATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);

                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Log.d("fengbeizaixian", response);
                response = response.trim();
                if (response.indexOf("callbakename") == 0) {
                    response = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                }
                Gson gson = new Gson();
                Type typeToken = new TypeToken<DeadCallBack>() {
                }.getType();
                callBack = gson.fromJson(response, typeToken);
                deadInfoFragment.infodate(callBack);
                deadFragment.initData(callBack);
                if (isFirst) {
                    mp3State(callBack);
                    isFirst = false;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "获取详细信息失败", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                ?callback=callbakename&id=
                HashMap<String, String> params = new HashMap<>();
                params.put("callback", "callbakename");
                params.put("id", mDeadId);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    /**
     * 只有第一次进入该界面是启动音乐，其他时间不启动音乐
     *
     * @param back
     */
    private void mp3State(DeadCallBack back) {
        Intent it = new Intent(DeadHomePageActivity.this, MusicService.class);
        switch (back.getMp3State()) {
            case "1":
                it.putExtra("musicid", 1);
                break;
            case "2":
                it.putExtra("musicid", 2);
                break;
            case "3":
                it.putExtra("musicid", 3);
                break;
            case "0":
                it.putExtra("musicid", 0);
                break;
            default:
                it.putExtra("musicid", -1);
                break;
        }
        startService(it);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(DeadHomePageActivity.this, MusicService.class);
        stopService(intent);
    }

    private long mExitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, "再按一次退出祭扫主页", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
