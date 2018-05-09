package com.rulaibao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rulaibao.R;
import com.rulaibao.bean.InteractiveNewsList3B;
import com.rulaibao.network.types.MouldList;


/**
 * 互动消息  Adapter 类
 */
public class InteractiveNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final MouldList<InteractiveNewsList3B> list;
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


    public InteractiveNewsAdapter(Context context, MouldList<InteractiveNewsList3B> list) {
        mContext = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View itemView = mInflater.inflate(R.layout.item_interactive_news, parent, false);

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
//            itemViewHolder.iv_interactive_news_photo.setImageURI(list.get(position).getCirclePhotoUrl());
            itemViewHolder.tv_interactive_news_name.setText(list.get(position).getName());
            itemViewHolder.tv_interactive_news_time.setText(list.get(position).getTime());
            itemViewHolder.tv_interactive_news_date.setText(list.get(position).getDate());
            itemViewHolder.tv_interactive_news_title.setText(list.get(position).getTitle());
            itemViewHolder.tv_interactive_news_reply.setText(list.get(position).getReply());

            initListener(itemViewHolder.itemView);
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
        private final ImageView iv_interactive_news_photo; // 头像
        private final TextView tv_interactive_news_name; // 姓名
        private final TextView tv_interactive_news_time; // 时间
        private final TextView tv_interactive_news_date; // 日期
        private final TextView tv_interactive_news_title; // 互动消息标题
        private final TextView tv_interactive_news_reply; // 回复内容

        public ItemViewHolder(View itemView) {
            super(itemView);
            iv_interactive_news_photo = (ImageView) itemView.findViewById(R.id.iv_interactive_news_photo);
            tv_interactive_news_name = (TextView) itemView.findViewById(R.id.tv_interactive_news_name);
            tv_interactive_news_time = (TextView) itemView.findViewById(R.id.tv_interactive_news_time);
            tv_interactive_news_date = (TextView) itemView.findViewById(R.id.tv_interactive_news_date);
            tv_interactive_news_title = (TextView) itemView.findViewById(R.id.tv_interactive_news_title);
            tv_interactive_news_reply = (TextView) itemView.findViewById(R.id.tv_interactive_news_reply);
        }
    }

    /**
     * item 点击监听
     *
     * @param itemView
     */
    private void initListener(View itemView) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //
//                    Toast.makeText(mContext, "poistion " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(mContext,TransactionDetailActivity.class);
//                    mContext.startActivity(intent);
            }
        });
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


    public void AddHeaderItem(MouldList<InteractiveNewsList3B> items) {
        list.addAll(0, items);
        notifyDataSetChanged();
    }

    public void AddFooterItem(MouldList<InteractiveNewsList3B> items) {
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
