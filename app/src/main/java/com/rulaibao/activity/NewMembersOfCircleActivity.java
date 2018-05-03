package com.rulaibao.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rulaibao.R;
import com.rulaibao.adapter.NewMembersCircleAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.NewMembersCircleList3B;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.widget.TitleBar;

/**
 *  圈子新成员
 * Created by junde on 2018/4/24.
 */

public class NewMembersOfCircleActivity extends BaseActivity implements View.OnClickListener{


    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recycler_view;
    private NewMembersCircleAdapter newMembersCircleAdapter;
    private MouldList<NewMembersCircleList3B> totalList = new MouldList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_new_members_of_circle);

        initTopTitle();
        initView();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false)
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_new_members_of_circle))
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

        for (int i = 0; i < 10; i++) {
            NewMembersCircleList3B bean = new NewMembersCircleList3B();
//            bean.setCirclePhotoUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524549050350&di=c08e4e280aacd82629613bfddd3a84a9&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F18d8bc3eb13533fadbe6ea08a2d3fd1f41345b47.jpg");
            bean.setStatus("同意");
            bean.setApplicantName("王小金");
            bean.setCircleName("基金交流圈子100"+i);
            totalList.add(bean);

            initRecylerView();
        }
    }

    private void initRecylerView() {
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        newMembersCircleAdapter = new NewMembersCircleAdapter(this, totalList);
        recycler_view.setAdapter(newMembersCircleAdapter);
        //添加动画
        recycler_view.setItemAnimator(new DefaultItemAnimator());

    }



    @Override
    public void onClick(View v) {

    }
}
