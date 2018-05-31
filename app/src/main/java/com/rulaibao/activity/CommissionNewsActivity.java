package com.rulaibao.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.rulaibao.R;
import com.rulaibao.adapter.CommissionNewsAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.CommissionNewsList1B;
import com.rulaibao.bean.CommissionNewsList2B;
import com.rulaibao.bean.PlatformBulletinList1B;
import com.rulaibao.bean.PlatformBulletinList2B;
import com.rulaibao.bean.UnreadNewsCount2B;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.widget.TitleBar;

import java.util.HashMap;

/**
 *  佣金消息
 * Created by junde on 2018/4/21.
 */

public class CommissionNewsActivity extends BaseActivity implements View.OnClickListener{

    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recycler_view;
    private CommissionNewsAdapter commissionNewsAdapter;
    private MouldList<CommissionNewsList2B> totalList = new MouldList<>();
    private int currentPage = 1;  //当前页
    private ViewSwitcher vs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_same_list_layout);

        initTopTitle();
        initView();
        initListener();
        requestData();
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
        vs = (ViewSwitcher) findViewById(R.id.vs);
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);

        TextView tv_empty = (TextView) findViewById(R.id.tv_empty);
        tv_empty.setText("暂无相关消息");

        initRecylerView();
    }

    private void initListener() {
        initPullRefresh();
        initLoadMoreListener();
    }

    private void initPullRefresh() {
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {  // 下拉刷新
                currentPage = 1;
                requestData();
            }
        });
    }

    private void initLoadMoreListener() {
        recycler_view.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private int firstVisibleItem = 0;
            private int lastVisibleItem = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //判断RecyclerView的状态 是空闲时，同时，是最后一个可见的ITEM时才加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == commissionNewsAdapter.getItemCount() && firstVisibleItem != 0) {
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

    private void initRecylerView() {
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        commissionNewsAdapter = new CommissionNewsAdapter(this, totalList);
        recycler_view.setAdapter(commissionNewsAdapter);
        //添加动画
        recycler_view.setItemAnimator(new DefaultItemAnimator());

    }

    private void requestData() {
// userId: "18042709525931594357"
        HashMap<String, Object> param = new HashMap<>();
        param.put("userId",userId);
        param.put("busiType", "commission");
        param.put("page", currentPage+"");

        HtmlRequest.getMessageListData(CommissionNewsActivity.this, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (swipe_refresh.isRefreshing()) {
                    //请求返回后，无论本次请求成功与否，都关闭下拉旋转
                    swipe_refresh.setRefreshing(false);
                }

                if (params==null || params.result == null) {
                    vs.setDisplayedChild(1);
 //                   Toast.makeText(CommissionNewsActivity.this, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                    return;
                }

                CommissionNewsList1B data = (CommissionNewsList1B) params.result;
                MouldList<CommissionNewsList2B> everyList = data.getList();
                if (everyList == null) {
                    return;
                }
                if (everyList.size() == 0 && currentPage != 1) {
                    Toast.makeText(CommissionNewsActivity.this, "已显示全部", Toast.LENGTH_SHORT).show();
                    commissionNewsAdapter.changeMoreStatus(commissionNewsAdapter.NO_LOAD_MORE);
                }
                if (currentPage == 1) {
                    //刚进来时 加载第一页数据，或下拉刷新 重新加载数据 。这两种情况之前的数据都清掉
                    totalList.clear();
                }
                totalList.addAll(everyList);
                // 0:从后台获取到数据展示的布局；1：从后台没有获取到数据时展示的布局；
                if (totalList.size() == 0) {
                    vs.setDisplayedChild(1);
                } else {
                    vs.setDisplayedChild(0);
                }
                if (totalList.size() != 0 && totalList.size() % 10 == 0) {
                    vs.setDisplayedChild(0);
                    commissionNewsAdapter.changeMoreStatus(commissionNewsAdapter.PULLUP_LOAD_MORE);
                } else {
                    commissionNewsAdapter.changeMoreStatus(commissionNewsAdapter.NO_LOAD_MORE);
                }
            }
        });
    }



    @Override
    public void onClick(View v) {

    }
}
