package com.rulaibao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rulaibao.R;
import com.rulaibao.adapter.TransactionRecordAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.RecommendRecordList1B;
import com.rulaibao.bean.RecommendRecordList2B;
import com.rulaibao.bean.TrackingList1B;
import com.rulaibao.bean.TrackingList2B;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.uitls.ActivityStack;
import com.rulaibao.widget.TitleBar;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 交易记录页面
 * Created by hong on 2018/4/10
 */
public class TransactionRecordActivity extends BaseActivity implements View.OnClickListener {

    private ActivityStack stack;
    private Intent intent;
    private TextView tv_total_commission; // 佣金总额
    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recycler_view;
    private MouldList<TrackingList2B> totalList = new MouldList<>();
    private TransactionRecordAdapter transactionRecordAdapter;
    private int currentPage = 1;    //当前页
    private String totalCommission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_transaction_record);

        initTopTitle();
        initView();
        requestTrackingRecordData();
        initListener();

    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false)
             .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_transaction_record))
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

    public void initView() {
        stack = ActivityStack.getActivityManage();
        stack.addActivity(this);
        totalCommission = getIntent().getStringExtra("totalCommission");

        tv_total_commission = (TextView) findViewById(R.id.tv_total_commission);
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
//        tv_total_commission.setText(totalCommission+"元");

        initRecylerView();
    }

    private void initRecylerView() {
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        transactionRecordAdapter = new TransactionRecordAdapter(this, totalList);
        recycler_view.setAdapter(transactionRecordAdapter);
        //添加动画
        recycler_view.setItemAnimator(new DefaultItemAnimator());

        //添加分割线
//        recycler_view.addItemDecoration(new RefreshItemDecoration(this,RefreshItemDecoration.VERTICAL_LIST));
//        recycler_view.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
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
                requestTrackingRecordData();

                //刷新完成
                swipe_refresh.setRefreshing(false);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        List<String> headDatas = new ArrayList<String>();
//                        for (int i = 20; i <30 ; i++) {
//                            headDatas.add("Heard Item "+i);
//                        }
//                        transactionRecordAdapter.AddHeaderItem(headDatas);
//
//                        //刷新完成
//                        swipe_refresh.setRefreshing(false);
//                        Toast.makeText(TransactionRecordActivity.this, "更新了 "+headDatas.size()+" 条目数据", Toast.LENGTH_SHORT).show();
//                    }
//                }, 3000);

            }
        });
    }

    private void initLoadMoreListener() {
        recycler_view.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private int firstVisibleItem;
            private int lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //判断RecyclerView的状态 是空闲时，同时，是最后一个可见的ITEM时才加载
                if(newState==RecyclerView.SCROLL_STATE_IDLE&&lastVisibleItem+1==transactionRecordAdapter.getItemCount()&& firstVisibleItem != 0){

                    //设置正在加载更多
//                    transactionRecordAdapter.changeMoreStatus(transactionRecordAdapter.LOADING_MORE);

                    currentPage ++;
                    requestTrackingRecordData();
                    //改为网络请求
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            //
//                            List<String> footerDatas = new ArrayList<String>();
//                            for (int i = 0; i< 10; i++) {
//                                footerDatas.add("footer  item" + i);
//                            }
//                            transactionRecordAdapter.AddFooterItem(footerDatas);
//                            //设置回到上拉加载更多
//                            transactionRecordAdapter.changeMoreStatus(transactionRecordAdapter.PULLUP_LOAD_MORE);
//                            //没有加载更多了
//                            //mRefreshAdapter.changeMoreStatus(mRefreshAdapter.NO_LOAD_MORE);
//                            Toast.makeText(TransactionRecordActivity.this, "更新了 "+footerDatas.size()+" 条目数据", Toast.LENGTH_SHORT).show();
//                        }
//                    }, 3000);
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
     *  获取 交易记录数据
     */
    private void requestTrackingRecordData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("userId", userId);
        param.put("page", currentPage + "");

        HtmlRequest.getTradeRecordData(TransactionRecordActivity.this, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params.result == null) {
                    Toast.makeText(TransactionRecordActivity.this, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                    return;
                }

                TrackingList1B data = (TrackingList1B) params.result;
                tv_total_commission.setText(data.getTotalCommission());
                MouldList<TrackingList2B> everyList = data.getRecordList();
                if ((everyList == null || everyList.size() == 0) && currentPage != 1) {
                    Toast.makeText(mContext, "已显示全部", Toast.LENGTH_SHORT).show();

                //没有加载更多了
                    transactionRecordAdapter.changeMoreStatus(transactionRecordAdapter.NO_LOAD_MORE);
                }
                if (currentPage == 1) {
                    //刚进来时 加载第一页数据，或下拉刷新 重新加载数据 。这两种情况之前的数据都清掉
                    totalList.clear();
                }
                totalList.addAll(everyList);
                //刷新数据
                transactionRecordAdapter.notifyDataSetChanged();

                //设置回到上拉加载更多
//                recommendRecordAdapter.changeMoreStatus(recommendRecordAdapter.PULLUP_LOAD_MORE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
//            case R.id.rl_setting_change_gesture_password: // 修改手势密码
//                intent = new Intent(TransactionRecordActivity.this, GestureVerifyActivity.class);
//                intent.putExtra("from", Urls.ACTIVITY_CHANGE_GESTURE);
//                intent.putExtra("title", getResources().getString(R.string.title_changegesture));
//                intent.putExtra("message", getResources().getString(R.string.set_gesture_pattern_old));
//                startActivityForResult(intent, 1001);
//
//                break;


            default:

                break;

        }

    }
}
