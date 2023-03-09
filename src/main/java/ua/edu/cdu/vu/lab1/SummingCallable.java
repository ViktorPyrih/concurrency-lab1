package ua.edu.cdu.vu.lab1;

import java.math.BigInteger;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class SummingCallable implements Callable<BigInteger> {

    private final CountDownLatch starter;
    private final int step;

    private BigInteger sum = BigInteger.ZERO;

    public SummingCallable(CountDownLatch starter, int step) {
        this.starter = starter;
        this.step = step;
    }

    @Override
    public BigInteger call() throws InterruptedException {
        starter.await();
        while (!Thread.interrupted()) {
            sum = sum.add(BigInteger.valueOf(step));
        }
        return sum;
    }
}
