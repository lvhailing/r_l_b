package com.rulaibao.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rulaibao.R;
import com.rulaibao.adapter.TrainingClassDiscussAdapter;
import com.rulaibao.adapter.TrainingHotAskListAdapter;
import com.rulaibao.bean.ResultCircleDetailsTopicCommentReplyItemBean;
import com.rulaibao.bean.ResultClassDetailsDiscussBean;
import com.rulaibao.bean.ResultClassDetailsDiscussItemBean;
import com.rulaibao.bean.ResultClassDetailsDiscussItemReplyBean;
import com.rulaibao.bean.ResultInfoBean;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.uitls.InputMethodUtils;
import com.rulaibao.uitls.PreferenceUtil;
import com.rulaibao.uitls.encrypt.DESUtil;
import com.rulaibao.widget.MyRecyclerView;
import com.rulaibao.widget.ViewPagerForScrollView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * 课程详情 研讨 栏
 */

@SuppressLint("ValidFragment")
public class TrainingDetailsDiscussFragment extends BaseFragment implements TrainingClassDiscussAdapter.DiscussReply,MyRecyclerView.OnResizeListener {


    @BindView(R.id.tv_introduction_discuss_count)
    TextView tvIntroductionDiscussCount;
    @BindView(R.id.lv_discuss)
    MyRecyclerView lvDiscuss;

    @BindView(R.id.btn_details_discuss)
    Button btnDetailsDiscuss;
    @BindView(R.id.et_detail_discuss)
    EditText etDetailDiscuss;
    @BindView(R.id.fl_details_discuss)
    FrameLayout flDetailsDiscuss;


    private ArrayList arrayList = new ArrayList();

    private TrainingClassDiscussAdapter adapter;

    private String string = "";
    private String courseId = "";
    private String toUserId = "";
    private String commentId = "";
    private int page = 1;

    private ViewPagerForScrollView vp;

    private MouldList<ResultClassDetailsDiscussItemBean> list;

    private int index;
    private int oldPosition = 0;
    private int bottomOffset = 0;
    private String commentName = "";
    private int position;

    public TrainingDetailsDiscussFragment(ViewPagerForScrollView vp) {
        this.vp = vp;
    }

    public TrainingDetailsDiscussFragment() {
    }

