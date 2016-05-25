package com.uniquedu.cemetery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uniquedu.cemetery.R;
import com.uniquedu.cemetery.bean.WorshipMessage;

import java.util.List;

/**
 * Created by ZhongHang on 2016/3/29.
 */
public class WorshipMsgAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<WorshipMessage> mData;

    public WorshipMsgAdapter(LayoutInflater mInflater, List<WorshipMessage> mData) {
        this.mInflater = mInflater;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.item_worship_msg, null);
        TextView textView = (TextView) convertView.findViewById(R.id.text_value);
        textView.setText(mData.get(position).getValue());
        return convertView;
    }
}
