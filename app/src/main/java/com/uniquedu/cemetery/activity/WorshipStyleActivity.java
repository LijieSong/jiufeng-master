package com.uniquedu.cemetery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.uniquedu.cemetery.BaseActivity;
import com.uniquedu.cemetery.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhongHang on 2016/3/4.
 */
public class WorshipStyleActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private TextView mTextViewBack;
    private GridView mWorship_grid;
    private int[] grid_image = {R.mipmap.worship_style, R.mipmap.flower_style, R.mipmap.wine_style,
            R.mipmap.thou_style, R.mipmap.msg_style, R.mipmap.clean_style};
    private String[] grid_text = {"祭拜", "献花", "敬酒", "上香", "留言", "清洁"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worship_style);
        initView();
    }

    private void initView() {
        mTextViewBack = (TextView) findViewById(R.id.textview_back);
        mWorship_grid = (GridView) findViewById(R.id.worship_grid);
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < grid_image.length; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("image", grid_image[i]);
            map.put("text", grid_text[i]);
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), list, R.layout.worship_grid_item,
                new String[]{"image", "text"}, new int[]{R.id.grid_item_image, R.id.grid_item_text});
        mWorship_grid.setAdapter(adapter);
        mWorship_grid.setOnItemClickListener(this);
        mTextViewBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textview_back:
                WorshipStyleActivity.this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("id", getIntent().getBundleExtra("id"));
        switch (position) {
            case 0:
                intent.setClass(this, WorshipActivity.class);
                startActivity(intent);
                break;
            case 1:
                intent.setClass(this, FlowerActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent.setClass(this, WineActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent.setClass(this, ThouActivity.class);
                startActivity(intent);
                break;
            case 4:
                intent.setClass(this, MessageActivity.class);
                startActivity(intent);
                break;
            case 5:
                intent.setClass(this, CleanActivity.class);
                startActivity(intent);
                break;
        }
    }
}
