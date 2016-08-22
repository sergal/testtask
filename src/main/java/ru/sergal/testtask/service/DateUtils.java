package ru.sergal.testtask.service;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static int getMonthFromDate(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public static int daysToBirthdayCount(Date from, Date birthday) {
        Calendar fromCal = Calendar.getInstance();
        fromCal.setTime(from);
        Calendar nearestBirthday = Calendar.getInstance();
        nearestBirthday.setTime(birthday);
        nearestBirthday.set(Calendar.YEAR, fromCal.get(Calendar.YEAR));
        if (nearestBirthday.getTime().before(from)) {
            nearestBirthday.set(Calendar.YEAR, fromCal.get(Calendar.YEAR) + 1);
        }
        return Days.daysBetween(new DateTime(from.getTime()).toLocalDate(),
                new DateTime(nearestBirthday.getTimeInMillis()).toLocalDate()).getDays();
    }
}
