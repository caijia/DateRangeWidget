package com.caijia.daterange.entity;

import java.util.List;

/**
 * Created by cai.jia on 2018/3/27.
 */

public class MonthBean {

    private int year;
    private int month;
    private List<DayBean> dayList;
    private boolean select;

    public MonthBean(int year, int month, List<DayBean> dayList) {
        this.year = year;
        this.month = month;
        this.dayList = dayList;
    }

    public MonthBean(int year, int month) {
        this.year = year;
        this.month = month;
    }

    public MonthBean() {
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

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
