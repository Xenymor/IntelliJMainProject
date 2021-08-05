package StandardClasses;

import StandardClasses.PathFinder.Position;

import java.awt.*;
import java.io.Serializable;
import java.util.Objects;

public class Vector2 implements Serializable {
    double x;
    double y;
    public static final long serialVersionUID = -1211420699578923808L;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Position toPosition() {
        return new Position((int) x, (int) y);
    }

    public Point toPoint() {
        return new Point((int)Math.round(x), (int)Math.round(y));
    }

    public Position toPosition(double blockSize) {
        return new Position((int) (x / blockSize), (int) (y / blockSize));
    }

    public Vector2(Vector2 vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2 vector2 = (Vector2) o;
        return Double.compare(vector2.x, x) == 0 &&
                Double.compare(vector2.y, y) == 0;
    }

    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public double getX() {
        return x;
    }

    public float getXAsFloat() {
        return (float)x;
    }

    public float getYAsFloat() {
        return (float)y;
    }

    public long getXAsLong() {
        return (long)x;
    }

    public long getYAsLong() {
        return (long)y;
    }

    public int getXAsInt() {
        return Math.round(getXAsFloat());
    }

    public int getYAsInt() {
        return Math.round(getYAsFloat());
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void mult(float multWith) {
        this.y *= multWith;
        this.x *= multWith;
    }

    public void div(float divWith) {
        this.y /= divWith;
        this.x /= divWith;
    }

    public void setLength(float length) {
        float oldLength = getLength();
        this.x /= oldLength;
        this.y /= oldLength;
        this.x *= length;
        this.y *= length;
    }

    public float getLength() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public static Vector2 randomVector2InRange(Vector2 min, Vector2 max) {
        double x = Random.randomIntInRange((int) min.getX(), (int) max.getX());
        double y = Random.randomIntInRange((int) min.getY(), (int) max.getY());
        return new Vector2(x, y);
    }
}
