package cn.sichu.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author sichu huang
 * @date 2024/09/16
 **/
public class DateUtil {
    private static final SimpleDateFormat YYYY_MM_DD_HH_MM_SS_SSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private static final SimpleDateFormat YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");

    public static Date parseToMills(Date date) throws ParseException {
        return YYYY_MM_DD_HH_MM_SS_SSS.parse(YYYY_MM_DD_HH_MM_SS_SSS.format(date));
    }

    public static Date parseToSeconds(Date date) throws ParseException {
        return YYYY_MM_DD_HH_MM_SS.parse(YYYY_MM_DD_HH_MM_SS.format(date));
    }

    public static Date parseToDays(Date date) throws ParseException {
        return YYYY_MM_DD.parse(YYYY_MM_DD.format(date));
    }
}
