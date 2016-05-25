package com.uniquedu.cemetery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uniquedu.cemetery.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ZhongHang on 2016/3/29.
 */
public class FlowerAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, Object>> list;
    private LayoutInflater inflater;

    public FlowerAdapter(LayoutInflater inflater, ArrayList<HashMap<String, Object>> list) {
        this.inflater = inflater;
        this.list = list;
    }

    private int select;

    public void setSelect(int select) {
        this.select = select;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
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
        convertView = inflater.inflate(R.layout.item_flwoer, null);
        ImageView image = (ImageView) convertView.findViewById(R.id.flower_image);
        TextView textView = (TextView) convertView.findViewById(R.id.flower_name);
        image.setImageResource((int) list.get(position).get("image"));
        textView.setText(list.get(position).get("text").toString());
        if (select == position) {
            image.setSelected(true);
        }
        return convertView;
    }
}

