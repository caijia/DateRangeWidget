package com.caijia.widget.selectdaterange;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.caijia.daterange.DateRangePickerDialog;
import com.caijia.daterange.DayBean;
import com.caijia.daterange.OnDateRangeSelectListener;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult;
    private DayBean startDate;
    private DayBean endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult = findViewById(R.id.tv_result);
    }

    public void showDateRange(View view) {
        DateRangePickerDialog instance = DateRangePickerDialog.getInstance(startDate, endDate);
        instance.setOnDateRangeSelectListener((v,isFinish, startDate, endDate) -> {
            this.startDate = startDate;
            this.endDate = endDate;
            tvResult.setText(String.format(Locale.CHINESE,
                    "开始时间%d年%02d月%02d日\n结束时间%d年%02d月%02d日",
                    startDate.getYear(), startDate.getMonth(), startDate.getDay(),
                    endDate.getYear(), endDate.getMonth(), endDate.getDay()));
        });
        instance.show(getSupportFragmentManager(), null);
    }
}
