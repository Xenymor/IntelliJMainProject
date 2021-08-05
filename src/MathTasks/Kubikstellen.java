package MathTasks;

public class Kubikstellen {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    double number = (i * 100) + (j * 10) + k;
                    double number2 = Math.pow(i, 3) + Math.pow(j, 3) + Math.pow(k, 3);
                    if (number == number2) {
                        System.out.println(number);
                    }
                }
            }
        }
    }
}
