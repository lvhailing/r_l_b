package com.rulaibao.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rulaibao.R;
import com.rulaibao.activity.TransactionRecordActivity;
import com.rulaibao.adapter.PolicyRecordAdapter;
import com.rulaibao.bean.PolicyRecordList1B;
import com.rulaibao.bean.PolicyRecordList2B;
import com.rulaibao.bean.TrackingList1B;
import com.rulaibao.bean.TrackingList2B;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;

import java.util.LinkedHashMap;


public class PolicyRecordListFragment extends Fragment {
    private static final String KEY = "param1";

    private String mParam1;
    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recycler_view;
    private PolicyRecordAdapter policyRecordAdapter;
    private MouldList<PolicyRecordList2B> totalList = new MouldList<>();
    private int currentPage = 1;    //当前页
    private Context context;


    public static PolicyRecordListFragment newInstance(String param1) {
        PolicyRecordListFragment fragment = new PolicyRecordListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY, param1);
        fragment.setArguments(bundle);
        return fragment;
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
        requestData();
        initListener();
        return view;
    }

    private void initView(View view) {
        context = getActivity();
        swipe_refresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);

//        for (int i = 0; i < 10; i++) {
//            PolicyRecordList2B bean = new PolicyRecordList2B();
//            bean.setInsuranceName("国华人寿-盛世年年年金保险C款");
//            bean.setStatus("待审核");
//            bean.setCustomerName("王小金");
//            bean.setInsurancPeremiums("2222.00元");
//            bean.setInsurancePeriod("2年");
//            totalList.add(bean);
//
//        }
            initRecyclerView();
    }

    private void initRecyclerView() {
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        policyRecordAdapter = new PolicyRecordAdapter(getActivity(), totalList);
        recycler_view.setAdapter(policyRecordAdapter);
        //添加动画
        recycler_view.setItemAnimator(new DefaultItemAnimator());
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

                //刷新完成
                Toast.makeText(context, "下拉刷新", Toast.LENGTH_SHORT).show();

                //2秒后关掉动画
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        swipe_refresh.setRefreshing(false);
                    }
                }, 2000);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        List<String> headDatas = new ArrayList<String>();
//                        for (int i = 20; i <30 ; i++) {
//                            headDatas.add("Heard Item "+i);
//                        }
//                        transactionRecordAdapter.AddHeaderItem(headDatas);
//
//                        //刷新完成
//                        swipe_refresh.setRefreshing(false);
//                        Toast.makeText(TransactionRecordActivity.this, "更新了 "+headDatas.size()+" 条目数据", Toast.LENGTH_SHORT).show();
//                    }
//                }, 3000);

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
                if(newState==RecyclerView.SCROLL_STATE_IDLE&&lastVisibleItem+1==policyRecordAdapter.getItemCount()&& firstVisibleItem != 0){

//                    swipe_refresh.setRefreshing(true);

                    currentPage++;
                    requestData();


                    //改为网络请求
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            //
//                            List<String> footerDatas = new ArrayList<String>();
//                            for (int i = 0; i< 10; i++) {
//                                footerDatas.add("footer  item" + i);
//                            }
//                            transactionRecordAdapter.AddFooterItem(footerDatas);
//                            //设置回到上拉加载更多
//                            transactionRecordAdapter.changeMoreStatus(transactionRecordAdapter.PULLUP_LOAD_MORE);
//                            //没有加载更多了
//                            //mRefreshAdapter.changeMoreStatus(mRefreshAdapter.NO_LOAD_MORE);
//                            Toast.makeText(TransactionRecordActivity.this, "更新了 "+footerDatas.size()+" 条目数据", Toast.LENGTH_SHORT).show();
//                        }
//                    }, 3000);
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                lastVisibleItem=layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void requestData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("userId", "18042513234098822058");
        param.put("page", currentPage + "");
        param.put("status", "all");

        HtmlRequest.getPolicyRecordListData(context, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params.result == null) {
                    Toast.makeText(context, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                    return;
                }

                PolicyRecordList1B data = (PolicyRecordList1B) params.result;
                MouldList<PolicyRecordList2B> everyList = data.getList();
                if ((everyList == null || everyList.size() == 0) && currentPage != 1) {
                    Toast.makeText(context, "已显示全部", Toast.LENGTH_SHORT).show();

                }
                if (currentPage == 1) {
                    //刚进来时 加载第一页数据，或下拉刷新 重新加载数据 。这两种情况之前的数据都清掉
                    totalList.clear();
                }
                totalList.addAll(everyList);
                //刷新数据
                policyRecordAdapter.notifyDataSetChanged();

                //上滑加载更多 数据填充后 关闭动画
//                swipe_refresh.setRefreshing(false);

                //设置回到上拉加载更多
//                recommendRecordAdapter.changeMoreStatus(recommendRecordAdapter.PULLUP_LOAD_MORE);

                //没有加载更多了
                policyRecordAdapter.changeMoreStatus(policyRecordAdapter.NO_LOAD_MORE);
            }
        });
    }


}