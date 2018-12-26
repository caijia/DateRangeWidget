package com.caijia.daterange;

import com.caijia.daterange.entity.DayBean;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        DayBean currentAfterDay = DateHelper.getCurrentAfterDay(4);
        System.out.println("当前日期的后多少天："+currentAfterDay);

        DayBean firstDayOfCurrMonth = DateHelper.getFirstDayOfCurrMonth();
        System.out.println("本月的第一天："+firstDayOfCurrMonth);

        System.out.println(DateHelper.formatDate(currentAfterDay,"yyyy年MM月dd日"));

        DayBean dayBean = DateHelper.stringToDayBean("2015-02-11", "yyyy-MM-dd");
        System.out.println("string to DayBean:" + dayBean);
    }
}