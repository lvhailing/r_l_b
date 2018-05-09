package com.rulaibao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rulaibao.R;
import com.rulaibao.activity.TrainingCircleDetailsActivity;
import com.rulaibao.adapter.holder.FooterViewHolder;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.ResultCircleDetailsTopicItemBean;
import com.rulaibao.bean.TestBean;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.uitls.RlbActivityManager;
import com.rulaibao.widget.CircularImage;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrainingMyCircleDetailsListAdapter extends RecyclerBaseAapter<RecyclerView.ViewHolder> {


    private MouldList<ResultCircleDetailsTopicItemBean> arrayList;
    private String circleId = "";

    public TrainingMyCircleDetailsListAdapter(Context context, MouldList<ResultCircleDetailsTopicItemBean> arrayList,String circleId) {
        super(context);
        this.arrayList = arrayList;
        this.circleId = circleId;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItem() {
        if(mHeaderView!=null){
            return arrayList.size() + 2;
        }else{
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
                    footerViewHolder.tvFooterMore.setText("上拉加载更多...");
                    break;
                case LOADING_MORE:
                    footerViewHolder.tvFooterMore.setText("正加载更多...");
                    break;
                case NO_LOAD_MORE:
                    //隐藏加载更多
                    footerViewHolder.tvFooterMore.setVisibility(View.GONE);
                    break;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder inflateItemView(ViewGroup parent) {

        View view = layoutInflater.inflate(R.layout.activity_my_circle_details_item, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void initHolderData(RecyclerView.ViewHolder holder, int position) {

        ViewHolder holder1 = (ViewHolder) holder;
        int index = position;
        if(getmHeaderView()!=null){
            index = position-1;
        }

        ImageLoader.getInstance().displayImage(arrayList.get(index).getCreatorPhoto(),holder1.ivTrainingCircleDetailsManager);

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

                RlbActivityManager.toTrainingTopicDetailsActivity((BaseActivity)context, map, false);
            }
        });

        if(arrayList.get(index).getLikeStatus().equals("yes")){
            holder1.ivCircleDetailsZan.setImageResource(R.mipmap.img_zaned_icon);
        }else {
            holder1.ivCircleDetailsZan.setImageResource(R.mipmap.img_zan_icon);
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder{
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

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
