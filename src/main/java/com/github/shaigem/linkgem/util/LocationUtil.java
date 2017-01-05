package com.github.shaigem.linkgem.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created on 2016-12-30.
 */
public class LocationUtil {


    /**
     * Gets the extension of a string. This is used for fetching favicon extensions.
     *
     * @param string the string to get the extension from
     * @return the given string's extension
     */
    public static String getExtension(String string) {
        String extension = "";

        int lastIndex = string.lastIndexOf('.');
        int p = Math.max(string.lastIndexOf('/'), string.lastIndexOf('\\'));

        if (lastIndex > p) {
            extension = string.substring(lastIndex + 1);
        }
        return extension;
    }


    public static String addHostAndProtocol(String baseUrl, String url) {
        if (!url.equals("") && !url.startsWith("http") && !url.startsWith("https") && !url.startsWith("wwww")) {
            if (url.startsWith("//")) {
                url = url.substring(2);
            } else if (url.startsWith("/"))
                url = baseUrl + url;
            else url = baseUrl + "/" + url;
        }
        return addHttp(url);
    }


    public static boolean validLocation(String string) {
        return string.matches("^(https?|ftp)://.*$");
    }

    public static String getDomainFromLocation(String location) {
        String u = null;
        try {
            u = URLDecoder.decode(location, "UTF-8").replaceFirst("(\\.[^/]+).*$", "$1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return u;
    }


    public static String addHttp(String string) {
        // if the string does not contain http, https or ftp, add it to the string
        if (!validLocation(string)) {
            string = "http://" + string;
        }
        return string;
    }
}


