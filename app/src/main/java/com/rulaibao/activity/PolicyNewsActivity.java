package com.rulaibao.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rulaibao.R;
import com.rulaibao.adapter.PolicyNewsAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.PolicyNewsList3B;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.widget.TitleBar;

/**
 * 保单消息
 * Created by junde on 2018/4/21.
 */

public class PolicyNewsActivity extends BaseActivity implements View.OnClickListener {

    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recycler_view;
    private MouldList<PolicyNewsList3B> totalList = new MouldList<>();
    private PolicyNewsAdapter policyNewsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_same_list_layout);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.mipmap.icon_back, false).setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_policy_news)).showMore(false).setOnActionListener(new TitleBar.OnActionListener() {

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
        for (int i = 0; i < 10; i++) {
            PolicyNewsList3B bean = new PolicyNewsList3B();
            bean.setProductName("国华盛世年年年金险保险C款" + i);
            bean.setDate("[04-12 12:12]" + "--10" + i);
            bean.setStatus("保单状态更改为已承保" + "--100" + i);
            totalList.add(bean);
        }

        initRecylerView();
    }

    private void initRecylerView() {
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        policyNewsAdapter = new PolicyNewsAdapter(this, totalList);
        recycler_view.setAdapter(policyNewsAdapter);
        //添加动画
        recycler_view.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void onClick(View v) {

    }
}
