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
import com.rulaibao.adapter.MyCollectionRecycleAdapter;
import com.rulaibao.bean.MyAskList1B;
import com.rulaibao.bean.MyAskList2B;
import com.rulaibao.bean.MyCollectionList1B;
import com.rulaibao.bean.MyCollectionList2B;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;

import java.util.LinkedHashMap;


public class MyCollectionFragment extends Fragment {
    private static final String KEY = "param1";

    private String mParam1;
    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recycler_view;
    private MyCollectionRecycleAdapter myCollectionRecycleAdapter;
    private MouldList<MyCollectionList2B> totalList = new MouldList<>();
    private Context context;
    private int currentPage = 1;
    private String type;


    public static MyCollectionFragment newInstance(String param1) {
        MyCollectionFragment fragment = new MyCollectionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY, param1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            requestData();
        }
    }

    private void requestData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("userId", "18032709463185347076");
        param.put("page", currentPage + "");
        param.put("category", type);
        Log.i("hh", "category------" + type);

        HtmlRequest.getCollectionListData(context, param, new BaseRequester.OnRequestListener() {
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

                MyCollectionList1B data = (MyCollectionList1B) params.result;
                MouldList<MyCollectionList2B> everyList = data.getList();
                if (everyList == null) {
                    return;
                }
                if (everyList.size() == 0 && currentPage != 1) {
                    Toast.makeText(context, "已显示全部", Toast.LENGTH_SHORT).show();
                    myCollectionRecycleAdapter.changeMoreStatus(myCollectionRecycleAdapter.NO_LOAD_MORE);
                }
                if (currentPage == 1) {
                    //刚进来时 加载第一页数据，或下拉刷新 重新加载数据 。这两种情况之前的数据都清掉
                    totalList.clear();
                }
                totalList.addAll(everyList);
                if (totalList.size() != 0 && totalList.size() % 10 == 0) {
                    myCollectionRecycleAdapter.changeMoreStatus(myCollectionRecycleAdapter.PULLUP_LOAD_MORE);
                } else {
                    myCollectionRecycleAdapter.changeMoreStatus(myCollectionRecycleAdapter.NO_LOAD_MORE);
                }
            }
        });
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
        swipe_refresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);

//        for (int i = 0; i < 10; i++) {
//            MyCollectionList2B bean = new MyCollectionList2B();
//            bean.setInsuranceName("盛世年年年金保险C款" + "10" + i);
//            totalList.add(bean);
//
//        }

        initRecyclerView();
    }

    private void initRecyclerView() {
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        myCollectionRecycleAdapter = new MyCollectionRecycleAdapter(getActivity(), totalList);
        recycler_view.setAdapter(myCollectionRecycleAdapter);
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
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == myCollectionRecycleAdapter.getItemCount() && firstVisibleItem != 0) {
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
            type = "";  //默认请求"全部"的数据
        } else if (currentPosition == 1) {
            type = "accident";  //意外险
        } else if (currentPosition == 2) {
            type = "criticalIllness";  //重疾险
        } else if (currentPosition == 3) {
            type = "annuity";  //年金险
        } else if (currentPosition == 4) {
            type = "medical";  //医疗险
        }else if (currentPosition == 5) {
            type = "property";  //家财险
        } else if (currentPosition == 6) {
            type = "wholeLife";  //终身寿险
        }else if (currentPosition == 7) {
            type = "enterpriseGroup";  //企业团体
        }
    }
}