package com.caijia.daterange;

import android.view.View;

import com.caijia.daterange.entity.DayBean;

public interface OnDateRangeSelectListener {

    void onDateRangeSelected(View view, boolean isFinish, DayBean startDate, DayBean endDate);
}