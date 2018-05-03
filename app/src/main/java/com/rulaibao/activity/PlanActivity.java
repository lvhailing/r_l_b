package com.rulaibao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.rulaibao.R;
import com.rulaibao.adapter.ConstellationAdapter;
import com.rulaibao.adapter.PlanAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.Plan2B;
import com.rulaibao.bean.Plan3B;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.uitls.DropDownMenu;
import com.rulaibao.uitls.ViewUtils;
import com.rulaibao.widget.TitleBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 计划书
 */
public class PlanActivity extends BaseActivity implements View.OnClickListener {
    private DropDownMenu mDropDownMenu;
    private String header = "保险公司";
    private String constellations[] = {"平安保险", "人寿保险", "人寿保险", "人寿保险", "平安保险", "人寿保险", "人寿保险"};
    private List<View> popupViews = new ArrayList<>();
    private ConstellationAdapter constellationAdapter;
    private int constellationPosition = 0;

    private PullToRefreshListView listView;
    private PlanAdapter mAdapter;
    private MouldList<Plan3B> totalList = new MouldList<>();
    private int currentPage = 1;    //当前页
    private ViewSwitcher vs;
    private String category="平安保险";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_plan);
        initTopTitle();
        initView();
        initData();
        initRefresh();
    }
    private void initTopTitle() {
        TitleBar titleBar = (TitleBar) findViewById(R.id.rl_title);
        titleBar.showLeftImg(true);
        titleBar.setFromActivity("3000");//搜索跳转
        titleBar.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false)
                .setIndicator(R.mipmap.icon_back)
                .setTitleRightButton(R.mipmap.ic_search)
                .setCenterText(getResources().getString(R.string.title_plan))
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
        mDropDownMenu= (DropDownMenu) findViewById(R.id.dropDownMenu);
    }
    //init 筛选
    private void initData() {
        final View constellationView = getLayoutInflater().inflate(R.layout.custom_layout, null);
        final GridView constellation = (GridView) constellationView.findViewById(R.id.constellation);
        constellationAdapter = new ConstellationAdapter(this, Arrays.asList(constellations));
        constellation.setAdapter(constellationAdapter);
        TextView tv_reset = (TextView) constellationView.findViewById(R.id.tv_reset);
        tv_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constellationAdapter.setCheckItem(0);
                constellationPosition = 0;
                constellationAdapter.notifyDataSetChanged();
             //   mDropDownMenu.setTabText(constellationPosition == 0 ? header : constellations[constellationPosition]);
             //   mDropDownMenu.closeMenu();
            }
        });
        TextView tv_ok = (TextView) constellationView.findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDropDownMenu.setTabText(constellationPosition == 0 ? header : constellations[constellationPosition]);
                mDropDownMenu.closeMenu();

                category=constellations[constellationPosition];
                currentPage = 1;
                requestListData();
            }
        });

        popupViews.add(constellationView);
        constellation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                constellationAdapter.setCheckItem(position);
                constellationPosition = position;
            }
        });
        mDropDownMenu.setDropDownMenu(header, popupViews);
    }
    private void initRefresh() {
        vs = (ViewSwitcher)findViewById(R.id.vs);
        TextView tv_empty = (TextView)findViewById(R.id.tv_empty);
        ImageView img_empty = (ImageView)findViewById(R.id.img_empty);
        tv_empty.setText("暂无计划书");
        img_empty.setBackgroundResource(R.mipmap.ic_empty_insurance);
        listView = (PullToRefreshListView)findViewById(R.id.listview_plan);
        //PullToRefreshListView  上滑加载更多及下拉刷新
        ViewUtils.slideAndDropDown(listView);
        mAdapter = new PlanAdapter(mContext, totalList);
        listView.setAdapter(mAdapter);

        requestListData();

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshView.isHeaderShown()) {
                    //下拉刷新
                    currentPage = 1;
                } else {
                    //上划加载下一页
                    currentPage++;
                }
                requestListData();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // item 点击监听
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Intent i_plan = new Intent(PlanActivity.this, WebActivity.class);
                i_plan.putExtra("type", WebActivity.WEBTYPE_PLAN_BOOK);
                i_plan.putExtra("url", totalList.get(position-1).getProspectus());
                i_plan.putExtra("title", "计划书");
                startActivity(i_plan);
            }
        });

    }
    /**
     * 获取计划书数据
     */
    private void requestListData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("page", currentPage + "");
        param.put("name", category);

        try {
            HtmlRequest.getPlanData(mContext, param, new BaseRequester.OnRequestListener() {
                @Override
                public void onRequestFinished(BaseParams params) {
                    if (params==null || params.result == null) {
                        vs.setDisplayedChild(1);
                        Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                        listView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                listView.onRefreshComplete();
                            }
                        }, 1000);
                        return;
                    }
                    Plan2B data = (Plan2B) params.result;
                    MouldList<Plan3B> everyList = data.getList();
                    if ((everyList == null || everyList.size() == 0) && currentPage != 1) {
                        Toast.makeText(mContext, "已显示全部", Toast.LENGTH_SHORT).show();
                    }
                    if (currentPage == 1) {
                        //刚进来时 加载第一页数据，或下拉刷新 重新加载数据 。这两种情况之前的数据都清掉
                        totalList.clear();
                    }
                    totalList.addAll(everyList);
                    if (totalList.size() == 0) {
                        vs.setDisplayedChild(1);
                    } else {
                        vs.setDisplayedChild(0);
                    }
                    //刷新数据
                    mAdapter.notifyDataSetChanged();

                    listView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listView.onRefreshComplete();
                        }
                    }, 1000);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
          /*  case R.id.:
                break;*/
        }
    }
    @Override
    public void onBackPressed() {
        //退出activity前关闭菜单
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
    }
}