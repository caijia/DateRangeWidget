package com.caijia.daterange;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caijia.adapterdelegate.ItemViewDelegate;

import java.util.List;

/**
 * Created by cai.jia on 2018/3/27.
 */

class DayDelegate extends ItemViewDelegate<DayBean, DayDelegate.DayVH> {

    private OnDateClickListener onDateClickListener;

    public DayDelegate(OnDateClickListener onDateClickListener) {
        this.onDateClickListener = onDateClickListener;
    }

    @Override
    public DayVH onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_day_delegate, parent, false);
        return new DayVH(view, onDateClickListener);
    }

    @Override
    public void onBindViewHolder(List<?> dataSource, DayBean item, RecyclerView.Adapter adapter,
                                 DayVH holder, int position) {
        holder.tvDay.setEnabled(item.isEnable());
        holder.tvDay.setText(item.hasDate() ? String.valueOf(item.getDay()) : "");
        holder.tvDay.setSelected(item.isSelected());
        holder.setItemExtra(item);
        holder.itemView.setOnClickListener(holder);
    }

    @Override
    public void onBindViewHolder(List<?> dataSource, DayBean item, RecyclerView.Adapter adapter,
                                 DayVH holder, int position, List<Object> payloads) {
        holder.tvDay.setSelected(item.isSelected());
    }

    @Override
    public boolean isForViewType(@NonNull Object item) {
        return item instanceof DayBean;
    }

    @Override
    public int maxRecycledViews() {
        return 35;
    }

    @Override
    public int getSpanCount(GridLayoutManager layoutManager) {
        return layoutManager.getSpanCount() / 7;
    }

    public interface OnDateClickListener {

        void onDateClickListener(View view, DayBean dayBean, int position);
    }

    static class DayVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDay;
        private DayBean itemExtra;
        private OnDateClickListener onDateClickListener;

        public DayVH(View itemView, OnDateClickListener onDateClickListener) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tv_day);
            this.onDateClickListener = onDateClickListener;
            itemView.getLayoutParams().height = itemView.getContext().getResources()
                    .getDisplayMetrics().widthPixels / 7;
        }

        public void setItemExtra(DayBean itemExtra) {
            this.itemExtra = itemExtra;
        }

        @Override
        public void onClick(View v) {
            if (itemExtra == null || !itemExtra.hasDate() || !itemExtra.isEnable()) {
                return;
            }

            if (onDateClickListener != null) {
                onDateClickListener.onDateClickListener(v, itemExtra, getAdapterPosition());
            }
        }
    }
}
