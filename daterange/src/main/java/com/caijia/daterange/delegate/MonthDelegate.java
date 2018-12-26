package com.caijia.daterange.delegate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caijia.adapterdelegate.ItemViewDelegate;
import com.caijia.daterange.R;
import com.caijia.daterange.entity.MonthBean;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by cai.jia on 2018/3/27.
 */

public class MonthDelegate extends ItemViewDelegate<MonthBean, MonthDelegate.MonthVH> {

    @Override
    public MonthVH onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_month_delegate, parent, false);
        return new MonthVH(view);
    }

    @Override
    public void onBindViewHolder(List<?> dataSource, MonthBean item, RecyclerView.Adapter adapter,
                                 MonthVH holder, int position) {
        holder.tvDate.setText(String.format(Locale.CHINESE,
                "%d年%d月", item.getYear(), item.getMonth()));
    }

    @Override
    public boolean isForViewType(@NonNull Object item) {
        return item instanceof MonthBean;
    }

    @Override
    public int getSpanCount(GridLayoutManager layoutManager) {
        return layoutManager.getSpanCount();
    }

    static class MonthVH extends RecyclerView.ViewHolder {
        TextView tvDate;

        MonthVH(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_year_month);
        }
    }
}
