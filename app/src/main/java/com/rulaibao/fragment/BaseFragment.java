package com.rulaibao.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {


    protected View mView;
    protected Context context;

    /**
     * 绑定布局文件
     * @return 布局文件id
     *
     */

    protected abstract View attachLayoutRes(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);


    /**
     * 初始化视图控件
     */
    protected abstract void initViews();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = attachLayoutRes(inflater, container, savedInstanceState);
        ButterKnife.bind(this, mView);
        initViews();
        return mView;
    }
}
