package com.caijia.daterange.entity;

import java.util.List;

public class YearBean {

    private int year;
    private List<MonthBean> monthBeans;

    public YearBean(int year, List<MonthBean> monthBeans) {
        this.year = year;
        this.monthBeans = monthBeans;
    }

    public YearBean() {
    }

    public YearBean(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<MonthBean> getMonthBeans() {
        return monthBeans;
    }

    public void setMonthBeans(List<MonthBean> monthBeans) {
        this.monthBeans = monthBeans;
    }
}
