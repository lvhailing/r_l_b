package com.rulaibao.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rulaibao.R;
import com.rulaibao.adapter.CommissionNewsAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.CommissionNewsList3B;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.widget.TitleBar;

/**
 *  佣金消息
 * Created by junde on 2018/4/21.
 */

public class CommissionNewsActivity extends BaseActivity implements View.OnClickListener{

    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recycler_view;
    private CommissionNewsAdapter commissionNewsAdapter;
    private MouldList<CommissionNewsList3B> totalList = new MouldList<>();


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
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false)
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_commission_news))
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
            CommissionNewsList3B bean = new CommissionNewsList3B();
            bean.setCommissionIncome("中纪委发文谈落马：十九大后“首虎”");
            bean.setIncomeDate("04-1"+i);
            bean.setIncomeTime("08:10"+"--"+i);
            totalList.add(bean);
        }

        initRecylerView();
    }

    private void initRecylerView() {
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        commissionNewsAdapter = new CommissionNewsAdapter(this, totalList);
        recycler_view.setAdapter(commissionNewsAdapter);
        //添加动画
        recycler_view.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void onClick(View v) {

    }
}
