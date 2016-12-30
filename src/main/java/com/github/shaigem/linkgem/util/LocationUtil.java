package com.github.shaigem.linkgem.util;

/**
 * Created on 2016-12-30.
 */
public class LocationUtil {

    public static boolean validLocation(String string) {
        return string.matches("^(https?|ftp)://.*$");
    }

}


