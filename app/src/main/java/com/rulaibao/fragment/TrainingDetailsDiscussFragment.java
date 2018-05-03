package com.rulaibao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.rulaibao.R;
import com.rulaibao.adapter.TrainingClassDiscussAdapter;
import com.rulaibao.adapter.TrainingClassListAdapter;
import com.rulaibao.adapter.TrainingHotAskListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 课程详情 研讨 栏
 */

public class TrainingDetailsDiscussFragment extends BaseFragment {


    @BindView(R.id.tv_introduction_discuss_count)
    TextView tvIntroductionDiscussCount;
    @BindView(R.id.lv_discuss)
    RecyclerView lvDiscuss;

    @BindView(R.id.btn_details_discuss)
    Button btnDetailsDiscuss;
    @BindView(R.id.et_detail_discuss)
    EditText etDetailDiscuss;

    private ArrayList arrayList = new ArrayList();

    private TrainingClassDiscussAdapter adapter;

    private String string = "";

    @Override
    protected View attachLayoutRes(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_training_details_discuss, container, false);

        } else {
            if (mView.getParent() != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
        }
        return mView;
    }

    @Override
    protected void initViews() {

        test();
        initRecyclerView();


    }

    public void initRecyclerView(){

        lvDiscuss.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TrainingClassDiscussAdapter(getActivity(),arrayList);
        lvDiscuss.setAdapter(adapter);


        lvDiscuss.setOnScrollListener(new RecyclerView.OnScrollListener() {
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

    public void test() {

        for (int i = 0; i < 10; i++) {

            String sd = string + "11" + i;
            arrayList.add(sd);

        }

    }

    @OnClick(R.id.btn_details_discuss)
    public void onclick(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
