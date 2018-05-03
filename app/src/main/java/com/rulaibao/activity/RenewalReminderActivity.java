package com.rulaibao.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rulaibao.R;
import com.rulaibao.adapter.RenewalReminderAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.PolicyRecordList2B;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.widget.TitleBar;

/**
 *  续保提醒
 * Created by junde on 2018/4/18.
 */

public class RenewalReminderActivity extends BaseActivity implements View.OnClickListener {

    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recycler_view;
    private RenewalReminderAdapter renewalReminderAdapter;
    private MouldList<PolicyRecordList2B> totalList = new MouldList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_renewal_reminder);

        initTopTitle();
        initView();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false)
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_renewal_reminder))
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

        for (int i = 0; i < 10; i++) {
            PolicyRecordList2B bean = new PolicyRecordList2B();
            bean.setInsuranceName("国华人寿-盛世年年年金保险C款");
            bean.setStatus("待审核");
            bean.setCustomerName("王小金");
            bean.setInsurancPeremiums("8888.00元");
            bean.setInsurancePeriod("4年");
            totalList.add(bean);

            initRecylerView();
        }
    }

    private void initRecylerView() {
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        renewalReminderAdapter = new RenewalReminderAdapter(this, totalList);
        recycler_view.setAdapter(renewalReminderAdapter);
        //添加动画
        recycler_view.setItemAnimator(new DefaultItemAnimator());

    }


    @Override
    public void onClick(View v) {

    }
}
