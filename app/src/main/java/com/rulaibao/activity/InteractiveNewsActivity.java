package com.rulaibao.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rulaibao.R;
import com.rulaibao.adapter.InteractiveNewsAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.InteractiveNewsList3B;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.widget.TitleBar;

/**
 *  互动消息
 * Created by junde on 2018/4/24.
 */

public class InteractiveNewsActivity extends BaseActivity implements View.OnClickListener{

    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recycler_view;
    private InteractiveNewsAdapter interactiveNewsAdapter;
    private MouldList<InteractiveNewsList3B> totalList = new MouldList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_same_list_layout);

        initTopTitle();
        initView();
//        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false)
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_interaction_news))
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
            InteractiveNewsList3B bean = new InteractiveNewsList3B();
//            bean.setPhotoUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524549050350&di=c08e4e280aacd82629613bfddd3a84a9&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F18d8bc3eb13533fadbe6ea08a2d3fd1f41345b47.jpg");
            bean.setName("小意忆");
            bean.setTitle("财富精灵PC端开发测试沟通，财富精灵APP功能规划原型设计制作");
            bean.setDate("04-05");
            bean.setTime("12:10");
            bean.setReply("王小小回复：基金交流圈子基金交流圈子基金交流圈子");
            totalList.add(bean);

            initRecylerView();
        }
    }

    private void initRecylerView() {
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        interactiveNewsAdapter = new InteractiveNewsAdapter(this, totalList);
        recycler_view.setAdapter(interactiveNewsAdapter);
        //添加动画
        recycler_view.setItemAnimator(new DefaultItemAnimator());

    }


    @Override
    public void onClick(View v) {
    }
}
