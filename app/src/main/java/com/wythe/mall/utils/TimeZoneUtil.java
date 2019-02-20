package com.wythe.mall.utils;

import java.util.Date;
import java.util.TimeZone;

/**
 * @author HuangWenwei
 * @date 2014年10月9日
 */
public class TimeZoneUtil {

    /**
     * 判断用户的设备时区是否为东八区（中国） 2014年7月31日
     *
     * @return
     */
    public static boolean isInEasternEightZones() {
        boolean defaultVaule = true;
        defaultVaule = TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08");
        return defaultVaule;
    }

    /**
     * 根据不同时区，转换时间 2014年7月31日
     *
     * @param
     * @return
     */
    public static Date transformTime(Date date, TimeZone oldZone, TimeZone newZone) {
        Date finalDate = null;
        if (date != null) {
            int timeOffset = oldZone.getOffset(date.getTime())
                    - newZone.getOffset(date.getTime());
            finalDate = new Date(date.getTime() - timeOffset);
        }
        return finalDate;
    }

    /**
     * 毫秒转为时间格式
     */
    public static String transfLongToString(String mSeconds){

        String returnString = "";
        if(mSeconds == null && "".equals(mSeconds)){
            return "";
        }
        try {
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            returnString = format.format(Long.parseLong(mSeconds));
        }catch (Exception e){
            PrintLog.printError("TimeZoneUtil",e.getMessage());
        }
        return returnString;
    }

}
