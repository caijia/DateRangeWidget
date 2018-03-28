package com.caijia.daterange;

import com.caijia.adapterdelegate.LoadMoreDelegationAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cai.jia on 2018/3/27.
 */

class DateRangePickerAdapter extends LoadMoreDelegationAdapter {

    private List<DayBean> startEndDate;

    public DateRangePickerAdapter(OnDateRangeSelectListener onDateRangeSelectListener) {
        super(false, null);
        startEndDate = new ArrayList<>();

        delegateManager.addDelegate(new MonthDelegate());
        delegateManager.addDelegate(new DayDelegate((view, dayBean, position) -> {
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
                notifyDataSetChanged();
                if (onDateRangeSelectListener != null) {
                    onDateRangeSelectListener.onDateRangeSelected(false, dayBean, null);
                }

            } else if (startEndDate.size() == 2) {
                List<DayBean> dateRange = getDateRange();
                setSelected(dateRange, true);
                notifyDataSetChanged();

                if (onDateRangeSelectListener != null) {
                    DayBean date1 = startEndDate.get(0);
                    DayBean date2 = startEndDate.get(1);
                    DayBean minDate = date1.compareDate(date2) < 0 ? date1 : date2;
                    DayBean maxDate = date1.compareDate(date2) > 0 ? date1 : date2;
                    onDateRangeSelectListener.onDateRangeSelected(true, minDate, maxDate);
                }
            }
        }));
        delegateManager.addDelegate(new HorizontalDividerDelegate());
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

        DayBean date1 = startEndDate.get(0);
        DayBean date2 = startEndDate.get(1);

        for (Object o : dataSource) {
            if (o != null && o instanceof DayBean) {
                DayBean dayBean = (DayBean) o;
                if (dayBean.inRange(date1, date2)) {
                    rangeList.add(dayBean);
                }
            }
        }
        return rangeList;
    }
}
