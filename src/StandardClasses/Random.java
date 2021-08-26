package StandardClasses;

public class Random {

    public static int randomIntInRange(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    public static double randomDoubleInRange(double min, double max) {
        return min + (max - min) * Math.random();
    }

    public static float randomFloatInRange(float min, float max) {
        return (float) (min + Math.random() * (max - min));
    }

    public static double randomIntInRangeNumbersGetMoreUnlikely(int min, int max) {
        return randomIntInRange(min / 9, max / 9) + randomIntInRange(min / 9, max / 9) + randomIntInRange(min / 9, max / 9) + randomIntInRange(min / 9, max / 9) + randomIntInRange(min / 9, max / 9) + randomIntInRange(min / 9, max / 9) + randomIntInRange(min / 9, max / 9) + randomIntInRange(min / 9, max / 9) + randomIntInRange(min / 9, max / 9);
    }
}
