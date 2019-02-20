package com.wythe.mall.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneUtil {
    public static boolean isTel(String phone) {
        String regExp = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phone);
        return m.matches();
    }
}
