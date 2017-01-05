package com.github.shaigem.linkgem.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Contains utilities for locations.
 */
public class LocationUtil {

    public static boolean validLocation(String string) {
        return string.matches("^(https?|ftp)://.*$");
    }

    public static String getDomainFromLocation(String location) {
        String url = null;
        try {
            url = URLDecoder.decode(location, "UTF-8").replaceFirst("(\\.[^/]+).*$", "$1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String addHttp(String string) {
        // if the string does not contain http, https or ftp, add it to the string
        if (!validLocation(string)) {
            string = "http://" + string;
        }
        return string;
    }
}


