package com.rulaibao.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rulaibao.R;
import com.rulaibao.activity.TrainingAskActivity;
import com.rulaibao.activity.TrainingTopicDetailsActivity;
import com.rulaibao.adapter.holder.FooterViewHolder;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.ResultCircleDetailsTopicItemBean;
import com.rulaibao.bean.ResultInfoBean;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.uitls.ImageLoaderManager;
import com.rulaibao.uitls.PreferenceUtil;
import com.rulaibao.uitls.RlbActivityManager;
import com.rulaibao.uitls.ViewUtils;
import com.rulaibao.uitls.encrypt.DESUtil;
import com.rulaibao.widget.CircularImage;

import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 圈子详情--话题列表 adapter
 */

public class TrainingMyCircleDetailsListAdapter extends RecyclerBaseAapter<RecyclerView.ViewHolder> {



    private MouldList<ResultCircleDetailsTopicItemBean> arrayList;
    private String circleId = "";
    private DisplayImageOptions displayImageOptions = ImageLoaderManager.initDisplayImageOptions(R.mipmap.ic_ask_photo_list_default,R.mipmap.ic_ask_photo_list_default,R.mipmap.ic_ask_photo_list_default);
    private String userId = null;
    private boolean isJoin = false;

    public void setJoin(boolean join) {
        isJoin = join;
    }

    public TrainingMyCircleDetailsListAdapter(Context context, MouldList<ResultCircleDetailsTopicItemBean> arrayList, String circleId, boolean isJoin) {
        super(context);
        this.arrayList = arrayList;
        this.circleId = circleId;
        this.isJoin = isJoin;
        layoutInflater = LayoutInflater.from(context);


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItem() {
        if (mHeaderView != null) {
            return arrayList.size() + 2;
        } else {
            return arrayList.size() + 1;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ViewHolder) {
            initHolderData(holder, position);
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            switch (mLoadMoreStatus) {
                case PULLUP_LOAD_MORE:
                    ViewGroup.LayoutParams params1 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    footerViewHolder.itemView.setLayoutParams(params1);
                    footerViewHolder.ivHotAskFooter.setVisibility(View.GONE);
                    footerViewHolder.tvFooterMore.setText("数据加载中...");
                    break;
                case LOADING_MORE:
                    ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    footerViewHolder.itemView.setLayoutParams(params2);
                    footerViewHolder.ivHotAskFooter.setVisibility(View.GONE);
                    footerViewHolder.tvFooterMore.setText("正加载更多...");
                    break;
                case NO_LOAD_MORE:
                    //隐藏加载更多
                    footerViewHolder.tvFooterMore.setVisibility(View.GONE);
                    break;
                case NO_LOAD_BLACK:
                    ViewGroup.LayoutParams params3 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    footerViewHolder.itemView.setLayoutParams(params3);
                    //隐藏加载更多  留空白
                    footerViewHolder.tvFooterMore.setText("");
                    footerViewHolder.ivHotAskFooter.setVisibility(View.GONE);
                    ViewGroup.LayoutParams lp = footerViewHolder.tvFooterMore.getLayoutParams();
                    lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    lp.height = ViewUtils.dip2px(context, 40);//lp.height=LayoutParams.WRAP_CONTENT;
                    footerViewHolder.tvFooterMore.setLayoutParams(lp);
//                    footerViewHolder.tvFooterMore.setVisibility(View.GONE);
                    break;
                case NO_DATA:
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    footerViewHolder.itemView.setLayoutParams(params);

                    //没有数据
                    footerViewHolder.tvFooterMore.setVisibility(View.VISIBLE);
                    footerViewHolder.ivHotAskFooter.setVisibility(View.VISIBLE);
                    footerViewHolder.tvFooterMore.setText(noDataMessage);
                    break;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder inflateItemView(ViewGroup parent) {

        View view = layoutInflater.inflate(R.layout.activity_my_circle_details_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void initHolderData(RecyclerView.ViewHolder holder, int position) {

        final ViewHolder holder1 = (ViewHolder) holder;
        int index = position;
        if (getmHeaderView() != null) {
            index = position - 1;
        }

        ImageLoader.getInstance().displayImage(arrayList.get(index).getCreatorPhoto(), holder1.ivTrainingCircleDetailsManager,displayImageOptions);

        holder1.tvTrainingCircleDetailsManagerName.setText(arrayList.get(index).getCreatorName());
        holder1.tvCircleDetailsContent.setText(arrayList.get(index).getTopicContent());
        holder1.tvCircleDetailsTime.setText(arrayList.get(index).getCreateTime());
        holder1.tvCircleDetailsMessageCount.setText(arrayList.get(index).getCommentCount());
        holder1.tvCircleDetailsZanCount.setText(arrayList.get(index).getLikeCount());

        final int finalIndex = index;
        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("appTopicId", arrayList.get(finalIndex).getTopicId());
                map.put("circleId", circleId);

                RlbActivityManager.toTrainingTopicDetailsActivity((BaseActivity) context, map, false);
            }
        });

        if (arrayList.get(index).getLikeStatus().equals("yes")) {
            holder1.ivCircleDetailsZan.setImageResource(R.mipmap.img_zaned_icon);
        } else {
            holder1.ivCircleDetailsZan.setImageResource(R.mipmap.img_zan_icon);
        }

        final int finalIndex1 = index;
        holder1.llCircleDetailsZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    RlbActivityManager.toTrainingAnswerDetailsActivity((BaseActivity)context,false);

                if(!PreferenceUtil.isLogin()){
                    HashMap<String,Object> map = new HashMap<>();


                    RlbActivityManager.toLoginActivity((BaseActivity)context,map,false);


                }else{
                    if(!PreferenceUtil.getCheckStatus().equals("success")){
                        ViewUtils.showToSaleCertificationDialog(context,"您还未认证，是否去认证");


                    }else{

                        if (arrayList.get(finalIndex).getLikeStatus().equals("no")) {

                            if(isJoin){
                                Toast.makeText(context, "请您加入该圈子后再进行相关操作", Toast.LENGTH_SHORT).show();
                            }else{
                                requestLikeData(arrayList.get(finalIndex).getTopicId(), finalIndex);
                                holder1.llCircleDetailsZan.setClickable(false);
                            }


                        }

                    }
                }



//                Toast.makeText(context,"别点我",Toast.LENGTH_SHORT).show();
            }
        });


    }


