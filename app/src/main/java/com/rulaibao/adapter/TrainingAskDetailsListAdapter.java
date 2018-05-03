package com.rulaibao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rulaibao.R;
import com.rulaibao.adapter.holder.FooterViewHolder;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.ResultAskDetailsAnswerItemBean;
import com.rulaibao.bean.TestBean;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.uitls.RlbActivityManager;
import com.rulaibao.widget.CircularImage;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrainingAskDetailsListAdapter extends RecyclerBaseAapter<RecyclerView.ViewHolder> {

    private MouldList<ResultAskDetailsAnswerItemBean> arrayList;
    private String questionId = "";

    public TrainingAskDetailsListAdapter(Context context, MouldList<ResultAskDetailsAnswerItemBean> arrayList,String questionId) {
        super(context);
        this.arrayList = arrayList;
        this.questionId = questionId;
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

    public ViewHolder inflateItemView(ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.activity_training_ask_details_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void initHolderData(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        int index = position;
        if(getmHeaderView()!=null){
            index = position-1;
        }
        holder1.tvTrainingAskDetailsManagerName.setText(arrayList.get(index).getAnswerName());
        ImageLoader.getInstance().displayImage(arrayList.get(index).getAnswerPhoto(),holder1.ivTrainingAskDetailsManager);
        holder1.tvAskDetailsContent.setText(arrayList.get(index).getAnswerContent());
        holder1.tvAskDetailsTime.setText(arrayList.get(index).getAnswerTime());
        holder1.tvAskDetailsMessageCount.setText(arrayList.get(index).getCommentCount());
        holder1.tvAskDetailsZanCount.setText(arrayList.get(index).getLikeCount());
        if(arrayList.get(index).getLikeStatus().equals("yes")){
            holder1.ivAskDetailsZan.setImageResource(R.mipmap.img_zaned_icon);
        }else{
            holder1.ivAskDetailsZan.setImageResource(R.mipmap.img_zan_icon);
        }

        final int finalIndex = index;
        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> map = new HashMap<>();
                map.put("questionId",questionId);
                map.put("answerId",arrayList.get(finalIndex).getAnswerId());
                RlbActivityManager.toTrainingAnswerDetailsActivity((BaseActivity)context,map,false);
            }
        });



    }

    @Override
    public int getItem() {
        if(mHeaderView!=null){
            return arrayList.size() + 2;
        }else{
            return arrayList.size() + 1;
        }


    }


    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_training_ask_details_manager)
        CircularImage ivTrainingAskDetailsManager;
        @BindView(R.id.tv_training_ask_details_manager_name)
        TextView tvTrainingAskDetailsManagerName;
        @BindView(R.id.tv_ask_details_content)
        TextView tvAskDetailsContent;
        @BindView(R.id.tv_ask_details_time)
        TextView tvAskDetailsTime;
        @BindView(R.id.tv_ask_details_message_count)
        TextView tvAskDetailsMessageCount;
        @BindView(R.id.iv_ask_details_zan)
        ImageView ivAskDetailsZan;
        @BindView(R.id.tv_ask_details_zan_count)
        TextView tvAskDetailsZanCount;
        @BindView(R.id.ll_ask_details_message)
        LinearLayout llAskDetailsMessage;
        @BindView(R.id.ll_ask_details_zan)
        LinearLayout llAskDetailsZan;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


            llAskDetailsMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            llAskDetailsZan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    RlbActivityManager.toTrainingAnswerDetailsActivity((BaseActivity)context,false);
                    Toast.makeText(context,"别点我",Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
