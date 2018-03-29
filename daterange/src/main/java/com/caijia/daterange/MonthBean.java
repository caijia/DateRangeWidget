package com.caijia.daterange;

import java.util.List;

/**
 * Created by cai.jia on 2018/3/27.
 */

public class MonthBean {

    private int year;
    private int month;
    private List<DayBean> dayList;

    public List<DayBean> getDayList() {
        return dayList;
    }

    public void setDayList(List<DayBean> dayList) {
        this.dayList = dayList;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}