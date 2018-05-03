package com.rulaibao.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rulaibao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FooterViewHolder extends RecyclerView.ViewHolder

{

    public TextView tvFooterMore;

    public FooterViewHolder(View itemView) {
        super(itemView);
        tvFooterMore = (TextView) itemView.findViewById(R.id.tv_footer_more);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    loadMoreData.getMoreData();

            }
        });
    }

}
