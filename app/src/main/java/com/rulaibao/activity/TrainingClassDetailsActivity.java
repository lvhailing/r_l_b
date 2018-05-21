package com.rulaibao.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.rulaibao.R;
import com.rulaibao.adapter.TrainingClassDiscussAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.ResultClassDetailsIntroductionBean;
import com.rulaibao.bean.ResultClassDetailsIntroductionItemBean;
import com.rulaibao.fragment.TrainingDetailsCatalogFragment;
import com.rulaibao.fragment.TrainingDetailsDiscussFragment;
import com.rulaibao.fragment.TrainingDetailsIntroductionFragment;
import com.rulaibao.fragment.TrainingDetailsPPTFragment;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.uitls.InputMethodUtils;
import com.rulaibao.uitls.PreferenceUtil;
import com.rulaibao.uitls.RlbActivityManager;
import com.rulaibao.widget.TitleBar;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 课程详情
 */


public class TrainingClassDetailsActivity extends BaseActivity implements TrainingClassDiscussAdapter.DiscussReply {


    static final String iframeStr = "<iframe height=498 width=510 src='http://player.youku.com/embed/XOTMyOTAwNDE2' frameborder=0 'allowfullscreen'></iframe>";
    static final String url1 = "http://player.youku.com/embed/XMzQ3MDUwMzkyMA"; // 优酷地址
    static final String url2 = "https://v.qq.com/iframe/player.html?vid=m1424gnxctl&tiny=0&auto=0"; // 腾讯地址
    static final String url3 = "https://v.qq.com/iframe/player.html?vid=b0517fv9m0z&tiny=0&auto=0"; // 腾讯地址


    @BindView(R.id.wv_training_class_details)
    WebView wvTrainingClassDetails;
    @BindView(R.id.tl_class_details)
    TabLayout tlClassDetails;
//    @BindView(R.id.vp_class_details)
//    ViewPagerForScrollView vpClassDetails;

    @BindView(R.id.vp_class_details)
    ViewPager vpClassDetails;
    @BindView(R.id.fl_details_discuss)
    FrameLayout flDetailsDiscuss;
    @BindView(R.id.et_detail_discuss)
    EditText etDetailDiscuss;
    @BindView(R.id.btn_details_discuss)
    Button btnDetailsDiscuss;

    private String url = "http://player.youku.com/player.php/sid/XMzUxMzM5MzQyMA==/v.swf";

    private TrainingDetailsIntroductionFragment introdutionFragment;            //  简介模块
    private TrainingDetailsCatalogFragment catalogFragment;            //  目录模块
    private TrainingDetailsDiscussFragment discussFragment;            //  研讨模块
    private TrainingDetailsPPTFragment pptFragment;            //  PPT模块


    private List<Fragment> fragments;
    private List<String> listTitles;
    private String id = "";
    private ResultClassDetailsIntroductionItemBean course;
    private String speechmakeId = "";       //  演讲人id
    private String courseId = "";       //  课程id


