package org.achymake.essentials.handlers;

import java.util.Date;

public class DateHandler {
    public Date getDate() {
        return new Date();
    }
    public Date getDate(long date) {
        return new Date(date);
    }
    public long addSeconds(int value) {
        var date = getDate();
        date.setSeconds(date.getSeconds() + value);
        return date.getTime();
    }
    public long addMinutes(int value) {
        var date = getDate();
        date.setMinutes(date.getMinutes() + value);
        return date.getTime();
    }
    public long addDays(int value) {
        var date = getDate();
        date.setDate(date.getDate() + value);
        return date.getTime();
    }
    public long addMonths(int value) {
        var date = getDate();
        date.setMonth(date.getMonth() + value);
        return date.getTime();
    }
    public long addYears(int value) {
        var date = getDate();
        date.setYear(date.getYear() + value);
        return date.getTime();
    }
    public boolean expired(long date) {
        return getDate().getTime() == getDate(date).getTime() || getDate().after(getDate(date));
    }
}