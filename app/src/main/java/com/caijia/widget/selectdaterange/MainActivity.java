package com.caijia.widget.selectdaterange;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.caijia.daterange.entity.DayBean;
import com.caijia.daterange.entity.MonthBean;
import com.caijia.daterange.entity.YearBean;
import com.caijia.daterange.widget.DateRangePickerDialog;
import com.caijia.daterange.widget.SelectDateView;

import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult;
    private DayBean startDate;
    private DayBean endDate;
    private SelectDateView selectDateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult = findViewById(R.id.tv_result);
        selectDateView = findViewById(R.id.select_date_view);
        selectDateView.setType(SelectDateView.TYPE_DAY);
//        selectDateView.setType(SelectDateView.TYPE_MONTH);
//        selectDateView.setType(SelectDateView.TYPE_YEAR);

//        LineChart lineChart = findViewById(R.id.chart);
//        lineChart.setDoubleTapToZoomEnabled(false);
//
//        //设置样式
//        YAxis rightAxis = lineChart.getAxisRight();
//        rightAxis.setEnabled(false);
//
//        YAxis leftAxis = lineChart.getAxisLeft();
//        leftAxis.setEnabled(true);
//        leftAxis.setDrawGridLines(false);
//        leftAxis.setAxisMinimum(0);
//
//        //设置x轴
//        XAxis xAxis = lineChart.getXAxis();
//        xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
//        xAxis.setDrawGridLines(false);
//        xAxis.setAxisMaximum(24);
////        xAxis.setLabelCount(4,true);
//
//        //透明化图例
//        Legend legend = lineChart.getLegend();
//        legend.setForm(Legend.LegendForm.NONE);
//        legend.setTextColor(Color.TRANSPARENT);
//
//        //隐藏x轴描述
//        Description description = new Description();
//        description.setEnabled(false);
//        lineChart.setDescription(description);
//
//        List<Entry> entries = new ArrayList<>();
//
//        for (int i = 6; i < 12; i++) {
//            entries.add(new Entry(i, i));
//        }
//
//        LineDataSet dataSet = new LineDataSet(entries, "");
//        dataSet.setDrawCircles(false);
//        dataSet.setHighlightEnabled(false);
//        dataSet.setDrawValues(false);
//        dataSet.setColor(Color.CYAN);
//
//        LineData lineData = new LineData(dataSet);
//        lineChart.setData(lineData);
//        lineChart.setVisibleXRangeMaximum(8);
    }

    public void showDateRange(View view) {
        DateRangePickerDialog instance = DateRangePickerDialog.getInstance(startDate, endDate);
        instance.setOnDateRangeSelectListener((v, isFinish, startDate, endDate) -> {
            this.startDate = startDate;
            this.endDate = endDate;
            tvResult.setText(String.format(Locale.CHINESE,
                    "开始时间%d年%02d月%02d日\n结束时间%d年%02d月%02d日",
                    startDate.getYear(), startDate.getMonth(), startDate.getDay(),
                    endDate.getYear(), endDate.getMonth(), endDate.getDay()));
        });
        instance.show(getSupportFragmentManager(), null);
    }

    public void showResult(View view) {
        List<DayBean> dateRange = selectDateView.getDateRange();
        if (dateRange == null || dateRange.size() < 2) {
            Toast.makeText(this, "请选择时间范围", Toast.LENGTH_SHORT).show();

        } else {
            this.startDate = dateRange.get(0);
            this.endDate = dateRange.get(1);
            tvResult.setText(String.format(Locale.CHINESE,
                    "开始时间%d年%02d月%02d日\n结束时间%d年%02d月%02d日",
                    startDate.getYear(), startDate.getMonth(), startDate.getDay(),
                    endDate.getYear(), endDate.getMonth(), endDate.getDay()));
        }

        MonthBean selectMonth = selectDateView.getSelectMonth();
        YearBean selectYear = selectDateView.getSelectYear();

        String month = selectMonth != null ? selectMonth.getYear() + "-" + selectMonth.getMonth() : "";
        String year = selectYear != null ? selectYear.getYear() + "" : "";
        Toast.makeText(this, year +"*****" + month, Toast.LENGTH_SHORT).show();

    }
}
