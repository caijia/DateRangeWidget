package com.caijia.daterange.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.caijia.adapterdelegate.LoadMoreDelegationAdapter;
import com.caijia.daterange.DateHelper;
import com.caijia.daterange.OnDateRangeSelectListener;
import com.caijia.daterange.PageChangeSnapHelper;
import com.caijia.daterange.R;
import com.caijia.daterange.delegate.DayDetailDelegate;
import com.caijia.daterange.delegate.MonthDetailDelegate;
import com.caijia.daterange.entity.DayBean;
import com.caijia.daterange.entity.MonthBean;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DayRangeView extends FrameLayout implements View.OnClickListener,
        DayDetailDelegate.OnDateClickListener, PageChangeSnapHelper.OnPageChangeListener {

    private TextView tvTitle;
    private TextView tvPrevious;
    private TextView tvAfter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private List<MonthBean> monthBeanList;
    private int currentPosition;
    private LoadMoreDelegationAdapter adapter;
    private List<DayBean> startEndDate = new ArrayList<>();
    private OnDateRangeSelectListener onDateRangeSelectListener;
    private OnDayTitleClickListener onDayTitleClickListener;

    public DayRangeView(@NonNull Context context) {
        this(context, null);
    }

    public DayRangeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayRangeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View childView = LayoutInflater.from(context)
                .inflate(R.layout.view_day_range, this, false);
        addView(childView);

        tvPrevious = childView.findViewById(R.id.tv_previous);
        tvAfter = childView.findViewById(R.id.tv_after);
        tvTitle = childView.findViewById(R.id.tv_title);
        recyclerView = childView.findViewById(R.id.recycler_view);

        RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(pool);
        adapter = new LoadMoreDelegationAdapter(false, null);
        layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        MonthDetailDelegate monthDetailDelegate = new MonthDetailDelegate(pool);
        monthDetailDelegate.setOnDateClickListener(this);
        adapter.delegateManager.addDelegate(monthDetailDelegate);
        recyclerView.setAdapter(adapter);

        PageChangeSnapHelper snapHelper = new PageChangeSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        snapHelper.addOnPageChangeListener(this);

        DayBean currentDate = DateHelper.getCurrentDate();
        tvTitle.setText(String.format("%s-%s", currentDate.getYear(), currentDate.getMonth()));
        tvAfter.setVisibility(GONE);
        monthBeanList = new ArrayList<>();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonth();
        for (int i = currentYear - 100; i < currentYear + 1; i++) {
            for (int j = 1; j <= 12; j++) {
                if (i == currentYear && j > currentMonth) {
                    continue;
                }
                MonthBean month = new MonthBean(i, j);
                monthBeanList.add(month);
            }
        }

        adapter.updateItems(monthBeanList);
        currentPosition = monthBeanList.size() - 1;
        layoutManager.scrollToPosition(monthBeanList.size() - 1);

        tvPrevious.setOnClickListener(this);
        tvAfter.setOnClickListener(this);
        tvTitle.setOnClickListener(this);
    }

    private void setTitle(MonthBean bean) {
        tvTitle.setText(String.format("%s-%s", bean.getYear(), bean.getMonth()));
    }

    public void setMonth(MonthBean bean) {

        int index = 0;
        int selectIndex = -1;
        for (MonthBean monthBean : monthBeanList) {
            if (monthBean.getYear() == bean.getYear() &&
                    monthBean.getMonth() == bean.getMonth()) {
                selectIndex = index;
                break;
            }
            index++;
        }
        if (selectIndex != -1) {
            setTitle(bean);
            recyclerView.scrollToPosition(selectIndex);
        }
    }

    @Override
    public void onClick(View v) {
        if (layoutManager == null) {
            return;
        }
        if (v == tvPrevious) {
            previous();

        } else if (v == tvAfter) {
            after();

        } else if (v == tvTitle) {
            if (onDayTitleClickListener != null) {
                onDayTitleClickListener.onDayTitleClick(monthBeanList.get(currentPosition));
            }
        }
    }

    public void setOnDayTitleClickListener(OnDayTitleClickListener onDayTitleClickListener) {
        this.onDayTitleClickListener = onDayTitleClickListener;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void setOnDateRangeSelected(OnDateRangeSelectListener listener) {
        this.onDateRangeSelectListener = listener;
    }

    @Override
    public void onDateClickListener(View view, DayBean dayBean, int position) {
        //点击的是同一个Item
        if (startEndDate.size() == 1 && startEndDate.get(0).dateIsEquals(dayBean)) {
            return;
        }

        //已经选择了起终点,再一次选择时，清除掉原来已选择的
        if (startEndDate.size() == 2) {
            setSelected(getDateRange(), false);
            startEndDate.clear();
        }

        startEndDate.add(dayBean);
        if (startEndDate.size() == 1) {
            dayBean.setSelected(true);
            setStateEndDateFlag();
            adapter.updateItems(monthBeanList);
            if (onDateRangeSelectListener != null) {
                onDateRangeSelectListener.onDateRangeSelected(view, false,
                        dayBean, null);
            }

        } else if (startEndDate.size() == 2) {
            DayBean start = startEndDate.get(0);
            DayBean end = startEndDate.get(1);
            boolean isValidate = start.compareDate(end) < 0; //开始时间要小于结束时间
            if (isValidate) {
                List<DayBean> dateRange = getDateRange();
                setSelected(dateRange, true);
                setStateEndDateFlag();
                adapter.updateItems(monthBeanList);
                if (onDateRangeSelectListener != null) {
                    onDateRangeSelectListener.onDateRangeSelected(view, true, start,
                            end);
                }

            } else {
                start.setSelected(false);
                startEndDate.remove(start);
                end.setSelected(true);
                setStateEndDateFlag();
                adapter.updateItems(monthBeanList);
                if (onDateRangeSelectListener != null) {
                    onDateRangeSelectListener.onDateRangeSelected(view, false, end,
                            null);
                }
            }
        }
    }

    public void setStateEndDateFlag() {
        if (monthBeanList != null) {
            for (MonthBean monthBean : monthBeanList) {
                List<DayBean> dayList = monthBean.getDayList();
                if (dayList == null) {
                    continue;
                }

                for (DayBean dayBean : dayList) {
                    dayBean.setStartDate(false);
                    dayBean.setEndDate(false);
                }
            }
        }

        if (startEndDate == null || startEndDate.isEmpty()) {
            return;
        }

        if (startEndDate.size() == 1) {
            startEndDate.get(0).setStartDate(true);

        } else if (startEndDate.size() == 2) {
            startEndDate.get(0).setStartDate(true);
            startEndDate.get(1).setEndDate(true);
        }
    }

    public void setSelected(List<DayBean> list, boolean isSelected) {
        if (list == null || list.isEmpty()) {
            return;
        }

        for (DayBean dayBean : list) {
            dayBean.setSelected(isSelected);
        }
    }

    public List<DayBean> getDateRange() {
        List<DayBean> rangeList = new ArrayList<>();
        if (startEndDate == null || startEndDate.size() != 2) {
            return rangeList;
        }

        if (monthBeanList == null) {
            return rangeList;
        }

        DayBean startDate = startEndDate.get(0);
        DayBean endDate = startEndDate.get(1);

        for (MonthBean monthBean : monthBeanList) {
            List<DayBean> dayList = monthBean.getDayList();
            if (dayList == null) {
                //减少范围
                dayList = DateHelper.toDayList(monthBean.getYear(), monthBean.getMonth() - 1);
                monthBean.setDayList(dayList);
            }

            for (DayBean dayBean : dayList) {
                if (dayBean.inRange(startDate, endDate)) {
                    rangeList.add(dayBean);
                }
                if (dayBean.dateIsEquals(startDate)) {
                    dayBean.setStartDate(true);

                } else if (dayBean.dateIsEquals(endDate)) {
                    dayBean.setEndDate(true);
                }
            }
        }
        return rangeList;
    }

    private void previous() {
        recyclerView.smoothScrollToPosition(currentPosition - 1);
    }

    private void after() {
        recyclerView.smoothScrollToPosition(currentPosition + 1);
    }

    @Override
    public void onPageSelected(RecyclerView.LayoutManager layoutManager, int position) {
        this.currentPosition = position;
        if (monthBeanList == null) {
            return;
        }

        int size = monthBeanList.size();
        tvPrevious.setVisibility(VISIBLE);
        tvAfter.setVisibility(VISIBLE);
        if (position == 0) {
            tvPrevious.setVisibility(GONE);
        }

        if (position == size - 1) {
            tvAfter.setVisibility(GONE);
        }

        if (monthBeanList != null && monthBeanList.size() > 0 && monthBeanList.size() > position) {
            MonthBean monthBean = monthBeanList.get(position);
            setTitle(monthBean);
        }
    }

    public interface OnDayTitleClickListener {
        void onDayTitleClick(MonthBean bean);
    }
}
