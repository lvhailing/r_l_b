package com.rulaibao.activity;

import android.content.Intent;
import android.nfc.TagLostException;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rulaibao.R;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.Collection2B;
import com.rulaibao.bean.InsuranceDetail1B;
import com.rulaibao.bean.OK2B;
import com.rulaibao.bean.ResultSentSMSContentBean;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.uitls.ImageLoaderManager;
import com.rulaibao.widget.TitleBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedHashMap;


/**
 * 保险产品详情
 */
public class InsuranceProductDetailActivity extends BaseActivity implements View.OnClickListener {
    private TitleBar titleBar;

    private ImageView iv_product_logo;//产品logo
    private DisplayImageOptions displayImageOptions= ImageLoaderManager.initDisplayImageOptions
            (R.mipmap.iv_product_default, R.mipmap.iv_product_default, R.mipmap.iv_product_default);
    private ImageView iv_collect;//收藏
    private TextView tv_product_name;//产品名称

    private TextView tv_age;//年龄
    private TextView tv_insurance_profession;//承保职业
    private TextView tv_gurantee_limit;//保障期限
    private TextView tv_purchase_limit;//限购份数
    private TextView tv_recommendations;//推荐说明

    private  boolean isShowGuarantee;//不显示保障责任
    private  boolean isShowInsureNotice;//不显示投保须知
    private  boolean isShowClauseData;//不显示条款资料
    private  boolean isShowClaimsProcess;//不显示理赔流程
    private  boolean isShowFAQ;//不显示常见问题

    private RelativeLayout rl_guarantee;//保障责任
    private RelativeLayout rl_insure_notice;//投保须知
    private RelativeLayout rl_clause_data;//条款资料
    private RelativeLayout rl_claims_process;//理赔流程
    private RelativeLayout rl_faq;//常见问题

    //  保障责任、投保须知、条款资料、理赔流程、常见问题  后面的箭头图标
    private ImageView iv_guarantee_arrow;
    private ImageView iv_insure_notice_arrow;
    private ImageView iv_clause_data_arrow;
    private ImageView iv_claims_process_arrow;
    private ImageView iv_faq_arrow;

    private LinearLayout ll_guarantee;//保障责任
    private LinearLayout ll_insure_notice;//投保须知
    private LinearLayout ll_clause_data;//条款资料
    private LinearLayout ll_claims_process;//理赔流程
    private LinearLayout ll_faq;//常见问题

    private WebView webview_guarantee;//保障责任
    private WebView webview_insure_notice;//投保须知
    private WebView webview_clause_data;//条款资料
    private WebView webview_claims_process;//理赔流程
    private WebView webview_faq;//常见问题

    private RelativeLayout rl_appointment;
    private Button btn_appointment;//预约
    private TextView tv_appointment_minimumPremium;
    private TextView tv_appointment_promotionmoney;

    private RelativeLayout rl_appointmented;
    private Button btn_planbook;//计划书
    private Button btn_buy;//购买
    private TextView tv_appointmented_minimumPremium;
    private TextView tv_appointmented_promotionmoney;

