package com.caijia.daterange.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cai.jia on 2018/3/27.
 */

public class DayBean implements Parcelable {

    /**
     * 是否选中
     */
    private boolean isSelected;

    /**
     * 是否是开始时间
     */
    private boolean isStartDate;

    /**
     * 是否是结束时间
     */
    private boolean isEndDate;

    private boolean enable = true;

    private int year;
    private int month;
    private int day;

    public DayBean(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public boolean dateIsEquals(DayBean other) {
        if (other == null) {
            return false;
        }
        return other.year == year && other.month == month && other.day == day;
    }

    public boolean hasDate() {
        return year > 0 && month >= 0 && day > 0;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isStartDate() {
        return isStartDate;
    }

    public void setStartDate(boolean startDate) {
        isStartDate = startDate;
    }

    public boolean isEndDate() {
        return isEndDate;
    }

    public void setEndDate(boolean endDate) {
        isEndDate = endDate;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public boolean inRange(DayBean bean1, DayBean bean2) {
        if (compareDate(bean1) >= 0 && compareDate(bean2) <= 0) {
            return true;
        }

        if (compareDate(bean2) >= 0 && compareDate(bean1) <= 0) {
            return true;
        }

        return false;
    }

    /**
     * 比较日期
     *
     * @param other
     * @return 大于为1，小于为-1，等于为0
     */
    public int compareDate(DayBean other) {
        if (dateIsEquals(other)) {
            return 0;
        }

        if (year > other.year) {
            return 1;
        }

        if (year == other.year && month > other.month) {
            return 1;
        }

        if (year == other.year && month == other.month && day > other.day) {
            return 1;
        }

        return -1;
    }

    @Override
    public String toString() {
        return "DayBean{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isStartDate ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isEndDate ? (byte) 1 : (byte) 0);
        dest.writeByte(this.enable ? (byte) 1 : (byte) 0);
        dest.writeInt(this.year);
        dest.writeInt(this.month);
        dest.writeInt(this.day);
    }

    public DayBean() {
    }

    protected DayBean(Parcel in) {
        this.isSelected = in.readByte() != 0;
        this.isStartDate = in.readByte() != 0;
        this.isEndDate = in.readByte() != 0;
        this.enable = in.readByte() != 0;
        this.year = in.readInt();
        this.month = in.readInt();
        this.day = in.readInt();
    }

    public static final Parcelable.Creator<DayBean> CREATOR = new Parcelable.Creator<DayBean>() {
        @Override
        public DayBean createFromParcel(Parcel source) {
            return new DayBean(source);
        }

        @Override
        public DayBean[] newArray(int size) {
            return new DayBean[size];
        }
    };
}
