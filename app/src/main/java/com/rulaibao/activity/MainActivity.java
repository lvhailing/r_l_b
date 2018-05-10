package com.rulaibao.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rulaibao.R;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.fragment.HomeFragment;
import com.rulaibao.fragment.MineFragment;
import com.rulaibao.fragment.PolicyPlanFragment;
import com.rulaibao.fragment.TrainingFragment;
import com.rulaibao.uitls.RlbActivityManager;
import com.rulaibao.widget.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private TitleBar title;

    private LinearLayout ll_tab_home;    // 首页
    private LinearLayout ll_tab_training;    // 研修
    private LinearLayout ll_tab_plan;    // 规划
    private LinearLayout ll_tab_mine; // 我的

    private ImageView iv_home;
    private ImageView iv_training;
    private ImageView iv_plan;
    private ImageView iv_mine;

    private TextView tv_home;
    private TextView tv_training;
    private TextView tv_plan;
    private TextView tv_mine;



    private List<Fragment> mFragments;
    private HomeFragment tab_home; //首页
    private TrainingFragment tab_training; // 研修
    private PolicyPlanFragment tab_plan; // 规划
    private MineFragment tab_mine; // 我的

    private int selectPage = 0;

    private String type = "";       //  answer：回答问题详情页  question：问题详情;  course:课程详情;  product:产品详情
    private String id = "";     //
    private String questionId = "";     //
    private String answerId = "";     //
    private String speechmakeId = "";     //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_main);
        selectPage = getIntent().getIntExtra("selectPage", 0);

        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            type = uri.getQueryParameter("type");
            id = uri.getQueryParameter("id");
            questionId = uri.getQueryParameter("questionId");
            answerId = uri.getQueryParameter("answerId");
            speechmakeId = uri.getQueryParameter("speechmakeId");
        }

        if(!TextUtils.isEmpty(type)){
            fromH5();
        }


        initTopTitle();
        initView();
        initVP();
        setSelect(selectPage);
        initData();
    }

    public void fromH5(){

        if(type.equals("answer")){          //  回答问题详情页


            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("questionId",questionId);
            map.put("answerId",answerId);
            RlbActivityManager.toTrainingAnswerDetailsActivity(this,map,false);


        }else if(type.equals("question")){      //  问题详情
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("questionId",id);
            RlbActivityManager.toTrainingAskDetailsActivity(this,map, false);
        }else if(type.equals("course")){        //  课程详情

            HashMap<String, Object> classMap = new HashMap<>();
            classMap.put("id", id);
            classMap.put("speechmakeId", speechmakeId);
            classMap.put("courseId", id);
            RlbActivityManager.toTrainingClassDetailsActivity(this, classMap, false);

        }else if(type.equals("product")){       //  产品详情
            Intent intent = new Intent(this, InsuranceProductDetailActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);

        }else {


        }



    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            selectPage = getIntent().getIntExtra("selectPage", 0);
            setSelect(selectPage);
        }
    }

    public void initTopTitle() {
        title = (TitleBar) findViewById(R.id.rl_title);
        title.setVisibility(View.GONE);
    }

    private void initView() {
//        title = (TitleBar) findViewById(R.id.rl_title);
//        title.setVisibility(View.GONE);

        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        ll_tab_home = (LinearLayout) findViewById(R.id.ll_tab_home);
        ll_tab_training = (LinearLayout) findViewById(R.id.ll_tab_training);
        ll_tab_plan = (LinearLayout) findViewById(R.id.ll_tab_plan);
        ll_tab_mine = (LinearLayout) findViewById(R.id.ll_tab_mine);

        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_training = (ImageView) findViewById(R.id.iv_training);
        iv_plan = (ImageView) findViewById(R.id.iv_plan);
        iv_mine = (ImageView) findViewById(R.id.iv_mine);

        tv_home = (TextView) findViewById(R.id.tv_home);
        tv_training = (TextView) findViewById(R.id.tv_training);
        tv_plan = (TextView) findViewById(R.id.tv_plan);
        tv_mine = (TextView) findViewById(R.id.tv_mine);

        ll_tab_home.setOnClickListener(this);
        ll_tab_training.setOnClickListener(this);
        ll_tab_plan.setOnClickListener(this);
        ll_tab_mine.setOnClickListener(this);
    }

    private void initVP() {
        mFragments = new ArrayList<>();
        tab_home = new HomeFragment();
        tab_training = new TrainingFragment();
        tab_plan = new PolicyPlanFragment();
        tab_mine = new MineFragment();

        mFragments.add(tab_home);
        mFragments.add(tab_training);
        mFragments.add(tab_plan);
        mFragments.add(tab_mine);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }
        };

        mViewPager.setAdapter(mAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                int currentItem = mViewPager.getCurrentItem();
                setTab(currentItem);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    public void setSelect(int i) {
        setTab(i);
        mViewPager.setCurrentItem(i);
    }

    private void initData() {
//        requestData(); //检查版本更新
    }

    private void setTab(int pos) {
        resetTvs();
        resetImages();

        switch (pos) {
            case 0:
                tv_home.setTextColor(getResources().getColor(R.color.main_color_yellow));
                iv_home.setImageResource(R.mipmap.bg_home_pressed);
                break;
            case 1:
                tv_training.setTextColor(getResources().getColor(R.color.main_color_yellow));
                iv_training.setImageResource(R.mipmap.bg_training_pressed);
                break;
            case 2:
                tv_plan.setTextColor(getResources().getColor(R.color.main_color_yellow));
                iv_plan.setImageResource(R.mipmap.bg_plan_pressed);
                break;
            case 3:
                tv_mine.setTextColor(getResources().getColor(R.color.main_color_yellow));
                iv_mine.setImageResource(R.mipmap.bg_mine_pressed);
                break;

        }
    }

    private void resetTvs() {
        tv_home.setTextColor(Color.parseColor("#999999"));
        tv_training.setTextColor(Color.parseColor("#999999"));
        tv_plan.setTextColor(Color.parseColor("#999999"));
        tv_mine.setTextColor(Color.parseColor("#999999"));
    }

    private void resetImages() {
        iv_home.setImageResource(R.mipmap.bg_home_normal);
        iv_training.setImageResource(R.mipmap.bg_training_normal);
        iv_plan.setImageResource(R.mipmap.bg_plan_normal);
        iv_mine.setImageResource(R.mipmap.bg_mine_normal);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_tab_home:  // 首页
                setSelect(0);
                break;
            case R.id.ll_tab_training:  // 研修
                setSelect(1);
                break;
            case R.id.ll_tab_plan:  // 规划
                setSelect(2);
                break;
            case R.id.ll_tab_mine:  // 我的
//                PreferenceUtil.setLogin(true);
//                if (PreferenceUtil.isLogin()) {
                    setSelect(3);
//                } else {
//                    Intent i_login = new Intent(this, LoginActivity.class);
//                    startActivity(i_login);

//                }

                break;

        }
    }

    private long lastTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
        }
        return false;
    }

    // 双击退出函数
    private void exitBy2Click() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime < 2000) {
            finish();
        } else {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            lastTime = currentTime;
        }
    }
}
