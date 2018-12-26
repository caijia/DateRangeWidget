package com.caijia.daterange.delegate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caijia.adapterdelegate.ItemViewDelegate;
import com.caijia.daterange.R;
import com.caijia.daterange.entity.MonthBean;

import java.text.MessageFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MonthChildDelegate extends ItemViewDelegate<MonthBean, MonthChildDelegate.MonthChildVH> {

    private OnMonthClickListener listener;

    @Override
    public MonthChildVH onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_month_child, parent, false);
        return new MonthChildVH(view);
    }

    @Override
    public void onBindViewHolder(List<?> dataSource, MonthBean item,
                                 RecyclerView.Adapter adapter, MonthChildVH holder, int position) {
        holder.tvMonth.setText(MessageFormat.format("{0}月", item.getMonth()));
        holder.tvMonth.setSelected(item.isSelect());

        holder.setItem(item);
        holder.itemView.setOnClickListener(holder);
    }

    @Override
    public boolean isForViewType(@NonNull Object item) {
        return item instanceof MonthBean;
    }

    @Override
    public int maxRecycledViews() {
        return 12;
    }

    public void setOnMonthClickListener(OnMonthClickListener listener) {
        this.listener = listener;
    }

    public interface OnMonthClickListener {
        void onMonthClick(View v, MonthBean bean, int position);
    }

    class MonthChildVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvMonth;
        private MonthBean item;

        public MonthChildVH(@NonNull View itemView) {
            super(itemView);
            tvMonth = itemView.findViewById(R.id.tv_month);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onMonthClick(v, item, getAdapterPosition());
            }
        }

        public void setItem(MonthBean item) {
            this.item = item;
        }
    }
}
