package com.rulaibao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rulaibao.R;
import com.rulaibao.activity.TrainingCircleActivity;
import com.rulaibao.activity.TrainingCircleDetailsActivity;
import com.rulaibao.bean.ResultCircleIndexItemBean;
import com.rulaibao.bean.ResultInfoBean;
import com.rulaibao.bean.TestBean;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.uitls.ImageLoaderManager;
import com.rulaibao.widget.CircularImage;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的圈子adapter
 */

public class TrainingMyCircleListAdapter extends BaseAdapter {

    private Context context;

    private MouldList<ResultCircleIndexItemBean> arrayList;
    private LayoutInflater layoutInflater;
    private String type = "";
    private String userId = "";
    private DisplayImageOptions displayImageOptions = ImageLoaderManager.initDisplayImageOptions(R.mipmap.img_training_class,R.mipmap.img_training_class,R.mipmap.img_training_class);

    public TrainingMyCircleListAdapter(Context context, MouldList<ResultCircleIndexItemBean> arrayList, String type,String userId) {
        this.context = context;
        this.arrayList = arrayList;
        this.type = type;
        this.userId = userId;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.activity_my_circle_item, null);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();

        }

        holder.tvMycircleName.setText(arrayList.get(position).getCircleName());
        holder.tvMycircleDescription.setText(arrayList.get(position).getCircleName());

        ImageLoader.getInstance().displayImage(arrayList.get(position).getCirclePhoto(),holder.ivMycircleSign,displayImageOptions);

//        holder.tvCircleJoin.setText(arrayList.get(position).getCircleName());

        if(type.equals(TrainingCircleActivity.RECOMMEND)){

            holder.tvCircleJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    requestAddCircle(arrayList.get(position).getCircleId(),userId);

                }
            });


        }else{
            holder.tvCircleJoin.setVisibility(View.GONE);

        }


        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.iv_mycircle_sign)
        CircularImage ivMycircleSign;
        @BindView(R.id.tv_mycircle_name)
        TextView tvMycircleName;
        @BindView(R.id.tv_mycircle_description)
        TextView tvMycircleDescription;
        @BindView(R.id.tv_circle_join)
        TextView tvCircleJoin;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    //加入圈子
    public void requestAddCircle(String circleId,String userId) {

//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

        map.put("circleId", circleId);
        map.put("userId", userId);

        HtmlRequest.getTrainingAddCircle(context, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if (params.result != null) {

                    ResultInfoBean bean = (ResultInfoBean) params.result;
                    if (bean.getFlag().equals("true")) {

                        Toast.makeText(context, bean.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, bean.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {

                }
            }
        });
    }

}
