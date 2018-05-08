package com.rulaibao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rulaibao.R;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.ResultHotAskItemBean;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.uitls.RlbActivityManager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrainingHotAskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context context;

    private MouldList<ResultHotAskItemBean> arrayList;
    private LayoutInflater layoutInflater;

    private static final int TYPE_ITEM = 0;     // item布局
    private static final int TYPE_FOOTER = 1;   //  footer布局


    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //没有加载更多 隐藏
    public static final int NO_LOAD_MORE = 2;

    //上拉加载更多状态-默认为0
    private int mLoadMoreStatus = 0;

    private LoadMoreData loadMoreData;


    public TrainingHotAskListAdapter(Context context, MouldList<ResultHotAskItemBean> arrayList, LoadMoreData loadMoreData) {
        this.context = context;
        this.arrayList = arrayList;
        layoutInflater = LayoutInflater.from(context);
        this.loadMoreData = loadMoreData;
    }

    // 重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            // 填充布局
            View view = layoutInflater.inflate(R.layout.activity_training_hot_ask_item, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        } else if (viewType == TYPE_FOOTER) {
            View view = layoutInflater.inflate(R.layout.activity_training_hot_ask_footer, parent, false);
            FooterViewHolder holder = new FooterViewHolder(view);
            return holder;
        }
        return null;

    }

    // 填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

//        holder.tvHotTitle.setText(arrayList.get(position).toString());

        if (holder instanceof ViewHolder) {
            ViewHolder itemViewHolder = (ViewHolder) holder;


            itemViewHolder.tvHotTitle.setText(arrayList.get(position).getTitle());
            itemViewHolder.tvHotContent.setText(arrayList.get(position).getDescript());
            itemViewHolder.tvHotName.setText(arrayList.get(position).getUserName());
            itemViewHolder.tvHotAskLeaveCount.setText(arrayList.get(position).getAnswerCount());

            itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String,Object> map = new HashMap<String,Object>();
                    map.put("questionId",arrayList.get(position).getQuestionId());
                    RlbActivityManager.toTrainingAskDetailsActivity((BaseActivity) context,map, false);
                }
            });


        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;

            switch (mLoadMoreStatus) {
                case PULLUP_LOAD_MORE:
                    footerViewHolder.tvFooterMore.setText("点击查看更多回答");
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
    public int getItemViewType(int position) {

        if (position + 1 == getItemCount()) {
            //最后一个item设置为footerView
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        //RecyclerView的count设置为数据总条数+ 1（footerView）
        return arrayList.size() + 1;

    }

//    loadMoreData = new loadMoreData();

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_hot_title)
        TextView tvHotTitle;
        @BindView(R.id.tv_hot_content)
        TextView tvHotContent;
        @BindView(R.id.tv_hot_name)
        TextView tvHotName;
        @BindView(R.id.tv_hot_ask_leave)
        TextView tvHotAskLeave;
        @BindView(R.id.tv_hot_ask_leave_count)
        TextView tvHotAskLeaveCount;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_footer_more)
        TextView tvFooterMore;

        public FooterViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadMoreData.getMoreData();

                }
            });
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

    public interface LoadMoreData {
        public void getMoreData();
    }


}
