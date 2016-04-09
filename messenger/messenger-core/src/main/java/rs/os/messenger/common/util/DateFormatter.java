package rs.os.messenger.common.util;

import java.util.Calendar;
import java.util.Date;

public class DateFormatter {

    private Calendar calendar;

    public DateFormatter(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
    }

    public String formatDate() {
        String formattedDate = getDay() + "." + (getMonth() + 1) + "." + getYear();
        return formattedDate;
    }

    public String formatTime() {
        String formattedTime = getHours() + ":" + getMinutes() + ":" + getSeconds();
        return formattedTime;
    }

    public String formatDateTime() {
        return formatDate() + " " + formatTime();
    }

    private int getDay() {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    private int getMonth() {
        int month = calendar.get(Calendar.MONTH);
        return month;
    }

    private int getYear() {
        int year = calendar.get(Calendar.YEAR);
        return year;
    }

    private int getHours() {
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        return hours;

    }

    private int getMinutes() {
        int minutes = calendar.get(Calendar.MINUTE);
        return minutes;
    }

    private int getSeconds() {
        int seconds = calendar.get(Calendar.SECOND);
        return seconds;
    }
}