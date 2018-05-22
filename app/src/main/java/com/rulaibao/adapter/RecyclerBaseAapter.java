package com.rulaibao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rulaibao.R;
import com.rulaibao.adapter.holder.FooterViewHolder;
import com.rulaibao.adapter.holder.HeaderViewHolder;


/**
 * RecyclerView 的adapter基类
 */

public abstract class RecyclerBaseAapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context context;

    protected LayoutInflater layoutInflater;

    protected static final int TYPE_ITEM = 0;     // item布局
    protected static final int TYPE_FOOTER = 1;   //  footer布局
    protected static final int TYPE_HEADER = 2;   //  header布局

    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //没有加载更多 隐藏
    public static final int NO_LOAD_MORE = 2;

    //没有加载更多 留空白布局
    public static final int NO_LOAD_BLACK = 3;

    //上拉加载更多状态-默认为0
    protected int mLoadMoreStatus = 1;


    public View mHeaderView;
    public View mFooterView;

    public View getmHeaderView() {
        return mHeaderView;
    }

    public void setmHeaderView(View mHeaderView) {
        this.mHeaderView = mHeaderView;
        notifyItemInserted(0);

//        notifyDataSetChanged();
    }

    public View getmFooterView() {
        return mFooterView;
    }

    public void setmFooterView(View mFooterView) {
        this.mFooterView = mFooterView;
        notifyItemInserted(getItemCount()-1);
    }


    public RecyclerBaseAapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public long getItemId(int position) {
        return position;

    }

    @Override
    public int getItemCount() {
//        if(mHeaderView!=null){
//            return getItem()+1;
//        }else{
//            return getItem();
//        }
        return getItem();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            // 填充布局
            return inflateItemView(parent);

        } else if (viewType == TYPE_FOOTER) {
            View view = layoutInflater.inflate(R.layout.activity_training_hot_ask_footer, parent, false);
            FooterViewHolder holder = new FooterViewHolder(view);
            return holder;
        }else if(viewType==TYPE_HEADER){
            HeaderViewHolder headerHolder = new HeaderViewHolder(mHeaderView);
            return headerHolder;
        }

        return null;
    }


    public abstract RecyclerView.ViewHolder inflateItemView(ViewGroup parent);


    public abstract void initHolderData(RecyclerView.ViewHolder holder, int position);

    public abstract int getItem();

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {

        if(mHeaderView!=null){
            if(position==0){
                return TYPE_HEADER;
            }else{
                if (position + 1 == getItemCount()) {
                    //最后一个item设置为footerView
                    return TYPE_FOOTER;
                } else {
                    return TYPE_ITEM;
                }
            }

        }else{
            if (position + 1 == getItemCount()) {
                //最后一个item设置为footerView
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;
            }
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