    //点赞
    public void requestLikeData(String appTopicId, final int index) {



        //        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();


        try {
            userId = DESUtil.decrypt(PreferenceUtil.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        map.put("appTopicId", appTopicId);      //  话题id
        map.put("userId", userId);
//        map.put("likeStatus", likeStatus);

        HtmlRequest.getTrainingCircleZan(context, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if (params.result != null) {

                    ResultInfoBean bean = (ResultInfoBean) params.result;
                    if(bean.getFlag().equals("true")){
                        arrayList.get(index).setLikeStatus("yes");
                        int count = Integer.parseInt(arrayList.get(index).getLikeCount());
                        arrayList.get(index).setLikeCount((count+1)+"");
                        notifyDataSetChanged();
                    }else{
                        Toast.makeText(context,bean.getMessage(),Toast.LENGTH_SHORT).show();
                    }


                } else {

                }
            }
        });
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_training_circle_details_manager)
        CircularImage ivTrainingCircleDetailsManager;
        @BindView(R.id.tv_training_circle_details_manager_name)
        TextView tvTrainingCircleDetailsManagerName;
        @BindView(R.id.tv_circle_details_content)
        TextView tvCircleDetailsContent;
        @BindView(R.id.tv_circle_details_time)
        TextView tvCircleDetailsTime;
        @BindView(R.id.tv_circle_details_message_count)
        TextView tvCircleDetailsMessageCount;
        @BindView(R.id.iv_circle_details_zan)
        ImageView ivCircleDetailsZan;
        @BindView(R.id.tv_circle_details_zan_count)
        TextView tvCircleDetailsZanCount;
        @BindView(R.id.ll_circle_details_zan)
        LinearLayout llCircleDetailsZan;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
