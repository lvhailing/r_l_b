package com.rulaibao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rulaibao.R;
import com.rulaibao.bean.ResultClassDetailsDiscussBean;
import com.rulaibao.bean.ResultClassDetailsDiscussItemBean;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.uitls.ImageLoaderManager;
import com.rulaibao.uitls.PreferenceUtil;
import com.rulaibao.uitls.encrypt.DESUtil;
import com.rulaibao.widget.CircularImage;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * 课程详情--研讨列表adapter
 *
 */

public class TrainingClassDiscussAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private MouldList<ResultClassDetailsDiscussItemBean> arrayList;
    private LayoutInflater layoutInflater;

    private static final int TYPE_ITEM = 0;     // item布局
    private static final int TYPE_FOOTER = 1;   //  footer布局
    private DisplayImageOptions displayImageOptions = ImageLoaderManager.initDisplayImageOptions(R.mipmap.ic_ask_photo_list_default,R.mipmap.ic_ask_photo_list_default,R.mipmap.ic_ask_photo_list_default);

    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //没有加载更多 隐藏
    public static final int NO_LOAD_MORE = 2;

    //上拉加载更多状态-默认为0
    private int mLoadMoreStatus = 0;

    private DiscussReply discussReply;
    private String speechmakeId = "";
    private String userId = "";


    public TrainingClassDiscussAdapter(Context context, MouldList<ResultClassDetailsDiscussItemBean> arrayList,DiscussReply discussReply,String speechmakeId) {
        this.context = context;
        this.arrayList = arrayList;
        layoutInflater = LayoutInflater.from(context);
        this.discussReply = discussReply;
        this.speechmakeId = speechmakeId;
        try {
            userId = DESUtil.decrypt(PreferenceUtil.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            // 填充布局

            View view = layoutInflater.inflate(R.layout.activity_training_class_discuss_item, parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        } else if (viewType == TYPE_FOOTER) {
            View view = layoutInflater.inflate(R.layout.activity_training_hot_ask_footer, parent, false);
            FooterViewHolder holder = new FooterViewHolder(view);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {

            ViewHolder holder1 = (ViewHolder) holder;

            holder1.tvTrainingDiscussName.setText(arrayList.get(position).getCommentName());
            ImageLoader.getInstance().displayImage(arrayList.get(position).getCommentPhoto(),holder1.ivTrainingDiscuss,displayImageOptions);
            holder1.tvTrainingDiscussDate.setText(arrayList.get(position).getCommentTime());

            holder1.tvTrainingDiscussContent.setText(arrayList.get(position).getCommentContent());

            if(arrayList.get(position).getReplys()==null||arrayList.get(position).getReplys().size()==0){

                holder1.tvTrainingDiscussRepayContent.setVisibility(View.GONE);
                if(speechmakeId.equals(userId)){
                    holder1.tvDiscussReply.setVisibility(View.VISIBLE);

                    holder1.tvDiscussReply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            discussReply.reply(arrayList.get(position).getCommentId(),arrayList.get(position).getCid(),arrayList.get(position).getCommentName(),position);
                        }
                    });
                }

            }else{
                holder1.tvDiscussReply.setVisibility(View.GONE);
                holder1.tvTrainingDiscussRepayContent.setVisibility(View.VISIBLE);
                holder1.tvTrainingDiscussRepayContent.setText(arrayList.get(position).getReplys().get(0).getReplyName()+" 回复 "+arrayList.get(position).getReplys().get(0).getReplyContent());
            }


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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return arrayList.size()+1;
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_training_discuss)
        CircularImage ivTrainingDiscuss;
        @BindView(R.id.tv_training_discuss_name)
        TextView tvTrainingDiscussName;
        @BindView(R.id.tv_training_discuss_date)
        TextView tvTrainingDiscussDate;
        @BindView(R.id.tv_training_discuss_content)
        TextView tvTrainingDiscussContent;
        @BindView(R.id.tv_discuss_reply)
        TextView tvDiscussReply;
        @BindView(R.id.tv_training_discuss_repay_content)
        TextView tvTrainingDiscussRepayContent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface DiscussReply{

        public void reply(String toUserId,String commentId,String commentName,int index);

    }



    @Override
    public int getItemViewType(int position) {

        if (position + 1 == getItemCount()) {
            //最后一个item设置为footerView
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_footer_more)
        TextView tvFooterMore;

        public FooterViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }

    }


    /**
     * 更新加载更多状态
     *
     * @param status
     */
    public void changeMoreStatus(int status) {
        mLoadMoreStatus = status;
        notifyDataSetChanged();
    }

}
