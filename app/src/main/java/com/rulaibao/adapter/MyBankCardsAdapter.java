package com.rulaibao.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rulaibao.R;
import com.rulaibao.activity.PolicyRecordDetailActivity;
import com.rulaibao.bean.BankCardList2B;
import com.rulaibao.network.types.MouldList;


/**
 * 我的银行卡  Adapter 类
 * Created by hong on 2018/11/14.
 */
public class MyBankCardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final MouldList<BankCardList2B> list;
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


    public MyBankCardsAdapter(Context context, MouldList<BankCardList2B> list) {
        mContext = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) { // 加载我的银行卡item布局

            View itemView = mInflater.inflate(R.layout.item_my_bank_card, parent, false);

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
            itemViewHolder.tv_bank_name.setText(list.get(position).getBank());
            itemViewHolder.tv_bank_card_num.setText(list.get(position).getBankcardNo());
//            itemViewHolder.tv_customer_name.setText(list.get(position).getCustomerName());
//            itemViewHolder.tv_insurance_premiums.setText(list.get(position).getInsurancPeremiums()+"元");
//            itemViewHolder.tv_insurance_period.setText(list.get(position).getInsurancePeriod());

            // 显示保险公司logo
//            ImageLoader.getInstance().displayImage(list.get(position).getCompanyLogo(),itemViewHolder.iv_company_logo);

//            initListener(itemViewHolder.itemView,list.get(position).getOrderId());
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;

            switch (mLoadMoreStatus) {
                case PULLUP_LOAD_MORE:
                    footerViewHolder.tvLoadText.setText("数据加载中...");
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
        return list == null ? 0 : list.size()+1;
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
        private final ImageView iv_bank_logo; // 银行Logo
        private final TextView tv_bank_name; // 银行名称
//        private final TextView tv_card_type; // 银行卡类型
        private final TextView tv_bank_card_num; // 银行卡号
        private final RelativeLayout rl_delete; // 删除当前银行卡
        private final RelativeLayout rl_make_payroll; // （是否）设为工资卡

        public ItemViewHolder(View itemView) {
            super(itemView);
            iv_bank_logo = (ImageView) itemView.findViewById(R.id.iv_bank_logo);
            tv_bank_name = (TextView) itemView.findViewById(R.id.tv_bank_name);
//            tv_card_type = (TextView) itemView.findViewById(R.id.tv_card_type);
            tv_bank_card_num = (TextView) itemView.findViewById(R.id.tv_bank_card_num);
            rl_delete = (RelativeLayout) itemView.findViewById(R.id.rl_delete);
            rl_make_payroll = (RelativeLayout) itemView.findViewById(R.id.rl_make_payroll);
        }

    }

    /**
     * item 点击监听
     *
     * @param itemView
     */
    private void initListener(View itemView,final String id) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 跳转到保单详情页
//                    Toast.makeText(mContext, "poistion " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, PolicyRecordDetailActivity.class);
                intent.putExtra("orderId", id);
                mContext.startActivity(intent);
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


    public void AddHeaderItem(MouldList<BankCardList2B> items) {
        list.addAll(0, items);
        notifyDataSetChanged();
    }

    public void AddFooterItem(MouldList<BankCardList2B> items) {
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
