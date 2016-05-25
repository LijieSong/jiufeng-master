package com.uniquedu.cemetery.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uniquedu.cemetery.Address;
import com.uniquedu.cemetery.BaseActivity;
import com.uniquedu.cemetery.R;
import com.uniquedu.cemetery.bean.Anthology;
import com.uniquedu.cemetery.bean.AnthologyInfo;
import com.uniquedu.cemetery.bean.Daily;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhongHang on 2016/3/28.
 */
public class ShowAnthologyActivity extends BaseActivity {
    private WebView mWebView;
    private Anthology mAnthology;
    private TextView mTextViewTitle;
    private TextView mTextViewSrc;
    private TextView mTextViewAuthor;
    private TextView mTextViewTime;
    private SimpleDraweeView mHeadView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_anthology);
        Intent intent = getIntent();
        mAnthology = intent.getParcelableExtra("anthology");
        mTextViewTitle = (TextView) findViewById(R.id.textview_title);
        mTextViewSrc = (TextView) findViewById(R.id.textview_src);
        mTextViewTime = (TextView) findViewById(R.id.textview_time);
        mTextViewAuthor = (TextView) findViewById(R.id.textview_author);
        mHeadView = (SimpleDraweeView) findViewById(R.id.image_head);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setUseWideViewPort(true);
        String url = Address.WONSHIP_ANTHOLOGY_WEB + "?id=" + mAnthology.getId();
        mWebView.loadUrl(url);
        getData();
    }

    private void getData() {

        StringRequest request = new StringRequest(Request.Method.POST, Address.WONSHIP_ANTHOLOGY_INFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                response = response.trim();
                if (response.indexOf("callbakename") == 0) {
                    response = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                }
                Gson gson = new Gson();
                try {
                    JSONObject obj = new JSONObject(response);
                    AnthologyInfo info = gson.fromJson(obj.getJSONObject("pInfo").toString(), AnthologyInfo.class);
//                    mWebView.
                    if (TextUtils.isEmpty(info.getArticleImg())) {
                        mHeadView.setVisibility(View.GONE);
                    } else {
                        mHeadView.setImageURI(Uri.parse(Address.URL + info.getArticleImg()));
                    }
                    mTextViewTitle.setText(info.getArticleTitle());
                    if (!TextUtils.isEmpty(info.getArticleSource())) {
                        mTextViewSrc.setText(info.getArticleSource());
                    }
                    if (!TextUtils.isEmpty(info.getCreateUser())) {
                        mTextViewAuthor.setText(info.getCreateUser());
                    }
                    if (!TextUtils.isEmpty(info.getCreateDate())) {
                        mTextViewTime.setText(info.getCreateDate());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("callback", "callbakename");
                params.put("id", mAnthology.getId());
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
}
