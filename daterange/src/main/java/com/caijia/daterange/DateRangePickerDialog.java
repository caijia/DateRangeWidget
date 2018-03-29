package com.caijia.daterange;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cai.jia on 2018/3/27.
 */

public class DateRangePickerDialog extends DialogFragment implements OnDateRangeSelectListener, View.OnClickListener {

    private static final String START_DATE = "params:startDate";
    private static final String END_DATE = "params:endDate";

    RecyclerView recyclerView;
    TextView tvYear;
    TextView tvAfterYear;
    TextView tvPreYear;
    TextView tvFinish;
    private DateRangePickerAdapter pickerAdapter;
    private int selectYear;
    private DayBean startDate;
    private DayBean endDate;
    private OnDateRangeSelectListener onDateRangeSelectListener;

    public static DateRangePickerDialog getInstance() {
        return new DateRangePickerDialog();
    }

    public static DateRangePickerDialog getInstance(DayBean startDate, DayBean endDate) {
        DateRangePickerDialog dialog = new DateRangePickerDialog();
        Bundle args = new Bundle();
        args.putParcelable(START_DATE, startDate);
        args.putParcelable(END_DATE, endDate);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetStyle);
        Bundle args = getArguments();
        if (args != null) {
            startDate = args.getParcelable(START_DATE);
            endDate = args.getParcelable(END_DATE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_date_range_picker, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        tvYear = view.findViewById(R.id.tv_year);
        tvAfterYear = view.findViewById(R.id.tv_after_year);
        tvPreYear = view.findViewById(R.id.tv_pre_year);
        tvFinish = view.findViewById(R.id.tv_finish);

        tvAfterYear.setOnClickListener(this);
        tvPreYear.setOnClickListener(this);
        tvFinish.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pickerAdapter = new DateRangePickerAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        recyclerView.setAdapter(pickerAdapter);
        selectYear = startDate != null ? startDate.getYear() : DateHelper.getCurrentYear();
        checkYearBounds();
        tvYear.setText(String.valueOf(selectYear));
        updateAdapterData();
    }

    private List<Object> toMultiLayoutData(List<MonthBean> monthBeans) {
        List<Object> list = new ArrayList<>();
        if (monthBeans == null || monthBeans.isEmpty()) {
            return list;
        }

        int index = 0;
        int monthSize = monthBeans.size();
        for (MonthBean monthBean : monthBeans) {
            list.add(monthBean);
            list.addAll(monthBean.getDayList());
            if (index < monthSize - 1) {
                list.add(new HorizontalDivider());
            }
            index++;
        }
        return list;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setWindowParams();
    }

    private void setWindowParams() {
        if (getDialog().getWindow() == null || getContext() == null) {
            return;
        }
        WindowManager.LayoutParams params = this.getDialog().getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        int height = (int) (getContext().getResources().getDisplayMetrics().heightPixels * 0.73f);
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = height;
        this.getDialog().getWindow().setAttributes(params);
        getDialog().setCanceledOnTouchOutside(true);
    }

    private void updateAdapterData() {
        if (pickerAdapter == null) {
            return;
        }

        List<Object> dataList = toMultiLayoutData(DateHelper.toMonthList(selectYear));
        List<DayBean> selectedDate = getStartEndDate();
        if (selectedDate != null && selectedDate.size() == 1) {
            DayBean startDate = selectedDate.get(0);
            for (Object o : dataList) {
                if (o != null && o instanceof DayBean) {
                    DayBean dayBean = (DayBean) o;
                    if (dayBean.dateIsEquals(startDate)) {
                        dayBean.setSelected(true);
                        break;
                    }
                }
            }

        } else {
            List<DayBean> dateRangeList = pickerAdapter.getDateRange(dataList);
            pickerAdapter.setSelected(dateRangeList, true);
        }
        pickerAdapter.updateItems(dataList);
    }

    private List<DayBean> getStartEndDate() {
        if (pickerAdapter == null) {
            return null;
        }
        List<DayBean> list = new ArrayList<>();
        if (startDate != null) {
            list.add(startDate);
        }

        if (endDate != null) {
            list.add(endDate);
        }

        pickerAdapter.setStartEndDate(list);
        return pickerAdapter.getStartEndDate();
    }

    /**
     * 检查年份的边界
     */
    private void checkYearBounds() {
        int currentYear = DateHelper.getCurrentYear();
        if (selectYear >= currentYear) {
            tvAfterYear.setVisibility(View.INVISIBLE);

        } else if (selectYear <= 1901) {
            tvPreYear.setVisibility(View.INVISIBLE);

        } else {
            tvPreYear.setVisibility(View.VISIBLE);
            tvAfterYear.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDateRangeSelected(boolean isFinish, DayBean startDate, DayBean endDate) {
        tvFinish.setVisibility(isFinish ? View.VISIBLE : View.GONE);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setOnDateRangeSelectListener(OnDateRangeSelectListener l) {
        this.onDateRangeSelectListener = l;
    }

    @Override
    public void onClick(View v) {
        if (v == tvPreYear) {
            --selectYear;
            updateAdapterData();

        } else if (v == tvAfterYear) {
            ++selectYear;
            updateAdapterData();

        } else if (v == tvFinish) {
            if (onDateRangeSelectListener != null) {
                onDateRangeSelectListener.onDateRangeSelected(true, startDate, endDate);
            }
            dismissAllowingStateLoss();
        }
        tvYear.setText(String.valueOf(selectYear));
        checkYearBounds();
    }
}
