package com.caijia.daterange.delegate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caijia.adapterdelegate.ItemViewDelegate;
import com.caijia.adapterdelegate.LoadMoreDelegationAdapter;
import com.caijia.daterange.R;
import com.caijia.daterange.entity.YearBean;
import com.caijia.daterange.entity.YearRange;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by cai.jia 2018/12/26 19:47
 */
public class YearParentDelegate extends ItemViewDelegate<YearRange, YearParentDelegate.YearParentVH> {

    private YearChildDelegate.OnYearClickListener onYearClickListener;
    private RecyclerView.RecycledViewPool pool;

    @Override
    public YearParentVH onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_year_parent, parent, false);
        return new YearParentVH(view);
    }

    @Override
    public void onBindViewHolder(List<?> dataSource, YearRange item,
                                 RecyclerView.Adapter adapter, YearParentVH holder, int position) {
        holder.updateAdapter(item.getYearBeans());
    }

    @Override
    public boolean isForViewType(@NonNull Object item) {
        return item instanceof YearRange;
    }

    public void setOnYearClickListener(YearChildDelegate.OnYearClickListener onYearClickListener) {
        this.onYearClickListener = onYearClickListener;
    }

    public void setPool(RecyclerView.RecycledViewPool pool) {
        this.pool = pool;
    }

    class YearParentVH extends RecyclerView.ViewHolder implements YearChildDelegate.OnYearClickListener {
        RecyclerView recyclerView;
        LoadMoreDelegationAdapter adapter;

        public YearParentVH(@NonNull View itemView) {
            super(itemView);
            Context context = itemView.getContext();
            recyclerView = itemView.findViewById(R.id.recycler_view);
            recyclerView.setRecycledViewPool(pool);
            adapter = new LoadMoreDelegationAdapter(false, null);
            YearChildDelegate yearChildDelegate = new YearChildDelegate();
            yearChildDelegate.setOnYearClickListener(this);
            adapter.delegateManager.addDelegate(11, yearChildDelegate);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
            recyclerView.setAdapter(adapter);
        }

        void updateAdapter(List<YearBean> list) {
            adapter.updateItems(list);
        }

        @Override
        public void onYearClick(View view, YearBean bean, int position) {
            if (onYearClickListener != null) {
                onYearClickListener.onYearClick(view, bean, position);
            }
        }
    }
}
