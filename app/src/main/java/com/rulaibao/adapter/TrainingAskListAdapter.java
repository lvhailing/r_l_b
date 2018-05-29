package com.rulaibao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rulaibao.R;
import com.rulaibao.adapter.holder.FooterViewHolder;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.ResultAskIndexItemBean;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.uitls.ImageLoaderManager;
import com.rulaibao.uitls.RlbActivityManager;
import com.rulaibao.uitls.ViewUtils;
import com.rulaibao.widget.CircularImage;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 研修--问答adapter
 */

public class TrainingAskListAdapter extends RecyclerBaseAapter<RecyclerView.ViewHolder> {
    private MouldList<ResultAskIndexItemBean> arrayList;
    private DisplayImageOptions displayImageOptions = ImageLoaderManager.initDisplayImageOptions(R.mipmap.img_default_photo,R.mipmap.img_default_photo,R.mipmap.img_default_photo);
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
                    footerViewHolder.tvFooterMore.setText("数据加载中...");
                    break;
                case LOADING_MORE:
                    footerViewHolder.tvFooterMore.setText("正加载更多...");
                    break;
                case NO_LOAD_MORE:
                    //隐藏加载更多
                    footerViewHolder.tvFooterMore.setVisibility(View.GONE);
                    break;
                case NO_LOAD_BLACK:
                    //隐藏加载更多  留空白
                    footerViewHolder.tvFooterMore.setText("");
                    ViewGroup.LayoutParams lp = footerViewHolder.tvFooterMore.getLayoutParams();
                    lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    lp.height= ViewUtils.dip2px(context,40);//lp.height=LayoutParams.WRAP_CONTENT;
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
            ImageLoader.getInstance().displayImage(arrayList.get(position).getAnswerPhoto(),holder1.ivTrainingAsk,displayImageOptions);


            String str1 = "答:";
            String str2 = str1 + arrayList.get(position).getAnswerContent();
            SpannableStringBuilder style = new SpannableStringBuilder(str2);
            style.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.txt_black1)), 0, str1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder1.tvTrainingAskAnswer.setText(style);


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
