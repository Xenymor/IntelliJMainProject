package StandardClasses;

public class MyMath {
    public static float map(float min1, float max1, float min2, float max2, float var) {
        return ((var - min1) / (max1 - min1)) * (max2 - min2) + min2;
    }

    public static double sigmoid(double x) {
        return (1 / (1 + Math.pow(Math.E, (-1 * x))));
    }
}
