package com.uniquedu.cemetery.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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
import com.uniquedu.cemetery.adapter.FlowerAdapter;
import com.uniquedu.cemetery.adapter.WorshipMsgAdapter;
import com.uniquedu.cemetery.bean.WorshipMessage;
import com.uniquedu.cemetery.utils.SharedPreferencesKey;
import com.uniquedu.cemetery.utils.SharedPreferencesUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/3/17.
 */
public class ThouActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private GridView mThou_grid;
    private int[] thou_image = {R.mipmap.thus_red, R.mipmap.thus_forefather, R.mipmap.thus_god};
    private String[] thou_name = {"红色高香", "祭祖高香", "灵香"};
    private TextView mFlower_back, mFlower_head;
    private Button mFlower_complete;
    private EditText medit_name, medit_title, medit_content;
    private String id;
    private int mThou_id;
    private Context mContext;
    private RequestQueue mRequestQueue;
    private ArrayList<HashMap<String, Object>> list;
    private FlowerAdapter mAdapter;
    private Handler mHandler = new Handler() {
    };
    private List<WorshipMessage> mName;
    private List<WorshipMessage> mTitle;
    private List<WorshipMessage> mMsg;
    private ImageView mImageViewAddName;
    private ImageView mImageViewAddTitle;
    private ImageView mImageViewAddMsg;
    private static final int STATE_NAME = 1;
    private static final int STATE_TITLE = 2;
    private static final int STATE_MSG = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower);
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
        mFlower_head = (TextView) findViewById(R.id.flower_head);
        mFlower_head.setText("上香");
        mFlower_back = (TextView) findViewById(R.id.flower_back);
        mFlower_complete = (Button) findViewById(R.id.flower_complete);
        medit_name = (EditText) findViewById(R.id.edit_name);
        medit_title = (EditText) findViewById(R.id.edit_title);
        medit_content = (EditText) findViewById(R.id.edit_content);
        mFlower_back.setOnClickListener(this);
        mFlower_complete.setOnClickListener(this);
        mThou_grid = (GridView) findViewById(R.id.flower_grid);
        list = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < thou_image.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("image", thou_image[i]);
            map.put("text", thou_name[i]);
            list.add(map);
        }
        mAdapter = new FlowerAdapter(mInflater, list);
        mThou_grid.setAdapter(mAdapter);
        mThou_grid.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flower_back:
                finish();
                break;
            case R.id.flower_complete:
                String name = medit_name.getText().toString();
                String title = medit_title.getText().toString();
                String content = medit_content.getText().toString();
                if (name.length() == 0 || title.length() == 0 || content.length() == 0) {
                    Toast.makeText(mContext, "请输入姓名，标题和留言", Toast.LENGTH_SHORT).show();
                } else {
                    mRequestQueue = Volley.newRequestQueue(this);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET,
                            Address.WONSHIP_THEME + id + "&user=" + name + "&title=" + title + "&content=" + content + "&actiontype=" + 4 + "&typenum=" + mThou_id,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    Toast.makeText(mContext, "上香成功", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mThou_id = position + 1;
        mAdapter.setSelect(position);
    }

    private void createDIYDialog(final int state) {
        final Dialog dialog = new Dialog(ThouActivity.this);
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
                                index = medit_name.getSelectionStart();//获取光标所在位置
                                edit = medit_name.getEditableText();//获取EditText的文字
                                break;
                            case STATE_TITLE:
                                value = mTitle.get(position).getValue();
                                index = medit_title.getSelectionStart();//获取光标所在位置
                                edit = medit_title.getEditableText();//获取EditText的文字
                                break;
                            case STATE_MSG:
                                value = mMsg.get(position).getValue();
                                index = medit_content.getSelectionStart();//获取光标所在位置
                                edit = medit_content.getEditableText();//获取EditText的文字
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

    private LayoutInflater mInflater;


}

