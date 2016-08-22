package ru.sergal.testtask.service;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DateUtilTest {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void daysToBirthdayCountTestThisMonthAfter() throws Exception {
        Date from = sdf.parse("2016-06-15");
        Date birthday = sdf.parse("1986-06-17");
        int days = DateUtils.daysToBirthdayCount(from, birthday);
        assertEquals(2, days);
    }

    @Test
    public void daysToBirthdayCountTestThisMonthAfterWithTime() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date from = sdf.parse("2016-08-22 00:06:00");
        Date birthday = sdf.parse("1977-08-23 00:00:00");

        int days = DateUtils.daysToBirthdayCount(from, birthday);

        assertEquals(1, days);
    }

    @Test
    public void daysToBirthdayCountTestThisMonthBefore() throws Exception {
        Date from = sdf.parse("2016-06-15");
        Date birthday = sdf.parse("1986-06-13");
        int days = DateUtils.daysToBirthdayCount(from, birthday);
        assertEquals(363, days);
    }

    @Test
    public void daysToBirthdayCountTestOtherMonth() throws Exception {
        Date from = sdf.parse("2016-03-01");
        Date birthday = sdf.parse("1986-05-01");
        int days = DateUtils.daysToBirthdayCount(from, birthday);
        assertEquals(61, days);
    }

    @Test
    public void daysToBirthdayCountTestOtherMonthBefore() throws Exception {
        Date from = sdf.parse("2016-03-01");
        Date birthday = sdf.parse("1986-02-28");
        int days = DateUtils.daysToBirthdayCount(from, birthday);
        assertEquals(364, days);
    }

}
