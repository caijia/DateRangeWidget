package com.caijia.daterange.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.caijia.adapterdelegate.LoadMoreDelegationAdapter;
import com.caijia.daterange.DateHelper;
import com.caijia.daterange.PageChangeSnapHelper;
import com.caijia.daterange.R;
import com.caijia.daterange.delegate.MonthChildDelegate;
import com.caijia.daterange.delegate.YearChildDelegate;
import com.caijia.daterange.delegate.YearParentDelegate;
import com.caijia.daterange.entity.MonthBean;
import com.caijia.daterange.entity.YearBean;
import com.caijia.daterange.entity.YearRange;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class YearRangeView extends FrameLayout implements View.OnClickListener,
        PageChangeSnapHelper.OnPageChangeListener, YearChildDelegate.OnYearClickListener {

    private TextView tvTitle;
    private TextView tvPrevious;
    private TextView tvAfter;
    private RecyclerView recyclerView;
    private List<YearRange> yearRanges;
    private int currentPosition;
    private LoadMoreDelegationAdapter adapter;

    public YearRangeView(@NonNull Context context) {
        this(context, null);
    }

    public YearRangeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YearRangeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View childView = LayoutInflater.from(context)
                .inflate(R.layout.view_year_range, this, false);
        addView(childView);

        tvPrevious = childView.findViewById(R.id.tv_previous);
        tvAfter = childView.findViewById(R.id.tv_after);
        tvTitle = childView.findViewById(R.id.tv_title);
        recyclerView = childView.findViewById(R.id.recycler_view);

        RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(pool);

        adapter = new LoadMoreDelegationAdapter(false, null);
        YearParentDelegate yearParentDelegate = new YearParentDelegate();
        yearParentDelegate.setOnYearClickListener(this);
        yearParentDelegate.setPool(pool);
        adapter.delegateManager.addDelegate(yearParentDelegate);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        PageChangeSnapHelper pageChangeSnapHelper = new PageChangeSnapHelper();
        pageChangeSnapHelper.attachToRecyclerView(recyclerView);
        pageChangeSnapHelper.addOnPageChangeListener(this);

        yearRanges = DateHelper.toYearRange(100, 12);
        int currentYear = DateHelper.getCurrentYear();
        tvTitle.setText(String.format(Locale.CHINESE,"%d-%d",currentYear - 12, currentYear));
        tvAfter.setVisibility(GONE);

        adapter.updateItems(yearRanges);
        currentPosition = yearRanges.size() - 1;
        layoutManager.scrollToPosition(yearRanges.size() - 1);

        tvPrevious.setOnClickListener(this);
        tvAfter.setOnClickListener(this);
    }

    private void previous() {
        recyclerView.smoothScrollToPosition(currentPosition - 1);
    }

    private void after() {
        recyclerView.smoothScrollToPosition(currentPosition + 1);
    }

    @Override
    public void onClick(View v) {
        if (v == tvPrevious) {
            previous();

        } else if (v == tvAfter) {
            after();
        }
    }

    @Override
    public void onPageSelected(RecyclerView.LayoutManager layoutManager, int position) {
        this.currentPosition = position;
        if (yearRanges == null) {
            return;
        }

        int size = yearRanges.size();
        tvPrevious.setVisibility(VISIBLE);
        tvAfter.setVisibility(VISIBLE);
        if (position == 0) {
            tvPrevious.setVisibility(GONE);
        }

        if (position == size - 1) {
            tvAfter.setVisibility(GONE);
        }

        if (yearRanges != null && yearRanges.size() > 0 && yearRanges.size() > position) {
            YearRange yearRange = yearRanges.get(position);
            tvTitle.setText(yearRange.getTitle());
        }
    }

    @Override
    public void onYearClick(View view, YearBean bean, int position) {
        if (yearRanges == null) {
            return;
        }
        for (YearRange yearRange : yearRanges) {
            List<YearBean> yearBeans = yearRange.getYearBeans();
            if (yearBeans == null || yearBeans.isEmpty()) {
                continue;
            }
            for (YearBean yearBean : yearBeans) {
                yearBean.setSelect(false);
            }
        }
        bean.setSelect(true);
        adapter.updateItems(yearRanges);
        if (onYearClickListener != null) {
            onYearClickListener.onYearClick(view, bean, position);
        }
    }

    private YearChildDelegate.OnYearClickListener onYearClickListener;

    public void setOnYearClickListener(YearChildDelegate.OnYearClickListener onYearClickListener) {
        this.onYearClickListener = onYearClickListener;
    }

    public void setYear(YearBean bean) {
        if (yearRanges == null) {
            return;
        }

        int index = 0;
        int selectIndex = -1;
        for (YearRange yearRange : yearRanges) {
            List<YearBean> yearBeans = yearRange.getYearBeans();
            if (yearBeans != null && !yearBeans.isEmpty()) {
                for (YearBean yearBean : yearBeans) {
                    if (bean.getYear() == yearBean.getYear()) {
                        selectIndex = index;
                        break;
                    }
                }
            }
            if (selectIndex != -1) {
                break;
            }
            index++;
        }

        if (selectIndex != -1) {
            currentPosition = selectIndex;
            recyclerView.scrollToPosition(selectIndex);
        }
    }
}
