package ua.edu.cdu.vu.lab1;

import java.math.BigInteger;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class Application {

    public static void main(String[] args) throws InterruptedException {
        int threadsCount = readInt();
        int sleepSeconds = readInt();
        int step = readInt();

        ExecutorService executorService = Executors.newFixedThreadPool(threadsCount);
        CountDownLatch starter = new CountDownLatch(1);

        List<Future<BigInteger>> futures = IntStream.rangeClosed(1, threadsCount)
                .mapToObj(i -> new SummingCallable(starter, step))
                .map(executorService::submit)
                .toList();

        starter.countDown();
        TimeUnit.SECONDS.sleep(sleepSeconds);

        executorService.shutdownNow();
        BigInteger[] results = futures.stream()
                .map(Application::getUnchecked)
                .toArray(BigInteger[]::new);
        print(results);
    }

    private static BigInteger getUnchecked(Future<BigInteger> future) {
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static int readInt() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    private static void print(BigInteger[] results) {
        for (int i = 0; i < results.length; i++) {
            System.out.printf("%d) Result = %s\n", i + 1, results[i]);
        }
    }
}
