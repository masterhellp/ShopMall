package com.wythe.mall.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ningwang on 2018/8/17.
 * <p>
 * 时间转换格式类
 */

public class DateUtil {
    /**
     * 将字符串时间格式 转换为 long   毫秒数
     *
     * @param dateString 字符串时间  2012-12-12 12:12：12
     * @param template   时间模板    yy-MM-dd hh:mm:ss
     * @return 当前时间的毫秒数
     */
    public static long dateStringToLong(String dateString, String template) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(template);
        try {
            Date date = dateFormat.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取预约提示的时间
     * 将播出时间 -- 15分钟
     *
     * @param airTime  播出时间
     * @param tempLate 格式化时间模板
     * @return
     */
    public static long getOrderTipTime(String airTime, String tempLate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(tempLate);
        try {
            Date date = dateFormat.parse(airTime);
            long airLong = date.getTime();
            return airLong - 15 * 60 * 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 获取时间的月--日
     *
     * @return
     */
    public static String getMonthAndDay(String time, String tempLate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(tempLate);
        try {
            Date date = dateFormat.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int mounth = (calendar.get(Calendar.MONTH) + 1);
            String tempMonth = String.valueOf(mounth);
            if (mounth < 10) {
                tempMonth = "0" + tempMonth;
            }
            String mm_day = calendar.get(Calendar.YEAR) + "-" + tempMonth + "-" + calendar.get(Calendar.DAY_OF_MONTH);
            return mm_day;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     *
     */
    public static String getLiveTime(String time, String tempLate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(tempLate);
        try {
            Date date = dateFormat.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            String hourString = "";
            if (hour < 10) {
                hourString = "0" + hour;
            } else {
                hourString = hour + "";
            }

            int minute = calendar.get(Calendar.MINUTE);
            String minString = "";
            if (minute < 10) {
                minString = "0" + minute;
            } else {
                minString = minute + "";
            }
            String mm_day = hourString + ":" + minString;
            return mm_day;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 格式化时间
     */
    public static String formatTime(String time, String tempLate) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(tempLate);
            Date date = dateFormat.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int mounth = (calendar.get(Calendar.MONTH) + 1);
            String tempMonth = String.valueOf(mounth);
            if (mounth < 10) {
                tempMonth = "0" + tempMonth;
            } else {
                tempMonth = String.valueOf(mounth);
            }
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            if (day < 10) {
                tempMonth = tempMonth + "-0" + day;
            } else {
                tempMonth = tempMonth + "-" + day;
            }
            //获取时分
            String hourAndMin = getLiveTime(time, tempLate);
            return tempMonth + " " + hourAndMin;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取当前时间
     * 时间模板
     */
    public static String getCurrentTime(String template) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(template);
        try {
            Date date = new Date();
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


}



