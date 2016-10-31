package com.manulaiko.tabitha.utils;

/**
 * Math utils collection
 *
 * Collection of different math helpers.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Math
{
    /**
     *
     * @param value
     * @param min
     * @param max
     *
     * @return
     */
    public static short clamp(short value, short min, short max)
    {
        if(value < min) {
            return min;
        }
        if(value > max) {
            return max;
        }

        return value;
    }

    /**
     * Calculates and returns the square of value
     *
     * @param value Value to calculate
     *
     * @return Squared value
     */
    public static double sqr(double value)
    {
        return java.lang.Math.pow(value, 2);
    }

    /**
     * Calculates and returns the hypotenuse of x and y
     *
     * @param x X Value
     * @param y Y Value
     *
     * @return Hypotenuse of X and Y
     */
    public static double hypotenuse(double x, double y)
    {
        return java.lang.Math.sqrt(Math.sqr(x) + Math.sqr(y));
    }
}
