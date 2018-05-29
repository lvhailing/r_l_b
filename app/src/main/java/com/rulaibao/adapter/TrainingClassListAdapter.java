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
import com.rulaibao.adapter.holder.FooterViewHolder;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.ResultClassIndexItemBean;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.uitls.RlbActivityManager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rulaibao.uitls.ImageUtils.getClassImgIndex;

/**
 * 课程列表
 */

public class TrainingClassListAdapter extends RecyclerBaseAapter<RecyclerView.ViewHolder> {


    private MouldList<ResultClassIndexItemBean> arrayList;
    private LoadMoreData loadMoreData;

    public TrainingClassListAdapter(Context context, MouldList<ResultClassIndexItemBean> arrayList) {
        super(context);
        this.context = context;
        this.arrayList = arrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder inflateItemView(ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.activity_training_class_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void initHolderData(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        int index = position;
        if (getmHeaderView() != null) {
            index = position - 1;
        }
        holder1.tvTrainingName.setText(arrayList.get(index).getTypeName());
        holder1.tvTrainingClassTime.setText(arrayList.get(index).getCourseTime());
        holder1.ivTrainingRecommend.setImageResource(getClassImgIndex(arrayList.get(index).getCourseLogo()));

        holder1.tvTrainingRecommendManager.setText(arrayList.get(index).getCourseName());
        holder1.tvTrainingRecommendManagerName.setText(arrayList.get(index).getSpeechmakeName() + " " + arrayList.get(position).getPosition());

        final int finalIndex = index;
        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", arrayList.get(finalIndex).getCourseId());
                map.put("speechmakeId", arrayList.get(finalIndex).getSpeechmakeId());
                map.put("courseId", arrayList.get(finalIndex).getCourseId());
                RlbActivityManager.toTrainingClassDetailsActivity((BaseActivity) context, map, false);
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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        super.onBindViewHolder(holder, position);
        if (holder instanceof ViewHolder) {
            initHolderData(holder, position);
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            switch (mLoadMoreStatus) {
                case PULLUP_LOAD_MORE:
                    ViewGroup.LayoutParams params1 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    footerViewHolder.itemView.setLayoutParams(params1);
                    footerViewHolder.tvFooterMore.setText("数据加载中...");
                    footerViewHolder.ivHotAskFooter.setVisibility(View.GONE);
                    break;
                case LOADING_MORE:
                    ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    footerViewHolder.itemView.setLayoutParams(params2);
                    footerViewHolder.tvFooterMore.setText("正加载更多...");
                    footerViewHolder.ivHotAskFooter.setVisibility(View.GONE);
                    break;
                case NO_LOAD_MORE:
                    ViewGroup.LayoutParams params3 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    footerViewHolder.itemView.setLayoutParams(params3);
                    //隐藏加载更多
                    footerViewHolder.tvFooterMore.setVisibility(View.GONE);
                    footerViewHolder.ivHotAskFooter.setVisibility(View.GONE);
                    break;
                case NO_DATA:
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    footerViewHolder.itemView.setLayoutParams(params);

                    //没有数据
                    footerViewHolder.tvFooterMore.setVisibility(View.VISIBLE);
                    footerViewHolder.ivHotAskFooter.setVisibility(View.VISIBLE);
                    footerViewHolder.tvFooterMore.setText(noDataMessage);

                    break;
                default:

                    break;
            }
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

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


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            tv_training_name = (TextView) itemView.findViewById(R.id.tv_training_name);

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
