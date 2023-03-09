package ua.edu.cdu.vu.lab1;

import java.math.BigInteger;
import java.util.concurrent.Callable;

public class SummingCallable implements Callable<BigInteger> {

    private final int step;

    private BigInteger sum = BigInteger.ZERO;

    public SummingCallable(int step) {
        this.step = step;
    }

    @Override
    public BigInteger call() {
        while (!Thread.interrupted()) {
            sum = sum.add(BigInteger.valueOf(step));
        }
        return sum;
    }
}
