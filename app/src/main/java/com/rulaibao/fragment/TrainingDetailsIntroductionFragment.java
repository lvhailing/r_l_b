package com.rulaibao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rulaibao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * 课程详情 简介栏
 *
 */

public class TrainingDetailsIntroductionFragment extends BaseFragment {

    @BindView(R.id.iv_introduction)
    ImageView ivIntroduction;
    @BindView(R.id.tv_introduction_manager)
    TextView tvIntroductionManager;
    @BindView(R.id.tv_introduction_manager_name)
    TextView tvIntroductionManagerName;
    @BindView(R.id.tv_introduction_class_name)
    TextView tvIntroductionClassName;
    @BindView(R.id.tv_introduction_class_time)
    TextView tvIntroductionClassTime;
    @BindView(R.id.tv_introduction_class_type)
    TextView tvIntroductionClassType;
    @BindView(R.id.tv_introduction_content)
    TextView tvIntroductionContent;

    @Override
    protected View attachLayoutRes(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_training_details_introduction, container, false);

        } else {
            if (mView.getParent() != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
        }
        return mView;
    }

    @Override
    protected void initViews() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

}
