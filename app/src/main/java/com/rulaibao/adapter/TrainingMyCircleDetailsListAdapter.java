package com.rulaibao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rulaibao.R;
import com.rulaibao.bean.ResultCircleDetailsTopicItemBean;
import com.rulaibao.bean.TestBean;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.widget.CircularImage;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrainingMyCircleDetailsListAdapter extends BaseAdapter {

    private Context context;

    private MouldList<ResultCircleDetailsTopicItemBean> arrayList;
    private LayoutInflater layoutInflater;
    private String type = "";

    public TrainingMyCircleDetailsListAdapter(Context context, MouldList<ResultCircleDetailsTopicItemBean> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.type = type;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.activity_my_circle_details_item, null);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();

        }

//        holder.ivTrainingCircleDetailsManager = "";
        ImageLoader.getInstance().displayImage(arrayList.get(position).getCreatorPhoto(),holder.ivTrainingCircleDetailsManager);

        holder.tvTrainingCircleDetailsManagerName.setText(arrayList.get(position).getCreatorName());
        holder.tvCircleDetailsContent.setText(arrayList.get(position).getTopicContent());
        holder.tvCircleDetailsTime.setText(arrayList.get(position).getCreateTime());
        holder.tvCircleDetailsMessageCount.setText(arrayList.get(position).getCommentCount());
        holder.tvCircleDetailsZanCount.setText(arrayList.get(position).getLikeCount());

        if(arrayList.get(position).getLikeStatus().equals("yes")){
            holder.ivCircleDetailsZan.setImageResource(R.mipmap.img_zaned_icon);
        }else {
            holder.ivCircleDetailsZan.setImageResource(R.mipmap.img_zan_icon);
        }

        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.iv_training_circle_details_manager)
        CircularImage ivTrainingCircleDetailsManager;
        @BindView(R.id.tv_training_circle_details_manager_name)
        TextView tvTrainingCircleDetailsManagerName;
        @BindView(R.id.tv_circle_details_content)
        TextView tvCircleDetailsContent;
        @BindView(R.id.tv_circle_details_time)
        TextView tvCircleDetailsTime;
        @BindView(R.id.tv_circle_details_message_count)
        TextView tvCircleDetailsMessageCount;
        @BindView(R.id.iv_circle_details_zan)
        ImageView ivCircleDetailsZan;
        @BindView(R.id.tv_circle_details_zan_count)
        TextView tvCircleDetailsZanCount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
