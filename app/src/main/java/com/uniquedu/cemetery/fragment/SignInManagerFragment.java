package com.uniquedu.cemetery.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.uniquedu.cemetery.Address;
import com.uniquedu.cemetery.BaseFragment;
import com.uniquedu.cemetery.R;
import com.uniquedu.cemetery.activity.DeadHomePageActivity;
import com.uniquedu.cemetery.activity.ManagerPhotoActivity;
import com.uniquedu.cemetery.bean.SignIn;
import com.uniquedu.cemetery.utils.SharedPreferencesUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by ZhongHang on 2016/4/24.
 */
public class SignInManagerFragment extends BaseFragment {
    @InjectView(R.id.textview_head)
    TextView textviewHead;
    @InjectView(R.id.imageview_header)
    SimpleDraweeView imageviewHeader;
    @InjectView(R.id.textview_memorialName)
    TextView textviewMemorialName;
    @InjectView(R.id.textview_accountPhone)
    TextView textviewAccountPhone;
    @InjectView(R.id.textview_accountEmail)
    TextView textviewAccountEmail;
    @InjectView(R.id.lineat_galley)
    LinearLayout lineatGalley;
    @InjectView(R.id.linear_goto)
    LinearLayout linearGoto;
    private SignIn signIn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.center_manager, null);
        ButterKnife.inject(this, view);
        Gson gson = new Gson();
        signIn = gson.fromJson(SharedPreferencesUtil.getData(getContext().getApplicationContext(), "signIn", "").toString(), SignIn.class);
        textviewMemorialName.setText("纪念馆名称：" + signIn.getMemorialName());
        textviewAccountEmail.setText(signIn.getAccountEmail());
        textviewAccountPhone.setText(signIn.getAccountPhone());
        String imgUrl = signIn.getMemorialPhoto();
        if (!TextUtils.isEmpty(imgUrl)) {
            imgUrl = imgUrl.trim();
            if (imgUrl.startsWith("."))
                imgUrl = imgUrl.substring(1);
            String imageUrl = Address.IMAGEADDRESS + imgUrl;
            Uri uri = Uri.parse(imageUrl);
            imageviewHeader.setImageURI(uri);
        } else {
            Uri uri = Uri.parse("");
            imageviewHeader.setImageURI(uri);
        }
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.textview_head, R.id.imageview_header, R.id.textview_memorialName, R.id.textview_accountPhone, R.id.textview_accountEmail, R.id.lineat_galley, R.id.linear_goto})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textview_head:
                break;
            case R.id.imageview_header:
                break;
            case R.id.textview_memorialName:
                break;
            case R.id.textview_accountPhone:
                break;
            case R.id.textview_accountEmail:
                break;
            case R.id.lineat_galley:{
                Intent intent = new Intent(getContext().getApplicationContext(), ManagerPhotoActivity.class);
                intent.putExtra(DeadHomePageActivity.EXTRA_DEAID, signIn.getMemorialId());
                startActivity(intent);
            }
                break;
            case R.id.linear_goto: {
                Intent intent = new Intent(getContext().getApplicationContext(), DeadHomePageActivity.class);
                intent.putExtra(DeadHomePageActivity.EXTRA_DEAID, signIn.getMemorialId());
                startActivity(intent);
            }
                break;
        }
    }
}
