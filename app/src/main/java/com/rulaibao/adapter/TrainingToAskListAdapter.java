package com.rulaibao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rulaibao.R;
import com.rulaibao.bean.ResultAskTypeItemBean;
import com.rulaibao.bean.TestBean;
import com.rulaibao.widget.MyGridView;

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
    private MyGridView gvTrainingToask;

    public TrainingToAskListAdapter(MyGridView gvTrainingToask,Context context, ArrayList<ResultAskTypeItemBean> arrayList) {
        this.gvTrainingToask = gvTrainingToask;
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

//            AbsListView.LayoutParams param = new AbsListView.LayoutParams(
//                    android.view.ViewGroup.LayoutParams.FILL_PARENT,
//                    holder.tvTrainingToask.getHeight());
//            convertView.setLayoutParams(param);

            convertView.setTag(holder);
//            holder.update();
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

//        int height=holder.tvTrainingToask.getMeasuredHeight();
//        int height=context.getResources().getDimensionPixelSize(R.dimen.height);
//        AbsListView.LayoutParams param = new AbsListView.LayoutParams(
//                android.view.ViewGroup.LayoutParams.MATCH_PARENT,height);
//        convertView.setLayoutParams(param);

//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//
//                LinearLayout.LayoutParams.MATCH_PARENT,
//
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        convertView.setLayoutParams(params);



//        AbsListView.LayoutParams param = new AbsListView.LayoutParams(
//                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
//                gvTrainingToask.getHeight()/3);
//        convertView.setLayoutParams(param);


        return convertView;
    }


    class ViewHolder {

        @BindView(R.id.tv_training_toask)
        TextView tvTrainingToask;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

//        public void update() {
//
//            // 精确计算GridView的item高度
//
//            tvTrainingToask.getViewTreeObserver().addOnGlobalLayoutListener(
//                    new ViewTreeObserver.OnGlobalLayoutListener() {
//                        public void onGlobalLayout() {
//                            int position = (Integer) tvTrainingToask.getTag();
//
//                            // 这里是保证同一行的item高度是相同的！！也就是同一行是齐整的 height相等
//
//                            if (position > 0) {
//                                View v = (View) tvTrainingToask.getTag();
//                                int height = v.getHeight();
//
//                                View view = gvTrainingToask.getChildAt(position - 1);
//                                int lastheight = view.getHeight();
//
//                                // 得到同一行的最后一个item和前一个item想比较，把谁的height大，就把两者中
//                                // height小的item的高度设定为height较大的item的高度一致，也就是保证同一
//                                // // 行高度相等即可
//
//                                if (height > lastheight) {
//                                    view.setLayoutParams(new GridView.LayoutParams(
//                                            GridView.LayoutParams.FILL_PARENT,
//                                            height));
//                                } else if (height < lastheight) {
//                                    v.setLayoutParams(new GridView.LayoutParams(
//                                            GridView.LayoutParams.FILL_PARENT,
//                                            lastheight));
//                                }
//                            }
//                        }
//                    });
//        }


    }
}
