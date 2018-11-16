package com.rulaibao.activity;

import android.os.Bundle;

import com.rulaibao.R;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.widget.TitleBar;

/**
 * 工资单详情
 * Created by hong on 2018/11/9.
 */

public class PayrollDetailActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_payroll_detail);

        initTopTitle();
        initView();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.mipmap.logo, false)
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_payroll_detail))
                .showMore(false).setOnActionListener(new TitleBar.OnActionListener() {

            @Override
            public void onMenu(int id) {
            }

            @Override
            public void onBack() {
                finish();
            }

            @Override
            public void onAction(int id) {
            }
        });
    }

    private void initView() {

    }

    @Override
    public void initData() {

    }
}
