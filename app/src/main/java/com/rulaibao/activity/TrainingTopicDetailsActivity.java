package com.rulaibao.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.widget.CircularImage;
import com.rulaibao.widget.TitleBar;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 话题详情
 */

public class TrainingTopicDetailsActivity extends BaseActivity implements TrainingAnswerDetailsListAdapter.Reply {


    @BindView(R.id.lv_topic_details)
    RecyclerView lvTopicDetails;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_training_topic_details);
        initTopTitle();

        initView();
        initData();

    }

    public void initData() {

        requestTopicDetailsData();

    }

    public void initView() {

        appTopicId = getIntent().getStringExtra("appTopicId");
        appTopic = new ResultCircleDetailsTopicDetailsItemBean();
        commentItemBeans = new MouldList<ResultCircleDetailsTopicCommentItemBean>();
//        test();


    }

    public void initRecyclerView() {

        lvTopicDetails.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TrainingAnswerDetailsListAdapter(this, commentItemBeans,TrainingTopicDetailsActivity.this);
//        adapter = new TrainingClassListAdapter(getActivity(),arrayList);
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

                    adapter.changeMoreStatus(TrainingHotAskListAdapter.LOADING_MORE);
                    page++;
                    requestCircleCommentData();
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

        initHeadData();

    }

    private void setHeaderView(RecyclerView view) {

        View header = LayoutInflater.from(this).inflate(R.layout.activity_training_topic_details_top, view, false);

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

        if(appTopic.getIsManager().equals("yes")){
            tv_answer_details_settop.setVisibility(View.VISIBLE);

        }else{
            tv_answer_details_settop.setVisibility(View.GONE);
        }

        tv_answer_details_settop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appTopic.getIsTop().equals("yes")){      //  已置顶     取消置顶
                    requestTopData("no");
                }else{
                    requestTopData("yes");
                }

            }
        });

        iv_answer_detailas_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appTopic.getLikeStatus().equals("yes")){      //  已点赞     不处理
//                    requestLikeData();
                }else{
                    requestLikeData();
                }

            }
        });

    }

    public void initHeadData() {

        ImageLoader.getInstance().displayImage(appTopic.getCreatorPhoto(), iv_answer_detatils_manager);
        tv_answer_details_manager_name.setText(appTopic.getCreatorName());
        tv_answer_details_content.setText(appTopic.getTopicContent());
        tv_training_topic_detils_name.setText(appTopic.getCircleName());
        tv_training_topic_detils_time.setText(appTopic.getCreateTime());
        tv_answer_detailas_zan_count.setText("给他一个赞(" + appTopic.getLikeCount() + ")");
        tv_answer_details_comment_count.setText(appTopic.getCommentCount() + "评论");

        if(appTopic.getLikeStatus().equals("yes")){
            iv_answer_detailas_zan.setImageResource(R.mipmap.img_answer_zan);
//            iv_answer_detailas_zan.setClickable(false);
        }else{
            iv_answer_detailas_zan.setImageResource(R.mipmap.img_answer_zaned);
//            iv_answer_detailas_zan.setClickable(true);
        }

        if(appTopic.getIsTop().equals("yes")){
            tv_answer_details_settop.setText("取消置顶");

        }else{

            tv_answer_details_settop.setText("设为置顶");
        }

    }

    // 获取详情主页信息
    public void requestTopicDetailsData() {

//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

        map.put("appTopicId", appTopicId);
        map.put("userId", userId);

        HtmlRequest.getTrainingCircleDetailsTopicDetails(this, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if (params.result != null) {

                    ResultCircleDetailsTopicDetailsBean bean = (ResultCircleDetailsTopicDetailsBean) params.result;
                    appTopic = bean.getAppTopic();
                    page = 1;
                    requestCircleCommentData();
//                    initRecyclerView();
                    initRecyclerView();
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
                    if(bean.getList().size()==0&&page!=1){
                        page--;
                        adapter.changeMoreStatus(RecyclerBaseAapter.NO_LOAD_MORE);
                    }else{
                        commentItemBeans.addAll(bean.getList());
                        adapter.notifyDataSetChanged();
                        adapter.changeMoreStatus(RecyclerBaseAapter.PULLUP_LOAD_MORE);
                    }



                } else {

                }
            }
        });
    }


    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false)
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.training_topic_details))
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
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_topic_details:

                String commentContent = etTopicDetails.getText().toString();

                if(TextUtils.isEmpty(commentId)){       //  评论
                    requestReplyData(commentContent);
                }else{              //  回复
                    requestReplyData(commentContent);
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

        if(TextUtils.isEmpty(commentId)){

            map.put("appTopicId", appTopicId);      //  话题id
            map.put("commentContent", commentContent);
            map.put("userId", userId);

        }else{

            map.put("appTopicId", appTopicId);      //  话题id
            map.put("commentId", commentId);
            map.put("commentContent", commentContent);
            map.put("toUserId", toUserId);          //  被回复人id
            map.put("userId", userId);
        }


        HtmlRequest.getTrainingCircleReply(this, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if (params.result != null) {

                    ResultInfoBean bean = (ResultInfoBean) params.result;
                    if(bean.getFlag().equals("true")){
//                        fyTrainingTopicDetails.setVisibility(View.GONE);

                        if(!TextUtils.isEmpty(commentId)){      //  回复
//                            ResultCircleDetailsTopicCommentReplyItemBean replyItemBean = new ResultCircleDetailsTopicCommentReplyItemBean();
//                            replyItemBean.setReplyContent(commentContent);
//                            replyItemBean.setReplyId(replyId);
//                            replyItemBean.setReplyToName(replyToName);
//                            commentItemBeans.get(index).getReplys().add(replyItemBean);
                            commentId = "";
                        }else{          //  评论

                        }
//                        requestCircleCommentData();
                        adapter.notifyDataSetChanged();
                        etTopicDetails.setText("");
                        etTopicDetails.setHint(getString(R.string.training_class_details_discuss_comment_hint));

                    }else{

                    }

                } else {

                }
            }
        });
    }

    //点赞
    public void requestLikeData() {

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
                    if(bean.getFlag().equals("true")){
                        if(appTopic.getLikeStatus().equals("yes")){
                            iv_answer_detailas_zan.setImageResource(R.mipmap.img_answer_zaned);
                            appTopic.setLikeStatus("no");
                            int count = Integer.parseInt(appTopic.getLikeCount());
                            appTopic.setLikeCount((count-1)+"");
//                            iv_answer_detailas_zan.setClickable(false);
                        }else{
                            int count = Integer.parseInt(appTopic.getLikeCount());
                            appTopic.setLikeCount((count+1)+"");
                            iv_answer_detailas_zan.setImageResource(R.mipmap.img_answer_zan);
//                            iv_answer_detailas_zan.setClickable(true);
                            appTopic.setLikeStatus("yes");
                        }

                        tv_answer_detailas_zan_count.setText("给他一个赞(" + appTopic.getLikeCount() + ")");

                    }else{

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
                    if(bean.getFlag().equals("true")){
                        if(appTopic.getIsTop().equals("yes")){
                            tv_answer_details_settop.setText("设为置顶");

                            appTopic.setIsTop("no");
                        }else{
                            tv_answer_details_settop.setText("取消置顶");
                            appTopic.setIsTop("yes");
                        }

                    }else{

                    }


                } else {

                }
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
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
    public void reply(String commentId,String toUserId,String replyToName,int index) {
        this.commentId = commentId;
        this.toUserId = toUserId;
        this.replyToName = replyToName;
        this.index = index;
        etTopicDetails.setHint("回复"+replyToName+"：");

    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

}
