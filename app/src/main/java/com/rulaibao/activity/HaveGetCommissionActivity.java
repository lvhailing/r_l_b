package com.rulaibao.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.rulaibao.R;
import com.rulaibao.adapter.HaveGetCommissionAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.CommissionList1B;
import com.rulaibao.bean.CommissionList2B;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.widget.TitleBar;

import java.util.LinkedHashMap;

/**
 * 已发佣金
 * Created by hong on 2018/11/9.
 */

public class HaveGetCommissionActivity extends BaseActivity {

    private ViewSwitcher vs;
    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recycler_view;
    private HaveGetCommissionAdapter haveGetCommissionAdapter;
    private MouldList<CommissionList2B> totalList = new MouldList<>();
    private int currentPage = 1; //当前页

    private MouldList<CommissionList2B> everyList;
    private String yes; // 已发佣金

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_have_get_commitssion);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.mipmap.logo, false)
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_have_get_commission))
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
        yes = getIntent().getStringExtra("commissionStatus");

        vs = (ViewSwitcher) findViewById(R.id.vs);
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);

        TextView tv_empty = (TextView) findViewById(R.id.tv_empty);
        tv_empty.setText("暂无已发佣金");

        initRecyclerView();
    }

    private void initRecyclerView() {
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        haveGetCommissionAdapter = new HaveGetCommissionAdapter(this, totalList);
        recycler_view.setAdapter(haveGetCommissionAdapter);
        //添加动画
        recycler_view.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void initData() {
        requestData();
    }

    /**
     *  获取 佣金列表（已发佣金）
     */
    private void requestData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("userId", userId);
        param.put("commissionStatus", yes);
        param.put("page", currentPage+"");

        HtmlRequest.getCommissionListData(this, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (swipe_refresh.isRefreshing()) {
                    //请求返回后，无论本次请求成功与否，都关闭下拉旋转
                    swipe_refresh.setRefreshing(false);
                }

                if (params==null || params.result == null) {
                    vs.setDisplayedChild(1);
                    // Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                    return;
                }
                CommissionList1B data = (CommissionList1B) params.result;
                everyList = data.getList();
                if (everyList == null) {
                    return;
                }
                if (everyList.size() == 0 && currentPage != 1) {
                    Toast.makeText(mContext, "已显示全部", Toast.LENGTH_SHORT).show();
                    haveGetCommissionAdapter.changeMoreStatus(haveGetCommissionAdapter.NO_LOAD_MORE);
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
//                if (totalList.size() != 0 && totalList.size() % 10 == 0) {
//                    myAskAdapter.changeMoreStatus(myAskAdapter.PULLUP_LOAD_MORE);
//                } else {
//                    myAskAdapter.changeMoreStatus(myAskAdapter.NO_LOAD_MORE);
//                }
                if (everyList.size() != 10) {
                    // 本次取回的数据为不是10条，代表取完了
                    haveGetCommissionAdapter.changeMoreStatus(haveGetCommissionAdapter.NO_LOAD_MORE);
                } else {
                    // 其他，均显示“数据加载中”的提示
                    haveGetCommissionAdapter.changeMoreStatus(haveGetCommissionAdapter.PULLUP_LOAD_MORE);
                }
            }
        });
    }
}
