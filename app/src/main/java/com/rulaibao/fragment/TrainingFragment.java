package com.rulaibao.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.rulaibao.R;
import com.rulaibao.activity.FindPasswordActivity;
import com.rulaibao.activity.LoginActivity;
import com.rulaibao.adapter.TrainingHotAskListAdapter;
import com.rulaibao.bean.ResultAskTypeBean;
import com.rulaibao.bean.ResultClassIndexBean;
import com.rulaibao.bean.ResultClassIndexQualityBean;
import com.rulaibao.bean.ResultCycleIndex2B;
import com.rulaibao.bean.ResultHotAskBean;
import com.rulaibao.bean.ResultHotAskItemBean;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.uitls.RlbActivityManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rulaibao.uitls.ImageUtils.getClassImgIndex;


/**
 * 研修
 * Created by junde on 2018/3/26.
 */

public class TrainingFragment extends BaseFragment implements TrainingHotAskListAdapter.LoadMoreData {


    @BindView(R.id.tv_training_class)
    TextView tvTrainingClass;       //  课程
    @BindView(R.id.tv_training_ask)
    TextView tvTrainingAsk;         //  问答
    @BindView(R.id.tv_training_circle)
    TextView tvTrainingCircle;      //  圈子
    @BindView(R.id.tv_training_promote)
    TextView tvTrainingPromote;     //  展业

    @BindView(R.id.tv_training_recommend_date)
    TextView tvTrainingRecommendDate;       //  课程推荐 时间
    @BindView(R.id.iv_training_recommend)
    ImageView ivTrainingRecommend;
    @BindView(R.id.ll_training_recommend)
    LinearLayout llTrainingRecommend;

    @BindView(R.id.tv_training_refresh)
    TextView tvTrainingRefresh;             //  精品课程  换一换
    @BindView(R.id.tv_training_boutique_first)
    TextView tvTrainingBoutiqueFirst;       //
    @BindView(R.id.iv_training_boutique_first)
    ImageView ivTrainingBoutiqueFirst;
    @BindView(R.id.tv_training_boutique_second)
    TextView tvTrainingBoutiqueSecond;
    @BindView(R.id.iv_training_boutique_second)
    ImageView ivTrainingBoutiqueSecond;
    @BindView(R.id.tv_training_boutique_third)
    TextView tvTrainingBoutiqueThird;
    @BindView(R.id.iv_training_boutique_third)
    ImageView ivTrainingBoutiqueThird;
    @BindView(R.id.tv_training_boutique_forth)
    TextView tvTrainingBoutiqueForth;
    @BindView(R.id.iv_training_boutique_forth)
    ImageView ivTrainingBoutiqueForth;
    @BindView(R.id.tv_training_recommend_manager)
    TextView tvTrainingRecommendManager;
    @BindView(R.id.tv_training_recommend_manager_name)
    TextView tvTrainingRecommendManagerName;
    @BindView(R.id.iv_training_refresh)
    ImageView ivTrainingRefresh;

    @BindView(R.id.lv_training_hot_ask)         //  热门回答
    RecyclerView lvTrainingHotAsk;

    @BindView(R.id.title_center)
    TextView titleCenter;           //  标题
    @BindView(R.id.rsv_fragment_training)
    NestedScrollView rsvFragmentTraining;

    private ArrayList<ResultCycleIndex2B> picList;
    private Animation mRefreshAnim;
    private Context context;

    private ArrayList arrayList = new ArrayList();
    private String string = "";
    private MouldList<ResultHotAskItemBean> list;

    private TrainingHotAskListAdapter adapter;
    private ResultClassIndexBean bean;
    private int hotPage = 1;
    private int classPage = 1;
    private ResultAskTypeBean typeBean;
    private int qualityCount = 1;        //  精品课程总页数

    /**
     * 绑定布局文件
     *
     * @param inflater           1
     * @param container          1
     * @param savedInstanceState 1
     * @return 1
     */
    @Override
    protected View attachLayoutRes(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_training, container, false);

        } else {
            if (mView.getParent() != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
        }
        return mView;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (context != null) {
                hotPage = 1;
                list.clear();
                requestIndexData();// 获取研修首页数据
                requestHotAskData();// 获取

                requestAskType();

            }

//            scrollView.smoothScrollTo(0, 0);
        } else {

        }

    }

    /**
     * 初始化参数、视图控件
     */
    @Override
    protected void initViews() {

        context = getActivity();

        picList = new ArrayList<ResultCycleIndex2B>();
        list = new MouldList<ResultHotAskItemBean>();
        bean = new ResultClassIndexBean();
        typeBean = new ResultAskTypeBean();

        titleCenter.setText(getString(R.string.training));

        mRefreshAnim = AnimationUtils.loadAnimation(context, R.anim.anim_rotate_refresh);
        setAdapterData();

    }

    public void setAdapterData() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                // 直接禁止垂直滑动
                return false;
            }
        };

        lvTrainingHotAsk.setLayoutManager(layoutManager);
        adapter = new TrainingHotAskListAdapter(getActivity(), list, this);
        lvTrainingHotAsk.setAdapter(adapter);

        //添加分割线
