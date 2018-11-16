package com.rulaibao.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rulaibao.R;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.widget.TitleBar;

/**
 * 新增银行卡
 * Created by hong on 2018/11/15.
 */

public class AddNewBankCardActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_user_name;
    private TextView tv_user_id;
    private TextView tv_account_opening_bank;
    private TextView tv_account_opening_bank_address;
    private TextView tv_user_phone;

    private EditText et_account_opening_bank_name;
    private EditText et_bank_card_num;
    private EditText et_input_validation_code;

    private RelativeLayout rl_account_opening_bank;
    private RelativeLayout rl_account_opening_bank_address;

    private Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_add_new_bank_card);

        initTopTitle();
        initView();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.mipmap.logo, false)
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_my_bank_cards))
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
        tv_user_name = (TextView)findViewById(R.id.tv_user_name);
        tv_user_id = (TextView)findViewById(R.id.tv_user_id);
        tv_account_opening_bank = (TextView)findViewById(R.id.tv_account_opening_bank);
        tv_account_opening_bank_address = (TextView)findViewById(R.id.tv_account_opening_bank_address);
        tv_user_phone = (TextView)findViewById(R.id.tv_user_phone);

        et_account_opening_bank_name = (EditText)findViewById(R.id.et_account_opening_bank_name);
        et_bank_card_num = (EditText)findViewById(R.id.et_bank_card_num);
        et_input_validation_code = (EditText)findViewById(R.id.et_input_validation_code);

        rl_account_opening_bank = (RelativeLayout)findViewById(R.id.rl_account_opening_bank);
        rl_account_opening_bank_address = (RelativeLayout)findViewById(R.id.rl_account_opening_bank_address);

        btn_save = (Button)findViewById(R.id.btn_save);

        btn_save.setOnClickListener(this);
    }
    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:

        }
    }
}
