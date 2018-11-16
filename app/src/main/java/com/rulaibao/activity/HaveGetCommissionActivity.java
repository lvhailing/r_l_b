package com.rulaibao.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.rulaibao.R;
import com.rulaibao.adapter.HaveGetCommissionAdapter;
import com.rulaibao.adapter.WaitingCommissionAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.HaveGetCommissionList2B;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.widget.TitleBar;

/**
 * 已发佣金
 * Created by hong on 2018/11/9.
 */

public class HaveGetCommissionActivity extends BaseActivity {

    private ViewSwitcher vs;
    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recycler_view;
    private HaveGetCommissionAdapter haveGetCommissionAdapter;
    private MouldList<HaveGetCommissionList2B> totalList = new MouldList<>();
    private int currentPage = 1; //当前页

    private MouldList<HaveGetCommissionList2B> everyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_have_get_commitssion);

        initTopTitle();
        initView();
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
    }
}
