package com.uniquedu.cemetery.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
import com.uniquedu.cemetery.adapter.DailyAdapter;
import com.uniquedu.cemetery.bean.Daily;
import com.uniquedu.cemetery.bean.Dead;
import com.uniquedu.cemetery.pulltorefresh.PullToRefreshBase;
import com.uniquedu.cemetery.pulltorefresh.PullToRefreshListView;

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
public class WorshipDailyFragment extends BaseFragment {
//    private ListView mListView;

    private ListView mListView;
    private PullToRefreshListView mPullToRefreshView;
    private List<Daily> mDailies;
    private DailyAdapter mAdapter;
    private RequestQueue mRequestQueue;
    private int page = 1;
    private String deadId;
    private static final int STATE_LOAD_MORE = 0;
    private static final int STATE_LOAD_REFRESH = 1;
    private TextView mTextViewNull;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily, null);
        deadId = ((DeadHomePageActivity) getActivity()).mDeadId;
        mTextViewNull = (TextView) view.findViewById(R.id.textview_null);
        mPullToRefreshView = (PullToRefreshListView) view.findViewById(R.id.daily_list);
        mListView = mPullToRefreshView.getRefreshableView();
        mDailies = new ArrayList<>();
        mAdapter = new DailyAdapter(inflater, mDailies);
        mListView.setAdapter(mAdapter);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(STATE_LOAD_REFRESH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(STATE_LOAD_MORE);
            }
        });
        loadData(STATE_LOAD_REFRESH);
        return view;
    }

    private void loadData(int state) {
        if (state == STATE_LOAD_MORE) {
            page++;
        } else {
            page = 1;
        }
        HashMap<String, String> loadmore = new HashMap<>();
        loadmore.put("callback", "callbakename");
        loadmore.put("page", "" + page);
        loadmore.put("rows", "12");
        loadmore.put("id", deadId);
        getDaily(loadmore, state);
    }

    private void getDaily(final HashMap<String, String> params, final int state) {
        StringRequest request = new StringRequest(Request.Method.POST, Address.WORSHIP_DAILY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println(response);
                List<Daily> dailies = resolve(response);
                if (state == STATE_LOAD_MORE) {
                    mDailies.addAll(dailies);
                    mAdapter.notifyDataSetChanged();
                    mPullToRefreshView.onRefreshComplete();
                } else {
                    if (dailies.size() == 0) {
                        mTextViewNull.setVisibility(View.VISIBLE);
                        mPullToRefreshView.setVisibility(View.INVISIBLE);
                    } else {
                        mPullToRefreshView.setVisibility(View.VISIBLE);
                        mTextViewNull.setVisibility(View.INVISIBLE);
                        mDailies.clear();
                        mDailies.addAll(dailies);
                        mAdapter.notifyDataSetChanged();
                        mPullToRefreshView.onRefreshComplete();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "加载数据错误", Toast.LENGTH_SHORT).show();
                mPullToRefreshView.onRefreshComplete();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private List<Daily> resolve(String response) {
        List<Daily> dailies = new ArrayList<>();
        if (TextUtils.isEmpty(response)) {
            return dailies;
        }
        response = response.trim();
        if (response.indexOf("callbakename") == 0) {
            response = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
        }
        Gson gson = new Gson();
        try {
            JSONObject obj = new JSONObject(response);
            obj.getJSONArray("rows");
            Type type = new TypeToken<List<Daily>>() {
            }.getType();
            dailies = gson.fromJson(obj.getJSONArray("rows").toString(), type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dailies;
    }

}
