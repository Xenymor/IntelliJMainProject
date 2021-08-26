package PCTest;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting");
        final int waitTime = 10_000;
        multiThreaded(1, waitTime);
        multiThreaded(2, waitTime);
        multiThreaded(4, waitTime);
        multiThreaded(8, waitTime);
        multiThreaded(16, waitTime);
        multiThreaded(32, waitTime);
        multiThreaded(64, waitTime);
        multiThreaded(128, waitTime);
        multiThreaded(256, waitTime);
        multiThreaded(512, waitTime);
        multiThreaded(1024, waitTime);
        multiThreaded(2048, waitTime);
        multiThreaded(4096, waitTime);
    }

    public static void multiThreaded(int threadCount, int totalRuntime) throws InterruptedException {
        AtomicBoolean runFlag = new AtomicBoolean(true);

        Thread[] threads = new Thread[threadCount];
        LongAdderIntern[] adders = new LongAdderIntern[threadCount];

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            adders[i] = new LongAdderIntern();
            threads[i] = new Thread(() -> {
                new PrimeSingleThreaded().run(runFlag, adders[index]);
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

        long sum = 0;
        for (LongAdderIntern adder : adders) {
            sum += adder.get();
        }

        float duration = (end - start) / 1_000_000_000F;
        System.out.println(threadCount + ": Duration: " + duration + "sec\tPrimes per second: " + (long) (sum / duration) + "\tPrimes per Thread per second: " + (long) (sum / duration / threadCount));
    }

    public static class PrimeSingleThreaded {

        public void run(AtomicBoolean run, final LongAdderIntern primesFound) {

            synchronized (run) {
                try {
                    run.wait(10_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            int l = 6;
            int prefetchBatches = 100;
            while (run.get()) {
                int max = (int) Math.sqrt(l + prefetchBatches * 6 + 1) + 1;

                long result = 0;
                for (int i = prefetchBatches * 6; i >= 0; i -= 6) {
                    if ((result & 1) == 0) {
                        result |= checkPrime(l + i + 1, max, primesFound);
                    }
                    result >>>= 2;
                    if ((result & 1) == 0) {
                        result |= checkPrime(l + i - 1, max, primesFound);
                    }
                    result >>>= 4;
                }

                l += (prefetchBatches + 1) * 6;
            }
            primesFound.publish();
        }

        private long checkPrime(final int l, final int max, final LongAdderIntern primesFound) {
            long result = 0;
            for (int i = 6; i <= max; i += 6) {
                final int mod1 = l % (i - 1);
                if (mod1 == 0) {
                    return result;
                }
                final int mod2 = l % (i + 1);
                if (mod2 == 0) {
                    return result;
                }
                if (mod1 < 64) {
                    result |= 1L << mod1;
                }
                if (mod2 < 64) {
                    result |= 1L << mod2;
                }
            }
            primesFound.increment();
            return result;
        }
    }

    private static class LongAdderIntern {
        long count = 0;
        AtomicLong publicCount = new AtomicLong(-1);

        public void increment() {
            count++;
        }

        public void publish() {
            publicCount.set(count);
        }

        public long get() {
            if (publicCount.get() < 0) {
                throw new IllegalStateException("Content was not published yet: " + publicCount.get() + " (" + count + ")");
            }
            return publicCount.get();
        }
    }

}