    @Override
    protected View attachLayoutRes(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_training_details_discuss, container, false);
//            vp.setObjectForPosition(mView,2);
        } else {
            if (mView.getParent() != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
        }
        return mView;
    }

    @Override
    protected void initViews() {

        list = new MouldList<ResultClassDetailsDiscussItemBean>();


        initRecyclerView();


    }

    public void initRecyclerView() {


        LinearLayoutManager layoutManager = new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                // 直接禁止垂直滑动
//                return false;
                return true;
            }
        };

        lvDiscuss.setLayoutManager(layoutManager);
        adapter = new TrainingClassDiscussAdapter(getActivity(), list, TrainingDetailsDiscussFragment.this);
        lvDiscuss.setOnResizeListener(this);
        lvDiscuss.setAdapter(adapter);


        lvDiscuss.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {

                    adapter.changeMoreStatus(TrainingHotAskListAdapter.LOADING_MORE);

                    page++;
                    requestData();

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
            courseId = getArguments().getString("courseId");
            if(list!=null){
                list.clear();
            }
            page=1;
            requestData();
//            scrollView.smoothScrollTo(0, 0);
        } else {

        }

    }

    public void requestData() {

//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

        map.put("courseId", courseId);      //
        map.put("userId", userId);      //
        map.put("page", page + "");      //

        HtmlRequest.getClassDetailsDiscuss(context, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if (params.result != null) {

                    ResultClassDetailsDiscussBean bean = (ResultClassDetailsDiscussBean) params.result;
                    if(bean.getList().size()==0&&page!=1){
                        page--;
                        adapter.changeMoreStatus(TrainingHotAskListAdapter.NO_LOAD_MORE);
                    }else{
                        tvIntroductionDiscussCount.setText("总共"+bean.getTotal()+"条研讨");
                        list.addAll(bean.getList());
                        adapter.notifyDataSetChanged();
                        adapter.changeMoreStatus(TrainingHotAskListAdapter.PULLUP_LOAD_MORE);
                    }

                } else {

                }
            }
        });
    }

    //评论

    public void requestReply(String commentContent) {

        String userId = null;
        try {
            userId = DESUtil.decrypt(PreferenceUtil.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

        if (TextUtils.isEmpty(commentId)) {

            map.put("courseId", courseId);      //话题课程id
            map.put("commentContent", commentContent);      //
            map.put("userId", userId);      //

        } else {

            map.put("courseId", courseId);      //话题课程id
            map.put("commentContent", commentContent);      //
            map.put("userId", userId);      //
            map.put("toUserId", toUserId);      //      回复的目标用户id（注：当回复时需要，评论不需要传）
            map.put("commentId", commentId);      //所属评论的id（注：当回复时需要，评论不需要传）

        }

        HtmlRequest.getClassDetailsDiscussReply(context, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if (params.result != null) {

                    ResultInfoBean bean = (ResultInfoBean) params.result;
                    if (bean.getFlag().equals("true")) {

                        if (!TextUtils.isEmpty(commentId)) {      //  回复

                            commentId = "";
                            hiddenInputLayout();
                        } else {          //  评论
                            hiddenInputLayout();
                            page = 1;
                            list.clear();
                            requestData();
                        }

                    } else {


                    }
//                    list.addAll(bean.getList());
//                    adapter.notifyDataSetChanged();

                } else {

                }
            }
        });
    }

    @OnClick(R.id.btn_details_discuss)
    public void onclick() {
        String commentContent = etDetailDiscuss.getText().toString();

        if (TextUtils.isEmpty(commentId)) {       //  评论

            requestReply(commentContent);

        } else {              //  回复

            ResultClassDetailsDiscussItemReplyBean itemBean = new ResultClassDetailsDiscussItemReplyBean();
            itemBean.setReplyContent(commentContent);

            itemBean.setReplyId(userId);      //      回复人id
            itemBean.setReplyToId(toUserId);    //  被回复人id

            String realName = "";
            try {
                realName = DESUtil.decrypt(PreferenceUtil.getUserRealName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            itemBean.setReplyName(realName);       //  回复人姓名
            itemBean.setReplyToName(commentName);      //  被回复人姓名

            list.get(index).getReplys().add(itemBean);
            adapter.notifyDataSetChanged();
            requestReply(commentContent);

        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void reply(String toUserId, String commentId, String commentName, int index) {
        this.toUserId = toUserId;
        this.commentId = commentId;
        this.index = index;
        this.commentName = commentName;
        etDetailDiscuss.setHint("回复" + commentName + "：");

        setBottomOffset(index);


    }


    //  弹出键盘

    public void setBottomOffset(int position) {

        LinearLayoutManager layoutManager = (LinearLayoutManager) lvDiscuss.getLayoutManager();
        int index = layoutManager.findFirstVisibleItemPosition();
//        int index = layoutManager.findFirstCompletelyVisibleItemPosition();
        if (index > position) {
            index = position;
        }

        bottomOffset = lvDiscuss.getChildAt(position - index).getBottom();
        if (!deal(position)) showInputLyaout();
    }

    private AtomicBoolean isShowComment = new AtomicBoolean(false);

    private boolean deal(int position) {
        if (isShowComment.get()) {
            if (oldPosition != position) {
                int offset = -(lvDiscuss.getHeight() - bottomOffset - flDetailsDiscuss.getHeight());
                putOffset(offset);
            }
            return true;
        }
        return false;
    }

    @OnTouch(R.id.lv_discuss)
    boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_MOVE == event.getAction()) {
            hiddenInputLayout();
        }
        return false;
    }


    private void showInputLyaout() {
        isShowComment.set(true);
//        flAnswerDetails.setVisibility(View.VISIBLE);
        InputMethodUtils.showSoftKeyboard(etDetailDiscuss);
    }

    private void putOffset(int offset) {
        lvDiscuss.smoothScrollBy(offset, 1000);
        oldPosition = position;
    }


    //  隐藏键盘

    private void hiddenInputLayout() {
        isShowComment.set(false);
        etDetailDiscuss.setText("");
        etDetailDiscuss.setHint(getString(R.string.training_class_details_discuss_comment_hint));
//        flAnswerDetails.setVisibility(View.GONE);
        InputMethodUtils.hiddenSoftKeyboard(context);
    }

    @Override
    public void OnResize(int w, int h, int oldw, int oldh) {
        if (oldh > h) {
            int offset = (oldh - h + flDetailsDiscuss.getHeight()) - (oldh - bottomOffset);
            putOffset(offset);
        }
    }


}
