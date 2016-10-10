package com.uniquedu.cemetery.adapter;

import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.uniquedu.cemetery.Address;
import com.uniquedu.cemetery.R;
import com.uniquedu.cemetery.bean.PhotoBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ZhongHang on 2016/3/15.
 */
public class PhotoAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<PhotoBean> mPhotos;
    private List<List<PhotoBean>> mLists;

    public void setOnItemListener(OnItemClick onItemListener) {
        OnItemListener = onItemListener;
    }

    public boolean isEditor() {
        return isEditor;
    }

    private OnItemClick OnItemListener;
    public interface OnItemClick {
        public void onItemClick(PhotoBean bean);
    }

    public void setPhotos(List<PhotoBean> mPhotos) {
        this.mPhotos = mPhotos;
        initDate();
        notifyDataSetChanged();
    }

    public List<PhotoBean> getListCheckedPhoto() {
        return mListCheckedPhoto;
    }

    private List<PhotoBean> mListCheckedPhoto = new ArrayList<>();
    private boolean isEditor = false;

    public void setEditor(boolean editor) {
        isEditor = editor;
        if (editor) {
            mListCheckedPhoto = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    public PhotoAdapter(LayoutInflater inflater, List<PhotoBean> photos) {
        mInflater = inflater;
        mPhotos = photos;
        initDate();
    }


    private Date dateOld;
    private Date dateNew;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public void initDate() {
        mLists = new ArrayList<>();
        List<PhotoBean> items = new ArrayList<>();
        dateOld = null;
        for (PhotoBean item : mPhotos) {
            try {
                dateNew = format.parse(item.getCreateDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (dateOld != null && dateNew.compareTo(dateOld) != 0) {
                //时间不同则另起一行
                mLists.add(items);
                items = new ArrayList<>();
                items.add(item);
            } else {
                if (items.size() >= 3) {
                    mLists.add(items);
                    items = new ArrayList<>();
                    items.add(item);
                } else {
                    items.add(item);
                }
            }
            dateOld = dateNew;
        }
        if (items.size() > 0) {
            mLists.add(items);
        }
        System.out.print(mLists.size());
    }

    @Override
    public int getCount() {
        return mLists.size();
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
            convertView = mInflater.inflate(R.layout.item_photo, null);
            vh = new ViewHolder();
            vh.textViewTime = (TextView) convertView.findViewById(R.id.textview_time);
            vh.view1 = convertView.findViewById(R.id.layout1);
            vh.view2 = convertView.findViewById(R.id.layout2);
            vh.view3 = convertView.findViewById(R.id.layout3);
            convertView.setTag(vh);
        }
        vh = (ViewHolder) convertView.getTag();
        vh.textViewTime.setText(mLists.get(position).get(0).getCreateDate());
        vh.textViewTime.setVisibility(View.VISIBLE);
        if (position > 0) {
            try {
                Date date1 = format.parse(mLists.get(position).get(0).getCreateDate());
                Date date2 = format.parse(mLists.get(position - 1).get(0).getCreateDate());
                if (date1.compareTo(date2) == 0) {
                    vh.textViewTime.setVisibility(View.GONE);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        vh.view1.setVisibility(View.INVISIBLE);
        vh.view2.setVisibility(View.INVISIBLE);
        vh.view3.setVisibility(View.INVISIBLE);
        int size = mLists.get(position).size();
        switch (size) {
            case 1:
                vh.view1.setVisibility(View.VISIBLE);
                showContent(vh.view1, mLists.get(position).get(0));
                break;
            case 2:
                vh.view1.setVisibility(View.VISIBLE);
                vh.view2.setVisibility(View.VISIBLE);
                showContent(vh.view1, mLists.get(position).get(0));
                showContent(vh.view2, mLists.get(position).get(1));
                break;
            case 3:
                vh.view1.setVisibility(View.VISIBLE);
                vh.view2.setVisibility(View.VISIBLE);
                vh.view3.setVisibility(View.VISIBLE);
                showContent(vh.view1, mLists.get(position).get(0));
                showContent(vh.view2, mLists.get(position).get(1));
                showContent(vh.view3, mLists.get(position).get(2));
                break;
        }
        return convertView;
    }

    public void showContent(View view, final PhotoBean item) {
        SimpleDraweeView iv = (SimpleDraweeView) view.findViewById(R.id.imageview);
        TextView tv = (TextView) view.findViewById(R.id.textview_title);
        final ImageView cb = (ImageView) view.findViewById(R.id.checkbox);
        if (isEditor) {
            cb.setVisibility(View.VISIBLE);
        } else {
            cb.setVisibility(View.INVISIBLE);
        }
        cb.setSelected(item.isChecked());
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setChecked(!cb.isSelected());
                cb.setSelected(!cb.isSelected());
                if (cb.isSelected()) {
                    mListCheckedPhoto.add(item);
                } else {
                    mListCheckedPhoto.remove(item);
                }

            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OnItemListener != null) {
                    OnItemListener.onItemClick(item);
                }
            }
        });


        String title = item.getTitle();
        tv.setText(title);
        String url = item.getPhoto();
        if (!TextUtils.isEmpty(url)) {
            url = url.trim();
            if (url.startsWith("."))
                url = url.substring(1);
            url = Address.IMAGEADDRESS + url;
            Uri uri = Uri.parse(url);
            iv.setImageURI(uri);
        }
    }

    class ViewHolder {
        TextView textViewTime;
        View view1;
        View view2;
        View view3;
    }
}
