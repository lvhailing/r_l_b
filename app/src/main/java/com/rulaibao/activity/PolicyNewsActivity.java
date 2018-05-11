package com.rulaibao.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.rulaibao.R;
import com.rulaibao.adapter.PolicyNewsAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.CommissionNewsList1B;
import com.rulaibao.bean.CommissionNewsList2B;
import com.rulaibao.bean.PolicyNewsList3B;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.widget.TitleBar;

import java.util.HashMap;

/**
 * 保单消息
 * Created by junde on 2018/4/21.
 */

public class PolicyNewsActivity extends BaseActivity {

    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recycler_view;
    private MouldList<CommissionNewsList2B> totalList = new MouldList<>();
    private PolicyNewsAdapter policyNewsAdapter;
    private int currentPage = 1;// 当前页


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_same_list_layout);

        initTopTitle();
        initView();
        requesData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.mipmap.icon_back, false).setIndicator(R.mipmap.icon_back)
             .setCenterText(getResources().getString(R.string.title_policy_news)).showMore(false).setOnActionListener(new TitleBar.OnActionListener() {

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

        initRecylerView();
    }

    private void requesData() {

        HashMap<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("busiType", "insurance ");

        HtmlRequest.getMessageListData(PolicyNewsActivity.this, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (swipe_refresh.isRefreshing()) {
                    //请求返回后，无论本次请求成功与否，都关闭下拉旋转
                    swipe_refresh.setRefreshing(false);
                }

                if (params.result == null) {
                    Toast.makeText(PolicyNewsActivity.this, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                    return;
                }

                CommissionNewsList1B data = (CommissionNewsList1B) params.result;
                MouldList<CommissionNewsList2B> everyList = data.getList();
                if (everyList == null) {
                    return;
                }
                if (everyList.size() == 0 && currentPage != 1) {
                    Toast.makeText(PolicyNewsActivity.this, "已显示全部", Toast.LENGTH_SHORT).show();
                    policyNewsAdapter.changeMoreStatus(policyNewsAdapter.NO_LOAD_MORE);
                }
                if (currentPage == 1) {
                    //刚进来时 加载第一页数据，或下拉刷新 重新加载数据 。这两种情况之前的数据都清掉
                    totalList.clear();
                }
                totalList.addAll(everyList);
                if (totalList.size() != 0 && totalList.size() % 10 == 0) {
                    policyNewsAdapter.changeMoreStatus(policyNewsAdapter.PULLUP_LOAD_MORE);
                } else {
                    policyNewsAdapter.changeMoreStatus(policyNewsAdapter.NO_LOAD_MORE);
                }
            }
        });
    }

    private void initRecylerView() {
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        policyNewsAdapter = new PolicyNewsAdapter(this, totalList);
        recycler_view.setAdapter(policyNewsAdapter);
        //添加动画
        recycler_view.setItemAnimator(new DefaultItemAnimator());

    }

}
