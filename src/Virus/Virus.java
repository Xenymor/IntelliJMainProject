package Virus;

public class Virus {
    public static void main(String[] args) {
        start();
        start();
        start();
    }

    public static void start() {
        Thread thread = new MyThread();
    }

    private static class MyThread extends Thread {
        @Override
        public void run() {

        }
    }
}
