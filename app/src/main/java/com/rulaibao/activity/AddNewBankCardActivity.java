package com.rulaibao.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rulaibao.R;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.dialog.BankDialog;
import com.rulaibao.uitls.PreferenceUtil;
import com.rulaibao.uitls.encrypt.DESUtil;
import com.rulaibao.widget.TitleBar;

/**
 * 新增银行卡
 * Created by hong on 2018/11/15.
 */

public class AddNewBankCardActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_user_name; // 真实姓名
    private TextView tv_user_id; // 身份证号
    private TextView tv_account_opening_bank; // 开户银行
    private TextView tv_account_opening_bank_address; // 开户银行地址
    private TextView tv_user_phone; // 手机号

    private EditText et_account_opening_bank_name; // 输入开户行名称
    private EditText et_bank_card_num; // 输入银行卡号
    private EditText et_input_validation_code; // 输入验证码

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
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_add_bank_card))
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
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_user_id = (TextView) findViewById(R.id.tv_user_id);
        tv_account_opening_bank = (TextView) findViewById(R.id.tv_account_opening_bank);
        tv_account_opening_bank_address = (TextView) findViewById(R.id.tv_account_opening_bank_address);
        tv_user_phone = (TextView) findViewById(R.id.tv_user_phone);

        et_account_opening_bank_name = (EditText) findViewById(R.id.et_account_opening_bank_name);
        et_bank_card_num = (EditText) findViewById(R.id.et_bank_card_num);
        et_input_validation_code = (EditText) findViewById(R.id.et_input_validation_code);

        rl_account_opening_bank = (RelativeLayout) findViewById(R.id.rl_account_opening_bank);
        rl_account_opening_bank_address = (RelativeLayout) findViewById(R.id.rl_account_opening_bank_address);

        btn_save = (Button) findViewById(R.id.btn_save);

        try {
            tv_user_name.setText(DESUtil.decrypt(PreferenceUtil.getUserRealName()));
            tv_user_phone.setText(DESUtil.decrypt(PreferenceUtil.getPhone()));
            tv_user_id.setText(DESUtil.decrypt(PreferenceUtil.getIdNo()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        rl_account_opening_bank.setOnClickListener(this);
        rl_account_opening_bank_address.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_account_opening_bank: // 选择开户银行
                showBankDialog();
                return;
            case R.id.rl_account_opening_bank_address: // 选择开户地
                return;
            case R.id.btn_save: // 保存
                return;

        }
    }

    /**
     * 选择开户银行
     */
    private void showBankDialog() {
        BankDialog dialog = new BankDialog(this, new BankDialog.BankChooseInterface() {
            @Override
            public void getBankName(String name) {
                tv_account_opening_bank.setText(name);
            }
        });
        dialog.setTitle("");
        dialog.setCancelOutside(true);//点击阴影处是否能取消对话框
        dialog.showDialog();
    }
}
