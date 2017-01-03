package com.github.shaigem.linkgem.util;

import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Created on 2016-12-31.
 */
public class RandomColorGenerator {


    private RandomColorGenerator() {
    }

    private Random random = new Random(System.currentTimeMillis());

    public Color getRandomColor() {
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        final Color mix = Color.BLACK;
        // mix the color
        if (mix != null) {
            red = (int) ((red + mix.getRed() * 255) / 2);
            green = (int) ((green + mix.getGreen() * 255) / 2);
            blue = (int) ((blue + mix.getBlue() * 255) / 2);
        }

        return Color.rgb(red,green,blue);
    }

    private static class LazyHolder {
        static final RandomColorGenerator INSTANCE = new RandomColorGenerator();
    }

    public static RandomColorGenerator getInstance() {
        return LazyHolder.INSTANCE;
    }

}
