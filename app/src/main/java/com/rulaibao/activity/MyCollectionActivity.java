package com.rulaibao.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.rulaibao.R;
import com.rulaibao.adapter.MyCollectionVpAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.fragment.MyCollectionFragment;
import com.rulaibao.fragment.PolicyRecordListFragment;
import com.rulaibao.widget.TitleBar;

/**
 *   我的收藏
 * Created by junde on 2018/4/21.
 */

public class MyCollectionActivity extends BaseActivity implements View.OnClickListener {

    private TabLayout sliding_tabs;
    private ViewPager viewpager;
    private String[] titles;
    private MyCollectionVpAdapter myCollectionVpAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_my_collection);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false)
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_my_collection))
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
        sliding_tabs = (TabLayout) findViewById(R.id.sliding_tabs);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
    }

    private void initData() {
        titles = new String[]{"全部", "重疾险","年金险","终身寿险","意外险", "医疗险","一老一小","企业团体"};
        myCollectionVpAdapter = new MyCollectionVpAdapter(getSupportFragmentManager(), titles, this);
        ((MyCollectionFragment) myCollectionVpAdapter.getItem(0)).setUserId(userId);
        viewpager.setAdapter(myCollectionVpAdapter);

        //将TabLayout和ViewPager关联起来
        sliding_tabs.setupWithViewPager(viewpager);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((MyCollectionFragment) myCollectionVpAdapter.getItem(position)).setUserId(userId);
                ((MyCollectionFragment) myCollectionVpAdapter.getItem(position)).getTabTitleCurrentPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onClick(View v) {

    }
}
