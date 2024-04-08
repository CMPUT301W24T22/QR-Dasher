package com.example.qr_dasher;

/**
 * Represents a date and time object with attributes for year, month, day, hour, and minute.
 */
public class DateTime {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    /**
     * Default constructor for the DateTime class.
     */
    public DateTime() {

    }

    /**
     * Parameterized constructor for the DateTime class.
     *
     * @param year   The year component of the date.
     * @param month  The month component of the date (1-12).
     * @param day    The day component of the date.
     * @param hour   The hour component of the time (0-23).
     * @param minute The minute component of the time (0-59).
     */
    public DateTime(int year, int month, int day, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Gets the year component of the date.
     *
     * @return The year.
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the year component of the date.
     *
     * @param year The year to set.
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Gets the month component of the date.
     *
     * @return The month (1-12).
     */
    public int getMonth() {
        return month;
    }

    /**
     * Sets the month component of the date.
     *
     * @param month The month to set (1-12).
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Gets the day component of the date.
     *
     * @return The day.
     */
    public int getDay() {
        return day;
    }

    /**
     * Sets the day component of the date.
     *
     * @param day The day to set.
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Gets the hour component of the time.
     *
     * @return The hour (0-23).
     */
    public int getHour() {
        return hour;
    }

    /**
     * Sets the hour component of the time.
     *
     * @param hour The hour to set (0-23).
     */
    public void setHour(int hour) {
        this.hour = hour;
    }

    /**
     * Gets the minute component of the time.
     *
     * @return The minute (0-59).
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Sets the minute component of the time.
     *
     * @param minute The minute to set (0-59).
     */
    public void setMinute(int minute) {
        this.minute = minute;
    }
}
