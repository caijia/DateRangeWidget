package com.caijia.daterange.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.caijia.daterange.delegate.MonthChildDelegate;
import com.caijia.daterange.delegate.YearChildDelegate;
import com.caijia.daterange.entity.DayBean;
import com.caijia.daterange.entity.MonthBean;
import com.caijia.daterange.entity.YearBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SelectDateView extends FrameLayout implements DayRangeView.OnDayTitleClickListener,
        MonthChildDelegate.OnMonthClickListener, MonthRangeView.OnMonthTitleClickListener,
        YearChildDelegate.OnYearClickListener {

    public static final int TYPE_DAY = 1;
    public static final int TYPE_MONTH = 2;
    public static final int TYPE_YEAR = 3;
    private YearRangeView yearRangeView;
    private MonthRangeView monthRangeView;
    private DayRangeView dayRangeView;
    private MonthBean selectMonth;
    private YearBean selectYear;
    public SelectDateView(@NonNull Context context) {
        this(context, null);
    }

    public SelectDateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectDateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setType(int type) {
        switch (type) {
            case TYPE_DAY: {
                dayRangeView = new DayRangeView(getContext());
                dayRangeView.setOnDayTitleClickListener(this);
                addView(dayRangeView);
                break;
            }

            case TYPE_MONTH: {
                monthRangeView = new MonthRangeView(getContext());
                monthRangeView.setOnMonthClickListener(this);
                monthRangeView.setOnMonthTitleClickListener(this);
                addView(monthRangeView);
                break;
            }

            case TYPE_YEAR: {
                yearRangeView = new YearRangeView(getContext());
                yearRangeView.setOnYearClickListener(this);
                addView(yearRangeView);
                break;
            }
        }
    }

    @Override
    public void onDayTitleClick(MonthBean monthBean) {
        if (monthRangeView == null) {
            monthRangeView = new MonthRangeView(getContext());
            monthRangeView.setOnMonthClickListener(this);
            monthRangeView.setOnMonthTitleClickListener(this);
            addView(monthRangeView);
        }
        showView(monthRangeView);
        this.selectMonth = monthBean;
        monthRangeView.setMonth(monthBean);
    }

    @Override
    public void onMonthClick(View v, MonthBean bean, int position) {
        selectMonth = bean;
        if (dayRangeView != null) {
            showView(dayRangeView);
            dayRangeView.setMonth(bean);
        }
    }

    @Override
    public void onMonthTitleClick(YearBean yearBean) {
        if (yearRangeView == null) {
            yearRangeView = new YearRangeView(getContext());
            yearRangeView.setOnYearClickListener(this);
            addView(yearRangeView);
        }
        selectYear = yearBean;
        yearRangeView.setYear(yearBean);
        showView(yearRangeView);
    }

    @Override
    public void onYearClick(View view, YearBean bean, int position) {
        selectYear = bean;
        if (monthRangeView != null) {
            showView(monthRangeView);
            monthRangeView.setMonth(new MonthBean(bean.getYear(), -1)); // -1没有选中的Item
        }
    }

    public List<DayBean> getDateRange() {
        if (dayRangeView != null) {
            return dayRangeView.getStartEndDate();
        }
        return null;
    }

    public MonthBean getSelectMonth() {
        return selectMonth;
    }

    public YearBean getSelectYear() {
        return selectYear;
    }

    public void showView(View view) {
        if (yearRangeView != null) {
            yearRangeView.setVisibility(view == yearRangeView ? VISIBLE : GONE);
        }

        if (monthRangeView != null) {
            monthRangeView.setVisibility(view == monthRangeView ? VISIBLE : GONE);
        }

        if (dayRangeView != null) {
            dayRangeView.setVisibility(view == dayRangeView ? VISIBLE : GONE);
        }
    }
}
