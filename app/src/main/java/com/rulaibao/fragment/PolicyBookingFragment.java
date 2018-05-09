package com.rulaibao.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rulaibao.R;
import com.rulaibao.activity.PolicyBookingListActivity;
import com.rulaibao.activity.PolicyRecordListActivity;
import com.rulaibao.adapter.PolicyBookingAdapter;
import com.rulaibao.bean.PolicyBookingList1B;
import com.rulaibao.bean.PolicyBookingList2B;
import com.rulaibao.bean.PolicyRecordList1B;
import com.rulaibao.bean.PolicyRecordList2B;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;

import java.util.LinkedHashMap;

/**
 * 预约列表 Fragment
 */
public class PolicyBookingFragment extends Fragment {
    private static final String KEY = "param1";

    private String mParam1;
    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recycler_view;
    private PolicyBookingAdapter policyBookingAdapter;
    private MouldList<PolicyBookingList2B> totalList = new MouldList<>();
    private int currentPage = 1;    //当前页
    private String status;
    private Context context;
    private PolicyBookingList1B data;


    public static PolicyBookingFragment newInstance(String param1) {
        PolicyBookingFragment fragment = new PolicyBookingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY, param1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // 页面可见时调接口
            requestData();
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

        initRecyclerView();
    }

    private void initRecyclerView() {
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        policyBookingAdapter = new PolicyBookingAdapter(getActivity(), totalList);
        recycler_view.setAdapter(policyBookingAdapter);
        //添加动画
        recycler_view.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 获取预约列表数据
     */
    public void requestData() {

        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("userId", "18042709525931594357");
        param.put("page", currentPage + "");
        param.put("auditStatus", status);


        HtmlRequest.getPolicyBookingListData(context, param, new BaseRequester.OnRequestListener() {
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

                data = (PolicyBookingList1B) params.result;
                ((PolicyBookingListActivity) getActivity()).refreshTabTitle(data);
                MouldList<PolicyBookingList2B> everyList = data.getList();
                if ((everyList == null || everyList.size() == 0) && currentPage != 1) {
                    Toast.makeText(context, "已显示全部", Toast.LENGTH_SHORT).show();

                }
                if (currentPage == 1) {
                    //刚进来时 加载第一页数据，或下拉刷新 重新加载数据 。这两种情况之前的数据都清掉
                    totalList.clear();
                }
                totalList.addAll(everyList);
                if (totalList.size() != 0 && totalList.size() % 20 == 0) {
                    policyBookingAdapter.changeMoreStatus(policyBookingAdapter.PULLUP_LOAD_MORE);
                } else {
                    policyBookingAdapter.changeMoreStatus(policyBookingAdapter.NO_LOAD_MORE);
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
                requestData();
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
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == policyBookingAdapter.getItemCount() && firstVisibleItem != 0) {
                    currentPage++;
                    requestData();
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

    public void getTabTitleCurrentPosition(int currentPosition) {
        if (currentPosition == 0) {
            status = "all";  // 全部
        } else if (currentPosition == 1) {
            status = "confirming";  //待确认
        } else if (currentPosition == 2) {
            status = "confirmed";  //已确认
        } else if (currentPosition == 3) {
            status = "refuse";  //已驳回
        } else if (currentPosition == 4) {
            status = "canceled";  // 已取消
        }
    }
}