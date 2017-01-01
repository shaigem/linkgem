package com.github.shaigem.linkgem.util;

import javafx.scene.paint.Color;

/**
 * Created on 2016-12-31.
 */
public final class ColorUtil {
    public static Color color(Color color) {

        int d;


        // Counting the perceptive luminance - human eye favors green color...
        double a = 1 - (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());

        if (a < 0.5)
            d = 0; // bright colors - black font
        else
            d = 255; // dark colors - white font

        return Color.rgb(d, d, d);

    }
}
