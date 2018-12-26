package com.caijia.daterange.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.caijia.adapterdelegate.LoadMoreDelegationAdapter;
import com.caijia.daterange.DateHelper;
import com.caijia.daterange.PageChangeSnapHelper;
import com.caijia.daterange.R;
import com.caijia.daterange.delegate.MonthChildDelegate;
import com.caijia.daterange.delegate.MonthParentDelegate;
import com.caijia.daterange.entity.MonthBean;
import com.caijia.daterange.entity.YearBean;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MonthView extends FrameLayout implements View.OnClickListener,
        PageChangeSnapHelper.OnPageChangeListener, MonthChildDelegate.OnMonthClickListener {

    private TextView tvTitle;
    private TextView tvPrevious;
    private TextView tvAfter;
    private RecyclerView recyclerView;
    private List<YearBean> yearBeans;
    private int currentPosition;
    private LoadMoreDelegationAdapter adapter;
    private OnMonthTitleClickListener onMonthTitleClickListener;
    private MonthChildDelegate.OnMonthClickListener onMonthClickListener;

    public MonthView(@NonNull Context context) {
        this(context, null);
    }

    public MonthView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View childView = LayoutInflater.from(context)
                .inflate(R.layout.view_month, this, false);
        addView(childView);

        tvPrevious = childView.findViewById(R.id.tv_previous);
        tvAfter = childView.findViewById(R.id.tv_after);
        tvTitle = childView.findViewById(R.id.tv_title);
        recyclerView = childView.findViewById(R.id.recycler_view);

        RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(pool);

        adapter = new LoadMoreDelegationAdapter(false, null);
        MonthParentDelegate monthParentDelegate = new MonthParentDelegate();
        monthParentDelegate.setPool(pool);
        monthParentDelegate.setOnMonthClickListener(this);
        adapter.delegateManager.addDelegate(monthParentDelegate);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        PageChangeSnapHelper pageChangeSnapHelper = new PageChangeSnapHelper();
        pageChangeSnapHelper.attachToRecyclerView(recyclerView);
        pageChangeSnapHelper.addOnPageChangeListener(this);

        yearBeans = new ArrayList<>();
        int currentYear = DateHelper.getCurrentYear();
        tvTitle.setText(String.valueOf(currentYear));
        tvAfter.setVisibility(GONE);
        for (int i = currentYear - 100; i < currentYear + 1; i++) {
            YearBean year = new YearBean(i);
            yearBeans.add(year);
        }

        adapter.updateItems(yearBeans);
        currentPosition = yearBeans.size() - 1;
        layoutManager.scrollToPosition(yearBeans.size() - 1);

        tvPrevious.setOnClickListener(this);
        tvAfter.setOnClickListener(this);
        tvTitle.setOnClickListener(this);
    }

    public void setMonth(MonthBean month) {

    }

    private void previous() {
        recyclerView.smoothScrollToPosition(currentPosition - 1);
    }

    private void after() {
        recyclerView.smoothScrollToPosition(currentPosition + 1);
    }

    @Override
    public void onClick(View v) {
        if (v == tvPrevious) {
            previous();

        } else if (v == tvAfter) {
            after();

        } else if (v == tvTitle) {
            if (onMonthTitleClickListener != null) {
                onMonthTitleClickListener.onMonthTitleClick();
            }
        }
    }

    public void setOnMonthTitleClickListener(OnMonthTitleClickListener onMonthTitleClickListener) {
        this.onMonthTitleClickListener = onMonthTitleClickListener;
    }

    @Override
    public void onPageSelected(RecyclerView.LayoutManager layoutManager, int position) {
        this.currentPosition = position;
        if (yearBeans == null) {
            return;
        }

        int size = yearBeans.size();
        tvPrevious.setVisibility(VISIBLE);
        tvAfter.setVisibility(VISIBLE);
        if (position == 0) {
            tvPrevious.setVisibility(GONE);
        }

        if (position == size - 1) {
            tvAfter.setVisibility(GONE);
        }

        if (yearBeans != null && yearBeans.size() > 0 && yearBeans.size() > position) {
            YearBean yearBean = yearBeans.get(position);
            tvTitle.setText(String.valueOf(yearBean.getYear()));
        }
    }

    @Override
    public void onMonthClick(View v, MonthBean bean, int position) {
        if (yearBeans == null) {
            return;
        }
        for (YearBean yearBean : yearBeans) {
            List<MonthBean> monthBeans = yearBean.getMonthBeans();
            if (monthBeans == null || monthBeans.isEmpty()) {
                continue;
            }
            for (MonthBean monthBean : monthBeans) {
                monthBean.setSelect(false);
            }
        }
        bean.setSelect(true);
        adapter.updateItems(yearBeans);

        if (onMonthClickListener != null) {
            onMonthClickListener.onMonthClick(v, bean, position);
        }
    }

    public void setOnMonthClickListener(MonthChildDelegate.OnMonthClickListener onMonthClickListener) {
        this.onMonthClickListener = onMonthClickListener;
    }

    public interface OnMonthTitleClickListener {
        void onMonthTitleClick();
    }
}
