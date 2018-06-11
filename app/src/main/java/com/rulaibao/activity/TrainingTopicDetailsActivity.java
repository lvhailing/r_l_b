package com.rulaibao.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rulaibao.R;
import com.rulaibao.adapter.RecyclerBaseAapter;
import com.rulaibao.adapter.TrainingAnswerDetailsListAdapter;
import com.rulaibao.adapter.TrainingHotAskListAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.ResultCircleDetailsTopicCommentItemBean;
import com.rulaibao.bean.ResultCircleDetailsTopicCommentListBean;
import com.rulaibao.bean.ResultCircleDetailsTopicCommentReplyItemBean;
import com.rulaibao.bean.ResultCircleDetailsTopicDetailsBean;
import com.rulaibao.bean.ResultCircleDetailsTopicDetailsItemBean;
import com.rulaibao.bean.ResultInfoBean;
import com.rulaibao.bean.TestBean;
import com.rulaibao.common.Urls;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.uitls.ImageLoaderManager;
import com.rulaibao.uitls.InputMethodUtils;
import com.rulaibao.uitls.PreferenceUtil;
import com.rulaibao.uitls.RlbActivityManager;
import com.rulaibao.uitls.ViewUtils;
import com.rulaibao.uitls.encrypt.DESUtil;
import com.rulaibao.widget.CircularImage;
import com.rulaibao.widget.MyRecyclerView;
import com.rulaibao.widget.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * 话题详情
 */

public class TrainingTopicDetailsActivity extends BaseActivity implements TrainingAnswerDetailsListAdapter.Reply, MyRecyclerView.OnResizeListener {


    private DisplayImageOptions displayImageOptions = ImageLoaderManager.initDisplayImageOptions(R.mipmap.ic_ask_photo_default, R.mipmap.ic_ask_photo_default, R.mipmap.ic_ask_photo_default);

    @BindView(R.id.lv_topic_details)
    MyRecyclerView lvTopicDetails;
    @BindView(R.id.btn_topic_details)
    Button btnTopicDetails;
    @BindView(R.id.et_topic_details)
    EditText etTopicDetails;
    @BindView(R.id.fy_training_topic_details)
    FrameLayout fyTrainingTopicDetails;

    private CircularImage iv_answer_detatils_manager;
    private TextView tv_answer_details_manager_name;
    private TextView tv_answer_details_settop;
    private TextView tv_answer_details_content;
    private TextView tv_training_topic_detils_name;
    private TextView tv_training_topic_detils_time;
    private TextView tv_answer_detailas_zan_count;          //  点赞条数
    private TextView tv_answer_details_comment_count;       //  评论条数
    private ImageView iv_answer_detailas_zan;       //  点赞

    private ArrayList<TestBean> arrayList = new ArrayList<TestBean>();
    private String string = "";
    private String appTopicId = "";     //  话题id
    private String circleId = "";     //  圈子id
    private TrainingAnswerDetailsListAdapter adapter;
    private ResultCircleDetailsTopicDetailsItemBean appTopic;

    private MouldList<ResultCircleDetailsTopicCommentItemBean> commentItemBeans;
    private int page = 1;

    private String commentId = "";
    private String toUserId = "";
    private String replyToName = "";
    private int index = 0;

    private int oldPosition = 0;
    private int bottomOffset = 0;
    private int position;
    private View header;
    private TitleBar title = null;

