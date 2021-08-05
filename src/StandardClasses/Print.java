package StandardClasses;

import java.util.Arrays;

public class Print {
    public static void println2DArray(double[][] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.println(Arrays.toString(array[i]));
        }
    }
    public static void println2DArray(String[][] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.println(Arrays.toString(array[i]));
        }
    }
}
