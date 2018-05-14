package com.rulaibao.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.rulaibao.R;
import com.rulaibao.adapter.RecommendRecordAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.Plan3B;
import com.rulaibao.bean.Recommend1B;
import com.rulaibao.bean.RecommendRecordList1B;
import com.rulaibao.bean.RecommendRecordList2B;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.widget.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *  推荐记录
 * Created by junde on 2018/4/20.
 */

public class RecommendRecordActivity extends BaseActivity{

    private TextView tv_amount_people;
    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recycler_view;
    private MouldList<RecommendRecordList2B> totalList = new MouldList<>();
    private RecommendRecordAdapter recommendRecordAdapter;
    private int currentPage = 1;    //当前页

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_recommend_record);

        initTopTitle();
        initView();
        initListener();
        requestData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false)
             .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.setting_recommend_record)).showMore(false)
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
        tv_amount_people = (TextView) findViewById(R.id.tv_amount_people);
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);

        initRecylerView();
    }

    private void initRecylerView() {
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recommendRecordAdapter = new RecommendRecordAdapter(this, totalList);
        recycler_view.setAdapter(recommendRecordAdapter);
        //添加动画
        recycler_view.setItemAnimator(new DefaultItemAnimator());

    }

    private void initListener() {
        initPullRefresh();
        initLoadMoreListener();
    }

    private void initPullRefresh() {
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                getRecommendRecordData();

                //刷新完成
//                swipe_refresh.setRefreshing(false);

            }
        });
    }

    private void initLoadMoreListener() {
        recycler_view.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem = 0;
            private int firstVisibleItem = 0;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //判断RecyclerView的状态 是空闲时，同时，是最后一个可见的ITEM时才加载
                if(newState==RecyclerView.SCROLL_STATE_IDLE&&lastVisibleItem+1==recommendRecordAdapter.getItemCount()&& firstVisibleItem != 0){
                    currentPage++;
                    getRecommendRecordData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                lastVisibleItem=layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    /**
     * 获取推荐记录数据
     */
    private void requestData() {
        getRecommendRecordData();
    }

    private void getRecommendRecordData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("userId", userId);
        param.put("page", currentPage + "");

        HtmlRequest.getRecommendRecordData(RecommendRecordActivity.this, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (swipe_refresh.isRefreshing()) {
                    //请求返回后，无论本次请求成功与否，都关闭下拉旋转
                    swipe_refresh.setRefreshing(false);
                }

                if (params.result == null) {
                    Toast.makeText(RecommendRecordActivity.this, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                    return;
                }

                RecommendRecordList1B  data = (RecommendRecordList1B) params.result;
                tv_amount_people.setText(data.getTotal());
                MouldList<RecommendRecordList2B> everyList = data.getRecommendList();
                if (everyList == null) {
                    return;
                }
                if (everyList.size() == 0 && currentPage != 1) {
                    Toast.makeText(mContext, "已显示全部", Toast.LENGTH_SHORT).show();
                    recommendRecordAdapter.changeMoreStatus(recommendRecordAdapter.NO_LOAD_MORE);
                }
                if (currentPage == 1) {
                    //刚进来时 加载第一页数据，或下拉刷新 重新加载数据 。这两种情况之前的数据都清掉
                    totalList.clear();
                }
                totalList.addAll(everyList);
                if (totalList.size() != 0 && totalList.size() % 10 == 0) {
                    recommendRecordAdapter.changeMoreStatus(recommendRecordAdapter.PULLUP_LOAD_MORE);
                } else {
                    recommendRecordAdapter.changeMoreStatus(recommendRecordAdapter.NO_LOAD_MORE);
                }
            }
        });
    }

}
