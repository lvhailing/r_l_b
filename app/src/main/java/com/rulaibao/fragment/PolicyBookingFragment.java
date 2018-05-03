package com.rulaibao.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rulaibao.R;
import com.rulaibao.adapter.PolicyBookingAdapter;
import com.rulaibao.bean.PolicyBookingList3B;
import com.rulaibao.network.types.MouldList;


public class PolicyBookingFragment extends Fragment {
    private static final String KEY = "param1";

    private String mParam1;
    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recycler_view;
    private PolicyBookingAdapter policyBookingAdapter;
    private MouldList<PolicyBookingList3B> totalList = new MouldList<>();


    public static PolicyBookingFragment newInstance(String param1) {
        PolicyBookingFragment fragment = new PolicyBookingFragment();
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
        return view;
    }

    private void initView(View view) {
        swipe_refresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);

        for (int i = 0; i < 10; i++) {
            PolicyBookingList3B bean = new PolicyBookingList3B();
            bean.setInsuranceName("10"+i+"国华人寿-盛世年年年金保险C款");
            bean.setStatus("待确认");
            bean.setInsurancePremiums("1000"+i+".00元");
            totalList.add(bean);

            recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
            policyBookingAdapter = new PolicyBookingAdapter(getActivity(), totalList);
            recycler_view.setAdapter(policyBookingAdapter);
            //添加动画
            recycler_view.setItemAnimator(new DefaultItemAnimator());
        }


    }
}