package com.rulaibao.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rulaibao.R;
import com.rulaibao.adapter.RecyclerBaseAapter;
import com.rulaibao.adapter.TrainingMyCircleDetailsListAdapter;
import com.rulaibao.adapter.TrainingMyCircleDetailsTitleListAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.ResultCircleDetailsBean;
import com.rulaibao.bean.ResultCircleDetailsItemBean;
import com.rulaibao.bean.ResultCircleDetailsTopItemBean;
import com.rulaibao.bean.ResultCircleDetailsTopicItemBean;
import com.rulaibao.bean.ResultCircleDetailsTopicListBean;
import com.rulaibao.bean.ResultInfoBean;
import com.rulaibao.bean.TestBean;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.uitls.ImageLoaderManager;
import com.rulaibao.uitls.PreferenceUtil;
import com.rulaibao.uitls.RlbActivityManager;
import com.rulaibao.widget.CircularImage;
import com.rulaibao.widget.MyListView;
import com.rulaibao.widget.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 圈子详情
 */

public class TrainingCircleDetailsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private DisplayImageOptions displayImageOptions = ImageLoaderManager.initDisplayImageOptions(R.mipmap.ic_ask_photo_default,R.mipmap.ic_ask_photo_default,R.mipmap.ic_ask_photo_default);

    @BindView(R.id.tv_circle_details_name)
    TextView tvCircleDetailsName;
    @BindView(R.id.sv_circle_details)
    NestedScrollView svCircleDetails;
    @BindView(R.id.swipe_circle_details)
    SwipeRefreshLayout swipeCircleDetails;
    @BindView(R.id.fl_issue_topic)
    FrameLayout flIssueTopic;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    private String status = "";      //  当前圈子状态  other 其他圈子    mine  我的圈子   join 我加入的圈子
    private static final String authority = "";

    @BindView(R.id.iv_circle_photo)
    CircularImage ivCirclePhoto;
    @BindView(R.id.tv_circle_details_person_num)
    TextView tvCircleDetailsPersonNum;
    @BindView(R.id.tv_circle_details_talk_num)
    TextView tvCircleDetailsTalkNum;
    @BindView(R.id.tv_circle_join)
    TextView tvCircleJoin;                  //  状态控制  加入  设置权限  退出圈子
    @BindView(R.id.tv_circile_details_desc)
    TextView tvCircileDetailsDesc;
    @BindView(R.id.lv_circle_title)
    MyListView lvCircleTitle;
    @BindView(R.id.lv_circle_talk)
    RecyclerView lvCircleTalk;
    @BindView(R.id.btn_training_cirlce_details)     //  发布话题（仅限当前圈子成员可发布）
            Button btnTrainingCirlceDetails;


    private TrainingMyCircleDetailsTitleListAdapter myCircleAdapter;
    private TrainingMyCircleDetailsListAdapter myCircleAdapterDetails;

    private ArrayList<TestBean> arrayList = new ArrayList<TestBean>();
    private String string = "";
    private String circleId = "";
    private String isApply = "";

    private MouldList<ResultCircleDetailsTopItemBean> topAppTopics;
    private MouldList<ResultCircleDetailsTopicItemBean> appTopics;
    private int page = 1;

    private ResultCircleDetailsItemBean appCircle;      //详情顶部数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_training_circle_details);
        initTopTitle();
        initView();

    }

    public void initData() {
        page = 1;
        appTopics.clear();
        requestTopData();
        requestTopicData();
    }

    public void setView(ResultCircleDetailsItemBean detailsItemBean) {

        ImageLoader.getInstance().displayImage(detailsItemBean.getCirclePhoto(), ivCirclePhoto,displayImageOptions);
        tvCircleDetailsName.setText(detailsItemBean.getCircleName());
        tvCircleDetailsPersonNum.setText(detailsItemBean.getMemberCount());
        tvCircleDetailsTalkNum.setText(detailsItemBean.getTopicCount());
        tvCircileDetailsDesc.setText(detailsItemBean.getCircleDesc());
        if (detailsItemBean.getIsManager().equals("yes")) {
            status = "mine";
        } else {
            if (detailsItemBean.getIsJoin().equals("yes")) {
                status = "join";
            } else {
                status = "other";
            }
        }
        if (status.equals("mine")) {      //  我的圈子

            tvCircleJoin.setText("设置权限");
            flIssueTopic.setVisibility(View.VISIBLE);
        } else if (status.equals("join")) {        //  我加入的圈子

            tvCircleJoin.setText("退出圈子");
            flIssueTopic.setVisibility(View.VISIBLE);
        } else if (status.equals("other")) {       //  其他圈子

            tvCircleJoin.setText("+ 加入");
            flIssueTopic.setVisibility(View.GONE);

        } else {

            tvCircleJoin.setText("+ 加入");
            flIssueTopic.setVisibility(View.GONE);
        }


    }

    public void initView() {

        circleId = getIntent().getStringExtra("circleId");
        isApply = getIntent().getStringExtra("isApply");
        topAppTopics = new MouldList<ResultCircleDetailsTopItemBean>();
        appTopics = new MouldList<ResultCircleDetailsTopicItemBean>();
        appCircle = new ResultCircleDetailsItemBean();


        //为SwipeRefreshLayout设置监听事件
        swipeCircleDetails.setOnRefreshListener(this);
        //为SwipeRefreshLayout设置刷新时的颜色变化，最多可以设置4种
        swipeCircleDetails.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        initTopicAdapterData();
    }

    //初始化顶部布局

    public void initTopAdapterData(ResultCircleDetailsItemBean itemBean) {

        setView(itemBean);

        myCircleAdapter = new TrainingMyCircleDetailsTitleListAdapter(this, topAppTopics);
        lvCircleTitle.setAdapter(myCircleAdapter);

        lvCircleTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("appTopicId", topAppTopics.get(position).getTopicId());
                map.put("circleId", circleId);
                RlbActivityManager.toTrainingTopicDetailsActivity(TrainingCircleDetailsActivity.this, map, false);

            }
        });

    }

    // 初始化话题列表

    public void initTopicAdapterData() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                // 直接禁止垂直滑动
                return false;
            }
        };

        lvCircleTalk.setLayoutManager(layoutManager);

        myCircleAdapterDetails = new TrainingMyCircleDetailsListAdapter(this, appTopics, circleId);
        lvCircleTalk.setAdapter(myCircleAdapterDetails);

        initLoadMoreListener();


    }


    public void initLoadMoreListener() {

        svCircleDetails.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    // 向下滑动
                }

                if (scrollY < oldScrollY) {
                    // 向上滑动
                }

                if (scrollY == 0) {
                    // 顶部
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    // 底部
                    myCircleAdapterDetails.changeMoreStatus(RecyclerBaseAapter.LOADING_MORE);
                    page++;
                    requestTopicData();

                }
            }
        });

    }


    // 获取详情数据

    public void requestTopData() {

//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

        map.put("circleId", circleId);
        map.put("userId", userId);

        HtmlRequest.getTrainingCircleDetails(this, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if (params.result != null) {

                    ResultCircleDetailsBean bean = (ResultCircleDetailsBean) params.result;
                    appCircle = bean.getAppCircle();
                    topAppTopics = bean.getTopAppTopics();
                    initTopAdapterData(bean.getAppCircle());

                } else {

                }
            }
        });
    }

    //  获取话题列表

    public void requestTopicData() {

//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

        map.put("circleId", circleId);
        map.put("userId", userId);
        map.put("page", page + "");

        HtmlRequest.getTrainingCircleDetailsTopic(this, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if (params.result != null) {

                    ResultCircleDetailsTopicListBean bean = (ResultCircleDetailsTopicListBean) params.result;
                    if (bean.getAppTopics().size() == 0) {     //  无数据情况

                        if (page != 1) {      //  非首次的处理
                            page--;
                            myCircleAdapterDetails.changeMoreStatus(RecyclerBaseAapter.NO_LOAD_BLACK);
                        } else {
                            myCircleAdapterDetails.changeMoreStatus(RecyclerBaseAapter.NO_LOAD_MORE);
                        }

                    } else {


                        appTopics.addAll(bean.getAppTopics());
                        myCircleAdapterDetails.notifyDataSetChanged();
                        if (appTopics.size() % 10 == 0) {
                            myCircleAdapterDetails.changeMoreStatus(RecyclerBaseAapter.PULLUP_LOAD_MORE);
                        } else {
                            myCircleAdapterDetails.changeMoreStatus(RecyclerBaseAapter.NO_LOAD_BLACK);
                        }


                    }
                    swipeCircleDetails.setRefreshing(false);

                } else {

                }
            }
        });
    }

    //加入圈子
    public void requestAddCircle() {

//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

        map.put("circleId", circleId);
        map.put("userId", userId);

        HtmlRequest.getTrainingAddCircle(this, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if (params.result != null) {

                    ResultInfoBean bean = (ResultInfoBean) params.result;
                    if (bean.getFlag().equals("true")) {
                        initData();
                        Toast.makeText(TrainingCircleDetailsActivity.this, bean.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(TrainingCircleDetailsActivity.this, bean.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {

                }
            }
        });
    }

    //退出圈子
    public void requestOutCircle() {

//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

        map.put("circleId", circleId);
        map.put("userId", userId);

        HtmlRequest.getTrainingOutCircle(this, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if (params.result != null) {

                    ResultInfoBean bean = (ResultInfoBean) params.result;
                    if (bean.getFlag().equals("true")) {
                        initData();
                        Toast.makeText(TrainingCircleDetailsActivity.this, bean.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(TrainingCircleDetailsActivity.this, bean.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                } else {

                }
            }
        });
    }


    @OnClick({R.id.btn_training_cirlce_details, R.id.tv_circle_join,R.id.iv_back})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_training_cirlce_details:
                if(TextUtils.isEmpty(userId)){
                    Toast.makeText(TrainingCircleDetailsActivity.this, "请登录", Toast.LENGTH_SHORT).show();
                }else{
                    if(!PreferenceUtil.getCheckStatus().equals("success")){
                        Toast.makeText(TrainingCircleDetailsActivity.this, "请认证", Toast.LENGTH_SHORT).show();
                    }else{
                        if(status.equals("other")){
                            Toast.makeText(TrainingCircleDetailsActivity.this, "请您加入该圈子后在进行相关操作", Toast.LENGTH_SHORT).show();
                        }else{
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("circleId", circleId);
                            RlbActivityManager.toTrainingIssueTopicActivity(this, map, false);
                        }
                    }
                }

                break;
            case R.id.tv_circle_join:

                if (status.equals("mine")) {      //  我的圈子


                    HashMap<String, Object> setMap = new HashMap<String, Object>();
                    setMap.put("circleId", circleId);
                    setMap.put("isNeedAduit", appCircle.getIsNeedAduit());

                    RlbActivityManager.toTrainingSetAuthorityActivity(this, setMap, false);

                } else if (status.equals("join")) {        //  我加入的圈子   -----    退出

                    new AlertDialog.Builder(this)

                            .setMessage("确认退出“圈子名称”吗？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

//                                Intent i_recharge = new Intent();
//                                i_recharge.setClass(context, ReChargeActivity.class);
//                                context.startActivity(i_recharge);
//                                        requestCancelData();
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestOutCircle();
                                    dialog.dismiss();
                                }
                            })
                            .show();




                } else if (status.equals("other")) {       //  其他圈子   -----    加入

                    requestAddCircle();

                }


                break;

            case R.id.iv_back:

                finish();

                break;

            default:
                break;

        }

    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setVisibility(View.GONE);

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

    @Override
    public void onRefresh() {
        initData();
    }
}
