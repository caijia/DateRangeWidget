package com.caijia.daterange.entity;

import java.util.List;

/**
 * Created by cai.jia 2018/12/26 19:50
 */
public class YearRange {

    private List<YearBean> yearBeans;

    public String getTitle(){
        if (yearBeans == null || yearBeans.isEmpty()) {
            return "";
        }

        int size = yearBeans.size();
        if (size == 1) {
            return yearBeans.get(0).getYear() + "";
        }else {
            return yearBeans.get(0).getYear() + "-" + yearBeans.get(size - 1).getYear();
        }
    }

    public YearRange() {
    }

    public YearRange(List<YearBean> yearBeans) {
        this.yearBeans = yearBeans;
    }

    public List<YearBean> getYearBeans() {
        return yearBeans;
    }

    public void setYearBeans(List<YearBean> yearBeans) {
        this.yearBeans = yearBeans;
    }
}