//        lvTrainingHotAsk.addItemDecoration(new RefreshItemDecoration(getActivity(),RefreshItemDecoration.VERTICAL_LIST));
//        lvTrainingHotAsk.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.shape_divider_training));

        initLoadMoreListener();

    }

    /**
     * ceshi
     */
    private void requestData() {

        LinkedHashMap<String, Object> param1 = new LinkedHashMap<>();
        param1.put("params", "params");

        HtmlRequest.getTest(context, param1, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params != null) {
                    if (params.result != null) {
                        MouldList<ResultCycleIndex2B> b = (MouldList<ResultCycleIndex2B>) params.result;
//                        picList = params.result;
                        Toast.makeText(context, "----" + b.get(0).getPicture(), Toast.LENGTH_SHORT).show();

                    }
                }
            }


        });

    }


    @OnClick({R.id.tv_training_class, R.id.tv_training_ask, R.id.tv_training_circle, R.id.tv_training_promote, R.id.iv_training_recommend, R.id.tv_training_refresh, R.id.iv_training_boutique_first, R.id.iv_training_boutique_second, R.id.iv_training_boutique_third, R.id.iv_training_boutique_forth})
    public void onclick(View view) {
        switch (view.getId()) {

            case R.id.tv_training_class:        //  课程

                RlbActivityManager.toTrainingClassActivity(getActivity(), false);

                break;

            case R.id.tv_training_ask:          // 问答
                HashMap<String, Object> map = new HashMap<>();
                map.put("type", typeBean.getList());

                RlbActivityManager.toTrainingAskActivity(getActivity(), map, false);

                break;

            case R.id.tv_training_circle:           //  圈子

                RlbActivityManager.toTrainingCircleActivity(getActivity(), false);

                break;

            case R.id.tv_training_promote:          // 展业

//                RlbActivityManager.toTrainingPromoteActivity(getActivity(), false);

                Intent i_login = new Intent(getActivity(), LoginActivity.class);
                startActivity(i_login);


                break;

            case R.id.iv_training_recommend:        //  推荐课程(跳转课程详情)
                HashMap<String, Object> classMap = new HashMap<>();
                classMap.put("id", bean.getCourseRecommend().getCourseId());
                classMap.put("speechmakeId", bean.getCourseRecommend().getSpeechmakeId());
                classMap.put("courseId", bean.getCourseRecommend().getCourseId());
                RlbActivityManager.toTrainingClassDetailsActivity(getActivity(), classMap, false);
                break;

            case R.id.tv_training_refresh:          //  精品课程 刷新（换一换）

                classPage = (classPage)%((qualityCount+3)/4)+1;
                startAnim();
                requestRefreshClassData();

                break;

            case R.id.iv_training_boutique_first:
                HashMap<String, Object> classFirstMap = new HashMap<>();
                classFirstMap.put("id", bean.getQualityCourseList().get(0).getCourseId());
                classFirstMap.put("speechmakeId", bean.getQualityCourseList().get(0).getSpeechmakeId());
                classFirstMap.put("courseId", bean.getQualityCourseList().get(0).getCourseId());
                RlbActivityManager.toTrainingClassDetailsActivity(getActivity(), classFirstMap, false);
                break;

            case R.id.iv_training_boutique_second:
                HashMap<String, Object> classSecondMap = new HashMap<>();
                classSecondMap.put("id", bean.getQualityCourseList().get(1).getCourseId());
                classSecondMap.put("speechmakeId", bean.getQualityCourseList().get(1).getSpeechmakeId());
                classSecondMap.put("courseId", bean.getQualityCourseList().get(1).getCourseId());
                RlbActivityManager.toTrainingClassDetailsActivity(getActivity(), classSecondMap, false);
                break;

            case R.id.iv_training_boutique_third:
                HashMap<String, Object> classThirdMap = new HashMap<>();
                classThirdMap.put("id", bean.getQualityCourseList().get(2).getCourseId());
                classThirdMap.put("speechmakeId", bean.getQualityCourseList().get(2).getSpeechmakeId());
                classThirdMap.put("courseId", bean.getQualityCourseList().get(2).getCourseId());
                RlbActivityManager.toTrainingClassDetailsActivity(getActivity(), classThirdMap, false);
                break;

            case R.id.iv_training_boutique_forth:
                HashMap<String, Object> classforthMap = new HashMap<>();
                classforthMap.put("id", bean.getQualityCourseList().get(3).getCourseId());
                classforthMap.put("speechmakeId", bean.getQualityCourseList().get(3).getSpeechmakeId());
                classforthMap.put("courseId", bean.getQualityCourseList().get(3).getCourseId());
                RlbActivityManager.toTrainingClassDetailsActivity(getActivity(), classforthMap, false);
                break;

            default:

                break;

        }

    }

    public void requestIndexData() {


//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

        HtmlRequest.getTrainingIndexClass(context, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if (params.result != null) {

                    bean = (ResultClassIndexBean) params.result;
                    qualityCount = bean.getCount();
                    setView();
//                    Toast.makeText(context,params.result.toString(),Toast.LENGTH_SHORT).show();

                } else {

                }

            }
        });

    }

    public void setView() {

        tvTrainingRecommendDate.setText(bean.getCourseRecommend().getCourseTime());
        ivTrainingRecommend.setImageResource(getClassImgIndex(bean.getCourseRecommend().getCourseLogo()));
        tvTrainingRecommendManager.setText(bean.getCourseRecommend().getCourseName());
        tvTrainingRecommendManagerName.setText(bean.getCourseRecommend().getSpeechmakeName() + "  " + bean.getCourseRecommend().getPosition());

        setQualityView();

    }

    public void setQualityView() {

        tvTrainingBoutiqueFirst.setText(bean.getQualityCourseList().get(0).getCourseName());
        ivTrainingBoutiqueFirst.setImageResource(getClassImgIndex(bean.getQualityCourseList().get(0).getCourseLogo()));

        tvTrainingBoutiqueSecond.setText(bean.getQualityCourseList().get(1).getCourseName());
        ivTrainingBoutiqueSecond.setImageResource(getClassImgIndex(bean.getQualityCourseList().get(1).getCourseLogo()));

        tvTrainingBoutiqueThird.setText(bean.getQualityCourseList().get(2).getCourseName());
        ivTrainingBoutiqueThird.setImageResource(getClassImgIndex(bean.getQualityCourseList().get(2).getCourseLogo()));

        tvTrainingBoutiqueForth.setText(bean.getQualityCourseList().get(3).getCourseName());
        ivTrainingBoutiqueForth.setImageResource(getClassImgIndex(bean.getQualityCourseList().get(3).getCourseLogo()));

    }

    //获取问答类型
    public void requestAskType() {


//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();


        HtmlRequest.getTrainingAskType(context, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if (params.result != null) {

                    typeBean = (ResultAskTypeBean) params.result;

//                    Toast.makeText(context,params.result.toString(),Toast.LENGTH_SHORT).show();

                } else {

                }

            }
        });


    }


    public void requestHotAskData() {


//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("page", hotPage + "");

        HtmlRequest.getTrainingHotAskClass(context, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if (params.result != null) {

                    ResultHotAskBean bean = (ResultHotAskBean) params.result;
//                    if(){}
                    if(bean.getList().size()==0 && hotPage!=1){     //  非首次的无数据情况
                        hotPage--;
                        adapter.changeMoreStatus(TrainingHotAskListAdapter.NO_LOAD_MORE);

                    }else{
                        adapter.changeMoreStatus(TrainingHotAskListAdapter.LOADING_MORE);
                        adapter.changeMoreStatus(TrainingHotAskListAdapter.PULLUP_LOAD_MORE);

                        list.addAll(bean.getList());
                    }

                    adapter.notifyDataSetChanged();

//                    Toast.makeText(context,bean.getList().get(0).getTitle(),Toast.LENGTH_SHORT).show();

                } else {

                }

            }
        });


    }


    public void requestRefreshClassData() {


//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("page", classPage + "");


        HtmlRequest.getTrainingRefreshClass(context, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                stopAnim();
                if (params.result != null) {
                    ResultClassIndexBean qualityBean = (ResultClassIndexBean) params.result;
//                    setQualityView();
//                    bean.getCount();
                    if(qualityBean.getQualityCourseList().size()==4){
                        bean.setQualityCourseList(qualityBean.getQualityCourseList());
                        setQualityView();
                    }


                } else {

                }

            }
        });

    }


    public void stopAnim() {
        mRefreshAnim.reset();
        ivTrainingRefresh.clearAnimation();
        ivTrainingRefresh.setBackgroundResource(R.mipmap.img_training_refresh);
    }

    public void startAnim() {
        mRefreshAnim.reset();
        ivTrainingRefresh.clearAnimation();
        ivTrainingRefresh.setBackgroundResource(R.mipmap.img_training_refresh);
        ivTrainingRefresh.startAnimation(mRefreshAnim);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    public void getMoreData() {

        hotPage++;
        requestHotAskData();

    }

    public void initLoadMoreListener() {

        rsvFragmentTraining.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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
                    hotPage++;
                    requestHotAskData();

                }
            }
        });

    }
}
