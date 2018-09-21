package com.rulaibao.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.rulaibao.R;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.ResultAskTypeItemBean;
import com.rulaibao.bean.ResultClassIndexBean;
import com.rulaibao.bean.ResultClassIndexItemBean;
import com.rulaibao.fragment.CxyTestClassFragment;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.widget.TitleBar;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 偶像练习工程师
 */

public class CxyTestActivity extends BaseActivity {


    @BindView(R.id.tl_test)
    TabLayout tlTest;
    @BindView(R.id.vp_test)
    ViewPager vpTest;
    private List<ResultAskTypeItemBean> titlesList;
    private List<Fragment> fragments;
    private MouldList<ResultClassIndexItemBean> courseTypeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_test_idol);
        initTopTitle();
        initView();
        requestIndexData();
    }

    public void initView() {
        titlesList=new ArrayList<>();
        fragments=new ArrayList<>();
        courseTypeList=new MouldList<>();
    }

    public void initData() {

        ResultAskTypeItemBean itemBean=new ResultAskTypeItemBean();
        itemBean.setTypeName("热门推荐");
        itemBean.setTypeCode("");
        titlesList.add(itemBean);

        for(int i=0;i<courseTypeList.size();i++){
            ResultAskTypeItemBean itemBean1=new ResultAskTypeItemBean();
            itemBean1.setTypeName(courseTypeList.get(i).getTypeName());
            itemBean1.setTypeCode(courseTypeList.get(i).getTypeCode());
            titlesList.add(itemBean1);
        }

        for (int i=0;i<titlesList.size();i++){
            CxyTestClassFragment fragment= CxyTestClassFragment.newInstance(titlesList.get(i).getTypeCode());
            fragments.add(fragment);
        }

        for (int i=0;i<titlesList.size();i++){
            tlTest.addTab(tlTest.newTab().setText(titlesList.get(i).getTypeName()));//添加tab选项
        }

        FragmentPagerAdapter mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }
            //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
            @Override
            public CharSequence getPageTitle(int position) {
                return titlesList.get(position).getTypeName();
            }
        };
        vpTest.setAdapter(mAdapter);
        tlTest.setupWithViewPager(vpTest);//将TabLayout和ViewPager关联起来。
        tlTest.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.mipmap.logo, false)
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.test_idol))
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

    public void requestIndexData() {

       LinkedHashMap<String,Object> map = new LinkedHashMap<String,Object>();
        map.put("page",1+"");
        map.put("typeCode","");

        HtmlRequest.getTrainingClassList(this, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if(params.result!=null){

                    ResultClassIndexBean bean = (ResultClassIndexBean)params.result;
                    courseTypeList = bean.getCourseTypeList();
                    initData();
                }else{

                }

            }
        });


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
