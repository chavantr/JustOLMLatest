package com.mywings.justolm.Utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tatyabhau Chavan on 8/11/2016.
 */
public class DateUtils {

    public static String parse(String format, String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            Date formattedDate = dateFormat.parse(date);
            return formattedDate.toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String parse(String date) {
        String[] parts = date.split("/");
        Date parse = new Date(parts[1] + "/" + parts[0] + "/" + parts[2]);
        return DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(parse);
    }
}
