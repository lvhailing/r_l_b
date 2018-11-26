package com.rulaibao.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rulaibao.R;
import com.rulaibao.adapter.PromotionMoneyListAdapter;
import com.rulaibao.bean.InsuranceDetail1B;
import com.rulaibao.bean.ResultPromotionMoneyItemBean;
import com.rulaibao.common.Urls;
import com.rulaibao.dialog.ShareSDKDialog;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.uitls.PreferenceUtil;
import com.rulaibao.uitls.ShareUtil;
import com.rulaibao.uitls.StringUtil;
import com.rulaibao.uitls.encrypt.DESUtil;
import com.rulaibao.widget.MyListView;

import java.util.LinkedHashMap;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * 保险产品详情
 */
public class InsuranceProductDetailActivity extends Activity implements View.OnClickListener {
    private RelativeLayout rl_appointment;
    private TextView btn_appointment;//预约
    private TextView tv_appointment_minimumPremium;
    private TextView tv_appointment_promotionmoney;

    private RelativeLayout rl_appointmented;
    private TextView btn_planbook;//计划书
    private TextView btn_buy;//购买
    private TextView tv_appointmented_minimumPremium;
    private TextView tv_appointmented_promotionmoney;
    private ImageView img_promotionmoney;

    private WebView mWebview;

    private TextView tv_web_title; // 标题
    private ImageView iv_back; // 返回按钮
    private ImageView iv_btn_share; // 分享按钮

