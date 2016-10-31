package com.manulaiko.tabitha.utils;

import com.manulaiko.tabitha.Console;

/**
 * Point class helper
 *
 * @author S7KYuuki
 */
public class Point
{
    /**
     * Adds 2 vectors
     *
     * @param one Point 1
     * @param two Point 2
     *
     * @return Point 1 + Point 2
     */
    public static Point plus(Point one, Point two)
    {
        return new Point(
                one.getX() + two.getX(),
                one.getY() + two.getY()
        );
    }

    /**
     * Minus 2 vectors
     *
     * @param one Point 1
     * @param two Point 2
     *
     * @return Point 1 - Point 2
     */
    public static Point minus(Point one, Point two)
    {
        return new Point(
                one.getX() - two.getX(),
                one.getY() - two.getY()
        );
    }

    /**
     * X Position
     */
    private int _x;

    /**
     * Y Position
     */
    private int _y;

    /**
     * Constructor
     */
    public Point()
    {

    }

    /**
     * Constructor
     *
     * @param x X position
     * @param y Y Position
     */
    public Point(int x, int y)
    {
        this._x = x;
        this._y = y;
    }

    /**
     * Constructor
     *
     * @param position X and Y position
     */
    public Point(int position)
    {
        this._x = position;
        this._y = position;
    }

    /**
     * Returns X position
     *
     * @return X position
     */
    public int getX()
    {
        return _x;
    }

    /**
     * Returns Y position
     *
     * @return Y Position
     */
    public int getY()
    {
        return _y;
    }

    /**
     * Sets X position
     *
     * @param x New position
     */
    public void setX(int x)
    {
        this._x = x;
    }

    /**
     * Sets Y position
     *
     * @param y New position
     */
    public void setY(int y)
    {
        this._y = y;
    }

    /**
     * Returns a vector at 0:0
     *
     * @return Empty vector
     */
    public Point reset()
    {
        return new Point(0, 0);
    }

    /**
     * Checks the distance between this vector and another.
     *
     * @param point Point to calculate the distance.
     *
     * @return The distance between this and Point.
     */
    public double distanceTo(Point point)
    {
        double x = this.getX() - point.getX();
        double y = this.getX() - point.getX();

        return Math.hypotenuse(x, y);
    }

    /**
     * Checks if this point is in a range between two points.
     *
     * @param from Starting position.
     * @param to   The radius of the range.
     *
     * @return True if this point is between `from` and `to`, false if not.
     */
    public boolean isInRange(Point from, Point to)
    {
        double x = this.getX() - from.getX();
        double y = this.getY() - from.getY();

        double position = Math.hypotenuse(x, y);
        double distance = from.distanceTo(to);

        return (position <= distance);
    }

    /**
     * Parses the object to a string
     *
     * @return The vector as a String
     */
    @Override
    public String toString()
    {
        return this.getX() + "," + this.getY();
    }

    /**
     * Checks if this vector is the same as obj
     *
     * @param obj Point to check
     *
     * @return Whether this == obj
     */
    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Point) {
            Point v2d = (Point)obj;

            return (
                v2d.getX() == this.getX() &&
                v2d.getY() == this.getY()
            );
        }

        return false;
    }

    /**
     * Hashes the vector
     *
     * @return Hashed vector
     */
    @Override
    public int hashCode()
    {
        return (this.getX() + " " + this.getY()).hashCode();
    }
}