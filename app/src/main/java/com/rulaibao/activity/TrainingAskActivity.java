package com.rulaibao.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.rulaibao.R;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.ResultAskTypeBean;
import com.rulaibao.bean.ResultAskTypeItemBean;
import com.rulaibao.fragment.TrainingAakFragment;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.uitls.PreferenceUtil;
import com.rulaibao.uitls.RlbActivityManager;
import com.rulaibao.widget.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 问答
 */

public class TrainingAskActivity extends BaseActivity {

    @BindView(R.id.tl_training_ask)
    TabLayout tlTrainingAsk;
    @BindView(R.id.vp_training_ask)
    ViewPager vpTrainingAsk;
    @BindView(R.id.btn_training_ask)
    Button btnTrainingAsk;

    private List<ResultAskTypeItemBean> listTitles;
    private List<Fragment> fragments;
    private Context context;
    private ArrayList<ResultAskTypeItemBean> typeBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_training_ask);
        initTopTitle();

        initView();

    }

    public void initView() {


        typeBean = (ArrayList<ResultAskTypeItemBean>)getIntent().getSerializableExtra("type");

        context = this;
        listTitles = new ArrayList<>();
        fragments = new ArrayList<>();

        ResultAskTypeItemBean hot = new ResultAskTypeItemBean();
        hot.setTypeCode("");
        hot.setTypeName("热门推荐");
        listTitles.add(hot);

        if(typeBean!=null){
            listTitles.addAll(typeBean);
        }


        for (int i = 0; i < listTitles.size(); i++) {
            TrainingAakFragment fragment = TrainingAakFragment.newInstance(listTitles.get(i));
            fragments.add(fragment);

        }
        //mTabLayout.setTabMode(TabLayout.SCROLL_AXIS_HORIZONTAL);//设置tab模式，当前为系统默认模式
        for (int i = 0; i < listTitles.size(); i++) {
            tlTrainingAsk.addTab(tlTrainingAsk.newTab().setText(listTitles.get(i).getTypeName()));//添加tab选项
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
                return listTitles.get(position).getTypeName();
            }
        };
        vpTrainingAsk.setAdapter(mAdapter);

        tlTrainingAsk.setupWithViewPager(vpTrainingAsk);//将TabLayout和ViewPager关联起来。
        tlTrainingAsk.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器

    }





    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false)
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.training_ask))
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

    @OnClick(R.id.btn_training_ask)
    public void onClick(){

        if(TextUtils.isEmpty(userId)){
            Toast.makeText(context, "请登录", Toast.LENGTH_SHORT).show();
        }else{
            String checkStatus = PreferenceUtil.getCheckStatus();
            if(!checkStatus.equals("success")){
                Toast.makeText(context, "请认证", Toast.LENGTH_SHORT).show();
            }else{

                HashMap<String,Object> map = new HashMap<>();
                map.put("type",typeBean);
                RlbActivityManager.toTrainingToAskActivity(this,map,false);

            }
        }



    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
