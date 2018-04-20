package com.caijia.daterange;

import android.view.View;

public interface OnDateRangeSelectListener {

    void onDateRangeSelected(View view,boolean isFinish, DayBean startDate, DayBean endDate);
}