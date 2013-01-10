package org.cdsframework.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author HLN Consulting, LLC
 */
public class DateUtils {

    public static String getISODateFormat(Date date) {
        String result;
        if (date == null) {
            result = "";
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            result = formatter.format(date);
        }
        return result;
    }

    public static Date parseISODateFormat(String dateString) throws ParseException {
        Date result;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        if (dateString == null || dateString.trim().isEmpty()) {
            result = null;
        } else {
            result = formatter.parse(dateString);
        }
        return result;
    }

    public static Date parseDate(String s) {
        return DatatypeConverter.parseDate(s).getTime();
    }

    public static String printDate(Date dt) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        return DatatypeConverter.printDate(cal);
    }

    public static Date parseDateTime(String s) {
        return DatatypeConverter.parseDateTime(s).getTime();
    }

    public static String printDateTime(Date dt) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return df.format(dt);
    }
}
