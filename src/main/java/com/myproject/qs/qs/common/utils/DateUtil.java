package com.myproject.qs.qs.common.utils;

import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class DateUtil {

    public Date dataFormat(Date originalDate) {

        java.sql.Date newDate = new java.sql.Date(originalDate.getTime());
        Date date1 = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(originalDate);
        // 24小时制
        calendar.add(Calendar.HOUR, 8);
        return newDate;
    }

    public boolean belongCalendar(Date from, Date to) {
        Date time = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(time);

        Calendar after = Calendar.getInstance();
        after.setTime(from);

        Calendar before = Calendar.getInstance();
        before.setTime(to);

        return (date.after(after) && date.before(before));
    }


}
