package com.caijia.daterange;

import com.caijia.daterange.entity.DayBean;
import com.caijia.daterange.entity.MonthBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by cai.jia on 2018/3/27.
 */

public class DateHelper {

    /**
     * 根据年份转变为该年的日期集合
     *
     * @param year 年
     * @return 日期集合
     */
    public static List<MonthBean> toMonthList(int year) {
        return toMonthList(year, false);
    }

    /**
     * 根据年份转变为该年的日期集合
     *
     * @param year 年
     * @return 日期集合
     */
    public static List<MonthBean> toMonthList(int year, boolean lazy) {
        Calendar calendar = Calendar.getInstance();
        int currYear = calendar.get(Calendar.YEAR);
        int monthCount = 12;
        if (year == currYear) {
            monthCount = calendar.get(Calendar.MONTH) + 1;
        }
        List<MonthBean> monthList = new ArrayList<>();
        for (int i = 0; i < monthCount; i++) {
            MonthBean monthBean = new MonthBean();
            monthBean.setYear(year);
            monthBean.setMonth((i + 1));
            if (!lazy) {
                monthBean.setDayList(toDayList(year, i));
            }
            monthList.add(monthBean);
        }
        return monthList;
    }

    /**
     * 获取当前的年
     *
     * @return
     */
    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前月份的第一天
     *
     * @return
     */
    public static @NonNull
    DayBean getFirstDayOfCurrMonth() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        return new DayBean(year, month, 1);
    }

    /**
     * string的日期转成dayBean 2018-01-22
     *
     * @param strDate    2018-01-22
     * @param pattern yyyy-MM-dd
     * @return
     */
    public static @Nullable
    DayBean stringToDayBean(String strDate, String pattern) {
        SimpleDateFormat d = new SimpleDateFormat(pattern, Locale.CHINESE);
        try {
            Date date = d.parse(strDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            return new DayBean(year, month, day);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前的时间
     *
     * @return
     */
    public static @NonNull DayBean getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DayBean(year, month, day);
    }

    /**
     * 获取当前日期的后多少天
     *
     * @param dayCount
     * @return
     */
    public static DayBean getCurrentAfterDay(int dayCount) {
        return getAfterDay(getCurrentDate(), dayCount);
    }

    /**
     * 获取指定日期的后多少天
     *
     * @param dayBean
     * @param dayCount
     * @return
     */
    public static @NonNull DayBean getAfterDay(DayBean dayBean, int dayCount) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(dayBean.getYear(), dayBean.getMonth() - 1, dayBean.getDay() + dayCount);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DayBean(year, month, day);
    }

    /**
     * 格式化日期
     *
     * @param dayBean
     * @param format  yyyy-MM-dd
     * @return
     */
    public static String formatDate(DayBean dayBean, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(dayBean.getYear(), dayBean.getMonth() - 1, dayBean.getDay());
        Date date = calendar.getTime();
        SimpleDateFormat d = new SimpleDateFormat(format, Locale.CHINESE);
        return d.format(date);
    }

    /**
     * 根据年，月转变为日期集合
     *
     * @param year  年
     * @param month 月 [0-11]
     * @return 日期集合
     */
    public static List<DayBean> toDayList(int year, int month) {
        Calendar c = Calendar.getInstance();
        int currYear = c.get(Calendar.YEAR);
        int currMonth = c.get(Calendar.MONTH);
        int currDay = c.get(Calendar.DAY_OF_MONTH);


        List<DayBean> dayList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        int dayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        //第一天不是周日,则要补上周日到当前周几的天数
        if (firstDayOfWeek != 0) {
            for (int i = 0; i < firstDayOfWeek; i++) {
                dayList.add(new DayBean());
            }
        }

        //当月的天数
        for (int i = 0; i < dayCount; i++) {
            DayBean dayBean = new DayBean();
            calendar.set(Calendar.DAY_OF_MONTH, (i + 1));
            dayBean.setDate(year, month + 1, (i + 1));
            if (year == currYear && month == currMonth && (i + 1) > currDay) {
                //表示今天以后的日期不可用
                dayBean.setEnable(false);
            }
            dayList.add(dayBean);
        }

        calendar.set(Calendar.DAY_OF_MONTH, dayCount);
        int lastDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        //如果最后一天不是周六,则要补上当前周几到周六的天数
        if (lastDayOfWeek != 6) {
            for (int i = 0; i < 6 - lastDayOfWeek; i++) {
                dayList.add(new DayBean());
            }
        }
        return dayList;
    }
}
