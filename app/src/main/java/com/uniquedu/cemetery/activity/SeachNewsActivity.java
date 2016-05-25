package com.uniquedu.cemetery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.uniquedu.cemetery.Address;
import com.uniquedu.cemetery.BaseActivity;
import com.uniquedu.cemetery.R;

/**
 * Created by ZhongHang on 2016/3/29.
 */
public class SeachNewsActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mImageViewBack;
    private EditText mEditTextCode;
    private Button mButtonSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach_news);
        mImageViewBack = (ImageView) findViewById(R.id.imageview_back);
        mEditTextCode = (EditText) findViewById(R.id.edittext_search);
        mButtonSearch = (Button) findViewById(R.id.button_search);
        mImageViewBack.setOnClickListener(this);
        mButtonSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_back:
                finish();
                break;
            case R.id.button_search:
                Toast.makeText(SeachNewsActivity.this, "搜索关键字" + mEditTextCode.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), SearchNewsResActivity.class);
                String keyword=mEditTextCode.getText().toString().trim();
                intent.putExtra("url", Address.NEWS_SEARCH + keyword);
                intent.putExtra("keyword",keyword);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
