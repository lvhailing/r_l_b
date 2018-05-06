package com.rulaibao.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rulaibao.R;
import com.rulaibao.adapter.MyPartakeAskAdapter;
import com.rulaibao.bean.MyAskList1B;
import com.rulaibao.bean.MyAskList2B;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;

import java.util.LinkedHashMap;

/**
 * 我参与的 Fragment
 */
public class MyPartakeAskFragment extends Fragment {
    private static final String KEY = "param1";

    private String mParam1;
    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recycler_view;
    private MyPartakeAskAdapter myPartakeAskAdapter;
    private MouldList<MyAskList2B> totalList = new MouldList<>();
    private int currentPage = 1;    //当前页
    private Context context;
    private int currentPosition; // 当前tab位置（0：提问，1：话题）
//    private MyAskList1B data;


    public static MyPartakeAskFragment newInstance(String param1) {
        MyPartakeAskFragment fragment = new MyPartakeAskFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY, param1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //页面可见时调接口刷新数据
            requestAskData();

        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycle_layout, container, false);
        initView(view);
        initListener();

        return view;
    }

    private void initView(View view) {
        context = getActivity();
        swipe_refresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);

//        for (int i = 0; i < 10; i++) {
//            MyPartakeList2B bean = new MyPartakeList2B();
//            bean.setTitle("中纪委发文中纪委发文" + "10" + i);
//            totalList.add(bean);
//
//        }
        initRecyclerView();
    }

    private void initRecyclerView() {
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        myPartakeAskAdapter = new MyPartakeAskAdapter(context, totalList);
        recycler_view.setAdapter(myPartakeAskAdapter);
        //添加动画
        recycler_view.setItemAnimator(new DefaultItemAnimator());
    }

    public void requestAskData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("userId", "18032709463185347076");
        param.put("page", currentPage + "");


        HtmlRequest.getMyPartakeAskListData(context, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (swipe_refresh.isRefreshing()) {
                    //请求返回后，无论本次请求成功与否，都关闭下拉旋转
                    swipe_refresh.setRefreshing(false);
                }

                if (params.result == null) {
                    Toast.makeText(context, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                    return;
                }

                MyAskList1B data = (MyAskList1B) params.result;
                MouldList<MyAskList2B> everyList = data.getList();
                if (everyList == null) {
                    return;
                }
                if (everyList.size() == 0 && currentPage != 1) {
                    Toast.makeText(context, "已显示全部", Toast.LENGTH_SHORT).show();
                    myPartakeAskAdapter.changeMoreStatus(myPartakeAskAdapter.NO_LOAD_MORE);
                }
                if (currentPage == 1) {
                    //刚进来时 加载第一页数据，或下拉刷新 重新加载数据 。这两种情况之前的数据都清掉
                    totalList.clear();
                }
                totalList.addAll(everyList);
                if (totalList.size() != 0 && totalList.size() % 10 == 0) {
                    myPartakeAskAdapter.changeMoreStatus(myPartakeAskAdapter.PULLUP_LOAD_MORE);
                } else {
                    myPartakeAskAdapter.changeMoreStatus(myPartakeAskAdapter.NO_LOAD_MORE);
                }
            }
        });
    }

    private void initListener() {
        initPullRefresh();
        initLoadMoreListener();
    }

    private void initPullRefresh() {
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {  // 下拉刷新
                currentPage = 1;
                requestAskData();
            }
        });
    }

    private void initLoadMoreListener() {
        recycler_view.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private int firstVisibleItem;
            private int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //判断RecyclerView的状态 是空闲时，同时，是最后一个可见的ITEM时才加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == myPartakeAskAdapter.getItemCount() && firstVisibleItem != 0) {
                    currentPage++;
                    requestAskData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    public void getCurrentTab(int position) {
        this.currentPosition = position;

    }
}