    private InsuranceDetail1B result;
    private String id;
    private String collectionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_insurance_detail);
        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        titleBar = (TitleBar) findViewById(R.id.rl_title);
        titleBar.showLeftImg(true);
        titleBar.setFromActivity("1000");
        titleBar.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false).setIndicator(R.mipmap.icon_back)
                .setCenterText(getResources().getString(R.string.title_insurance_product_detail)).showMore(false).setTitleRightButton(R.drawable.ic_share_title)
                .setOnActionListener(new TitleBar.OnActionListener() {

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
        iv_product_logo= (ImageView) findViewById(R.id.iv_product_logo);
        iv_collect= (ImageView) findViewById(R.id.iv_collect);
        tv_product_name= (TextView) findViewById(R.id.tv_product_name);

        tv_age= (TextView) findViewById(R.id.tv_age);
        tv_insurance_profession= (TextView) findViewById(R.id.tv_insurance_profession);
        tv_gurantee_limit= (TextView) findViewById(R.id.tv_gurantee_limit);
        tv_purchase_limit= (TextView) findViewById(R.id.tv_purchase_limit);
        tv_recommendations= (TextView) findViewById(R.id.tv_recommendations);

        rl_guarantee= (RelativeLayout) findViewById(R.id.rl_guarantee);
        rl_insure_notice= (RelativeLayout) findViewById(R.id.rl_insure_notice);
        rl_clause_data= (RelativeLayout) findViewById(R.id.rl_clause_data);
        rl_claims_process= (RelativeLayout) findViewById(R.id.rl_claims_process);
        rl_faq= (RelativeLayout) findViewById(R.id.rl_faq);

        iv_guarantee_arrow= (ImageView) findViewById(R.id.iv_guarantee_arrow);
        iv_insure_notice_arrow= (ImageView) findViewById(R.id.iv_insure_notice_arrow);
        iv_clause_data_arrow= (ImageView) findViewById(R.id.iv_clause_data_arrow);
        iv_claims_process_arrow= (ImageView) findViewById(R.id.iv_claims_process_arrow);
        iv_faq_arrow= (ImageView) findViewById(R.id.iv_faq_arrow);

        ll_guarantee= (LinearLayout) findViewById(R.id.ll_guarantee);
        ll_insure_notice= (LinearLayout) findViewById(R.id.ll_insure_notice);
        ll_clause_data= (LinearLayout) findViewById(R.id.ll_clause_data);
        ll_claims_process= (LinearLayout) findViewById(R.id.ll_claims_process);
        ll_faq= (LinearLayout) findViewById(R.id.ll_faq);

        webview_guarantee= (WebView) findViewById(R.id.webview_guarantee);
        webview_insure_notice= (WebView) findViewById(R.id.webview_insure_notice);
        webview_clause_data= (WebView) findViewById(R.id.webview_clause_data);
        webview_claims_process= (WebView) findViewById(R.id.webview_claims_process);
        webview_faq= (WebView) findViewById(R.id.webview_faq);

        rl_appointment= (RelativeLayout) findViewById(R.id.rl_appointment);
        btn_appointment= (Button) findViewById(R.id.btn_appointment);
        tv_appointment_minimumPremium= (TextView) findViewById(R.id.tv_appointment_minimumPremium);
        tv_appointment_promotionmoney= (TextView) findViewById(R.id.tv_appointment_promotionmoney);

        rl_appointmented= (RelativeLayout) findViewById(R.id.rl_appointmented);
        btn_planbook= (Button) findViewById(R.id.btn_planbook);
        btn_buy= (Button) findViewById(R.id.btn_buy);
        tv_appointmented_minimumPremium= (TextView) findViewById(R.id.tv_appointmented_minimumPremium);
        tv_appointmented_promotionmoney= (TextView) findViewById(R.id.tv_appointmented_promotionmoney);
    }

    private void initData() {
        id=getIntent().getStringExtra("id");
        iv_collect.setOnClickListener(this);
        rl_guarantee.setOnClickListener(this);
        rl_insure_notice.setOnClickListener(this);
        rl_clause_data.setOnClickListener(this);
        rl_claims_process.setOnClickListener(this);
        rl_faq.setOnClickListener(this);
        btn_appointment.setOnClickListener(this);
        btn_planbook.setOnClickListener(this);
        btn_buy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_collect://收藏
                iv_collect.setClickable(false);
                collection();
                break;
            case R.id.rl_guarantee:
                if (!isShowGuarantee) {
                    ll_guarantee.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(result.getSecurityResponsibility())) {
                        setWebView(result.getSecurityResponsibility(),webview_guarantee);
                    }
                    iv_guarantee_arrow.setBackgroundResource(R.mipmap.ic_data_close);
                    isShowGuarantee = true;
                } else {
                    isShowGuarantee = false;
                    ll_guarantee.setVisibility(View.GONE);
                    iv_guarantee_arrow.setBackgroundResource(R.mipmap.ic_data_open);
                }
                break;
            case R.id.rl_insure_notice:
                if (!isShowInsureNotice) {
                    ll_insure_notice.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(result.getCoverNotes())) {
                        setWebView(result.getCoverNotes(),webview_insure_notice);
                    }
                    iv_insure_notice_arrow.setBackgroundResource(R.mipmap.ic_data_close);
                    isShowInsureNotice = true;
                } else {
                    isShowInsureNotice = false;
                    ll_insure_notice.setVisibility(View.GONE);
                    iv_insure_notice_arrow.setBackgroundResource(R.mipmap.ic_data_open);
                }
                break;
            case R.id.rl_clause_data:
                if (!isShowClauseData) {
                    ll_clause_data.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(result.getDataTerms())) {
                        setWebView(result.getDataTerms(),webview_clause_data);
                    }
                    iv_clause_data_arrow.setBackgroundResource(R.mipmap.ic_data_close);
                    isShowClauseData = true;
                } else {
                    isShowClauseData = false;
                    ll_clause_data.setVisibility(View.GONE);
                    iv_clause_data_arrow.setBackgroundResource(R.mipmap.ic_data_open);
                }
                break;
            case R.id.rl_claims_process:
                if (!isShowClaimsProcess) {
                    ll_claims_process.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(result.getClaimProcess())) {
                        setWebView(result.getClaimProcess(),webview_claims_process);
                    }
                    iv_claims_process_arrow.setBackgroundResource(R.mipmap.ic_data_close);
                    isShowClaimsProcess = true;
                } else {
                    isShowClaimsProcess = false;
                    ll_claims_process.setVisibility(View.GONE);
                    iv_claims_process_arrow.setBackgroundResource(R.mipmap.ic_data_open);
                }
                break;
            case R.id.rl_faq:
                if (!isShowFAQ) {
                    ll_faq.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(result.getCommonProblem())) {
                        setWebView(result.getCommonProblem(),webview_faq);
                    }
                    iv_faq_arrow.setBackgroundResource(R.mipmap.ic_data_close);
                    isShowFAQ = true;
                } else {
                    isShowFAQ = false;
                    ll_faq.setVisibility(View.GONE);
                    iv_faq_arrow.setBackgroundResource(R.mipmap.ic_data_open);
                }
                break;

            /**
             *
             *以下要判断是否登录，是否认证
             *
             *
             */
            case R.id.btn_appointment://预约
                Intent intent = new Intent(this, ProductAppointmentActivity.class);
                intent.putExtra("id",result.getId());
                intent.putExtra("companyName",result.getCompanyName());
                intent.putExtra("name",result.getName());
                intent.putExtra("category",result.getCategory());
                startActivity(intent);
                break;
            case R.id.btn_planbook://计划书
                Intent i_plan = new Intent(this, WebActivity.class);
                i_plan.putExtra("type", WebActivity.WEBTYPE_PLAN_BOOK);
                i_plan.putExtra("url", result.getProspectus());
                i_plan.putExtra("title", "计划书");
                startActivity(i_plan);
                break;
            case R.id.btn_buy://购买
                Intent i_buy = new Intent(this, WebActivity.class);
                i_buy.putExtra("type", WebActivity.WEBTYPE_BUY);
                i_buy.putExtra("url",result.getProductLink());
                i_buy.putExtra("title", "购买");
                startActivity(i_buy);
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
        final LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("userId", userId);
        param.put("id", id);

        HtmlRequest.getInsuranceDetails(InsuranceProductDetailActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                if (param==null || params.result==null){
                    Toast.makeText(InsuranceProductDetailActivity.this, "加载失败，请确认网络通畅",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                result = (InsuranceDetail1B) params.result;
                setData(result);
            }
        });
    }

    /**
     * 收藏
     */
    private void collection() {
        final LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("userId", userId);
        param.put("productId", id);
        param.put("dataStatus", collectionStatus);
        param.put("collectionId", result.getCollectionId());
        HtmlRequest.collection(InsuranceProductDetailActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                if (param==null || params.result==null){
                    Toast.makeText(InsuranceProductDetailActivity.this, "加载失败，请确认网络通畅",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                Collection2B collection2B = (Collection2B) params.result;
                iv_collect.setClickable(true);
                if ("true".equals(collection2B.getFlag())){
                    Toast.makeText(InsuranceProductDetailActivity.this, collection2B.getMessage(),
                            Toast.LENGTH_LONG).show();
                    //是否收藏
                    if ("valid".equals(collection2B.getDataStatus())){//收藏
                        iv_collect.setImageResource(R.mipmap.ic_collected);
                        collectionStatus="invalid";
                    }else if("invalid".equals(collection2B.getDataStatus())){//未收藏
                        iv_collect.setImageResource(R.mipmap.ic_collect);
                        collectionStatus="valid";
                    }
                }else{
                    Toast.makeText(InsuranceProductDetailActivity.this, collection2B.getMessage(),
                            Toast.LENGTH_LONG).show();

                }
            }
        });
    }
    private void setData(InsuranceDetail1B data) {
        String logo=data.getLogo();
        String collectionDataStatus=data.getCollectionDataStatus();
        String name=data.getName();
        String insuranceAge=data.getInsuranceAge();
        String insuranceOccupation=data.getInsuranceOccupation();
        String insurancePeriod=data.getInsurancePeriod();
        String purchaseNumber=data.getPurchaseNumber();
        String recommendations=data.getRecommendations();
        String appointmentStatus=data.getAppointmentStatus();
        String type=data.getType();

        // ImageLoader 加载图片
        if (!TextUtils.isEmpty(logo)) {
            ImageLoader.getInstance().displayImage(logo, iv_product_logo, displayImageOptions);
        }
        //是否收藏
        if ("valid".equals(collectionDataStatus)){//收藏
            iv_collect.setImageResource(R.mipmap.ic_collected);
            collectionStatus="invalid";
        }else if("invalid".equals(collectionDataStatus)){//未收藏
            iv_collect.setImageResource(R.mipmap.ic_collect);
            collectionStatus="valid";
        }
        //产品名称
        if (!TextUtils.isEmpty(name)) {
            tv_product_name.setText(name);
        }else{
            tv_product_name.setText("--");
        }
        //年龄
        if (!TextUtils.isEmpty(insuranceAge)) {
            tv_age.setText(insuranceAge);
        }else{
            tv_age.setText("--");
        }
        //承保职业
        if (!TextUtils.isEmpty(insuranceOccupation)) {
            tv_insurance_profession.setText(insuranceOccupation);
        }else{
            tv_insurance_profession.setText("--");
        }
        //保障期限
        if (!TextUtils.isEmpty(insurancePeriod)) {
            tv_gurantee_limit.setText(insurancePeriod);
        }else{
            tv_gurantee_limit.setText("--");
        }
        //限购份数
        if (!TextUtils.isEmpty(purchaseNumber)) {
            tv_purchase_limit.setText(purchaseNumber);
        }else{
            tv_purchase_limit.setText("--");
        }
        //推荐说明
        if (!TextUtils.isEmpty(recommendations)) {
            tv_recommendations.setText(recommendations);
        }else{
            tv_recommendations.setText("--");
        }
        /**
         *
         * 是是是是否认证可见
         *
         */
        //根据预约type longTermInsurance:长期险;shortTermInsurance:短期险 判断显示底部布局
        if (!TextUtils.isEmpty(type)) {
            if ("longTermInsurance".equals(type)) {//长期险---有预约
                rl_appointment.setVisibility(View.GONE);
                rl_appointmented.setVisibility(View.VISIBLE);
                tv_appointmented_minimumPremium.setText(data.getMinimumPremium()+"元起");
                tv_appointmented_promotionmoney.setText("推广费:"+data.getPromotionMoney());

            }else if ("shortTermInsurance".equals(type)){//短期险---计划书购买
                rl_appointment.setVisibility(View.VISIBLE);
                rl_appointmented.setVisibility(View.GONE);
                tv_appointment_minimumPremium.setText(data.getMinimumPremium()+"元起");
                tv_appointment_promotionmoney.setText("推广费:"+data.getPromotionMoney());
            }
        }
        //是否预约
        if (!TextUtils.isEmpty(appointmentStatus)) {
            if ("true".equals(appointmentStatus)) {
                btn_appointment.setVisibility(View.GONE);

            }else if ("false".equals(appointmentStatus)){
                btn_appointment.setVisibility(View.VISIBLE);
            }
        }

    }
    private void setWebView(String html,WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //取消滚动条白边效果
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setDefaultTextEncodingName("UTF-8") ;
        webView.getSettings().setBlockNetworkImage(false);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            webView.getSettings().setMixedContentMode(webView.getSettings()
                    .MIXED_CONTENT_ALWAYS_ALLOW);  //注意安卓5.0以上的权限
        }
        webView.loadDataWithBaseURL(null, getNewContent(html), "text/html", "UTF-8", null);
    }
    private String getNewContent(String htmltext){

        Document doc= Jsoup.parse(htmltext);
        Elements elements=doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width","100%").attr("height","auto");
        }

        Log.d("VACK", doc.toString());
        return doc.toString();
    }
}