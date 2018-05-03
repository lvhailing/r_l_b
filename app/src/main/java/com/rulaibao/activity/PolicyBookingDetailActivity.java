package com.rulaibao.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rulaibao.R;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.dialog.CancelBookingDialog;
import com.rulaibao.widget.TitleBar;

/**
 *  保单预约详情
 * Created by junde on 2018/4/19.
 */

public class PolicyBookingDetailActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_audit_status; // 审核状态
    private ImageView iv_delete; // 删除 图标
    private TextView tv_turn_down; // 驳回原因
    private RelativeLayout rl_insurance_name; // 保险产品名布局
    private TextView tv_insurance_name; // 保险产品名字
    private ImageView iv_arrow;
    private TextView tv_policy_booking_status; // 预约状态
    private TextView tv_policy_booking_time; // 预约时间
    private TextView tv_policy_booking_people; // 预约人
    private TextView tv_policy_booking_phone; // 预约电话
    private TextView tv_insurance_company; // 保险公司
    private TextView tv_insurance_plan; // 保险计划
    private TextView tv_insurance_amount; // 保险金额
    private TextView tv_annual_premium; // 年缴保费
    private TextView tv_insurance_period; // 保险期限
    private TextView tv_payment_period; // 缴费期限
    private TextView tv_expected_policy; // 预计交单
    private TextView tv_remarks_description; // 备注内容
    private Button btn_cancel_booking; // 取消预约


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_policy_booking_detail);

        initTopTitle();
        initView();
        requestData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false)
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_policy_booking_detail))
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
        iv_arrow = (ImageView) findViewById(R.id.iv_arrow);
        tv_insurance_name = (TextView) findViewById(R.id.tv_insurance_name);
        tv_policy_booking_status = (TextView) findViewById(R.id.tv_policy_booking_status);
        tv_policy_booking_time = (TextView) findViewById(R.id.tv_policy_booking_time);
        tv_policy_booking_people = (TextView) findViewById(R.id.tv_policy_booking_people);
        tv_policy_booking_phone = (TextView) findViewById(R.id.tv_policy_booking_phone);
        tv_insurance_company = (TextView) findViewById(R.id.tv_insurance_company);
        tv_insurance_plan = (TextView) findViewById(R.id.tv_insurance_plan);
        tv_insurance_amount = (TextView) findViewById(R.id.tv_insurance_amount);
        tv_annual_premium = (TextView) findViewById(R.id.tv_annual_premium);
        tv_insurance_period = (TextView) findViewById(R.id.tv_insurance_period);
        tv_payment_period = (TextView) findViewById(R.id.tv_payment_period);
        tv_expected_policy = (TextView) findViewById(R.id.tv_expected_policy);
        tv_remarks_description = (TextView) findViewById(R.id.tv_remarks_description);
        btn_cancel_booking = (Button) findViewById(R.id.btn_cancel_booking);

        rl_insurance_name.setOnClickListener(this);
        btn_cancel_booking.setOnClickListener(this);
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

                break;
            case R.id.btn_cancel_booking: // 取消预约
                showDialog();
                break;

            default:
                break;
        }
   }

    private void showDialog() {
        CancelBookingDialog dialog = new CancelBookingDialog(this, new CancelBookingDialog.IsCancelBooking() {
            @Override
            public void onConfirm() {
                Toast.makeText(PolicyBookingDetailActivity.this, "取消成功", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
            }
        });

        dialog.show();
    }
}
