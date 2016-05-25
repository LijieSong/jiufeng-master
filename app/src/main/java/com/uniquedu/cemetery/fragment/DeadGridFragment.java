package com.uniquedu.cemetery.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.uniquedu.cemetery.BaseFragment;
import com.uniquedu.cemetery.R;
import com.uniquedu.cemetery.activity.DeadHomePageActivity;
import com.uniquedu.cemetery.adapter.DeadGridAdapter;
import com.uniquedu.cemetery.bean.Dead;
import com.uniquedu.cemetery.bean.SignIn;
import com.uniquedu.cemetery.pulltorefresh.PullToRefreshBase;
import com.uniquedu.cemetery.pulltorefresh.PullToRefreshGridView;
import com.uniquedu.cemetery.utils.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by ZhongHang on 2016/3/4.
 */
public class DeadGridFragment extends BaseFragment {
    public String tag = "MainActivity";
    private int index = 1;
    private EditText et_search;
    private TextView text_sweep;
    private ImageView iv_search;
    private PullToRefreshGridView mPullRefreshGridView;
    private GridView mGridView;
    private List<Dead> mList;
    private DeadGridAdapter adapter = null;
    private RequestQueue mRequestQueue;
    private SignIn signIn;
    private static final String TAG = "fengbeizaixian";

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deadgrid, null);
        initView(view);
        initData();
        return view;
    }

    private void initView(final View view) {
        mPullRefreshGridView = (PullToRefreshGridView) view.findViewById(R.id.pull_refresh_grid);
        mGridView = mPullRefreshGridView.getRefreshableView();
        et_search = (EditText) view.findViewById(R.id.dead_edit);
        iv_search = (ImageView) view.findViewById(R.id.image_seach);
        text_sweep = (TextView) view.findViewById(R.id.text_sweep);
        mList = new ArrayList<>();
        adapter = new DeadGridAdapter(getContext(), mList);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DeadHomePageActivity.class);
                Dead dead = mList.get(position);
                intent.putExtra(DeadHomePageActivity.EXTRA_DEAID, dead.getId());
                startActivity(intent);
            }
        });
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initsearch();
            }
        });
        mPullRefreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                initdatamore();
            }
        });
        //i want to sweep  我要祭扫  默认登录进去的
        text_sweep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = (boolean) SharedPreferencesUtil.getData(getContext().getApplicationContext(), "login", false);
                if (isLogin != false) {
                    Gson gson = new Gson();
                    signIn = gson.fromJson(SharedPreferencesUtil.getData(getContext().getApplicationContext(), "signIn", "").toString(), SignIn.class);
                    Intent intent = new Intent(getActivity(), DeadHomePageActivity.class);
                    intent.putExtra(DeadHomePageActivity.EXTRA_DEAID, signIn.getMemorialId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), DeadHomePageActivity.class);
                    Dead dead = mList.get(0);
                    intent.putExtra(DeadHomePageActivity.EXTRA_DEAID, dead.getId());
                    startActivity(intent);
                }
            }
        });
    }

    private void initData() {
        HashMap<String, String> first = new HashMap<>();
        first.put("callback", "callbakename");
        first.put("page", "1");
        first.put("rows", "12");
        loadDate(first);
    }

    /**
     * 加载更多数据
     */
    private void initdatamore() {
        index++;
        HashMap<String, String> first = new HashMap<>();
        first.put("callback", "callbakename");
        first.put("page", "" + index);
        first.put("rows", "12");
        if (!et_search.getText().toString().equals("")) {
            first.put("name", et_search.getText().toString());
        }
        loadDate(first);
    }

    /**
     * 解析搜索数据
     */
    private void initsearch() {
        index = 1;
        mList.clear();
        adapter.notifyDataSetChanged();
        HashMap<String, String> first = new HashMap<>();
        first.put("callback", "callbakename");
        first.put("page", "" + index);
        first.put("rows", "12");
        first.put("name", et_search.getText().toString());
        loadDate(first);
    }

    /**
     * 加载数据,传递不同的参数
     */
    private void loadDate(final HashMap<String, String> params) {
        mRequestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Address.PersonalMemorial, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d(TAG, "得到的数据" + s);
                mPullRefreshGridView.onRefreshComplete();
                resolveData(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        mRequestQueue.add(stringRequest);
    }

    /**
     * 用于解析服务器返回的数据
     *
     * @param s
     */
    private void resolveData(String s) {
        Log.d("shuju", "服务器返回的数据" + s);
        try {
            if (TextUtils.isEmpty(s)) {
                return;
            }
            s = s.trim();
            if (s.contains("callbake")) {
                s = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
            }
            JSONObject obj = new JSONObject(s);
            JSONArray array = obj.getJSONArray("rows");
            Gson gson = new Gson();
            Type type = new TypeToken<List<Dead>>() {
            }.getType();
            Object fromJson2 = gson.fromJson(array.toString(), type);
            List<Dead> list = (List<Dead>) fromJson2;
            if (list.size() != 0) {
                mList.addAll(list);
                adapter.notifyDataSetChanged();
                mGridView.smoothScrollToPosition(mList.size() - 1);
                Log.d("shuju", "加载成功" + mList.size());
            } else {
                Toast.makeText(getActivity(), "请重新加载", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
