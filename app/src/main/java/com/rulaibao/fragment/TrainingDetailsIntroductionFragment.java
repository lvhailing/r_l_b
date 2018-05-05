package com.rulaibao.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rulaibao.R;
import com.rulaibao.bean.ResultClassDetailsIntroductionBean;
import com.rulaibao.bean.ResultClassDetailsIntroductionItemBean;
import com.rulaibao.bean.ResultInfoBean;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.widget.CircularImage;
import com.rulaibao.widget.ViewPagerForScrollView;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * 课程详情 简介栏
 *
 */

@SuppressLint("ValidFragment")
public class TrainingDetailsIntroductionFragment extends BaseFragment {

    @BindView(R.id.iv_introduction)
    CircularImage ivIntroduction;
    @BindView(R.id.tv_introduction_manager)
    TextView tvIntroductionManager;
    @BindView(R.id.tv_introduction_manager_name)
    TextView tvIntroductionManagerName;
    @BindView(R.id.tv_introduction_class_name)
    TextView tvIntroductionClassName;
    @BindView(R.id.tv_introduction_class_time)
    TextView tvIntroductionClassTime;
    @BindView(R.id.tv_introduction_class_type)
    TextView tvIntroductionClassType;
    @BindView(R.id.tv_introduction_content)
    TextView tvIntroductionContent;

    private String id = "";
    private ResultClassDetailsIntroductionItemBean course;
    private Context context;
    private ViewPagerForScrollView vp;


    public TrainingDetailsIntroductionFragment(ViewPagerForScrollView vp) {
        this.vp = vp;
    }

    @Override
    protected View attachLayoutRes(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_training_details_introduction, container, false);
            vp.setObjectForPosition(mView,0);
        } else {
            if (mView.getParent() != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
        }
        return mView;
    }

    @Override
    protected void initViews() {

        context = getActivity();
        id = getArguments().getString("id");
        course = new ResultClassDetailsIntroductionItemBean();
//        course = (ResultClassDetailsIntroductionItemBean)getArguments().getSerializable("course");
        requestData();
//

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            if(context!=null){

            }

//            scrollView.smoothScrollTo(0, 0);
        } else {

        }

    }



    public void requestData() {

//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

        map.put("id", id);      //  课程id

        HtmlRequest.getClassDetailsDesc(context, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if (params.result != null) {

                    ResultClassDetailsIntroductionBean bean = (ResultClassDetailsIntroductionBean) params.result;
                    course = bean.getCourse();
                    setView();
                } else {

                }
            }
        });
    }

    public void setView(){

        ImageLoader.getInstance().displayImage(course.getHeadPhoto(),ivIntroduction);
        tvIntroductionManager.setText(course.getPosition());
        tvIntroductionManagerName.setText(course.getRealName());
        tvIntroductionClassName.setText(course.getCourseName());
        tvIntroductionClassTime.setText(course.getCourseTime());
        tvIntroductionClassType.setText(course.getTypeName());
        tvIntroductionContent.setText(course.getCourseContent());

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

}
