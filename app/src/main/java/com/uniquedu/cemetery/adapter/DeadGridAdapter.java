package com.uniquedu.cemetery.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.uniquedu.cemetery.Address;
import com.uniquedu.cemetery.R;
import com.uniquedu.cemetery.bean.Dead;

import java.util.List;

/**
 * Created by ZhongHang on 2016/3/4.
 */
public class DeadGridAdapter extends BaseAdapter {

    private Context context;
    private List<Dead> mList;

    public DeadGridAdapter(Context context, List<Dead> list) {
        this.context = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dead, null);
            vh.textViewName = (TextView) convertView.findViewById(R.id.textview_name);
            vh.image = (SimpleDraweeView) convertView.findViewById(R.id.imageview);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        Dead dead = mList.get(position);
        vh.textViewName.setText(dead.getMemorialName());
        String imgUrl = dead.getManPhoto();
        if (!TextUtils.isEmpty(imgUrl)) {
            imgUrl = imgUrl.trim();
            if (imgUrl.startsWith("."))
                imgUrl = imgUrl.substring(1);
            String imageUrl = Address.IMAGEADDRESS + imgUrl;
            Uri uri = Uri.parse(imageUrl);
            vh.image.setImageURI(uri);
        }else{
            Uri uri = Uri.parse("");
            vh.image.setImageURI(uri);
        }
        position = getCount() - 1;
        return convertView;
    }

    class ViewHolder {
        TextView textViewName;
        SimpleDraweeView image;
    }
}
