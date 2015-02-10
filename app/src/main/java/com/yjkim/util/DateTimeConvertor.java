package com.yjkim.util;

/**
 * Created by jehyeok on 2/9/15.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeConvertor {
    private Date result;

    public DateTimeConvertor(String dateTime, String format) {
        SimpleDateFormat fromFormat = new SimpleDateFormat(format);
        try {
            result = fromFormat.parse(dateTime);
        }
        catch (ParseException e) {
            System.out.println("DateTimeConvertor ERROR");
        }
    }

    public String getTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return timeFormat.format(result).toString();
    }

    public String getDate() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        return timeFormat.format(result).toString();
    }

    public boolean isToday() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String currDate = dateFormat.format(date);
//	    System.out.println("equal?: " + getDate().equals(currDate.toString()));
//	    System.out.println("getDate(): " + getDate());
//	    System.out.println("curDate(): " + currDate.toString());
        return getDate().equals(currDate.toString());
    }

    public String getDateTime() {
        return result.toString();
    }

}
