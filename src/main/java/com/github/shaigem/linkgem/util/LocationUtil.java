package com.github.shaigem.linkgem.util;

/**
 * Created on 2016-12-30.
 */
public class LocationUtil {

    public static boolean validLocation(String string) {
        return string.matches("^(https?|ftp)://.*$");
    }


    public static String addHttp(String string) {
        // if the string does not contain http, https or ftp, add it to the string
        if (!LocationUtil.validLocation(string)) {
            string = "http://" + string;
        }
        return string;
    }
}


