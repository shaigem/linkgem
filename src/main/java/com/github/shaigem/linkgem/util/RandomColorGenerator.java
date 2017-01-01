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
        return new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1.0);
    }


    private static class LazyHolder {
        static final RandomColorGenerator INSTANCE = new RandomColorGenerator();
    }

    public static RandomColorGenerator getInstance() {
        return LazyHolder.INSTANCE;
    }

}
