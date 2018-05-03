package com.rulaibao.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.rulaibao.R;
import com.rulaibao.activity.InsuranceProductDetailActivity;
import com.rulaibao.activity.SearchForPolicyPlanActivity;
import com.rulaibao.adapter.PolicyPlanAdapter;
import com.rulaibao.bean.PolicyPlan2B;
import com.rulaibao.bean.PolicyPlan3B;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.uitls.PreferenceUtil;
import com.rulaibao.uitls.ViewUtils;
import com.rulaibao.uitls.encrypt.DESUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 保单规划
 */

public class PolicyPlanFragment extends Fragment {
    private View mView;
    private Context context;
    private ImageView iv_right_btn;
    private PullToRefreshListView listView;
    private PolicyPlanAdapter mAdapter;
    private MouldList<PolicyPlan3B> totalList = new MouldList<>();
    private ViewSwitcher vs;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_policy_plan, container, false);
            try {
                initView(mView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (mView.getParent() != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
        }

        return mView;
    }
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if(context!=null){
                requestListData();
            }
        } else {

        }

     }
    private void initView(View mView) {
        context = getActivity();
        iv_right_btn= (ImageView) mView.findViewById(R.id.iv_right_btn);
        iv_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SearchForPolicyPlanActivity.class);
                context.startActivity(intent);
            }
        });
        vs = (ViewSwitcher)mView.findViewById(R.id.vs);
        TextView tv_empty = (TextView)mView.findViewById(R.id.tv_empty);
        ImageView img_empty = (ImageView)mView.findViewById(R.id.img_empty);
        tv_empty.setText("暂无保单规划");
        img_empty.setBackgroundResource(R.mipmap.ic_empty_insurance);
        listView = (PullToRefreshListView)mView.findViewById(R.id.listview);
        //PullToRefreshListView  上滑加载更多及下拉刷新
        ViewUtils.slideAndDropDown(listView);
        mAdapter = new PolicyPlanAdapter(context, totalList);
        listView.setAdapter(mAdapter);

        requestListData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // item 点击监听
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Intent intent = new Intent(context, InsuranceProductDetailActivity.class);
                //      intent.putExtra("hid", totalList.get(position - 1).getHid());
                startActivity(intent);
                Toast.makeText(context, "dianji"+position, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 获取保单规划数据
     */
    private void requestListData() {
        String userId = null;
        try {
            userId = DESUtil.decrypt(PreferenceUtil.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("userId", userId);
        param.put("customerName", "");

        try {
            HtmlRequest.getPolicyPlanData(context, param, new BaseRequester.OnRequestListener() {
                @Override
                public void onRequestFinished(BaseParams params) {
                    if (params.result == null) {
                        vs.setDisplayedChild(1);
                        Toast.makeText(context, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                        listView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                listView.onRefreshComplete();
                            }
                        }, 1000);
                        return;
                    }
                    PolicyPlan2B data = (PolicyPlan2B) params.result;
                    if ("true".equals(data.getFlag())){
                        MouldList<PolicyPlan3B> everyList = data.getList();

                        totalList.clear();
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
                    }else{
                        vs.setDisplayedChild(1);
                        Toast.makeText(context, data.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
