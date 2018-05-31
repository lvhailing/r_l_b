package com.rulaibao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rulaibao.R;
import com.rulaibao.adapter.holder.FooterViewHolder;
import com.rulaibao.bean.ResultCircleDetailsTopicCommentItemBean;
import com.rulaibao.bean.ResultCircleDetailsTopicCommentReplyItemBean;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.uitls.ImageLoaderManager;
import com.rulaibao.widget.CircularImage;
import com.rulaibao.widget.MyListView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 话题详情  问题详情 adapter
 */

public class TrainingAnswerDetailsListAdapter extends RecyclerBaseAapter<RecyclerView.ViewHolder> {


    private MouldList<ResultCircleDetailsTopicCommentItemBean> arrayList;
    private Reply reply;
    private int index = 0;
    private ResultCircleDetailsTopicCommentReplyItemBean replyItemBean;
    private ReplyAdapter replyAdapter;
    private DisplayImageOptions displayImageOptions = ImageLoaderManager.initDisplayImageOptions(R.mipmap.img_default_photo, R.mipmap.img_default_photo, R.mipmap.img_default_photo);

    public TrainingAnswerDetailsListAdapter(Context context, MouldList<ResultCircleDetailsTopicCommentItemBean> arrayList, Reply reply) {
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
                    ViewGroup.LayoutParams params3 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    footerViewHolder.itemView.setLayoutParams(params3);
                    //隐藏加载更多
                    footerViewHolder.ivHotAskFooter.setVisibility(View.GONE);
                    footerViewHolder.tvFooterMore.setVisibility(View.GONE);
                    break;

                case NO_DATA:
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    footerViewHolder.itemView.setPadding(0, 50, 0, 0);
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
    public void initHolderData(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        int index = position;
        if (getmHeaderView() != null) {
            index = position - 1;
        }
//        holder1.tvAnswerDetailsName.setText(arrayList.get(index).getTitle());


        ImageLoader.getInstance().displayImage(arrayList.get(index).getCommentPhoto(), holder1.ivAnswerDetails, displayImageOptions);

        holder1.tvAnswerDetailsName.setText(arrayList.get(index).getCommentName());
        holder1.tvAnswerDetailsDate.setText(arrayList.get(index).getCommentTime());
        holder1.tvAnswerDetailsContent.setText(arrayList.get(index).getCommentContent());

        replyAdapter = new ReplyAdapter(context, index);
        replyAdapter.clearAll();
        replyAdapter.addAll(arrayList.get(index).getReplys());

        if ((index + 1) == arrayList.size()) {
            holder1.tvAnswerDetailasLine.setVisibility(View.INVISIBLE);
        } else {
            holder1.tvAnswerDetailasLine.setVisibility(View.VISIBLE);
        }
        holder1.lvAnswerDetails.setAdapter(replyAdapter);

        final int finalIndex = index;
        //  回复
        holder1.tvAnswerDetailsReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reply.reply(arrayList.get(finalIndex).getCid(), arrayList.get(finalIndex).getCommentId(), arrayList.get(finalIndex).getCommentName(), finalIndex, arrayList.get(finalIndex).getCid());

            }
        });

    }

    @Override
    public int getItem() {
        if (mHeaderView != null) {
            return arrayList.size() + 2;
        } else {
            return arrayList.size() + 1;
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.tv_answer_detailas_line)
        TextView tvAnswerDetailasLine;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    public interface Reply {

        public void reply(String commentId, String toUserId, String replyToName, int index, String linkId);

    }

    class ChildViewHolder {

        TextView tvCommit;

    }

    public void refresh() {
//        replyAdapter.notifyDataSetChanged();
    }

    class ReplyAdapter extends BaseAdapter {

        private Context context;
        private MouldList<ResultCircleDetailsTopicCommentReplyItemBean> list;
        private int index = 0;


        public ReplyAdapter(Context context, int index) {
            this.context = context;
            list = new MouldList<ResultCircleDetailsTopicCommentReplyItemBean>();
            this.index = index;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        public void addAll(MouldList<ResultCircleDetailsTopicCommentReplyItemBean> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public void clearAll() {
            this.list.clear();
            notifyDataSetChanged();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ChildViewHolder holder1 = null;
            if (convertView == null) {
                holder1 = new ChildViewHolder();
                convertView = layoutInflater.inflate(R.layout.activity_training_answer_details_item_child, null);
                holder1.tvCommit = (TextView) convertView.findViewById(R.id.tv_commit);
                convertView.setTag(holder1);
            } else {

                holder1 = (ChildViewHolder) convertView.getTag();

            }

            replyItemBean = list.get(position);
            holder1.tvCommit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (replyItemBean.getReplyId().equals(replyItemBean.getReplyToId())) {
//
//                        reply.reply(arrayList.get(index).getCid(), list.get(position).getReplyId(), list.get(position).getReplyName(), index, arrayList.get(index).getCid());
//
//                    } else {

                        reply.reply(arrayList.get(index).getCid(), list.get(position).getReplyId(), list.get(position).getReplyName(), index, list.get(position).getRid());

//                    }
                }
            });

            String str = "";
            if (replyItemBean.getReplyId().equals(replyItemBean.getReplyToId())) {
                str = replyItemBean.getReplyName() + "：" + replyItemBean.getReplyContent();
                String str2 = replyItemBean.getReplyName() + "：";

                SpannableStringBuilder style = new SpannableStringBuilder(str);

                style.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.txt_black1)), 0, replyItemBean.getReplyName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder1.tvCommit.setText(style);
            } else {
                str = replyItemBean.getReplyName() + " 回复 " + replyItemBean.getReplyToName() + "：" + replyItemBean.getReplyContent();

                String str2 = replyItemBean.getReplyName() + " 回复 ";
                String str3 = str2 + replyItemBean.getReplyToName();

                SpannableStringBuilder style = new SpannableStringBuilder(str);

                style.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.txt_black1)), 0, replyItemBean.getReplyName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                style.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.txt_black1)), str2.length(), str3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder1.tvCommit.setText(style);
            }

            return convertView;
        }

    }
}
