package com.uniquedu.cemetery.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.uniquedu.cemetery.BaseFragment;
import com.uniquedu.cemetery.R;
import com.uniquedu.cemetery.utils.SharedPreferencesUtil;

/**
 * Created by ZhongHang on 2016/3/4.
 */
public class CenterFragment extends BaseFragment {
    private FrameLayout mFrameLayout;
    private CenterSigninFragment centerSigninFragment;
    private SignInManagerFragment signInManagerFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_center, container, false);
        mFrameLayout = (FrameLayout) view.findViewById(R.id.frame_container);
        centerSigninFragment = new CenterSigninFragment();
        signInManagerFragment = new SignInManagerFragment();
        boolean isLogin = (boolean) SharedPreferencesUtil.getData(getContext().getApplicationContext(), "login", false);
        if (isLogin) {
            getChildFragmentManager().beginTransaction().add(R.id.frame_container, signInManagerFragment, "manager").commit();
        } else {
            getChildFragmentManager().beginTransaction().add(R.id.frame_container, centerSigninFragment, "login").commit();
        }
        getChildFragmentManager().executePendingTransactions();

        return view;
    }

    public void replace() {
        getChildFragmentManager().beginTransaction().replace(R.id.frame_container, signInManagerFragment, "manager").commit();
        getChildFragmentManager().executePendingTransactions();
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}
