package com.rulaibao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rulaibao.R;
import com.rulaibao.bean.ResultAskTypeItemBean;
import com.rulaibao.bean.TestBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * 我要提问 问题类型adapter
 *
 */

public class TrainingToAskListAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<ResultAskTypeItemBean> arrayList;
    private LayoutInflater layoutInflater;

    public TrainingToAskListAdapter(Context context, ArrayList<ResultAskTypeItemBean> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
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

            convertView = layoutInflater.inflate(R.layout.activity_training_toask_item, null);
            holder = new ViewHolder(convertView);
            holder.tvTrainingToask = (TextView) convertView.findViewById(R.id.tv_training_toask);

            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();

        }
        holder.tvTrainingToask.setText(arrayList.get(position).getTypeName());
        if(arrayList.get(position).getFlag()){

            holder.tvTrainingToask.setBackgroundResource(R.drawable.shape_ring_orange);
            holder.tvTrainingToask.setTextColor(context.getResources().getColor(R.color.txt_orange));
        }else{
            holder.tvTrainingToask.setBackgroundResource(R.drawable.shape_ring_gray);
            holder.tvTrainingToask.setTextColor(context.getResources().getColor(R.color.txt_gray));
        }


        return convertView;
    }


    static class ViewHolder {

        @BindView(R.id.tv_training_toask)
        TextView tvTrainingToask;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
