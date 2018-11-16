package com.rulaibao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.rulaibao.R;
import com.rulaibao.adapter.MyBankCardsAdapter;
import com.rulaibao.adapter.RenewalReminderAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.MyBankCardsList2B;
import com.rulaibao.bean.RenewalReminderList2B;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.widget.TitleBar;

/**
 * 我的银行卡
 * Created by hong on 2018/11/14.
 */

public class MyBankCardsActivity extends BaseActivity implements View.OnClickListener {

    private ViewSwitcher vs;
    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recycler_view;
    private MyBankCardsAdapter myBankCardsAdapter;
    private MouldList<MyBankCardsList2B> totalList = new MouldList<>();
    private int currentPage = 1;    //当前页
    private Button btn_add_new_bank_card; //  新增银行卡

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_my_bank_cards);

        initTopTitle();
        initView();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.mipmap.logo, false).setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_my_bank_cards)).showMore(false).setOnActionListener(new TitleBar.OnActionListener() {

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
        tv_empty.setText("暂无待续保保单");
        btn_add_new_bank_card = (Button) findViewById(R.id.btn_add_new_bank_card);

        btn_add_new_bank_card.setOnClickListener(this);
        initRecylerView();
    }

    private void initRecylerView() {
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        myBankCardsAdapter = new MyBankCardsAdapter(this, totalList);
        recycler_view.setAdapter(myBankCardsAdapter);
        //添加动画
        recycler_view.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_new_bank_card:
                Intent intent = new Intent(this, AddNewBankCardActivity.class);
                startActivity(intent);
        }

    }
}
