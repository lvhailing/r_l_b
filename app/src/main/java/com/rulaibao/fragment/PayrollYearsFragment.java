package com.rulaibao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rulaibao.R;
import com.rulaibao.adapter.PayrollListAdapter;
import com.rulaibao.adapter.RecyclerBaseAapter;
import com.rulaibao.bean.MyPayrollYears2B;
import com.rulaibao.bean.PayrollList1B;
import com.rulaibao.bean.PayrollList2B;
import com.rulaibao.bean.ResultAskTypeItemBean;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的工资单年份列表 frament
 */

public class PayrollYearsFragment extends BaseFragment {

    private static final String KEY = "title";
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    private ArrayList arrayList = new ArrayList();
    private String string = "";
    private PayrollListAdapter adapter;
    private MouldList<PayrollList2B> payrollList2B;
    private int currentPage = 1;
    private boolean noDataFlag = true;   // 控制无数据不加载
    private MyPayrollYears2B currentYear;
    private MouldList<PayrollList2B> everyList;

    @Override
    protected View attachLayoutRes(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_payroll_list, container, false);

        } else {
            if (mView.getParent() != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
        }
        return mView;
    }

    @Override
    protected void initViews() {
        payrollList2B = new MouldList<PayrollList2B>();
        initRecyclerView();
    }

    public void initRecyclerView() {
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new PayrollListAdapter(getActivity(), payrollList2B);
        recycler_view.setAdapter(adapter);
        recycler_view.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {

                    if (noDataFlag) {
                        adapter.changeMoreStatus(PayrollListAdapter.LOADING_MORE);
                        currentPage++;
                        requestPayrollList(currentYear);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (context != null) {
                payrollList2B.clear();
            }
            currentYear = new MyPayrollYears2B();
            currentYear = (MyPayrollYears2B) getArguments().getSerializable(KEY);
            currentPage = 1;
            noDataFlag = true;
            requestPayrollList(currentYear);

        }
    }


    /**
     * 获取工资单列表
     */
    public void requestPayrollList(MyPayrollYears2B year) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("userId", userId);
        map.put("currentYear", currentYear);
        map.put("page", currentPage + "");

        HtmlRequest.getPayrollListData(context, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params.result == null) {
                    return;
                }
                PayrollList1B data = (PayrollList1B) params.result;
                everyList = data.getList();
//                if (everyList == null) {
//                    vs.setDisplayedChild(1);
//                    return;
//                }
                if (everyList.size() == 0 && currentPage != 1) {
                    Toast.makeText(context, "已显示全部", Toast.LENGTH_SHORT).show();
                    adapter.changeMoreStatus(PayrollListAdapter.NO_LOAD_MORE);
                }
                if (currentPage == 1) {
                    //刚进来时 加载第一页数据，或下拉刷新 重新加载数据 。这两种情况之前的数据都清掉
                    payrollList2B.clear();
                }
                //模拟数据
//                if (everyList.size() != 0) {
//                    while (everyList.size() < 10) {
//                        everyList.add(everyList.get(0));
//                    }
//                }
                payrollList2B.addAll(everyList);
                // 0:从后台获取到数据展示的布局；1：从后台没有获取到数据时展示的布局；
                if (payrollList2B.size() == 0) {
//                    vs.setDisplayedChild(1);
                    return;
                }
//                vs.setDisplayedChild(0);

                if (everyList.size() != 10) {
                    // 本次取回的数据为不是10条，代表取完了
                    adapter.changeMoreStatus(PayrollListAdapter.NO_LOAD_MORE);
                } else {
                    // 其他，均显示“数据加载中”的提示
                    adapter.changeMoreStatus(PayrollListAdapter.PULLUP_LOAD_MORE);
                }

            }
        });
    }

    public static PayrollYearsFragment newInstance(MyPayrollYears2B year) {
        PayrollYearsFragment fragment = new PayrollYearsFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
//        bundle.putSerializable(KEY, year);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}