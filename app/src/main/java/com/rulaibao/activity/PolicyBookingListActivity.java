package com.rulaibao.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.rulaibao.R;
import com.rulaibao.adapter.PolicyBookingVpAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.widget.TitleBar;

/**
 *  保险预约列表
 * Created by junde on 2018/4/19.
 */

public class PolicyBookingListActivity extends BaseActivity{


    private TabLayout tab_layout;
    private ViewPager vp;
    private String[] titles;
    private PolicyBookingVpAdapter vpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_policy_booking_list);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false)
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_policy_booking_list))
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
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        vp = (ViewPager) findViewById(R.id.vp);
    }

    private void initData() {
        titles = new String[]{"全部（）", "待确认（）", "已确认（）", "已驳回（）"};

        vpAdapter = new PolicyBookingVpAdapter(getSupportFragmentManager(), titles, this);
        vp.setAdapter(vpAdapter);

        //将TabLayout和ViewPager关联起来
        tab_layout.setupWithViewPager(vp);
    }
}
