package org.maktab36.taskapp.util;

import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {
    public static Date getRandomDate(int startYear, int endYear) {
        GregorianCalendar gc = new GregorianCalendar();
        int year = randBetween(startYear, endYear);
        gc.set(GregorianCalendar.YEAR, year);
        int dayOfYear = randBetween(1, gc.getActualMaximum(GregorianCalendar.DAY_OF_YEAR));
        gc.set(GregorianCalendar.DAY_OF_YEAR, dayOfYear);

        int hourOfDay=randBetween(0,gc.getActualMaximum(GregorianCalendar.HOUR_OF_DAY));
        gc.set(GregorianCalendar.HOUR_OF_DAY,hourOfDay);

        int minute=randBetween(0,gc.getActualMaximum(GregorianCalendar.MINUTE));
        gc.set(GregorianCalendar.MINUTE,minute);

        int second=randBetween(0,gc.getActualMaximum(GregorianCalendar.SECOND));
        gc.set(GregorianCalendar.SECOND,second);

        return gc.getTime();
    }

    public static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }
}
