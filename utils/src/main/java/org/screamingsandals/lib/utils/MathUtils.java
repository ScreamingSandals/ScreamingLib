package org.screamingsandals.lib.utils;


/**
 * Copyright Minestom guys, all rights reserved
 */
public final class MathUtils {

    private MathUtils() {

    }

    public static int square(int num) {
        return num * num;
    }

    public static double square(double num) {
        return num * num;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        final long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static float round(float value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        final long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (float) tmp / factor;
    }

    public static float clampFloat(float t, float a, float b) {
        return Math.max(a, Math.min(t, b));
    }

    public static boolean isBetween(byte number, byte min, byte max) {
        return number >= min && number <= max;
    }

    public static boolean isBetween(int number, int min, int max) {
        return number >= min && number <= max;
    }

    public static boolean isBetween(float number, float min, float max) {
        return number >= min && number <= max;
    }

    public static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        } else {
            return Math.min(value, max);
        }
    }

    public static double mod(final double a, final double b) {
        return (a % b + b) % b;
    }
}