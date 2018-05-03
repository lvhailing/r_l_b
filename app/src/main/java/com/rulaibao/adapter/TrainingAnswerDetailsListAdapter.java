package com.rulaibao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rulaibao.R;
import com.rulaibao.adapter.holder.FooterViewHolder;
import com.rulaibao.bean.ResultCircleDetailsTopicCommentItemBean;
import com.rulaibao.bean.ResultCircleDetailsTopicCommentReplyItemBean;
import com.rulaibao.bean.TestBean;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.widget.CircularImage;
import com.rulaibao.widget.MyListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 话题详情  问题详情adapter
 */

public class TrainingAnswerDetailsListAdapter extends RecyclerBaseAapter<RecyclerView.ViewHolder> {

    private MouldList<ResultCircleDetailsTopicCommentItemBean> arrayList;
    private Reply reply;
    private int index = 0;
    private ResultCircleDetailsTopicCommentReplyItemBean replyItemBean;

    public TrainingAnswerDetailsListAdapter(Context context, MouldList<ResultCircleDetailsTopicCommentItemBean> arrayList,Reply reply) {
        super(context);
        this.context = context;
        this.arrayList = arrayList;
        this.reply = reply;
    }


    @Override
    public RecyclerView.ViewHolder inflateItemView(ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.activity_training_answer_details_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
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
    public void initHolderData(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        index = position;
        if(getmHeaderView()!=null){
            index = position-1;
        }
//        holder1.tvAnswerDetailsName.setText(arrayList.get(index).getTitle());


        ImageLoader.getInstance().displayImage(arrayList.get(index).getCommentPhoto(),holder1.ivAnswerDetails);
        holder1.tvAnswerDetailsName.setText(arrayList.get(index).getCommentName());
        holder1.tvAnswerDetailsDate.setText(arrayList.get(index).getCommentTime());
        holder1.tvAnswerDetailsContent.setText(arrayList.get(index).getCommentContent());

        holder1.lvAnswerDetails.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return arrayList.get(index).getReplys().size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ChildViewHolder holder1 = null;
                if (convertView == null) {
                    holder1 = new ChildViewHolder();
                    convertView = layoutInflater.inflate(R.layout.activity_training_answer_details_item_child, null);
                    holder1.tvCommit = (TextView) convertView.findViewById(R.id.tv_commit);
                    convertView.setTag(holder1);
                } else {

                    holder1 = (ChildViewHolder) convertView.getTag();

                }

                replyItemBean = arrayList.get(index).getReplys().get(position);


                holder1.tvCommit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reply.reply(arrayList.get(index).getCid(),replyItemBean.getReplyId(),replyItemBean.getReplyName(),index);
                    }
                });

                String str = "";
                if(replyItemBean.getReplyId().equals(replyItemBean.getReplyToId())){
                    str = replyItemBean.getReplyName()+"："+replyItemBean.getReplyContent();
                }else{
                    str = replyItemBean.getReplyName()+"回复"+replyItemBean.getReplyToName()+"："+replyItemBean.getReplyContent();
                }

                holder1.tvCommit.setText(str);
                return convertView;
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
        @BindView(R.id.iv_answer_details)
        CircularImage ivAnswerDetails;
        @BindView(R.id.tv_answer_details_name)
        TextView tvAnswerDetailsName;
        @BindView(R.id.tv_answer_details_date)
        TextView tvAnswerDetailsDate;
        @BindView(R.id.tv_answer_details_content)
        TextView tvAnswerDetailsContent;
        @BindView(R.id.tv_answer_details_reply)
        TextView tvAnswerDetailsReply;
        @BindView(R.id.lv_answer_details)
        MyListView lvAnswerDetails;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            tvAnswerDetailsReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reply.reply(arrayList.get(index).getCid(),arrayList.get(index).getCommentId(),arrayList.get(index).getCommentName(),index);
                }
            });
        }
    }

    public interface Reply{

        public void reply(String commentId,String toUserId,String replyToName,int index);

    }

    class ChildViewHolder {

        TextView tvCommit;




    }
}
