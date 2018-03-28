package com.caijia.daterange;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caijia.adapterdelegate.ItemViewDelegate;

import java.util.List;

/**
 * Created by cai.jia on 2018/3/27.
 */

class HorizontalDividerDelegate extends ItemViewDelegate<HorizontalDivider, HorizontalDividerDelegate.HorizontalDividerVH> {

    @Override
    public HorizontalDividerVH onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_horizontal_delegate, parent, false);
        return new HorizontalDividerVH(view);
    }

    @Override
    public void onBindViewHolder(List<?> dataSource, HorizontalDivider item,
                                 RecyclerView.Adapter adapter, HorizontalDividerVH holder, int position) {

    }

    @Override
    public boolean isForViewType(@NonNull Object item) {
        return item instanceof HorizontalDivider;
    }

    @Override
    public int getSpanCount(GridLayoutManager layoutManager) {
        return layoutManager.getSpanCount();
    }

    static class HorizontalDividerVH extends RecyclerView.ViewHolder {
        public HorizontalDividerVH(View itemView) {
            super(itemView);
        }
    }
}
