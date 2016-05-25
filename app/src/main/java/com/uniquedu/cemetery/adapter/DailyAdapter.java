package com.uniquedu.cemetery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uniquedu.cemetery.R;
import com.uniquedu.cemetery.bean.Daily;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ZhongHang on 2016/3/4.
 * 祭扫日志适配器
 */
public class DailyAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Daily> mDailies;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatYear = new SimpleDateFormat("yyyy年");
    private SimpleDateFormat formatTime = new SimpleDateFormat("MM月dd日");

    public DailyAdapter(LayoutInflater inflater, List<Daily> dailies) {
        mInflater = inflater;
        mDailies = dailies;
    }

    @Override
    public int getCount() {
        return mDailies.size();
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
            convertView = mInflater.inflate(R.layout.item_daily, null);
            vh = new ViewHolder();
            vh.textViewTime = (TextView) convertView.findViewById(R.id.textview_time);
            vh.textViewYear = (TextView) convertView.findViewById(R.id.textview_year);
            vh.textViewTitle = (TextView) convertView.findViewById(R.id.textview_title);
            vh.textViewContent = (TextView) convertView.findViewById(R.id.textview_content);
            vh.textViewName = (TextView) convertView.findViewById(R.id.textview_name);
            vh.textViewIP = (TextView) convertView.findViewById(R.id.textview_ip);
            convertView.setTag(vh);
        }
        vh = (ViewHolder) convertView.getTag();
        Daily daily = mDailies.get(position);
        vh.textViewName.setText(daily.getActionUser() + "  为逝者 " + daily.getActionName());
        vh.textViewContent.setText(daily.getArticleContent());
        vh.textViewTitle.setText(daily.getTitle());
        String ip = daily.getIPAddress();
        String filed[] = ip.split("\\.");
        if (filed.length > 2) {
            ip = filed[0] + "." + filed[1] + ".**.**";
        }
        vh.textViewIP.setText(ip);

        try {
            Date date = format.parse(daily.getCreateDate());
            vh.textViewTime.setText(formatTime.format(date));
            vh.textViewYear.setText(formatYear.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return convertView;
    }

    class ViewHolder {
        private TextView textViewTime;
        private TextView textViewTitle;
        private TextView textViewContent;
        private TextView textViewName;
        private TextView textViewYear;
        private TextView textViewIP;
    }
}
