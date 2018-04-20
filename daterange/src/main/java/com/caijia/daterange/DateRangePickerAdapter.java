package com.caijia.daterange;

import android.content.Context;

import com.caijia.adapterdelegate.LoadMoreDelegationAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cai.jia on 2018/3/27.
 */

class DateRangePickerAdapter extends LoadMoreDelegationAdapter {

    private List<DayBean> startEndDate;

    public DateRangePickerAdapter(Context context, OnDateRangeSelectListener onDateRangeSelectListener) {
        super(false, null);
        startEndDate = new ArrayList<>();

        delegateManager.addDelegate(new MonthDelegate());
        delegateManager.addDelegate(new DayDelegate(context, (view, dayBean, position) -> {
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
                notifyDataSetChanged();
                if (onDateRangeSelectListener != null) {
                    onDateRangeSelectListener.onDateRangeSelected(view, false, dayBean, null);
                }

            } else if (startEndDate.size() == 2) {
                DayBean start = startEndDate.get(0);
                DayBean end = startEndDate.get(1);
                boolean isValidate = start.compareDate(end) < 0; //开始时间要小于结束时间
                if (isValidate) {
                    List<DayBean> dateRange = getDateRange();
                    setSelected(dateRange, true);
                    setStateEndDateFlag();
                    notifyDataSetChanged();
                    if (onDateRangeSelectListener != null) {
                        onDateRangeSelectListener.onDateRangeSelected(view, true, start, end);
                    }

                } else {
                    start.setSelected(false);
                    startEndDate.remove(start);
                    end.setSelected(true);
                    setStateEndDateFlag();
                    notifyDataSetChanged();
                    if (onDateRangeSelectListener != null) {
                        onDateRangeSelectListener.onDateRangeSelected(view, false, end, null);
                    }
                }
            }
        }));
        delegateManager.addDelegate(new HorizontalDividerDelegate());
    }

    public void setStateEndDateFlag() {
        List<?> dataSource = getDataSource();
        if (dataSource == null) {
            return;
        }

        for (Object o : dataSource) {
            if (o instanceof DayBean) {
                ((DayBean) o).setStartDate(false);
                ((DayBean) o).setEndDate(false);
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

    public List<DayBean> getStartEndDate() {
        return startEndDate;
    }

    public void setStartEndDate(List<DayBean> list) {
        if (startEndDate != null && list != null) {
            startEndDate.clear();
            startEndDate.addAll(list);
        }
    }

    private List<DayBean> getDateRange() {
        return getDateRange(getDataSource());
    }

    public List<DayBean> getDateRange(List<?> dataSource) {
        List<DayBean> rangeList = new ArrayList<>();
        if (startEndDate == null || startEndDate.size() != 2) {
            return rangeList;
        }

        if (dataSource == null) {
            return rangeList;
        }

        DayBean startDate = startEndDate.get(0);
        DayBean endDate = startEndDate.get(1);

        for (Object o : dataSource) {
            if (o != null && o instanceof DayBean) {
                DayBean dayBean = (DayBean) o;
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
}
