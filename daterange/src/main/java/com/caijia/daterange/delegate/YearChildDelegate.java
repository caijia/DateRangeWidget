package com.caijia.daterange.delegate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caijia.adapterdelegate.ItemViewDelegate;
import com.caijia.daterange.R;
import com.caijia.daterange.entity.YearBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by cai.jia 2018/12/26 19:43
 */
public class YearChildDelegate extends ItemViewDelegate<YearBean, YearChildDelegate.YearChildVH> {

    private OnYearClickListener onYearClickListener;

    @Override
    public YearChildVH onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_year_child, parent, false);
        return new YearChildVH(view);
    }

    @Override
    public void onBindViewHolder(List<?> dataSource, YearBean item,
                                 RecyclerView.Adapter adapter, YearChildVH holder, int position) {
        holder.tvYear.setText(String.valueOf(item.getYear()));
        holder.tvYear.setSelected(item.isSelect());
        holder.setItem(item);
        holder.itemView.setOnClickListener(holder);
    }

    @Override
    public boolean isForViewType(@NonNull Object item) {
        return item instanceof YearBean;
    }

    public void setOnYearClickListener(OnYearClickListener onYearClickListener) {
        this.onYearClickListener = onYearClickListener;
    }

    public interface OnYearClickListener {
        void onYearClick(View view, YearBean bean, int position);
    }

    class YearChildVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvYear;
        private YearBean item;

        public YearChildVH(@NonNull View itemView) {
            super(itemView);
            tvYear = itemView.findViewById(R.id.tv_year);
        }

        @Override
        public void onClick(View v) {
            if (onYearClickListener != null) {
                onYearClickListener.onYearClick(v, item, getAdapterPosition());
            }
        }

        public void setItem(YearBean item) {
            this.item = item;
        }
    }
}
