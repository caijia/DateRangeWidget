package com.caijia.daterange.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.caijia.daterange.delegate.MonthChildDelegate;
import com.caijia.daterange.entity.MonthBean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SelectDateView extends FrameLayout implements DayRangeView.OnDayTitleClickListener, MonthChildDelegate.OnMonthClickListener {

    private MonthView monthView;
    private DayRangeView dayRangeView;

    public SelectDateView(@NonNull Context context) {
        this(context, null);
    }

    public SelectDateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectDateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        dayRangeView = new DayRangeView(context);
        dayRangeView.setOnDayTitleClickListener(this);
        addView(dayRangeView);
    }

    @Override
    public void onDayTitleClick(MonthBean monthBean) {
        if (monthView == null) {
            monthView = new MonthView(getContext());
            monthView.setOnMonthClickListener(this);
            addView(monthView);
        }
        monthView.setVisibility(VISIBLE);
        dayRangeView.setVisibility(GONE);
        monthView.setMonth(monthBean);
    }

    @Override
    public void onMonthClick(View v, MonthBean bean, int position) {
        dayRangeView.setMonth(bean);
        dayRangeView.setVisibility(VISIBLE);
        monthView.setVisibility(GONE);
    }
}
