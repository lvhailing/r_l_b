package com.rulaibao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.rulaibao.R;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.ResultClassIndexItemBean;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.uitls.RlbActivityManager;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rulaibao.uitls.ImageUtils.getClassImgIndex;

public class TrainingClassListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;

    private MouldList<ResultClassIndexItemBean> arrayList;
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

    private TrainingHotAskListAdapter.LoadMoreData loadMoreData;


    public TrainingClassListAdapter(Context context, MouldList<ResultClassIndexItemBean> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            // 填充布局

            View view = layoutInflater.inflate(R.layout.activity_training_class_item, null);
            Holder holder = new Holder(view);
            return holder;
        } else if (viewType == TYPE_FOOTER) {
            View view = layoutInflater.inflate(R.layout.activity_training_hot_ask_footer, parent, false);
            FooterViewHolder holder = new FooterViewHolder(view);
            return holder;
        }


        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof Holder) {

            Holder holder1 = (Holder) holder;

            holder1.tvTrainingName.setText(arrayList.get(position).getTypeName());
            holder1.tvTrainingClassTime.setText(arrayList.get(position).getCourseTime());

            holder1.ivTrainingRecommend.setImageResource(getClassImgIndex(arrayList.get(position).getCourseLogo()));


            holder1.tvTrainingRecommendManager.setText(arrayList.get(position).getCourseName());
            holder1.tvTrainingRecommendManagerName.setText(arrayList.get(position).getSpeechmakeName()+" "+arrayList.get(position).getPosition());
//            itemViewHolder.tv_policy_number.setText(str);

//            itemViewHolder.tv_commission.setText(str);

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
        return arrayList.size() + 1;
    }


    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_training_name)
        TextView tvTrainingName;
        @BindView(R.id.tv_training_class_time)
        TextView tvTrainingClassTime;
        @BindView(R.id.iv_training_recommend)
        ImageView ivTrainingRecommend;
        @BindView(R.id.tv_training_recommend_manager)
        TextView tvTrainingRecommendManager;
        @BindView(R.id.tv_training_recommend_manager_name)
        TextView tvTrainingRecommendManagerName;
        @BindView(R.id.rsv_fragment_training)
        FrameLayout rsvFragmentTraining;



        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            tv_training_name = (TextView) itemView.findViewById(R.id.tv_training_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RlbActivityManager.toTrainingClassDetailsActivity((BaseActivity) context, false);
                }
            });
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

    class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_footer_more)
        TextView tvFooterMore;

        public FooterViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    loadMoreData.getMoreData();

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
