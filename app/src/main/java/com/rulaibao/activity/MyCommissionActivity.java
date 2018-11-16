package com.rulaibao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.rulaibao.R;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.widget.TitleBar;

/**
 *  我的佣金
 * Created by hong on 2018/11/5.
 */

public class MyCommissionActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_waiting_commission; // 待发佣金
    private LinearLayout ll_my_commission; // 已发佣金
    private RelativeLayout rl_my_payroll; // 我的工资单
    private RelativeLayout rl_my_bank_card; // 我的银行卡
    private RelativeLayout rl_tax_rules; // 扣税规则

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_my_commission);

        initTopTitle();
        initView();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.mipmap.logo, false)
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_my_commission))
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
        ll_waiting_commission = (LinearLayout) findViewById(R.id.ll_waiting_commission);
        ll_my_commission = (LinearLayout) findViewById(R.id.ll_my_commission);
        rl_my_payroll = (RelativeLayout) findViewById(R.id.rl_my_payroll);
        rl_my_bank_card = (RelativeLayout) findViewById(R.id.rl_my_bank_card);
        rl_tax_rules = (RelativeLayout) findViewById(R.id.rl_tax_rules);

        ll_waiting_commission.setOnClickListener(this);
        ll_my_commission.setOnClickListener(this);
        rl_my_payroll.setOnClickListener(this);
        rl_my_bank_card.setOnClickListener(this);
        rl_tax_rules.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_waiting_commission: // 待发佣金
                intent = new Intent(this, WaitingCommissionActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_my_commission: // 已发佣金
                intent = new Intent(this, HaveGetCommissionActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_my_payroll: // 我的工资单
                intent = new Intent(this, MyPayrollActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_my_bank_card: // 我的银行卡
                intent = new Intent(this, MyBankCardsActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_tax_rules: // 扣税规则
                intent = new Intent(this, TaxDeductionRulesActivity.class);
                startActivity(intent);
                break;
        }
    }
}
