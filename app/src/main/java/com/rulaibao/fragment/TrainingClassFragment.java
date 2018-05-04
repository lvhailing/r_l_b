package com.rulaibao.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rulaibao.R;
import com.rulaibao.adapter.TrainingClassListAdapter;
import com.rulaibao.adapter.TrainingHotAskListAdapter;
import com.rulaibao.bean.ResultClassIndexBean;
import com.rulaibao.bean.ResultClassIndexItemBean;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.uitls.RlbActivityManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrainingClassFragment extends BaseFragment {


    @BindView(R.id.lv_training_class)
    RecyclerView lvTrainingClass;

    private static final String KEY = "title";
    private ArrayList arrayList = new ArrayList();
    private String string = "";
    private TrainingClassListAdapter adapter;
    private int page = 0;
    private String typeCode = "";
    private MouldList<ResultClassIndexItemBean> courseList;

    @Override
    protected View attachLayoutRes(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_training_class, container, false);

        } else {
            if (mView.getParent() != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
        }
        string = getArguments().getString(KEY);
        return mView;
    }

    @Override
    protected void initViews() {

        context = getActivity();
        typeCode = getArguments().getString(KEY);
        courseList = new MouldList<ResultClassIndexItemBean>();
        test();

        initRecyclerView();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            if(context!=null){
//                requestIndexData();//
//            }
            requestIndexData();//
//            scrollView.smoothScrollTo(0, 0);
        } else {

        }

    }


    public void initRecyclerView(){

        lvTrainingClass.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TrainingClassListAdapter(getActivity(),courseList);
        lvTrainingClass.setAdapter(adapter);


        lvTrainingClass.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {

                    adapter.changeMoreStatus(TrainingHotAskListAdapter.LOADING_MORE);

//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            test();
//                        }
//                    }, 2000);
                    if(arrayList.size()<30){
                        test();
                        adapter.changeMoreStatus(TrainingHotAskListAdapter.PULLUP_LOAD_MORE);
                    }else{

                        adapter.changeMoreStatus(TrainingHotAskListAdapter.NO_LOAD_MORE);
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

    public void requestIndexData(){


//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String,Object> map = new LinkedHashMap<String,Object>();
        map.put("page",page);
        map.put("typeCode",typeCode);

        HtmlRequest.getTrainingClassList(context, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if(params.result!=null){

                    ResultClassIndexBean bean = (ResultClassIndexBean)params.result;
                    courseList.addAll(bean.getCourseList());
                    adapter.notifyDataSetChanged();

                }else{

                }

            }
        });



    }


    public void test(){

        for(int i=0;i<10;i++){

            String sd = string+"11"+i;
            arrayList.add(sd);

        }

    }

    public static TrainingClassFragment newInstance(String newsId) {
        TrainingClassFragment fragment = new TrainingClassFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        bundle.putString(KEY,newsId);
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
