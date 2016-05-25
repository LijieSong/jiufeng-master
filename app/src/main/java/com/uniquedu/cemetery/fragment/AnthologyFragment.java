package com.uniquedu.cemetery.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.uniquedu.cemetery.activity.ShowAnthologyActivity;
import com.uniquedu.cemetery.adapter.AnthologyAdapter;
import com.uniquedu.cemetery.bean.Anthology;
import com.uniquedu.cemetery.bean.Dead;
import com.uniquedu.cemetery.pulltorefresh.PullToRefreshBase;
import com.uniquedu.cemetery.pulltorefresh.PullToRefreshListView;

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
 * 纪念文选界面
 */
public class AnthologyFragment extends BaseFragment {
    private ListView mListView;
    private PullToRefreshListView mPullToRefreshView;
    private List<Anthology> mAnthologies;
    private AnthologyAdapter mAdapter;
    private String deadId;
    private int page = 1;
    private static final int STATE_LOAD_MORE = 0;
    private static final int STATE_LOAD_REFRESH = 1;
    private TextView mTextViewNull;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anthology, null);
        deadId = ((DeadHomePageActivity) getActivity()).mDeadId;
        mPullToRefreshView = (PullToRefreshListView) view.findViewById(R.id.anthlolay_listview);
        mTextViewNull = (TextView) view.findViewById(R.id.textview_null);
        mListView = mPullToRefreshView.getRefreshableView();
        mAnthologies = new ArrayList<>();
        mAdapter = new AnthologyAdapter(inflater, mAnthologies);
        mListView.setAdapter(mAdapter);
        loadData(STATE_LOAD_REFRESH);
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
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Anthology anthology = mAnthologies.get(position - 1);//此处position一定记得减一，因为加了header
                Intent intent = new Intent(getActivity(), ShowAnthologyActivity.class);
                intent.putExtra("anthology", anthology);
                startActivity(intent);
            }
        });
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
        getInfomation(loadmore, state);
    }

    private void getInfomation(final HashMap<String, String> params, final int state) {
        StringRequest request = new StringRequest(Request.Method.POST, Address.WONSHIP_ANTHOLOGY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<Anthology> anthologies = resloveData(response);
                if (state == STATE_LOAD_MORE) {
                    mAnthologies.addAll(anthologies);
                    mAdapter.notifyDataSetChanged();
                    mPullToRefreshView.onRefreshComplete();
                } else {
                    if (anthologies.size() == 0) {
                        mTextViewNull.setVisibility(View.VISIBLE);
                        mPullToRefreshView.setVisibility(View.INVISIBLE);
                    } else {
                        mPullToRefreshView.setVisibility(View.VISIBLE);
                        mTextViewNull.setVisibility(View.INVISIBLE);
                        mAnthologies.clear();
                        mAnthologies.addAll(anthologies);
                        mAdapter.notifyDataSetChanged();
                        mPullToRefreshView.onRefreshComplete();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "获取详细信息失败", Toast.LENGTH_LONG).show();
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

    private List<Anthology> resloveData(String response) {
        List<Anthology> list = new ArrayList<>();
        System.out.println(response);
        if (TextUtils.isEmpty(response)) {
            return list;
        }
        response = response.trim();
        if (response.indexOf("callbakename") == 0) {
            response = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
        }
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray array = obj.getJSONArray("rows");
            Gson gson = new Gson();
            Type type = new TypeToken<List<Anthology>>() {
            }.getType();
            Object fromJson2 = gson.fromJson(array.toString(), type);

            list = (List<Anthology>) fromJson2;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

}
