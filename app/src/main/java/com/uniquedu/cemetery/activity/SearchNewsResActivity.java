package com.uniquedu.cemetery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.uniquedu.cemetery.Address;
import com.uniquedu.cemetery.BaseActivity;
import com.uniquedu.cemetery.MyWebChromeClient;
import com.uniquedu.cemetery.R;

/**
 * Created by ZhongHang on 2016/3/29.
 */
public class SearchNewsResActivity extends BaseActivity {
    private WebView mWebView;
    private TextView mTextViewBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_res);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String keyword = intent.getStringExtra("keyword");
        mWebView = (WebView) findViewById(R.id.webview);
        mTextViewBack = (TextView) findViewById(R.id.textview_back);
        mTextViewBack.setText(" " + keyword);
        mTextViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //启用支持javascript
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

//        mWebView.loadUrl("file:///android_asset/index.html");
        mWebView.addJavascriptInterface(new MyJs(), "useAndroidMethod");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Intent intent = new Intent(getActivity(), WebInfomationActivity.class);
//                intent.putExtra("url", url);
//                startActivity(intent);
                return true;
            }
        });
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.loadUrl(url);
    }

    class MyJs {
        @JavascriptInterface
        public void toWebActivity(String title, String url) {
            Intent intent = new Intent(getApplicationContext(), WebInfomationActivity.class);
            intent.putExtra("url", Address.URL + url);
            intent.putExtra("title", title);
            startActivity(intent);
        }
    }

}