    private int index;
    private int oldPosition = 0;
    private int bottomOffset = 0;
    private String commentName = "";
    private int position;
    private String toUserId = "";
    private String commentId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_training_class_details);
        initTopTitle();
        initView();


    }

    public void initView() {
        id = getIntent().getStringExtra("id");
        speechmakeId = getIntent().getStringExtra("speechmakeId");
        courseId = getIntent().getStringExtra("courseId");
        requestData();


    }

    public void initTabView() {

        fragments = new ArrayList<>();
        listTitles = new ArrayList<>();

//        introdutionFragment = new TrainingDetailsIntroductionFragment(vpClassDetails);
        introdutionFragment = new TrainingDetailsIntroductionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("speechmakeId", speechmakeId);
        bundle.putSerializable("course", course);
        bundle.putSerializable("courseId", courseId);
        introdutionFragment.setArguments(bundle);
        fragments.add(introdutionFragment);

//        catalogFragment = new TrainingDetailsCatalogFragment(vpClassDetails);
        catalogFragment = new TrainingDetailsCatalogFragment();
        catalogFragment.setArguments(bundle);
        fragments.add(catalogFragment);

//        discussFragment = new TrainingDetailsDiscussFragment(vpClassDetails);
        discussFragment = new TrainingDetailsDiscussFragment(this);
        discussFragment.setArguments(bundle);
        fragments.add(discussFragment);

//        pptFragment = new TrainingDetailsPPTFragment(vpClassDetails);
        pptFragment = new TrainingDetailsPPTFragment();
        pptFragment.setArguments(bundle);
        fragments.add(pptFragment);


        listTitles.add("简介");
        listTitles.add("目录");
        listTitles.add("研讨");
        listTitles.add("PPT");

        //mTabLayout.setTabMode(TabLayout.SCROLL_AXIS_HORIZONTAL);//设置tab模式，当前为系统默认模式
        for (int i = 0; i < listTitles.size(); i++) {
            tlClassDetails.addTab(tlClassDetails.newTab().setText(listTitles.get(i)));//添加tab选项
        }


        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
            @Override
            public CharSequence getPageTitle(int position) {
                return listTitles.get(position);
            }
        };
        vpClassDetails.setAdapter(mAdapter);

        tlClassDetails.setupWithViewPager(vpClassDetails);//将TabLayout和ViewPager关联起来。
        tlClassDetails.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器

        vpClassDetails.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position % 4 == 2) {
                    flDetailsDiscuss.setVisibility(View.VISIBLE);
                } else {
                    flDetailsDiscuss.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageSelected(int position) {
//                vpClassDetails.resetHeight(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        vpClassDetails.resetHeight(0);


    }


    public void requestData() {

//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

        map.put("id", id);      //  课程id

        HtmlRequest.getClassDetailsDesc(this, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if (params.result != null) {

                    ResultClassDetailsIntroductionBean bean = (ResultClassDetailsIntroductionBean) params.result;
                    course = bean.getCourse();
                    initPlayView();
                    initTabView();

                } else {

                }
            }
        });
    }


    public void initPlayView() {

        WebSettings ws = wvTrainingClassDetails.getSettings();
        ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
        ws.setUseWideViewPort(true);// 可任意比例缩放
        ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        ws.setSavePassword(true);
        ws.setSaveFormData(true);// 保存表单数据
        ws.setJavaScriptEnabled(true);

        ws.setDomStorageEnabled(true);
        ws.setSupportMultipleWindows(true);// 新加

        /**
         * 解决Android 5.0以后，WebView默认不支持同时加载Https和Http混合模式，
         * 当一个安全的站点（https）去加载一个非安全的站点（http）时，需要配置Webview加载内容的混合模式
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wvTrainingClassDetails.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        wvTrainingClassDetails.setWebChromeClient(new WebChromeClient());
        wvTrainingClassDetails.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        wvTrainingClassDetails.loadUrl(course.getCourseVideo());


    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false)
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.training_class_details))
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


    @Override
    protected void onResume() {
        super.onResume();
        //恢复播放
        wvTrainingClassDetails.resumeTimers();
    }

    @Override
    protected void onStart() {
        super.onStart();
        tlClassDetails.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tlClassDetails, 20, 20);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //暂停播放
        wvTrainingClassDetails.pauseTimers();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //一定要销毁，否则无法停止播放
        wvTrainingClassDetails.destroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            wvTrainingClassDetails.loadData("", "text/html; charset=UTF-8", null);
            this.finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }


    }


    @OnClick(R.id.btn_details_discuss)
    public void onclick() {

        String commentContent = etDetailDiscuss.getText().toString();

        if(!PreferenceUtil.isLogin()){
            HashMap<String,Object> map = new HashMap<>();


            RlbActivityManager.toLoginActivity(this,map,false);


        }else{
            if(!PreferenceUtil.getCheckStatus().equals("success")){

                new AlertDialog.Builder(this)

                        .setMessage("您还未认证，是否去认证")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("去认证", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                HashMap<String,Object> map = new HashMap<>();

                                map.put("realName",PreferenceUtil.getUserRealName());
                                map.put("status",PreferenceUtil.getCheckStatus());

                                RlbActivityManager.toSaleCertificationActivity(TrainingClassDetailsActivity.this,map,false);
                                dialog.dismiss();
                            }
                        })
                        .show();


            }else{

                discussFragment.toReply(commentContent,toUserId,commentId,commentName,index);
                hiddenInputLayout();

            }
        }



    }





    @Override
    public void reply(String toUserId, String commentId, String commentName, int index) {
        this.toUserId = toUserId;
        this.commentId = commentId;
        this.index = index;
        this.commentName = commentName;
        etDetailDiscuss.setHint("回复" + commentName + "：");

//        setBottomOffset(index);


    }

    private void hiddenInputLayout() {
//        isShowComment.set(false);
        etDetailDiscuss.setText("");
        etDetailDiscuss.setHint(getString(R.string.training_class_details_discuss_comment_hint));
//        flAnswerDetails.setVisibility(View.GONE);
        InputMethodUtils.hiddenSoftKeyboard(this);
    }


}
