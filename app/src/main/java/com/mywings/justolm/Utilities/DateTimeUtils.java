package com.mywings.justolm.Utilities;

/**
 * Created by Admin on 5/19/2016.
 */
public class DateTimeUtils {

    private static DateTimeUtils ourInstance;

    public static synchronized DateTimeUtils getInstance() {
        if (null == ourInstance) {
            ourInstance = new DateTimeUtils();
        }
        return ourInstance;
    }


    /**
     * @param year
     * @param month
     * @param day
     */
    public String update(int year, int month, int day) {
        String strday;
        String strmonth;

        if ((month + 1) <= 9) {
            strmonth = "0" + String.valueOf(month + 1);
        } else {
            strmonth = String.valueOf(month + 1);
        }

        if (day <= 9) {
            strday = "0" + String.valueOf(day);
        } else {
            strday = String.valueOf(day);
        }

        return strmonth + "/" + strday + "/" + year;
    }


}
