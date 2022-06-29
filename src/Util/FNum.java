package Util;

import java.util.Random;

public class FNum {

    public static int randomInt(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    public static int randomInt(String min, String max) {
        return new Random().nextInt((ri(max) - ri(min)) + 1) + ri(min);
    }

    public static double randomDouble(double min, double max) {
        return (Math.random() * (max - min) + min);
    }

    public static double randomDouble(String min, String max) {
        return (Math.random() * (rd(max) - rd(min)) + rd(min));
    }

    public static double randomDoubleNnega(double d) {
        double nega = d;
        nega *= -1;
        return (Math.random() * (d - nega) + nega);
    }

    public static double rd(String s) {
        return Double.valueOf(s);
    }

    public static int ri(String s) {
        return Integer.valueOf(s);
    }
}
