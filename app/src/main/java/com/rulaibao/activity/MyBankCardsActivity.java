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
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.rulaibao.R;
import com.rulaibao.adapter.MyBankCardsAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.BankCardList1B;
import com.rulaibao.bean.BankCardList2B;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.widget.TitleBar;

import java.util.LinkedHashMap;

/**
 * 我的银行卡
 * Created by hong on 2018/11/14.
 */

public class MyBankCardsActivity extends BaseActivity implements View.OnClickListener {

    private ViewSwitcher vs;
    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recycler_view;
    private MyBankCardsAdapter myBankCardsAdapter;
    private MouldList<BankCardList2B> totalList = new MouldList<>();
    private int currentPage = 1;    //当前页
    private Button btn_add_new_bank_card; //  新增银行卡
    private MouldList<BankCardList2B> everyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_my_bank_cards);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.mipmap.logo, false).setIndicator(R.mipmap.icon_back)
             .setCenterText(getResources().getString(R.string.title_my_bank_cards)).showMore(false).setOnActionListener(new TitleBar.OnActionListener() {

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
        requestData();
    }

    private void requestData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("userId", userId);

        HtmlRequest.getBankCardListData(this, param, new BaseRequester.OnRequestListener() {
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
                BankCardList1B data = (BankCardList1B) params.result;
                everyList = data.getUserBankCardList();
                if (everyList == null) {
                    return;
                }
                if (everyList.size() == 0 && currentPage != 1) {
                    Toast.makeText(mContext, "已显示全部", Toast.LENGTH_SHORT).show();
                    myBankCardsAdapter.changeMoreStatus(myBankCardsAdapter.NO_LOAD_MORE);
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
                    myBankCardsAdapter.changeMoreStatus(myBankCardsAdapter.NO_LOAD_MORE);
                } else {
                    // 其他，均显示“数据加载中”的提示
                    myBankCardsAdapter.changeMoreStatus(myBankCardsAdapter.PULLUP_LOAD_MORE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_new_bank_card: // 新增银行卡
                Intent intent = new Intent(this, AddNewBankCardActivity.class);
                startActivity(intent);
        }

    }
}
