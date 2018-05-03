package com.rulaibao.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rulaibao.R;
import com.rulaibao.adapter.PlatformBulletinAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.PlatformBulletinList3B;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.widget.TitleBar;

/**
 *   平台公告 列表
 * Created by junde on 2018/4/16.
 */

public class PlatformBulletinActivity extends BaseActivity implements View.OnClickListener {

    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recycler_view;
    private PlatformBulletinAdapter platformBulletinAdapter;
    private MouldList<PlatformBulletinList3B> totalList = new MouldList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_platform_bulletin);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false)
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_platform_bulletin))
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
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
    }

    private void initData() {
         for(int i=0;i<10;i++){
        PlatformBulletinList3B bean = new PlatformBulletinList3B();
        bean.setSendTime("04-12 12:30");
        bean.setDescription("哈哈，我是测试的内容！哈哈，我是测试的内容！");
        bean.setTitle("测试数据公告标题测试数据公告标题测试数据测试数据公告标题测试数据公告标题测试数据");
        bean.setBulletinId("2222");
        totalList.add(bean);
    }

        initRecylerView();
    }

    private void initRecylerView() {
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        platformBulletinAdapter = new PlatformBulletinAdapter(this, totalList);
        recycler_view.setAdapter(platformBulletinAdapter);
        //添加动画
        recycler_view.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void onClick(View v) {

    }


//    public void test(){
//        for(int i=0;i<10;i++){
//            PlatformBulletinList3B bean = new PlatformBulletinList3B();
//            bean.setSendTime("04-12 12:30");
//            bean.setDescription("-----");
//            bean.setTitle("公告标题测试数据");
//            bean.setBulletinId("2222");
//            totalList.add(bean);
//        }
//
//    }
}
