package com.caijia.daterange.delegate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caijia.adapterdelegate.ItemViewDelegate;
import com.caijia.adapterdelegate.LoadMoreDelegationAdapter;
import com.caijia.daterange.DateHelper;
import com.caijia.daterange.R;
import com.caijia.daterange.entity.MonthBean;
import com.caijia.daterange.entity.YearBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MonthParentDelegate extends ItemViewDelegate<YearBean, MonthParentDelegate.MonthParentVH> {

    private MonthChildDelegate.OnMonthClickListener listener;
    private RecyclerView.RecycledViewPool pool;

    @Override
    public MonthParentVH onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_year, parent, false);
        return new MonthParentVH(view);
    }

    @Override
    public void onBindViewHolder(List<?> dataSource, YearBean item,
                                 RecyclerView.Adapter adapter, MonthParentVH holder, int position) {
        List<MonthBean> monthBeans = item.getMonthBeans();
        if (monthBeans == null) {
            item.setMonthBeans(DateHelper.toMonthList(item.getYear(), true));
        }
        holder.updateAdapter(item.getMonthBeans());
    }

    @Override
    public boolean isForViewType(@NonNull Object item) {
        return item instanceof YearBean;
    }

    public void setOnMonthClickListener(MonthChildDelegate.OnMonthClickListener listener) {
        this.listener = listener;
    }

    public void setPool(RecyclerView.RecycledViewPool pool) {
        this.pool = pool;
    }

    class MonthParentVH extends RecyclerView.ViewHolder implements MonthChildDelegate.OnMonthClickListener {
        RecyclerView recyclerView;
        LoadMoreDelegationAdapter adapter;

        public MonthParentVH(@NonNull View itemView) {
            super(itemView);
            Context context = itemView.getContext();
            recyclerView = itemView.findViewById(R.id.recycler_view);
            recyclerView.setRecycledViewPool(pool);
            adapter = new LoadMoreDelegationAdapter(false, null);
            MonthChildDelegate monthChildDelegate = new MonthChildDelegate();
            monthChildDelegate.setOnMonthClickListener(this);
            adapter.delegateManager.addDelegate(11, monthChildDelegate);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
            recyclerView.setAdapter(adapter);
        }

        void updateAdapter(List<MonthBean> list) {
            adapter.updateItems(list);
        }

        @Override
        public void onMonthClick(View v, MonthBean bean, int position) {
            if (listener != null) {
                listener.onMonthClick(v, bean, position);
            }
        }
    }
}