    private InsuranceDetail1B result;
    private String id;
    private Intent intent;
    private String userId=null;
    public String title;
    private String url;
    private MyListView listView;
    private PromotionMoneyListAdapter adapter;
    private MouldList<ResultPromotionMoneyItemBean> list;
    private FrameLayout fl_promotionmoney;
    private ImageView img_close;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_insurance_detail);
        initView();
    }
    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initView() {
        id = getIntent().getStringExtra("id");
        String userId = null;
        try {
            userId = DESUtil.decrypt(PreferenceUtil.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(userId)){
            userId=null;
        }
        url = Urls.URL_INSURANCE_DETAILS_NEW_HTML5+"/"+id+"/"+userId;

        tv_web_title = (TextView) findViewById(R.id.tv_web_title);
        tv_web_title.setText("产品详情");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_btn_share = (ImageView) findViewById(R.id.iv_btn_share);
        rl_appointment = (RelativeLayout) findViewById(R.id.rl_appointment);
        btn_appointment = (TextView) findViewById(R.id.btn_appointment);
        tv_appointment_minimumPremium = (TextView) findViewById(R.id.tv_appointment_minimumPremium);
        tv_appointment_promotionmoney = (TextView) findViewById(R.id.tv_appointment_promotionmoney);
        rl_appointmented = (RelativeLayout) findViewById(R.id.rl_appointmented);
        btn_planbook = (TextView) findViewById(R.id.btn_planbook);
        btn_buy = (TextView) findViewById(R.id.btn_buy);
        tv_appointmented_minimumPremium = (TextView) findViewById(R.id.tv_appointmented_minimumPremium);
        tv_appointmented_promotionmoney = (TextView) findViewById(R.id.tv_appointmented_promotionmoney);
        img_promotionmoney= (ImageView) findViewById(R.id.img_promotionmoney);
        fl_promotionmoney= (FrameLayout) findViewById(R.id.fl_promotionmoney);
        img_close= (ImageView) findViewById(R.id.img_close);
        list = new MouldList<ResultPromotionMoneyItemBean>();

        listView= (MyListView) findViewById(R.id.lv_promotion_money);
        adapter = new PromotionMoneyListAdapter(this, list);
        listView.setAdapter(adapter);

        btn_appointment.setOnClickListener(this);
        btn_planbook.setOnClickListener(this);
        img_promotionmoney.setOnClickListener(this);
        btn_buy.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        img_close.setOnClickListener(this);
        iv_btn_share.setOnClickListener(this);
        iv_btn_share.setClickable(false);

        mWebview = (WebView) findViewById(R.id.webview_web);
        mWebview.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        mWebview.getSettings().setBuiltInZoomControls(true);
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();

                super.onReceivedSslError(view, handler, error);

            }
        });
        mWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        mWebview.getSettings().setUseWideViewPort(true);

        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setTextZoom(100);
        mWebview.addJavascriptInterface(new MyJavaScriptinterface(), "click");


        HtmlRequest.synCookies(this, url);

        mWebview.loadUrl(url);

    }
    public class MyJavaScriptinterface {
        @JavascriptInterface
        public void result() {
            /*if (type.equals(WEBTYPE_WITHDRAW)) {
                setResult(RESULT_OK);
			} */
            InsuranceProductDetailActivity.this.finish();
        }

        @JavascriptInterface
        public void getToMyLogin() {//收藏未登录跳转
                Intent i_login = new Intent();
                i_login.setClass(InsuranceProductDetailActivity.this, LoginActivity.class);
                startActivity(i_login);
        }
        @JavascriptInterface
        public void getToMyPDF(String filePath) {//条款资料本地展示
            String[] perms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
      //      filePath = result.getAttachmentPath();
            if (!TextUtils.isEmpty(filePath)) {
                if (!EasyPermissions.hasPermissions(InsuranceProductDetailActivity.this, perms)) {
                    EasyPermissions.requestPermissions((Activity) InsuranceProductDetailActivity.this, "需要访问手机存储权限！", 10086, perms);
                } else {
                    FileDisplayActivity.show(InsuranceProductDetailActivity.this, filePath);
                }

            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_btn_share:
                final String shareUrl= Urls.URL_SHARE_PRODUCT_DETAILS+id;
                // 设置分享参数
                ShareSDKDialog dialog = new ShareSDKDialog(InsuranceProductDetailActivity.this, new ShareSDKDialog.OnShare() {
                    @Override
                    public void onConfirm(int position) {
                        ShareUtil.sharedSDK(InsuranceProductDetailActivity.this, position, result.getName(), "推荐说明："+result.getRecommendations(), shareUrl);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                dialog.show();
                break;
            case R.id.iv_back:
                finish();
                break;
            /**
             *以下要判断是否登录，是否认证
             */
            case R.id.btn_appointment://预约
                if (!PreferenceUtil.isLogin()) {
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                if (!"success".equals(result.getCheckStatus())) {
                    intent = new Intent(this, SalesCertificationActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(this, ProductAppointmentActivity.class);
                intent.putExtra("id", result.getId());
                intent.putExtra("companyName", result.getCompanyName());
                intent.putExtra("name", result.getName());
                intent.putExtra("category", result.getCategory());
                startActivity(intent);
                break;
            case R.id.btn_planbook://计划书
                if (!PreferenceUtil.isLogin()) {
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                if (!"success".equals(result.getCheckStatus())) {
                    intent = new Intent(this, SalesCertificationActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(this, WebActivity.class);
                intent.putExtra("type", WebActivity.WEBTYPE_PLAN_BOOK);
                intent.putExtra("url", result.getProspectus());
                intent.putExtra("title", "计划书");
                startActivity(intent);
                break;
            case R.id.btn_buy://购买
                if (!PreferenceUtil.isLogin()) {
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                if (!"success".equals(result.getCheckStatus())) {
                    intent = new Intent(this, SalesCertificationActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(this, WebActivity.class);
                intent.putExtra("type", WebActivity.WEBTYPE_BUY);
                intent.putExtra("url", result.getProductLink());
                intent.putExtra("title", "购买");
                startActivity(intent);
                break;
            case R.id.img_promotionmoney://长期险--推广费
                if (!PreferenceUtil.isLogin()) {
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                fl_promotionmoney.setVisibility(View.VISIBLE);
                break;
            case R.id.img_close://关闭长期险--推广费
                fl_promotionmoney.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    /**
     * 保险详情
     */
    private void requestData() {
        try {
            userId = DESUtil.decrypt(PreferenceUtil.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        final LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("userId", userId);
        param.put("id", id);

        HtmlRequest.getInsuranceDetailsNew(InsuranceProductDetailActivity.this, param, new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                if (param == null || params.result == null) {
            //        Toast.makeText(InsuranceProductDetailActivity.this, "加载失败，请确认网络通畅",Toast.LENGTH_LONG).show();
                    return;
                }
                result = (InsuranceDetail1B) params.result;
                String productStatus=result.getProductStatus();
                if ("normal".equals(productStatus)){
                    setData(result);
                    iv_btn_share.setClickable(true);
                }else if ("delete".equals(productStatus)){
                    iv_btn_share.setVisibility(View.GONE);
                    rl_appointment.setVisibility(View.GONE);
                    rl_appointmented.setVisibility(View.GONE);
                } else if ("down".equals(productStatus)){
                    iv_btn_share.setVisibility(View.GONE);
                    rl_appointment.setVisibility(View.GONE);
                    rl_appointmented.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setData(InsuranceDetail1B data) {
        list.clear();
        String appointmentStatus = data.getAppointmentStatus();
        String prospectusStatus = data.getProspectusStatus();
        String type = data.getType();
        /**
         * 是否认证可见
         */
        //根据预约type longTermInsurance:长期险;shortTermInsurance:短期险 判断显示底部布局
        if (!TextUtils.isEmpty(type)) {
            if ("longTermInsurance".equals(type)) {//长期险---有预约、计划书
                rl_appointment.setVisibility(View.VISIBLE);
                rl_appointmented.setVisibility(View.GONE);
                if (TextUtils.isEmpty(data.getMinimumPremium())){
                    tv_appointment_minimumPremium.setText("--元起");
                }else{
                    tv_appointment_minimumPremium.setText(setTextStyle2(this,data.getMinimumPremium()));
                }

                if ("success".equals(data.getCheckStatus())) {

                    tv_appointment_promotionmoney.setText(setTextStyle1(this,data.getPromotionMoney()));
                } else {
                    tv_appointment_promotionmoney.setText(setTextStyle1(this,"认证可见"));
                }
                ResultPromotionMoneyItemBean bean1=new ResultPromotionMoneyItemBean();
                bean1.setPromotionMoney("第一年推广费："+data.getPromotionMoney()+"%");
                list.add(bean1);
                if (!TextUtils.isEmpty(data.getPromotionMoney2())){
                    ResultPromotionMoneyItemBean bean2=new ResultPromotionMoneyItemBean();
                    bean2.setPromotionMoney("第二年推广费："+data.getPromotionMoney2()+"%");
                    list.add(bean2);
                }
                if (!TextUtils.isEmpty(data.getPromotionMoney3())){
                    ResultPromotionMoneyItemBean bean3=new ResultPromotionMoneyItemBean();
                    bean3.setPromotionMoney("第三年推广费："+data.getPromotionMoney3()+"%");
                    list.add(bean3);
                }
                if (!TextUtils.isEmpty(data.getPromotionMoney4())){
                    ResultPromotionMoneyItemBean bean4=new ResultPromotionMoneyItemBean();
                    bean4.setPromotionMoney("第四年推广费："+data.getPromotionMoney4()+"%");
                    list.add(bean4);
                }
                if (!TextUtils.isEmpty(data.getPromotionMoney5())){
                    ResultPromotionMoneyItemBean bean5=new ResultPromotionMoneyItemBean();
                    bean5.setPromotionMoney("第五年推广费："+data.getPromotionMoney5()+"%");
                    list.add(bean5);
                }
                adapter.notifyDataSetChanged();

            } else if ("shortTermInsurance".equals(type)) {//短期险---购买

                rl_appointment.setVisibility(View.GONE);
                rl_appointmented.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(data.getMinimumPremium())){
                    tv_appointmented_minimumPremium.setText("--元起");
                }else{
                    tv_appointmented_minimumPremium.setText(setTextStyle2(this,data.getMinimumPremium()));
                }
                if ("success".equals(data.getCheckStatus())) {
                    tv_appointmented_promotionmoney.setText(setTextStyle1(this,data.getPromotionMoney()));
                } else {
                    tv_appointmented_promotionmoney.setText(setTextStyle1(this,"认证可见"));
                }

            }
        }
        //是否预约
        if (!TextUtils.isEmpty(appointmentStatus)) {
            if ("true".equals(appointmentStatus)) {
                btn_appointment.setVisibility(View.INVISIBLE);

            } else if ("false".equals(appointmentStatus)) {
                btn_appointment.setVisibility(View.VISIBLE);
            }
        }
        //是否有计划书
        if (!TextUtils.isEmpty(prospectusStatus)) {
            if ("yes".equals(prospectusStatus)) {
                btn_planbook.setVisibility(View.VISIBLE);

            } else if ("no".equals(prospectusStatus)) {
                btn_planbook.setVisibility(View.INVISIBLE);
            }
        }

    }
    private static SpannableStringBuilder setTextStyle1(Context context, String str) {
        String str3;
        String str1= "推广费:";
        String str2=str1+str;
        if ("认证可见".equals(str)){
             str3=str2+"";
        }else{
             str3=str2+"%";
        }
        SpannableStringBuilder ssb= StringUtil.setTextStyle(context, str1, str2, str3,
                R.color.txt_black2, R.color.txt_black1, R.color.txt_black1,
                14, 16, 14, 0, 0, 0);
        return  ssb;
    }
    private static SpannableStringBuilder  setTextStyle2(Context context, String str) {
        String str1= str;
        String str2=str1+"元起";
        String str3=str2+"";
        SpannableStringBuilder ssb= StringUtil.setTextStyle(context, str1, str2, str3,
                R.color.txt_black1, R.color.txt_black2, R.color.txt_black2,
                16, 14, 14, 0, 0, 0);
        return  ssb;
    }
    @Override
    protected void onPause() {
        super.onPause();
        mWebview.reload();
    }
}