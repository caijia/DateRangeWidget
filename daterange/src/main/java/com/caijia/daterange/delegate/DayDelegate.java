package com.caijia.daterange.delegate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caijia.adapterdelegate.ItemViewDelegate;
import com.caijia.daterange.R;
import com.caijia.daterange.entity.DayBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by cai.jia on 2018/3/27.
 */

public class DayDelegate extends ItemViewDelegate<DayBean, DayDelegate.DayVH> {

    private OnDateClickListener onDateClickListener;
    private int enableColor;
    private int selectColor;
    private int boundsColor;
    private int normalColor;

    public DayDelegate(Context context, OnDateClickListener onDateClickListener) {
        this.onDateClickListener = onDateClickListener;
        enableColor = ContextCompat.getColor(context, R.color.color_cecece);
        selectColor = ContextCompat.getColor(context, R.color.color_11A6FF);
        normalColor = ContextCompat.getColor(context, R.color.color_333333);
        boundsColor = ContextCompat.getColor(context, R.color.white);
    }

    @Override
    public DayVH onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_day_delegate, parent, false);
        return new DayVH(view, onDateClickListener);
    }

    @Override
    public void onBindViewHolder(List<?> dataSource, DayBean item, RecyclerView.Adapter adapter,
                                 DayVH holder, int position) {
        holder.tvStartOrEnd.setVisibility(item.isStartDate() || item.isEndDate() ? View.VISIBLE : View.GONE);
        if (!item.isEnable()) {
            holder.tvDay.setTextColor(enableColor);
            holder.itemView.setBackgroundResource(R.drawable.shape_solid_00000000);

        } else if (item.isStartDate()) {
            holder.tvDay.setTextColor(boundsColor);
            holder.itemView.setBackgroundResource(R.drawable.shape_solid_1ea1f3_lt4_lb4);
            holder.tvStartOrEnd.setText("开始时间");

        } else if (item.isEndDate()) {
            holder.tvDay.setTextColor(boundsColor);
            holder.itemView.setBackgroundResource(R.drawable.shape_solid_1ea1f3_rt4_rb4);
            holder.tvStartOrEnd.setText("结束时间");

        } else if (item.isSelected()) {
            holder.tvDay.setTextColor(selectColor);
            holder.itemView.setBackgroundResource(R.drawable.shape_solid_ececec);

        } else {
            holder.tvDay.setTextColor(normalColor);
            holder.itemView.setBackgroundResource(R.drawable.shape_solid_00000000);
        }
        holder.tvDay.setText(item.hasDate() ? String.valueOf(item.getDay()) : "");
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
    public int getSpanCount(GridLayoutManager layoutManager) {
        return layoutManager.getSpanCount() / 7;
    }

    public interface OnDateClickListener {

        void onDateClickListener(View view, DayBean dayBean, int position);
    }

    static class DayVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDay;
        TextView tvStartOrEnd;
        private DayBean itemExtra;
        private OnDateClickListener onDateClickListener;

        public DayVH(View itemView, OnDateClickListener onDateClickListener) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tv_day);
            tvStartOrEnd = itemView.findViewById(R.id.tv_start_end);
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
