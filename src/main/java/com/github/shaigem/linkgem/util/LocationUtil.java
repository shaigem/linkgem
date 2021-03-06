package com.github.shaigem.linkgem.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Contains utilities for locations.
 */
public class LocationUtil {

    /**
     * Checks if the location is valid.
     *
     * @param string the location or URL like http://google.ca
     * @return true if valid else false if invalid
     */
    public static boolean validLocation(String string) {
        return string.matches("^(https?|ftp)://.*$");
    }

    /**
     * Gets the domain from a location.
     *
     * @param location the location to get the domain from
     * @return the domain of the location
     */
    public static String getDomainFromLocation(String location) {
        String url = null;
        try {
            url = URLDecoder.decode(location, "UTF-8").replaceFirst("(\\.[^/]+).*$", "$1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Adds http:// to a string if it does not contain one.
     *
     * @param string the string to add http:// to
     * @return the string with http://
     */
    public static String addHttp(String string) {
        // if the string does not contain http, https or ftp, add it to the string
        if (!validLocation(string)) {
            string = "http://" + string;
        }
        return string;
    }
}


