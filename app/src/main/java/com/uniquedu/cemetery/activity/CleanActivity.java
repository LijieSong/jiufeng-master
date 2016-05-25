package com.uniquedu.cemetery.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.uniquedu.cemetery.R;
import com.uniquedu.cemetery.adapter.WorshipMsgAdapter;
import com.uniquedu.cemetery.bean.WorshipMessage;
import com.uniquedu.cemetery.utils.SharedPreferencesKey;
import com.uniquedu.cemetery.utils.SharedPreferencesUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/17.
 */
public class CleanActivity extends BaseActivity implements View.OnClickListener {
    private TextView mGrid_back, mGrid_head;
    private Button mComplete;
    private EditText mStyle_name, mStyle_title, mStyle_content;
    private String id;
    private Context mContext;
    private RequestQueue mRequestQueue;
    private List<WorshipMessage> mName;
    private List<WorshipMessage> mTitle;
    private List<WorshipMessage> mMsg;
    private LayoutInflater mInflater;
    private Handler mHandler = new Handler() {
    };
    private ImageView mImageViewAddName;
    private ImageView mImageViewAddTitle;
    private ImageView mImageViewAddMsg;
    private static final int STATE_NAME = 1;
    private static final int STATE_TITLE = 2;
    private static final int STATE_MSG = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worship);
        mInflater = getLayoutInflater();
        mContext = this;
        infoID();
        Bundle bundle = getIntent().getBundleExtra("id");
        id = bundle.getString("id");
    }

    private void infoID() {
        mImageViewAddName = (ImageView) findViewById(R.id.imageview_add_name);
        mImageViewAddName.setOnClickListener(this);
        mImageViewAddTitle = (ImageView) findViewById(R.id.imageview_add_title);
        mImageViewAddTitle.setOnClickListener(this);
        mImageViewAddMsg = (ImageView) findViewById(R.id.imageview_add_msg);
        mImageViewAddMsg.setOnClickListener(this);
        mName = getDropDown(SharedPreferencesKey.KEY_USER);
        mTitle = getDropDown(SharedPreferencesKey.KEY_TITLE);
        mMsg = getDropDown(SharedPreferencesKey.KEY_CONTENT);
        mGrid_head = (TextView) findViewById(R.id.grid_head);
        mGrid_head.setText("清洁");
        mGrid_back = (TextView) findViewById(R.id.grid_back);
        mComplete = (Button) findViewById(R.id.complete);
        mStyle_name = (EditText) findViewById(R.id.edit_name);
        mStyle_title = (EditText) findViewById(R.id.edit_title);
        mStyle_content = (EditText) findViewById(R.id.edit_content);
        mGrid_back.setOnClickListener(this);
        mComplete.setOnClickListener(this);
    }

    private List<WorshipMessage> getDropDown(String key) {
        List<WorshipMessage> content = new ArrayList<>();
        String user = (String) SharedPreferencesUtil.getData(getApplicationContext(), key, "");
        Log.d("jiufeng", "得到留言提示：" + user);
        if (!TextUtils.isEmpty(user)) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<WorshipMessage>>() {
            }.getType();
            content = gson.fromJson(user, type);
        }
        return content;
    }

    private void createDIYDialog(final int state) {
        final Dialog dialog = new Dialog(CleanActivity.this);
        View dialogView = mInflater.inflate(R.layout.dialog_list, null);
        //此处不能直接使用findViewById,因为该ImageView存在于dialogView上
        ListView listView = (ListView) dialogView.findViewById(R.id.listview);
        TextView title = (TextView) dialogView.findViewById(R.id.textview_title);
        WorshipMsgAdapter mAdapterName = null;
        switch (state) {
            case STATE_NAME:
                mAdapterName = new WorshipMsgAdapter(getLayoutInflater(), mName);
                title.setText("姓名");
                break;
            case STATE_TITLE:
                mAdapterName = new WorshipMsgAdapter(getLayoutInflater(), mTitle);
                title.setText("标题");
                break;
            case STATE_MSG:
                mAdapterName = new WorshipMsgAdapter(getLayoutInflater(), mMsg);
                title.setText("内容");
                break;
        }

        listView.setAdapter(mAdapterName);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                dialog.dismiss();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String value = "";
                        int index = 0;
                        Editable edit = null;
                        switch (state) {
                            case STATE_NAME:
                                value = mName.get(position).getValue();
                                index = mStyle_name.getSelectionStart();//获取光标所在位置
                                edit = mStyle_name.getEditableText();//获取EditText的文字
                                break;
                            case STATE_TITLE:
                                value = mTitle.get(position).getValue();
                                index = mStyle_title.getSelectionStart();//获取光标所在位置
                                edit = mStyle_title.getEditableText();//获取EditText的文字
                                break;
                            case STATE_MSG:
                                value = mMsg.get(position).getValue();
                                index = mStyle_content.getSelectionStart();//获取光标所在位置
                                edit = mStyle_content.getEditableText();//获取EditText的文字
                                break;
                        }

                        if (index < 0 || index >= edit.length()) {
                            edit.append(value);
                        } else {
                            edit.insert(index, value);//光标所在位置插入文字
                        }
                    }
                }, 50);
            }
        });
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉Dialog自带的标题。必须在setContentView之前调用
        dialog.setContentView(dialogView);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.grid_back:
                finish();
                break;
            case R.id.complete:
                String name = mStyle_name.getText().toString();
                String title = mStyle_title.getText().toString();
                String content = mStyle_content.getText().toString();
                if (name.length() == 0 || title.length() == 0 || content.length() == 0) {
                    Toast.makeText(mContext, "请输入姓名，标题和留言", Toast.LENGTH_SHORT).show();
                } else {
                    mRequestQueue = Volley.newRequestQueue(this);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET,
                            Address.WONSHIP_THEME + id + "&user=" + name + "&title=" + title + "&content=" + content + "&actiontype=" + 3 + "&typenum=" + 0, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Toast.makeText(mContext, "跪拜成功", Toast.LENGTH_SHORT).show();
                            //这里使用了singleTask的启动模式，会自动关闭以上界面
                            Intent intent = new Intent(getApplicationContext(), DeadHomePageActivity.class);
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.e("TAG", volleyError.toString());
                        }
                    });

                    mRequestQueue.add(stringRequest);

                }
                break;
            case R.id.imageview_add_name:
                createDIYDialog(STATE_NAME);
                break;
            case R.id.imageview_add_title:
                createDIYDialog(STATE_TITLE);
                break;
            case R.id.imageview_add_msg:
                createDIYDialog(STATE_MSG);
                break;
            default:
                break;
        }
    }

}
