package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.bukkit.configuration.file.FileConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    /**
     * get new date
     * @return date
     * @since many moons ago
     */
    public Date getDate() {
        return new Date();
    }
    /**
     * get date from long
     * @param date long
     * @return date
     * @since many moons ago
     */
    public Date getDate(long date) {
        return new Date(date);
    }
    /**
     * add seconds
     * @param value integer
     * @return long
     * @since many moons ago
     */
    public long addSeconds(int value) {
        var date = getDate();
        date.setSeconds(date.getSeconds() + value);
        return date.getTime();
    }
    /**
     * add minutes
     * @param value integer
     * @return long
     * @since many moons ago
     */
    public long addMinutes(int value) {
        var date = getDate();
        date.setMinutes(date.getMinutes() + value);
        return date.getTime();
    }
    /**
     * add days
     * @param value integer
     * @return long
     * @since many moons ago
     */
    public long addDays(int value) {
        var date = getDate();
        date.setSeconds(0);
        date.setMinutes(0);
        date.setHours(0);
        date.setDate(date.getDate() + value);
        return date.getTime();
    }
    /**
     * add months
     * @param value integer
     * @return long
     * @since many moons ago
     */
    public long addMonths(int value) {
        var date = getDate();
        date.setSeconds(0);
        date.setMinutes(0);
        date.setHours(0);
        date.setMonth(date.getMonth() + value);
        return date.getTime();
    }
    /**
     * add years
     * @param value integer
     * @return long
     * @since many moons ago
     */
    public long addYears(int value) {
        var date = getDate();
        date.setSeconds(0);
        date.setMinutes(0);
        date.setHours(0);
        date.setYear(date.getYear() + value);
        return date.getTime();
    }
    /**
     * expired
     * @param date long
     * @return true if long is expired else false
     * @since many moons ago
     */
    public boolean expired(long date) {
        return getDate().getTime() == getDate(date).getTime() || getDate().after(getDate(date));
    }
    /**
     * get date format
     * @param date long
     * @return string
     * @since many moons ago
     */
    public String getFormatted(long date) {
        return new SimpleDateFormat(getConfig().getString("date.format")).format(getDate(date));
    }
}