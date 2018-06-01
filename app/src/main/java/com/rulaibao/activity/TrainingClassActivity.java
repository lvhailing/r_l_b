package com.rulaibao.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rulaibao.R;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.ResultAskTypeItemBean;
import com.rulaibao.bean.ResultClassIndexBean;
import com.rulaibao.bean.ResultClassIndexItemBean;
import com.rulaibao.fragment.TrainingClassFragment;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.widget.TitleBar;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 课程
 */

public class TrainingClassActivity extends BaseActivity {

    ViewPager zxVp;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LayoutInflater mInflater;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private View view1, view2, view3, view4, view5;//页卡视图
    private List<View> mViewList = new ArrayList<>();//页卡视图集合
    private List<ResultAskTypeItemBean> listTitles;
    private List<Fragment> fragments;
    private List<TextView> listTextViews;
    private MouldList<ResultClassIndexItemBean> courseTypeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_training_class);
        initTopTitle();
        initView();
        requestIndexData();
    }

    public void initView() {

        listTitles = new ArrayList<ResultAskTypeItemBean>();
        fragments = new ArrayList<>();
        listTextViews = new ArrayList<>();
        courseTypeList = new MouldList<ResultClassIndexItemBean>();

        mTabLayout = (TabLayout) findViewById(R.id.zx_tl);
        mViewPager = (ViewPager) findViewById(R.id.zx_vp);

    }

    public void initData(){



        ResultAskTypeItemBean itemBean = new ResultAskTypeItemBean();
        itemBean.setTypeName("热门推荐");
        itemBean.setTypeCode("");
        listTitles.add(itemBean);

        for(int i=0;i<courseTypeList.size();i++){
            ResultAskTypeItemBean itemBean1 = new ResultAskTypeItemBean();
            itemBean1.setTypeName(courseTypeList.get(i).getTypeName());
            itemBean1.setTypeCode(courseTypeList.get(i).getTypeCode());
            listTitles.add(itemBean1);
        }


        for (int i = 0; i < listTitles.size(); i++) {
            TrainingClassFragment fragment = TrainingClassFragment.newInstance(listTitles.get(i).getTypeCode());
            fragments.add(fragment);

        }
        //mTabLayout.setTabMode(TabLayout.SCROLL_AXIS_HORIZONTAL);//设置tab模式，当前为系统默认模式
        for (int i=0;i<listTitles.size();i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(listTitles.get(i).getTypeName()));//添加tab选项
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
        mViewPager.setAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器

    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.mipmap.logo, false)
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.training_class))
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

    public void requestIndexData(){

//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String,Object> map = new LinkedHashMap<String,Object>();
        map.put("page",1+"");
        map.put("typeCode","");

        HtmlRequest.getTrainingClassList(this, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if(params.result!=null){

                    ResultClassIndexBean bean = (ResultClassIndexBean)params.result;
//                    courseList.addAll(bean.getCourseList());
//                    adapter.notifyDataSetChanged();
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
