package com.uniquedu.cemetery.adapter;

import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.facebook.drawee.view.SimpleDraweeView;
import com.uniquedu.cemetery.Address;
import com.uniquedu.cemetery.R;
import com.uniquedu.cemetery.bean.Anthology;

import java.util.List;

/**
 * Created by ZhongHang on 2016/3/4.
 */
public class AnthologyAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Anthology> mAnthologies;

    public AnthologyAdapter(LayoutInflater inflater, List<Anthology> anthologies) {
        mInflater = inflater;
        mAnthologies = anthologies;
    }

    @Override
    public int getCount() {
        return mAnthologies.size();
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
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_anthology, null);
            vh = new ViewHolder();
            vh.anthlolgy_image = (SimpleDraweeView) convertView.findViewById(R.id.anthlolgy_image);
            vh.anthlolgy_title = (TextView) convertView.findViewById(R.id.anthlolgy_title);
            vh.anthlolgy_date = (TextView) convertView.findViewById(R.id.anthlolgy_date);
            vh.anthlolgy_user = (TextView) convertView.findViewById(R.id.anthlolgy_user);
            convertView.setTag(vh);
        }
        vh = (ViewHolder) convertView.getTag();
        Anthology anthology = mAnthologies.get(position);
        String imgUrl = anthology.getArticleImg();
        vh.anthlolgy_image.setImageURI(null);
        if (!TextUtils.isEmpty(imgUrl)) {
            imgUrl = imgUrl.trim();
            if (imgUrl.startsWith("."))
                imgUrl = imgUrl.substring(1);
            String imageUrl = Address.IMAGEADDRESS + imgUrl;
            vh.anthlolgy_image.setImageURI(Uri.parse(imgUrl));
        } else {
            vh.anthlolgy_image.setImageURI(null);
        }
        vh.anthlolgy_title.setText(anthology.getArticleTitle());
        vh.anthlolgy_date.setText(anthology.getCreateDate());
        vh.anthlolgy_user.setText(anthology.getCreateUser());
        return convertView;
    }

    class ViewHolder {
        SimpleDraweeView anthlolgy_image;
        TextView anthlolgy_title, anthlolgy_date, anthlolgy_user;
    }
}
