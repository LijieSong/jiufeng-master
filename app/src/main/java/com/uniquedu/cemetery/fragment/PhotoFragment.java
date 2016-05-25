package com.uniquedu.cemetery.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
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
import com.uniquedu.cemetery.activity.ShowPhotoActivity;
import com.uniquedu.cemetery.adapter.PhotoAdapter;
import com.uniquedu.cemetery.bean.Dead;
import com.uniquedu.cemetery.bean.PhotoBean;
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
 * Created by ZhongHang on 2016/3/15.
 */
public class PhotoFragment extends BaseFragment {
    private ListView mListView;
    private PullToRefreshListView mPullRefreshView;
    private LayoutInflater mInflater;
    private String deadId;
    private ArrayList<PhotoBean> mList;
    private int page = 1;
    private PhotoAdapter mAdapter;
    private static final int STATE_LOAD_MORE = 0;
    private static final int STATE_LOAD_REFRESH = 1;
    private TextView mTextViewNull;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photolist, null);
        deadId = ((DeadHomePageActivity) getActivity()).mDeadId;
        mInflater = inflater;
        mPullRefreshView = (PullToRefreshListView) view.findViewById(R.id.photo_listview);
        mListView = mPullRefreshView.getRefreshableView();
        mTextViewNull = (TextView) view.findViewById(R.id.textview_null);
        mList = new ArrayList<>();
        mAdapter = new PhotoAdapter(inflater, mList);
        mListView.setAdapter(mAdapter);
        mPullRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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
        mAdapter.setOnItemListener(new PhotoAdapter.OnItemClick() {
            @Override
            public void onItemClick(PhotoBean bean) {
                Intent intent = new Intent(getActivity(), ShowPhotoActivity.class);
                intent.putExtra("photo", bean);
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
        getPhotos(loadmore, state);
    }

    private void getPhotos(final HashMap<String, String> params, final int state) {
        if (deadId != null) {
            StringRequest request = new StringRequest(Request.Method.POST, Address.WORSHIP_PHOTO, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("返回照片信息", response);
                    List<PhotoBean> list = resloveData(response);
                    if (state == STATE_LOAD_MORE) {
                        if (list.size() != 0) {
                            mList.addAll(list);
                            mAdapter.setPhotos(mList);
                        } else {
                            Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_LONG).show();
                        }
                        mPullRefreshView.onRefreshComplete();
                    } else {
                        if (list.size() != 0) {
                            mList.clear();
                            mList.addAll(list);
                            mAdapter.setPhotos(mList);
                            mPullRefreshView.setVisibility(View.VISIBLE);
                            mTextViewNull.setVisibility(View.INVISIBLE);
                        } else {
                            mTextViewNull.setVisibility(View.VISIBLE);
                            mPullRefreshView.setVisibility(View.INVISIBLE);
                            //显示无数据
                        }
                        mPullRefreshView.onRefreshComplete();
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
        } else {
            Toast.makeText(getActivity(), "获取详细信息失败", Toast.LENGTH_LONG).show();
        }

    }

    private List<PhotoBean> resloveData(String response) {
        List<PhotoBean> list = new ArrayList<>();
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
            Type type = new TypeToken<List<PhotoBean>>() {
            }.getType();
            Object fromJson2 = gson.fromJson(array.toString(), type);
            list = (List<PhotoBean>) fromJson2;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
