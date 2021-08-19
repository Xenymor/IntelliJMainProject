package PCTest;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.LongAdder;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting");
        multiThreaded(1, 10000);
        multiThreaded(2, 10000);
        multiThreaded(4, 10000);
        multiThreaded(8, 10000);
        multiThreaded(16, 10000);
        multiThreaded(32, 10000);
        multiThreaded(64, 10000);
    }

    public static void multiThreaded(int threadCount, int totalRuntime) throws InterruptedException {
        AtomicBoolean runFlag = new AtomicBoolean(true);
        LongAdder primesFound = new LongAdder();

        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                new PrimeSingleThreaded().run(runFlag, primesFound);
            });
            threads[i].start();
        }

        Thread.sleep(100L);

        long start = System.nanoTime();
        synchronized (runFlag) {
            runFlag.notifyAll();
        }
        Thread.sleep(totalRuntime);
        runFlag.set(false);
        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
        }
        long end = System.nanoTime();

        float duration = (end - start) / 1_000_000_000F;
        System.out.println(threadCount + ": Duration: " + duration + "sec\tPrimes per second: " + primesFound.sum() / duration + "\tPrimes per Thread per second: " + (primesFound.sum() / duration / threadCount));
    }

    public static class PrimeSingleThreaded {

        public void run(AtomicBoolean run, final LongAdder primesFound) {

            synchronized (run) {
                try {
                    run.wait(10_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            long l = 1;
            while (run.get()) {
                checkPrime(l * 6 - 1, primesFound);
                checkPrime(l * 6 + 1, primesFound);
                l++;
            }
        }

        private void checkPrime(final long l, final LongAdder primesFound) {
            int max = (int) Math.sqrt(l);
            for (long i = 6; i <= max + 1; i += 6) {
                if ((l % (i - 1)) == 0 || (l % (i + 1)) == 0) {
                    return;
                }
            }
            primesFound.increment();
            //foundPrime(l);
        }

        private void foundPrime(final long l) {
            System.out.println("Prime: " + l);
        }
    }

}
