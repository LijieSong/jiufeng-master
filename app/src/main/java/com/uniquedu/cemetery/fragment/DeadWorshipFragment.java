package com.uniquedu.cemetery.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.uniquedu.cemetery.Address;
import com.uniquedu.cemetery.BaseFragment;
import com.uniquedu.cemetery.R;
import com.uniquedu.cemetery.activity.DeadHomePageActivity;
import com.uniquedu.cemetery.activity.MPlayerActivity;
import com.uniquedu.cemetery.activity.WorshipStyleActivity;
import com.uniquedu.cemetery.bean.DeadCallBack;
import com.uniquedu.cemetery.bean.DeadInformation;
import com.uniquedu.cemetery.service.MusicService;

import java.util.List;

/**
 * Created by ZhongHang on 2016/3/4.
 */
public class DeadWorshipFragment extends BaseFragment implements View.OnClickListener {
    /**
     * 墓碑头像
     */
    private SimpleDraweeView mImageViewHead;
    /**
     * 左上角的在线祭扫按钮
     */
    private ImageView mImageViewWorship;
    /**
     * 右上角音乐按钮
     */
    private ImageView mImageViewMusic;
    /**
     * 花的图片
     */
    private ImageView mImageViewFlower;
    /**
     * 酒的图片
     */
    private ImageView mImageViewWine;
    private ImageView mImageViewWine2;
    /**
     * 香的图片
     */
    private ImageView mImageViewThus;
    /**
     * 中间的名字
     */
    private TextView mTextViewNameCenter;
    /**
     * 中间的生卒年
     */
    private TextView mTextViewAgeCenter;
    private TextView mTextViewNameLeft;
    private TextView mTextViewAgeLeft;
    private TextView mTextViewNameRight;
    private TextView mTextViewAgeRight;
    private List<DeadInformation> mInformations;
    private DeadCallBack back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worship, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mImageViewMusic = (ImageView) view.findViewById(R.id.imageview_music);
        mImageViewMusic.setOnClickListener(this);
        mImageViewWorship = (ImageView) view.findViewById(R.id.imageview_worship);
        mImageViewWorship.setOnClickListener(this);
        mImageViewHead = (SimpleDraweeView) view.findViewById(R.id.imageview_head);
        mImageViewHead.setOnClickListener(this);
        mTextViewNameCenter = (TextView) view.findViewById(R.id.textview_name_center);
        mTextViewAgeCenter = (TextView) view.findViewById(R.id.textview_age_center);
        mTextViewNameLeft = (TextView) view.findViewById(R.id.textview_name_left);
        mTextViewAgeLeft = (TextView) view.findViewById(R.id.textview_age_left);
        mTextViewNameRight = (TextView) view.findViewById(R.id.textview_name_right);
        mTextViewAgeRight = (TextView) view.findViewById(R.id.textview_age_right);
        mImageViewFlower = (ImageView) view.findViewById(R.id.imageview_flower);
        mImageViewWine = (ImageView) view.findViewById(R.id.imageview_wine);
        mImageViewWine2 = (ImageView) view.findViewById(R.id.imageview_wine2);
        mImageViewThus = (ImageView) view.findViewById(R.id.imageview_thus);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (back != null) {
            initData(back);
        }
    }

    public void initData(DeadCallBack back) {
        this.back = back;
        initHeadImage(back);
        initText(back);
        flowerState(back);
        cupState(back);
        incensoryState(back);
    }


    private void initHeadImage(DeadCallBack back) {
        String imgUrl = back.getManPhoto();
        imgUrl = imgUrl.trim();
        if (imgUrl.startsWith("."))
            imgUrl = imgUrl.substring(1);
        String imageUrl = Address.IMAGEADDRESS + imgUrl;
        mImageViewHead.setImageURI(Uri.parse(imageUrl));
    }

    private void initText(DeadCallBack back) {
        mInformations = back.getRows();
        if (mInformations.size() == 1) {
            mTextViewNameCenter.setVisibility(View.VISIBLE);
            mTextViewAgeCenter.setVisibility(View.VISIBLE);
            mTextViewNameCenter.setText(mInformations.get(0).getDeadName().trim());
            mTextViewAgeCenter.setText(mInformations.get(0).getBFYEAR().trim());
            mTextViewAgeLeft.setVisibility(View.INVISIBLE);
            mTextViewNameLeft.setVisibility(View.INVISIBLE);
            mTextViewNameRight.setVisibility(View.INVISIBLE);
            mTextViewAgeRight.setVisibility(View.INVISIBLE);

        } else {
            mTextViewNameCenter.setVisibility(View.INVISIBLE);
            mTextViewAgeCenter.setVisibility(View.INVISIBLE);
            mTextViewAgeLeft.setVisibility(View.VISIBLE);
            mTextViewNameLeft.setVisibility(View.VISIBLE);
            mTextViewNameRight.setVisibility(View.VISIBLE);
            mTextViewAgeRight.setVisibility(View.VISIBLE);
            if (mInformations.size() == 2) {
                for (int i = 0; i < mInformations.size(); i++) {
                    if (i == 0) {
                        mTextViewNameLeft.setText(mInformations.get(i).getDeadName().trim());
                        mTextViewAgeLeft.setText(mInformations.get(i).getBFYEAR().trim());
                    } else {
                        mTextViewNameRight.setText(mInformations.get(i).getDeadName().trim());
                        mTextViewAgeRight.setText(mInformations.get(i).getBFYEAR().trim());
                    }
                }

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_worship:
                Intent intent = new Intent(getActivity(), WorshipStyleActivity.class);
                Bundle b = new Bundle();
                b.putString("id", back.getId());
                intent.putExtra("id", b);
                startActivity(intent);
                break;
            case R.id.imageview_music:
                Intent intent1 = new Intent(getActivity(), MPlayerActivity.class);
                intent1.putExtra("musicId", back.getMp3State());
                intent1.putExtra("id", back.getId());
                startActivity(intent1);
                break;
            case R.id.imageview_head:
                ((DeadHomePageActivity) getActivity()).toInfomation();
                //滑动一页
                break;
            default:
                break;
        }
    }

    //鲜花
    private void flowerState(DeadCallBack back) {
        mImageViewFlower.setVisibility(View.VISIBLE);
        switch (back.getFlowerState()) {
            case "1":
                mImageViewFlower.setImageResource(R.mipmap.flower1);
                break;
            case "2":
                mImageViewFlower.setImageResource(R.mipmap.flower2);
                break;
            case "3":
                mImageViewFlower.setImageResource(R.mipmap.flower3);
                break;
            case "4":
                mImageViewFlower.setImageResource(R.mipmap.flower4);
                break;
            case "5":
                mImageViewFlower.setImageResource(R.mipmap.flower5);
                break;
            case "6":
                mImageViewFlower.setImageResource(R.mipmap.flower6);
                break;
            case "7":
                mImageViewFlower.setImageResource(R.mipmap.flower7);
                break;
            case "8":
                mImageViewFlower.setImageResource(R.mipmap.flower8);
                break;
            default:
                mImageViewFlower.setVisibility(View.INVISIBLE);
                break;
        }
    }

    //酒
    private void incensoryState(DeadCallBack back) {
        mImageViewWine.setVisibility(View.VISIBLE);
        mImageViewWine2.setVisibility(View.VISIBLE);
        switch (back.getIncensoryState()) {
            case "1":
                mImageViewWine.setImageResource(R.mipmap.wine1);
                mImageViewWine2.setImageResource(R.mipmap.wine1);
                break;
            case "2":
                mImageViewWine.setImageResource(R.mipmap.wine2);
                mImageViewWine2.setImageResource(R.mipmap.wine2);
                break;
            case "3":
                mImageViewWine.setImageResource(R.mipmap.wine3);
                mImageViewWine2.setImageResource(R.mipmap.wine3);
                break;
            case "4":
                mImageViewWine.setImageResource(R.mipmap.wine4);
                mImageViewWine2.setImageResource(R.mipmap.wine4);
                break;
            default:
                mImageViewWine.setVisibility(View.INVISIBLE);
                mImageViewWine2.setVisibility(View.INVISIBLE);
                break;
        }

    }

    //香
    private void cupState(DeadCallBack back) {
        mImageViewThus.setVisibility(View.VISIBLE);
        switch (back.getCupState()) {
            case "1":
                mImageViewThus.setImageResource(R.mipmap.thus_red);
                break;
            case "2":
                mImageViewThus.setImageResource(R.mipmap.thus_forefather);
                break;
            case "3":
                mImageViewThus.setImageResource(R.mipmap.thus_god);
                break;
            default:
                mImageViewThus.setVisibility(View.INVISIBLE);
                break;
        }
    }


}
