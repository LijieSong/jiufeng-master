package com.uniquedu.cemetery.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.uniquedu.cemetery.Address;
import com.uniquedu.cemetery.BaseFragment;
import com.uniquedu.cemetery.R;
import com.uniquedu.cemetery.activity.DeadHomePageActivity;
import com.uniquedu.cemetery.bean.Dead;
import com.uniquedu.cemetery.bean.DeadCallBack;
import com.uniquedu.cemetery.bean.DeadInformation;

import java.util.List;


/**
 * Created by ZhongHang on 2016/3/4.
 * 逝者详细资料
 */
public class DeadInfomationFragment extends BaseFragment {
    private View view;
    private TabLayout mTabLayout;
    private SimpleDraweeView image_head;
    private TextView text_dead_name, text_dead_sex, text_dead_nationality,
            text_dead_birthday, text_dead_feteday, text_dead_nativeplace, text_dead_summary;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dead_infomation, null);
        mTabLayout = (TabLayout) view.findViewById(R.id.dead_tablayout);
        infoview();
        return view;
    }

    private void infoview() {
        image_head = (SimpleDraweeView) view.findViewById(R.id.image_head);
        text_dead_name = (TextView) view.findViewById(R.id.text_dead_name);
        text_dead_sex = (TextView) view.findViewById(R.id.text_dead_sex);
        text_dead_nationality = (TextView) view.findViewById(R.id.text_dead_nationality);
        text_dead_birthday = (TextView) view.findViewById(R.id.text_dead_birthday);
        text_dead_feteday = (TextView) view.findViewById(R.id.text_dead_feteday);
        text_dead_nativeplace = (TextView) view.findViewById(R.id.text_dead_nativeplace);
        text_dead_summary = (TextView) view.findViewById(R.id.text_dead_summary);
    }

    public void infodate(final DeadCallBack back) {
        String url = back.getManPhoto();
        if (!TextUtils.isEmpty(url)) {
            url = url.trim();
            if (url.startsWith(".")) ;
            url = url.substring(1);
            String imageUrl = Address.IMAGEADDRESS + url;
            image_head.setImageURI(Uri.parse(imageUrl));
        }
        mTabLayout.removeAllTabs();
        if (back.getTombType().trim().equals("单人馆")) {
            mTabLayout.addTab(mTabLayout.newTab().setText(back.getRows().get(0).getDeadName()));
        } else {
            mTabLayout.addTab(mTabLayout.newTab().setText(back.getRows().get(0).getDeadName()));
            mTabLayout.addTab(mTabLayout.newTab().setText(back.getRows().get(1).getDeadName()));
        }
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                setData(back.getRows().get(position));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setData(back.getRows().get(0));
    }

    private void setData(DeadInformation information) {

        text_dead_name.setText("姓名：" + information.getDeadName());
        text_dead_birthday.setText("生辰：" + information.getBirthday());
        text_dead_feteday.setText("祭日：" + information.getFeteday());
        text_dead_nationality.setText("民族：" + information.getNationality());
        text_dead_nativeplace.setText("籍贯：" + information.getNativeplace());
        text_dead_sex.setText("性别：" + information.getSex());
        Spanned spanned = Html.fromHtml(information.getSummary());
        text_dead_summary.setText(spanned);
    }
}
