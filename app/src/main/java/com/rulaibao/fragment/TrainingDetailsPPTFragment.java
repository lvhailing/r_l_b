package com.rulaibao.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rulaibao.R;
import com.rulaibao.adapter.TrainingClassDiscussAdapter;
import com.rulaibao.adapter.TrainingClassListAdapter;
import com.rulaibao.adapter.TrainingClassPPTAdapter;
import com.rulaibao.adapter.TrainingHotAskListAdapter;
import com.rulaibao.bean.ResultClassDetailsDiscussBean;
import com.rulaibao.bean.ResultClassDetailsPPTBean;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.uitls.PreferenceUtil;
import com.rulaibao.uitls.encrypt.DESUtil;
import com.rulaibao.widget.ViewPagerForScrollView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 课程详情 PPT 栏
 */

@SuppressLint("ValidFragment")
public class TrainingDetailsPPTFragment extends BaseFragment {
    @BindView(R.id.lv_ppt)
    RecyclerView lvPpt;

    private ArrayList arrayList = new ArrayList();

    private String string = "";
    private TrainingClassPPTAdapter adapter;
    private String courseId = "";

    private ArrayList<String> pptImgs;

    private ViewPagerForScrollView vp;

    public TrainingDetailsPPTFragment(ViewPagerForScrollView vp) {
        this.vp = vp;
    }
    public TrainingDetailsPPTFragment() {
    }

    @Override
    protected View attachLayoutRes(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_training_details_ppt, container, false);
//            vp.setObjectForPosition(mView,3);
        } else {
            if (mView.getParent() != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
        }
        return mView;
    }

    @Override
    protected void initViews() {
        courseId = getArguments().getString("courseId");
        pptImgs = new ArrayList<String>();

        test();
        initRecyclerView();
        requestData();
    }

    public void initRecyclerView(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                // 直接禁止垂直滑动
//                return false;
                return true;
            }
        };

        lvPpt.setLayoutManager(layoutManager);
        adapter = new TrainingClassPPTAdapter(getActivity(),pptImgs);
        lvPpt.setAdapter(adapter);


        lvPpt.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {

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

    public void requestData() {


//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

        map.put("id", courseId);      //

        HtmlRequest.getClassDetailsPPT(context, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if (params.result != null) {

                    ResultClassDetailsPPTBean bean = (ResultClassDetailsPPTBean) params.result;

                    adapter.changeMoreStatus(TrainingHotAskListAdapter.NO_LOAD_MORE);

                    pptImgs.addAll(bean.getPptImgs());
                    adapter.notifyDataSetChanged();

                } else {

                }
            }
        });
    }




    public void test() {

        for (int i = 0; i < 10; i++) {

            String sd = string + "11" + i;
            arrayList.add(sd);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
