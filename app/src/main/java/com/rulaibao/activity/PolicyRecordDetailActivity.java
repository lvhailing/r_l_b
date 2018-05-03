package com.rulaibao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rulaibao.R;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.widget.TitleBar;

/**
 *  保单详情
 * Created by junde on 2018/4/18.
 */

public class PolicyRecordDetailActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_audit_status; // 审核状态
    private ImageView iv_delete; // 删除 图标
    private TextView tv_turn_down; // 驳回原因
    private RelativeLayout rl_insurance_name; // 保险产品名布局
    private TextView tv_insurance_name; // 保险产品名字
    private TextView tv_policy_status; // 保单状态
    private LinearLayout ll_underwriting_time; //  承保时间 布局（待审核、驳回状态时不显示）
    private TextView tv_underwriting_time; // 承保时间
    private TextView tv_product_name; // 产品名称
    private LinearLayout ll_policy_number; // 保单编号 布局（待审核、驳回状态时不显示）
    private TextView tv_policy_number; // 保单编号
    private TextView tv_customer_name; // 客户姓名
    private TextView tv_id_number; // 身份证号
    private TextView tv_insurance_period; // 保险期限
    private TextView tv_payment_period; // 缴费期限
    private TextView tv_renewal_date2; // 续期日期
    private TextView tv_have_insurance_premiums; // 已交保费
    private TextView tv_promotion_fee; // 推广费
    private TextView tv_record_date; // 记录日期
    private ImageView iv1; // 身份证正面
    private ImageView iv2; // 身份证反面
    private ImageView iv3; // 银行卡
    private ImageView iv4; // 其他
    private ImageView iv5; // 其他
    private TextView tv_remarks_description; // 备注内容


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_policy_record_detail);

        initTopTitle();
        initView();
        requestData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false)
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_policy_record_detail))
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
        rl_audit_status = (RelativeLayout) findViewById(R.id.rl_audit_status);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        tv_turn_down = (TextView) findViewById(R.id.tv_turn_down);
        rl_insurance_name = (RelativeLayout) findViewById(R.id.rl_insurance_name);
        tv_insurance_name = (TextView) findViewById(R.id.tv_insurance_name);
        tv_policy_status = (TextView) findViewById(R.id.tv_policy_status);
        ll_underwriting_time = (LinearLayout) findViewById(R.id.ll_underwriting_time);
        tv_underwriting_time = (TextView) findViewById(R.id.tv_underwriting_time);
        tv_product_name = (TextView) findViewById(R.id.tv_product_name);
        ll_policy_number = (LinearLayout) findViewById(R.id.ll_policy_number);
        tv_policy_number = (TextView) findViewById(R.id.tv_policy_number);
        tv_customer_name = (TextView) findViewById(R.id.tv_customer_name);
        tv_id_number = (TextView) findViewById(R.id.tv_id_number);
        tv_insurance_period = (TextView) findViewById(R.id.tv_insurance_period);
        tv_payment_period = (TextView) findViewById(R.id.tv_payment_period);
        tv_renewal_date2 = (TextView) findViewById(R.id.tv_renewal_date2);
        tv_have_insurance_premiums = (TextView) findViewById(R.id.tv_have_insurance_premiums);
        tv_promotion_fee = (TextView) findViewById(R.id.tv_promotion_fee);
        tv_record_date = (TextView) findViewById(R.id.tv_record_date);
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        iv3 = (ImageView) findViewById(R.id.iv3);
        iv4 = (ImageView) findViewById(R.id.iv4);
        iv5 = (ImageView) findViewById(R.id.iv5);
        tv_remarks_description = (TextView) findViewById(R.id.tv_remarks_description);

        rl_insurance_name.setOnClickListener(this);
    }

    /**
     *  获取保单详情页数据
     */
    private void requestData() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_insurance_name: //
                // Todo  跳转保险产品详情页
//                Intent intent = new Intent(this,InsuranceProductDetailActivity.class );
//                startActivity(intent);
        }
   }
}
