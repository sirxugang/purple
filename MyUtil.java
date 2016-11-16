package com.xugang.hwa5d4;

/**
 * Created by ASUS on 2016-09-22.
 */
public class MyUtil {
    public static String getKey(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.length());
    }
}
