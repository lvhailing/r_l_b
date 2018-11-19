package com.rulaibao.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.rulaibao.R;
import com.rulaibao.adapter.CommissionDetailAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.CommissionDetailList2B;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.widget.TitleBar;

/**
 *  佣金明细
 * Created by junde on 2018/11/13.
 */

public class CommissionListActivity extends BaseActivity {


    private ViewSwitcher vs;
    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recycler_view;
    private CommissionDetailAdapter commissionDetailAdapter;
    private MouldList<CommissionDetailList2B> totalList = new MouldList<>();
    private MouldList<CommissionDetailList2B> everyList;
    private int currentPage = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_commission_list);

        initTopTitle();
        initView();
        initListener();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.mipmap.logo, false)
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_commission_detail))
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
        vs = (ViewSwitcher) findViewById(R.id.vs);
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);

        TextView tv_empty = (TextView) findViewById(R.id.tv_empty);
        tv_empty.setText("暂无佣金");

        initRecyclerView();
    }

    private void initRecyclerView() {
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        commissionDetailAdapter = new CommissionDetailAdapter(this, totalList);
        recycler_view.setAdapter(commissionDetailAdapter);
        //添加动画
        recycler_view.setItemAnimator(new DefaultItemAnimator());
    }

    private void initListener() {
        initPullRefresh();
        initLoadMoreListener();
    }

    /**
     *  佣金明细列表下拉监听
     */
    private void initPullRefresh() {
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {  // 下拉刷新
                totalList.clear();
                currentPage = 1;
                requestData();
            }
        });
    }

    private void requestData() {

    }

    /**
     *  列表上拉监听
     */
    private void initLoadMoreListener() {
        recycler_view.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private int firstVisibleItem = 0;
            private int lastVisibleItem = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //判断RecyclerView的状态 是空闲时，同时，是最后一个可见的ITEM时才加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == commissionDetailAdapter.getItemCount() && firstVisibleItem != 0) {
                    if (everyList.size() == 0) {
                        return;
                    }
                    currentPage++;
                    requestData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }
    @Override
    public void initData() {

    }
}
