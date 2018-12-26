package com.caijia.daterange.delegate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caijia.adapterdelegate.ItemViewDelegate;
import com.caijia.daterange.R;
import com.caijia.daterange.entity.HorizontalDivider;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by cai.jia on 2018/3/27.
 */

public class HorizontalDividerDelegate extends ItemViewDelegate<HorizontalDivider,
        HorizontalDividerDelegate.HorizontalDividerVH> {

    @Override
    public HorizontalDividerVH onCreateViewHolder(LayoutInflater inflater,
                                                  ViewGroup parent, int viewType) {
        View view = inflater
                .inflate(R.layout.item_horizontal_delegate, parent, false);
        return new HorizontalDividerVH(view);
    }

    @Override
    public void onBindViewHolder(List<?> dataSource, HorizontalDivider item,
                                 RecyclerView.Adapter adapter,
                                 HorizontalDividerVH holder, int position) {

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
