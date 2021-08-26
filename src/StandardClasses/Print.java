package StandardClasses;

import java.util.Arrays;

public class Print {
    public static void println2DArray(double[][] array) {
        for (double[] doubles : array) {
            System.out.println(Arrays.toString(doubles));
        }
    }
    public static void println2DArray(String[][] array) {
        for (String[] strings : array) {
            System.out.println(Arrays.toString(strings));
        }
    }
}
