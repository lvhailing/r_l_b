package com.rulaibao.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rulaibao.R;
import com.rulaibao.adapter.PolicyRrcordListVPAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.PolicyRecordList1B;
import com.rulaibao.fragment.PolicyRecordListFragment;
import com.rulaibao.widget.TitleBar;

/**
 * 保单记录
 * Created by junde on 2018/4/13.
 */

public class PolicyRecordListActivity extends BaseActivity {

    private TabLayout sliding_tabs;
    private ViewPager viewpager;
    private String[] titles;
    private PolicyRrcordListVPAdapter vpAdapter;
    private String flag; // 如果flag =1000,则说明是要展示指定的tab及对应的内容；
    private int currentTabPosition; // currentTabPosition = 0 (全部)、currentTabPosition = 1 (待审核)、currentTabPosition = 2 (已承保)、currentTabPosition = 3 (问题件)、currentTabPosition = 4 (回执签收)
    private TabLayout.Tab tab;
    private PolicyRecordListFragment fragment;
//    private PolicyRecordList1B data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_policy_record_list);

        initTopTitle();
        initView();
        initData();

    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false)
                .setIndicator(R.mipmap.icon_back)
                .setCenterText(getResources().getString(R.string.title_policy_record))
                .showMore(false)
                .setOnActionListener(new TitleBar.OnActionListener() {

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
//        flag = getIntent().getStringExtra("flag");
        currentTabPosition = getIntent().getIntExtra("position", 0);

        sliding_tabs = (TabLayout) findViewById(R.id.sliding_tabs);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
    }

    private void initData() {
        titles = new String[]{"全部（）", "待审核（）", "已承保（）", "问题件（）", "回执签收（）"};
        vpAdapter = new PolicyRrcordListVPAdapter(getSupportFragmentManager(), titles, this);
        ((PolicyRecordListFragment) vpAdapter.getItem(currentTabPosition)).getTabTitleCurrentPosition(currentTabPosition);
        viewpager.setAdapter(vpAdapter);
        sliding_tabs.setupWithViewPager(viewpager);  //将TabLayout和ViewPager关联起来
        chooseAppointedTab(currentTabPosition);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((PolicyRecordListFragment) vpAdapter.getItem(position)).getTabTitleCurrentPosition(position);
                ((PolicyRecordListFragment) vpAdapter.getItem(position)).requestData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     *
     */
    private void chooseAppointedTab(int position) {
        for (int i = 0; i < sliding_tabs.getTabCount(); i++) {
            TabLayout.Tab tab = sliding_tabs.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(getTabView(i));
            }
        }

        //设置1tab选中
        sliding_tabs.getTabAt(currentTabPosition).select();
        viewpager.setCurrentItem(currentTabPosition);
    }

    public void refreshTabTitle(PolicyRecordList1B data) {
        View titleView1 = (View) sliding_tabs.getTabAt(0).getCustomView();
        TextView title1 = (TextView) titleView1.findViewById(R.id.tv_title);
        title1.setText("全部（" + data.getAllTotal() + "）");

        View titleView2 = (View) sliding_tabs.getTabAt(1).getCustomView();
        TextView title2 = (TextView) titleView2.findViewById(R.id.tv_title);
        title2.setText("待审核（" + data.getInitTotal() + "）");

        View titleView3 = (View) sliding_tabs.getTabAt(2).getCustomView();
        TextView title3 = (TextView) titleView3.findViewById(R.id.tv_title);
        title3.setText("已承保（" + data.getPayedTotal() + "）");

        View titleView4 = (View) sliding_tabs.getTabAt(3).getCustomView();
        TextView title4 = (TextView) titleView4.findViewById(R.id.tv_title);
        title4.setText("问题件（" + data.getRejectedTotal() + "）");

        View titleView5 = (View) sliding_tabs.getTabAt(4).getCustomView();
        TextView title5 = (TextView) titleView5.findViewById(R.id.tv_title);
        title5.setText("回执签收（" + data.getReceiptSignedTotal() + "）");

    }

    public View getTabView(int position) {
        View titleView = LayoutInflater.from(this).inflate(R.layout.title_item, null);
        TextView title = (TextView) titleView.findViewById(R.id.tv_title);
        title.setText(titles[position]);
        if (position == currentTabPosition) {
            title.setTextColor(this.getResources().getColor(R.color.txt_black1));
        } else {
            title.setTextColor(this.getResources().getColor(R.color.txt_black2));
        }
//        TextView num = (TextView) titleView.findViewById(R.id.tv_num);
//        num.setVisibility(View.VISIBLE);
//        num.setText("1");
        return titleView;
    }


}
