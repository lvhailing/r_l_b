package com.rulaibao.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rulaibao.R;
import com.rulaibao.activity.TransactionDetailActivity;
import com.rulaibao.bean.NewMembersCircleList2B;
import com.rulaibao.network.types.MouldList;


/**
 * 圈子新成员  Adapter 类
 */
public class NewMembersCircleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final MouldList<NewMembersCircleList2B> list;
    Context mContext;
    LayoutInflater mInflater;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //没有加载更多 隐藏
    public static final int NO_LOAD_MORE = 2;

    //上拉加载更多状态-默认为0
    private int mLoadMoreStatus = 0;


    public NewMembersCircleAdapter(Context context, MouldList<NewMembersCircleList2B> list) {
        mContext = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View itemView = mInflater.inflate(R.layout.item_new_members_circle, parent, false);

            return new ItemViewHolder(itemView);
        } else if (viewType == TYPE_FOOTER) {
            View itemView = mInflater.inflate(R.layout.load_more_footview_layout, parent, false);

            return new FooterViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.tv_applicant_name.setText(list.get(position).getApplyUserName());
            String status = list.get(position).getAuditStatus();
            if ("submit".equals(status)) {
                itemViewHolder.tv_status.setText("待加入");
            } else if ("agree".equals(status)) {
                itemViewHolder.tv_status.setText("已加入");
                itemViewHolder.tv_status.setBackgroundResource(R.drawable.shape_non_clickable);
            } else if ("refuse".equals(status)) {
                itemViewHolder.tv_status.setText("已拒绝");
            }
            itemViewHolder.tv_circle_name.setText(list.get(position).getApplyCircleName());

            // 加载图片
            ImageLoader.getInstance().displayImage(list.get(position).getApplyPhoto(), itemViewHolder.iv_circle_photo);
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;

            switch (mLoadMoreStatus) {
                case PULLUP_LOAD_MORE:
                    footerViewHolder.tvLoadText.setText("上拉加载更多...");
                    break;
                case LOADING_MORE:
                    footerViewHolder.tvLoadText.setText("正加载更多...");
                    break;
                case NO_LOAD_MORE:
                    //隐藏加载更多
                    footerViewHolder.loadLayout.setVisibility(View.GONE);
                    break;

            }
        }

    }

    @Override
    public int getItemCount() {
        //RecyclerView的count设置为数据总条数+ 1（footerView）
        return list.size() + 1;
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

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iv_circle_photo; // 圈子头像
        private final TextView tv_applicant_name; // 申请人姓名
        private final TextView tv_circle_name; // 圈子名
        private final Button tv_status; // 状态

        public ItemViewHolder(View itemView) {
            super(itemView);
            iv_circle_photo = (ImageView) itemView.findViewById(R.id.iv_circle_photo);
            tv_applicant_name = (TextView) itemView.findViewById(R.id.tv_applicant_name);
            tv_circle_name = (TextView) itemView.findViewById(R.id.tv_circle_name);
            tv_status = (Button) itemView.findViewById(R.id.tv_status);

            initListener(itemView);
        }

        /**
         * item 点击监听
         *
         * @param itemView
         */
        private void initListener(View itemView) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // 跳转到交易明细
//                    Toast.makeText(mContext, "poistion " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(mContext, TransactionDetailActivity.class);
//                    mContext.startActivity(intent);
                }
            });
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        private final ProgressBar pbLoad;
        private final TextView tvLoadText;
        private final LinearLayout loadLayout;

        public FooterViewHolder(View itemView) {
            super(itemView);

            pbLoad = (ProgressBar) itemView.findViewById(R.id.pbLoad);
            tvLoadText = (TextView) itemView.findViewById(R.id.tvLoadText);
            loadLayout = (LinearLayout) itemView.findViewById(R.id.loadLayout);
        }
    }


    public void AddHeaderItem(MouldList<NewMembersCircleList2B> items) {
        list.addAll(0, items);
        notifyDataSetChanged();
    }

    public void AddFooterItem(MouldList<NewMembersCircleList2B> items) {
        list.addAll(items);
        notifyDataSetChanged();
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
