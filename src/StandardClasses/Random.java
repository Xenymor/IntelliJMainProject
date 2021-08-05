package StandardClasses;

public class Random {
    private static java.util.Random r = new java.util.Random();

    public static int randomIntInRange(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    public static double randomDoubleInRange(double min, double max) {
        return min + (max - min) * r.nextDouble();
    }

    public static float randomFloatInRange(float min, float max) {
        return (float) (min + Math.random() * (max - min));
    }
}
