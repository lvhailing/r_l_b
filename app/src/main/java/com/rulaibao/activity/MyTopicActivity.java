package com.rulaibao.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rulaibao.R;
import com.rulaibao.adapter.MyTopicAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.MyTopicList3B;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.widget.TitleBar;

/**
 *  我的话题
 * Created by junde on 2018/4/23.
 */

public class MyTopicActivity extends BaseActivity implements View.OnClickListener {

    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recycler_view;
    private MyTopicAdapter myTopicAdapter;
    private MouldList<MyTopicList3B> totalList = new MouldList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_my_topic);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false)
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_my_topic))
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
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
    }

    private void initData() {
        for(int i=0;i<10;i++){
            MyTopicList3B bean = new MyTopicList3B();
            bean.setCircleName("保险100"+i+"圈子");
            bean.setGroupName("财富小组"+i);
            bean.setTopicTitle("话题的标题"+i);
            bean.setZanNumber("10"+i);
            bean.setCommentNumber("20"+i);

            totalList.add(bean);
        }

        initRecylerView();
    }

    private void initRecylerView() {
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        myTopicAdapter = new MyTopicAdapter(this, totalList);
        recycler_view.setAdapter(myTopicAdapter);
        //添加动画
        recycler_view.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void onClick(View v) {

    }
}
