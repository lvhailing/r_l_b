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
import com.rulaibao.adapter.holder.FooterViewHolder;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.ResultAskIndexItemBean;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.uitls.RlbActivityManager;
import com.rulaibao.widget.CircularImage;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 研修--问答adapter
 */

public class TrainingAskListAdapter extends RecyclerBaseAapter<RecyclerView.ViewHolder> {
    private MouldList<ResultAskIndexItemBean> arrayList;

    public TrainingAskListAdapter(Context context, MouldList<ResultAskIndexItemBean> arrayList) {
        super(context);
        this.arrayList = arrayList;
        layoutInflater = LayoutInflater.from(context);
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
        View view = layoutInflater.inflate(R.layout.activity_training_ask_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void initHolderData(RecyclerView.ViewHolder holder, final int position) {

        ViewHolder holder1 = (ViewHolder) holder;
        holder1.tvTrainingAskTitle.setText(arrayList.get(position).getTitle());
        holder1.tvTrainingAskCount.setText(arrayList.get(position).getAnswerCount()+"回答");
        holder1.tvTrainingAskTime.setText(arrayList.get(position).getTime());

        if(Integer.parseInt(arrayList.get(position).getAnswerCount())==0){
            holder1.ivTrainingAsk.setVisibility(View.GONE);
            holder1.tvTrainingAskAnswer.setVisibility(View.GONE);
            holder1.tvTrainingAskManager.setVisibility(View.GONE);
        }else{
            holder1.ivTrainingAsk.setVisibility(View.VISIBLE);
            holder1.tvTrainingAskAnswer.setVisibility(View.VISIBLE);
            holder1.tvTrainingAskManager.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(arrayList.get(position).getAnswerPhoto(),holder1.ivTrainingAsk);
            holder1.tvTrainingAskAnswer.setText(arrayList.get(position).getAnswerContent());
            holder1.tvTrainingAskManager.setText(arrayList.get(position).getAnswerName());
        }

        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> map = new HashMap<String,Object>();
                map.put("questionId",arrayList.get(position).getQuestionId());
                RlbActivityManager.toTrainingAskDetailsActivity((BaseActivity) context,map, false);
            }
        });

    }

    @Override
    public int getItem() {
        return arrayList.size() + 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_training_ask_title)
        TextView tvTrainingAskTitle;
        @BindView(R.id.tv_training_ask_count)
        TextView tvTrainingAskCount;
        @BindView(R.id.tv_training_ask_time)
        TextView tvTrainingAskTime;
        @BindView(R.id.iv_training_ask)
        CircularImage ivTrainingAsk;
        @BindView(R.id.tv_training_ask_manager)
        TextView tvTrainingAskManager;
        @BindView(R.id.tv_training_ask_answer)
        TextView tvTrainingAskAnswer;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}
