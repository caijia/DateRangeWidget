package com.caijia.daterange.delegate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caijia.adapterdelegate.ItemViewDelegate;
import com.caijia.adapterdelegate.LoadMoreDelegationAdapter;
import com.caijia.daterange.DateHelper;
import com.caijia.daterange.R;
import com.caijia.daterange.entity.DayBean;
import com.caijia.daterange.entity.MonthBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MonthDetailDelegate extends ItemViewDelegate<MonthBean, MonthDetailDelegate.MonthDetailVH> {

    private RecyclerView.RecycledViewPool pool;
    private DayDetailDelegate.OnDateClickListener onDateClickListener;

    public MonthDetailDelegate(RecyclerView.RecycledViewPool pool) {
        this.pool = pool;
    }

    @Override
    public MonthDetailVH onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_month_detail, parent, false);
        return new MonthDetailVH(view);
    }

    @Override
    public void onBindViewHolder(List<?> dataSource, MonthBean monthBean,
                                 RecyclerView.Adapter adapter, MonthDetailVH holder, int position) {
        List<DayBean> dayList = monthBean.getDayList();
        if (dayList == null) {
            monthBean.setDayList(DateHelper.toDayList(monthBean.getYear(), monthBean.getMonth() - 1));
        }
        holder.updateAdapter(monthBean.getDayList());
    }

    @Override
    public boolean isForViewType(@NonNull Object item) {
        return item instanceof MonthBean;
    }

    public void setOnDateClickListener(DayDetailDelegate.OnDateClickListener onDateClickListener) {
        this.onDateClickListener = onDateClickListener;
    }

    class MonthDetailVH extends RecyclerView.ViewHolder implements DayDetailDelegate.OnDateClickListener {
        RecyclerView recyclerView;
        LoadMoreDelegationAdapter adapter;

        public MonthDetailVH(@NonNull View itemView) {
            super(itemView);
            Context context = itemView.getContext();
            recyclerView = itemView.findViewById(R.id.recycler_view);
            recyclerView.setRecycledViewPool(pool);
            adapter = new LoadMoreDelegationAdapter(false, null);
            adapter.delegateManager.addDelegate(11, new DayDetailDelegate(context, this));
            recyclerView.setLayoutManager(new GridLayoutManager(context, 7));
            recyclerView.setAdapter(adapter);
        }

        void updateAdapter(List<DayBean> dayList) {
            adapter.updateItems(dayList);
        }

        @Override
        public void onDateClickListener(View view, DayBean dayBean, int position) {
            if (onDateClickListener != null) {
                onDateClickListener.onDateClickListener(view, dayBean, position);
            }
        }
    }
}
