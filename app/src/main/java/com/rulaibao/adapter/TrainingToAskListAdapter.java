package com.rulaibao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.rulaibao.R;
import com.rulaibao.bean.ResultAskTypeItemBean;
import com.rulaibao.widget.MyGridView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我要提问 问题类型adapter
 */

public class TrainingToAskListAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<ResultAskTypeItemBean> arrayList;
    private LayoutInflater layoutInflater;
    private GridView gvTrainingToask;

    public TrainingToAskListAdapter(GridView gvTrainingToask, Context context, ArrayList<ResultAskTypeItemBean> arrayList) {
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
            holder.update();
        } else {

            holder = (ViewHolder) convertView.getTag();

        }
        holder.tvTrainingToask.setText(arrayList.get(position).getTypeName());
        if (arrayList.get(position).getFlag()) {

            holder.tvTrainingToask.setBackgroundResource(R.drawable.shape_ring_orange);
            holder.tvTrainingToask.setTextColor(context.getResources().getColor(R.color.txt_orange));
        } else {
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
        holder.tvTrainingToask.setTag(convertView);
        holder.tvTrainingToaskBlack.setTag(position);

        return convertView;
    }


    class ViewHolder {

        @BindView(R.id.tv_training_toask)
        TextView tvTrainingToask;
        @BindView(R.id.tv_training_toask_black)
        TextView tvTrainingToaskBlack;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void update() {
            // 精确计算GridView的item高度
            tvTrainingToaskBlack.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        public void onGlobalLayout() {
                            int position = (Integer) tvTrainingToaskBlack.getTag();
                            if (position > 0) {
                                int height1 = 0;
                                int height2 = 0;
                                int height3 = 0;
                                View v1 = null;
                                View v2 = null;
                                View v3 = null;

                                if (position % 3 == 1) {
                                    try {
                                        v2 = (View) tvTrainingToask.getTag();
                                        height2 = v2.getHeight();
                                        v1 = gvTrainingToask.getChildAt(position - 1);
                                        height1 = v1.getHeight();

                                        if (height1 > height2) {
                                            v2.setLayoutParams(new GridView.LayoutParams(
                                                    GridView.LayoutParams.MATCH_PARENT,
                                                    height1));
                                        } else {
                                            v1.setLayoutParams(new GridView.LayoutParams(
                                                    GridView.LayoutParams.MATCH_PARENT,
                                                    height2));
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }


                                }else if (position % 3 == 2) {
                                    try {
                                        v3 = (View) tvTrainingToask.getTag();
                                        height3 = v3.getHeight();
                                        v1 = gvTrainingToask.getChildAt(position - 2);
                                        height1 = v1.getHeight();
                                        v2 = gvTrainingToask.getChildAt(position - 1);
                                        height2 = v2.getHeight();

                                        if (height1 > height2) {
                                            if (height2 > height3) {        //  v1
                                                v2.setLayoutParams(new GridView.LayoutParams(
                                                        GridView.LayoutParams.MATCH_PARENT,
                                                        height1));
                                                v3.setLayoutParams(new GridView.LayoutParams(
                                                        GridView.LayoutParams.MATCH_PARENT,
                                                        height1));
                                            } else {
                                                if (height1 > height3) {        //  v1
                                                    v2.setLayoutParams(new GridView.LayoutParams(
                                                            GridView.LayoutParams.MATCH_PARENT,
                                                            height1));
                                                    v3.setLayoutParams(new GridView.LayoutParams(
                                                            GridView.LayoutParams.MATCH_PARENT,
                                                            height1));
                                                } else {            //  v3
                                                    v1.setLayoutParams(new GridView.LayoutParams(
                                                            GridView.LayoutParams.MATCH_PARENT,
                                                            height3));
                                                    v2.setLayoutParams(new GridView.LayoutParams(
                                                            GridView.LayoutParams.MATCH_PARENT,
                                                            height3));
                                                }
                                            }
                                        } else {
                                            if (height1 > height3) {        //v2
                                                v1.setLayoutParams(new GridView.LayoutParams(
                                                        GridView.LayoutParams.MATCH_PARENT,
                                                        height2));
                                                v3.setLayoutParams(new GridView.LayoutParams(
                                                        GridView.LayoutParams.MATCH_PARENT,
                                                        height2));
                                            } else {
                                                if (height2 > height3) {        //  v2
                                                    v1.setLayoutParams(new GridView.LayoutParams(
                                                            GridView.LayoutParams.MATCH_PARENT,
                                                            height2));
                                                    v3.setLayoutParams(new GridView.LayoutParams(
                                                            GridView.LayoutParams.MATCH_PARENT,
                                                            height2));
                                                } else {                // v3
                                                    v1.setLayoutParams(new GridView.LayoutParams(
                                                            GridView.LayoutParams.MATCH_PARENT,
                                                            height3));
                                                    v2.setLayoutParams(new GridView.LayoutParams(
                                                            GridView.LayoutParams.MATCH_PARENT,
                                                            height3));
                                                }
                                            }
                                        }

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }


                                }

                            }


                        }
                    });
        }
    }
}