    private String allowClick = "true";     //      处理频繁点击置顶操作
    private String linkId = "";
    private boolean noDataFlag = true;      //  控制无数据不加载

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_training_topic_details);
        initTopTitle();

        initView();


    }

    public void initData() {
        noDataFlag = true;
        page = 1;
        commentItemBeans.clear();
        requestTopicDetailsData();

    }

    public void initView() {

        appTopicId = getIntent().getStringExtra("appTopicId");
        appTopic = new ResultCircleDetailsTopicDetailsItemBean();
        commentItemBeans = new MouldList<ResultCircleDetailsTopicCommentItemBean>();

        setRereshEnable(true);

        initRecyclerView();

    }

    public void initRecyclerView() {

        lvTopicDetails.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TrainingAnswerDetailsListAdapter(this, commentItemBeans, TrainingTopicDetailsActivity.this);

//        adapter = new TrainingClassListAdapter(getActivity(),arrayList);
        lvTopicDetails.setOnResizeListener(this);

        lvTopicDetails.setAdapter(adapter);


        //为RecyclerView添加HeaderView和FooterView
        setHeaderView(lvTopicDetails);
//        setFooterView(lvAskDetails);


        lvTopicDetails.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {

                    if (!(lastVisibleItem == 1 && page == 1)) {
                        if (noDataFlag) {
                            adapter.changeMoreStatus(TrainingHotAskListAdapter.LOADING_MORE);
                            page++;
                            requestCircleCommentData();
                        }

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

    private void setHeaderView(RecyclerView view) {

        header = LayoutInflater.from(this).inflate(R.layout.activity_training_topic_details_top, view, false);

        iv_answer_detatils_manager = (CircularImage) header.findViewById(R.id.iv_answer_detatils_manager);
        tv_answer_details_manager_name = (TextView) header.findViewById(R.id.tv_answer_details_manager_name);
        tv_answer_details_settop = (TextView) header.findViewById(R.id.tv_answer_details_settop);
        tv_answer_details_content = (TextView) header.findViewById(R.id.tv_answer_details_content);
        tv_training_topic_detils_name = (TextView) header.findViewById(R.id.tv_training_topic_detils_name);
        tv_training_topic_detils_time = (TextView) header.findViewById(R.id.tv_training_topic_detils_time);
        tv_answer_detailas_zan_count = (TextView) header.findViewById(R.id.tv_answer_detailas_zan_count);
        tv_answer_details_comment_count = (TextView) header.findViewById(R.id.tv_answer_details_comment_count);
        iv_answer_detailas_zan = (ImageView) header.findViewById(R.id.iv_answer_detailas_zan);


        adapter.setmHeaderView(header);

    }

    public void initHeadData() {


        String id = "23232323";
        String url = Urls.URL_SHARED_TOPIC + appTopicId;
        title.setActivityParameters(url, id, appTopic.getCircleName(), "话题描述：" + appTopic.getTopicContent());

        ImageLoader.getInstance().displayImage(appTopic.getCreatorPhoto(), iv_answer_detatils_manager, displayImageOptions);
        tv_answer_details_manager_name.setText(appTopic.getCreatorName());
        tv_answer_details_content.setText(appTopic.getTopicContent());
        tv_training_topic_detils_name.setText(appTopic.getCircleName());
        tv_training_topic_detils_time.setText(appTopic.getCreateTime());
        tv_answer_detailas_zan_count.setText("给Ta一个赞(" + appTopic.getLikeCount() + ")");
//        tv_answer_details_comment_count.setText(appTopic.getCommentCount() + "评论");

        if (appTopic.getIsManager().equals("yes")) {
            tv_answer_details_settop.setVisibility(View.VISIBLE);

        } else {
            tv_answer_details_settop.setVisibility(View.GONE);
        }

        tv_answer_details_settop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        allowClick = "true";

                    }

                }, 3000);

                if (allowClick.equals("true")) {
                    allowClick = "false";
                    if (appTopic.getIsTop().equals("yes")) {      //  已置顶     取消置顶
                        tv_answer_details_settop.setClickable(false);
                        requestTopData("no");
                    } else {
                        requestTopData("yes");
                    }

                } else {
                    Toast.makeText(TrainingTopicDetailsActivity.this, "请勿频繁操作", Toast.LENGTH_SHORT).show();
                }


            }
        });

        iv_answer_detailas_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appTopic.getLikeStatus().equals("yes")) {      //  已点赞     不处理
//                    requestLikeData();
                } else {
                    if (!PreferenceUtil.isLogin()) {
                        HashMap<String, Object> map = new HashMap<>();
                        RlbActivityManager.toLoginActivity(TrainingTopicDetailsActivity.this, map, false);

                    } else {
                        if (!PreferenceUtil.getCheckStatus().equals("success")) {
                            ViewUtils.showToSaleCertificationDialog(TrainingTopicDetailsActivity.this, "您还未认证，是否去认证");

                        } else {
                            if (appTopic.getIsJoin().equals("no")) {
                                Toast.makeText(TrainingTopicDetailsActivity.this, "请您加入该圈子后再进行相关操作", Toast.LENGTH_SHORT).show();
                            } else {
                                requestLikeData();
                            }
                        }
                    }


                }

            }
        });

        if (appTopic.getLikeStatus().equals("yes")) {
            iv_answer_detailas_zan.setImageResource(R.mipmap.img_answer_zan);
//            iv_answer_detailas_zan.setClickable(false);
        } else {
            iv_answer_detailas_zan.setImageResource(R.mipmap.img_answer_zaned);
//            iv_answer_detailas_zan.setClickable(true);
        }

        if (appTopic.getIsTop().equals("yes")) {
            tv_answer_details_settop.setText("取消置顶");

        } else {

            tv_answer_details_settop.setText("设为置顶");
        }

    }

    // 获取详情主页信息
    public void requestTopicDetailsData() {

//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
//        appTopicId = "111";
        map.put("appTopicId", appTopicId);
        map.put("userId", userId);

        HtmlRequest.getTrainingCircleDetailsTopicDetails(this, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if (params.result != null) {

                    ResultCircleDetailsTopicDetailsBean bean = (ResultCircleDetailsTopicDetailsBean) params.result;
                    if (bean.getFlag().equals("true")) {
                        appTopic = bean.getAppTopic();
                        page = 1;
                        requestCircleCommentData();
//                    initRecyclerView();
                        initHeadData();

                    } else {
                        if (bean.getCode().equals("1001")) {      //  参数错误

                            Toast.makeText(TrainingTopicDetailsActivity.this, bean.getMessage(), Toast.LENGTH_SHORT).show();

                        } else if (bean.getCode().equals("1002")) {        //  该话题已删除
                            ViewUtils.showDeleteDialog(TrainingTopicDetailsActivity.this, bean.getMessage());
//                            Toast.makeText(TrainingTopicDetailsActivity.this, bean.getMessage(), Toast.LENGTH_SHORT).show();
//                            finish();
                        }

                    }

                } else {

                }
            }
        });
    }

    // 获取评论列表
    public void requestCircleCommentData() {

//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

        map.put("appTopicId", appTopicId);
        map.put("userId", userId);
        map.put("page", page + "");

        HtmlRequest.getTrainingCircleCommentList(this, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if (params.result != null) {

                    ResultCircleDetailsTopicCommentListBean bean = (ResultCircleDetailsTopicCommentListBean) params.result;
                    if (bean.getList() != null) {
                        if (bean.getList().size() == 0) {
                            if (page != 1) {
                                page--;
                                adapter.changeMoreStatus(RecyclerBaseAapter.NO_LOAD_MORE);
                            } else {
                                adapter.setNoDataMessage("暂无评论");
                                adapter.changeMoreStatus(RecyclerBaseAapter.NO_DATA_WRAP_CONTENT);
                                noDataFlag = false;
                                tv_answer_details_comment_count.setVisibility(View.GONE);
                            }


                        } else {
                            tv_answer_details_comment_count.setVisibility(View.VISIBLE);
                            tv_answer_details_comment_count.setText(bean.getTotal() + "评论");
                            commentItemBeans.addAll(bean.getList());
                            adapter.notifyDataSetChanged();

                            if (commentItemBeans.size() % 10 == 0) {
                                adapter.changeMoreStatus(RecyclerBaseAapter.PULLUP_LOAD_MORE);
                            } else {
                                adapter.changeMoreStatus(RecyclerBaseAapter.NO_LOAD_MORE);
                            }

                        }
                    }



                } else {

                }
                swipe.setRefreshing(false);
            }
        });
    }


    private void initTopTitle() {
        title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setFromActivity("1000");
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.mipmap.logo, false)
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.training_topic_details)).showMore(false).setTitleRightButton(R.drawable.ic_share_title)
                .showMore(false).setOnActionListener(new TitleBar.OnActionListener() {
            @Override
            public void onMenu(int id) {
            }

            @Override
            public void onBack() {
                finish();
            }

            @Override
            public void onAction(int id) {

            }
        });
    }

    @OnClick(R.id.btn_topic_details)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topic_details:

                String commentContent = etTopicDetails.getText().toString();

                if (!PreferenceUtil.isLogin()) {

                    HashMap<String, Object> map = new HashMap<>();
                    RlbActivityManager.toLoginActivity(TrainingTopicDetailsActivity.this, map, false);

                } else {

                    if (!PreferenceUtil.getCheckStatus().equals("success")) {
                        ViewUtils.showToSaleCertificationDialog(this, "您还未认证，是否去认证");

                    } else {
                        if (appTopic.getIsJoin().equals("no")) {
                            Toast.makeText(TrainingTopicDetailsActivity.this, "请您加入该圈子后再进行相关操作", Toast.LENGTH_SHORT).show();
                        } else {
                            if (TextUtils.isEmpty(commentId)) {       //  评论
                                hiddenInputLayout();
                                requestReplyData(commentContent);
                            } else {              //  回复


                                hiddenInputLayout();
                                requestReplyData(commentContent);
                            }
                        }
                    }


                }

                break;

            default:

                break;

        }

    }

    //回复评论
    public void requestReplyData(final String commentContent) {

//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

        if (TextUtils.isEmpty(commentId)) {

            map.put("appTopicId", appTopicId);      //  话题id
            map.put("commentContent", commentContent);
            map.put("userId", userId);
            map.put("linkId", linkId);
        } else {

            map.put("appTopicId", appTopicId);      //  话题id
            map.put("commentId", commentId);
            map.put("commentContent", commentContent);
            map.put("toUserId", toUserId);          //  被回复人id
            map.put("userId", userId);
            map.put("linkId", linkId);
        }


        HtmlRequest.getTrainingCircleReply(this, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if (params.result != null) {

                    ResultInfoBean bean = (ResultInfoBean) params.result;
                    if (bean.getFlag().equals("true")) {
//                        fyTrainingTopicDetails.setVisibility(View.GONE);

                        if (!TextUtils.isEmpty(commentId)) {      //  回复
//                            ResultCircleDetailsTopicCommentReplyItemBean replyItemBean = new ResultCircleDetailsTopicCommentReplyItemBean();
//                            replyItemBean.setReplyContent(commentContent);
//                            replyItemBean.setReplyId(replyId);
//                            replyItemBean.setReplyToName(replyToName);
//                            commentItemBeans.get(index).getReplys().add(replyItemBean);

                            ResultCircleDetailsTopicCommentReplyItemBean itemBean = new ResultCircleDetailsTopicCommentReplyItemBean();
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
                            itemBean.setReplyToName(replyToName);      //  被回复人姓名
                            itemBean.setRid(commentId);

                            commentItemBeans.get(index).getReplys().add(itemBean);

                            commentId = "";
                            adapter.notifyDataSetChanged();
                        } else {          //  评论
                            page = 1;
                            commentItemBeans.clear();
                            requestCircleCommentData();
                        }


                    } else {
                        Toast.makeText(TrainingTopicDetailsActivity.this, bean.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {

                }
            }
        });
    }

    //点赞
    public void requestLikeData() {

        iv_answer_detailas_zan.setClickable(false);

//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

        map.put("appTopicId", appTopicId);      //  话题id
        map.put("userId", userId);
//        map.put("likeStatus", likeStatus);

        HtmlRequest.getTrainingCircleZan(this, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if (params.result != null) {

                    ResultInfoBean bean = (ResultInfoBean) params.result;
                    if (bean.getFlag().equals("true")) {
                        if (appTopic.getLikeStatus().equals("yes")) {
                            iv_answer_detailas_zan.setImageResource(R.mipmap.img_answer_zaned);
                            appTopic.setLikeStatus("no");
                            int count = Integer.parseInt(appTopic.getLikeCount());
                            appTopic.setLikeCount((count - 1) + "");
//                            iv_answer_detailas_zan.setClickable(false);
                        } else {
                            int count = Integer.parseInt(appTopic.getLikeCount());
                            appTopic.setLikeCount((count + 1) + "");
                            iv_answer_detailas_zan.setImageResource(R.mipmap.img_answer_zan);
//                            iv_answer_detailas_zan.setClickable(true);
                            appTopic.setLikeStatus("yes");
                        }
                        iv_answer_detailas_zan.setClickable(true);
                        tv_answer_detailas_zan_count.setText("给他一个赞(" + appTopic.getLikeCount() + ")");

                    } else {
                        Toast.makeText(TrainingTopicDetailsActivity.this, bean.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                } else {

                }
            }
        });
    }

    //置顶或者取消置顶

    public void requestTopData(String topStatus) {

//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

        map.put("appTopicId", appTopicId);      //  话题id
        map.put("userId", userId);
        map.put("circleId", appTopic.getCircleId());
        map.put("topStatus", topStatus);    //  yes:置顶；no:取消置顶

        HtmlRequest.getTrainingCircleSetTop(this, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if (params.result != null) {

                    ResultInfoBean bean = (ResultInfoBean) params.result;
                    if (bean.getFlag().equals("true")) {
                        if (appTopic.getIsTop().equals("yes")) {
                            tv_answer_details_settop.setText("设为置顶");

                            appTopic.setIsTop("no");
                        } else {
                            tv_answer_details_settop.setText("取消置顶");
                            appTopic.setIsTop("yes");
                        }
                        Toast.makeText(TrainingTopicDetailsActivity.this, bean.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TrainingTopicDetailsActivity.this, bean.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                } else {

                }
                tv_answer_details_settop.setClickable(true);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    // 回复回调
    @Override
    public void reply(String commentId, String toUserId, String replyToName, int index, String linkId) {
        this.commentId = commentId;
        this.toUserId = toUserId;
        this.replyToName = replyToName;
        this.index = index;
        this.linkId = linkId;
        etTopicDetails.setHint("回复" + replyToName + "：");

        setBottomOffset(index);

    }


    //  弹出键盘

    public void setBottomOffset(int position) {
//        position = position-1;
        LinearLayoutManager layoutManager = (LinearLayoutManager) lvTopicDetails.getLayoutManager();
        int index = layoutManager.findFirstVisibleItemPosition();
//        int indexCom = layoutManager.findFirstCompletelyVisibleItemPosition();
//        if(indexCom-index>1){
//            index++;
//        }
//        int index = 0;

        try {
            if (index > position) {
                index = position;
                if (isShowComment.get()) {
                    bottomOffset = lvTopicDetails.getChildAt(position - index).getBottom();
                } else {
                    bottomOffset = lvTopicDetails.getChildAt(index - position).getBottom() - lvTopicDetails.getChildAt(index - position).getHeight();
                }
            } else {
                bottomOffset = lvTopicDetails.getChildAt(position - index).getBottom();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        if (!deal(position))
            showInputLyaout();
    }

    private AtomicBoolean isShowComment = new AtomicBoolean(false);

    private boolean deal(int position) {
        if (isShowComment.get()) {
            if (oldPosition != position) {
                int offset = -(lvTopicDetails.getHeight() - bottomOffset);
                putOffset(offset);
            }
            return true;
        }
        return false;
    }

    @OnTouch(R.id.lv_topic_details)
    boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_MOVE == event.getAction()) {
            commentId = "";
            hiddenInputLayout();
        }
        return false;
    }


    private void showInputLyaout() {
        isShowComment.set(true);
//        flAnswerDetails.setVisibility(View.VISIBLE);
        InputMethodUtils.showSoftKeyboard(etTopicDetails);
    }

    private void putOffset(int offset) {
        try {
            lvTopicDetails.smoothScrollBy(0, offset);
            oldPosition = index;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //  隐藏键盘

    private void hiddenInputLayout() {
        isShowComment.set(false);
        etTopicDetails.setText("");
        etTopicDetails.setHint(getString(R.string.training_class_details_discuss_comment_hint));

//        flAnswerDetails.setVisibility(View.GONE);
        InputMethodUtils.hiddenSoftKeyboard(this);
    }

    public int getItemHeight() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) lvTopicDetails.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition() - 1;
//        int position = layoutManager.findFirstCompletelyVisibleItemPosition();

        int set = 0;
        try {
            if (position > index) {
                position = index;
//                set = 0;
            } else {

            }
            set = lvTopicDetails.getChildAt(index - position).getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return set;
    }


    @Override
    public void OnResize(int w, int h, int oldw, int oldh) {
        if (oldh > h) {
            int offset = (oldh - h + getItemHeight()) - (oldh - bottomOffset);
//            int offset = (oldh - h ) - (oldh - bottomOffset);
            putOffset(offset);
        }
    }

}
