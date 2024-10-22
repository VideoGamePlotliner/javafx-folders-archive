package vgp.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedHashMap;

import vgp.dispose.Disposable;

/**
 * A means to calculate the nth Fibonacci number.
 * 
 * @author (to be added)
 * @version 2.4
 * @since 2.4
 */
public final class Fibonacci implements Disposable {

    /**
     * Keys are values of n. Value for each key is that key's Fibonacci number.
     */
    private final LinkedHashMap<Integer, BigInteger> fibonnacciNthNumberCache = new LinkedHashMap<>();

    /**
     * Must never change from {@code true} to {@code false}.
     */
    private boolean disposed = false;

    /**
     * Create a new object of this class with default characteristics.
     */
    public Fibonacci() {
        super();
    }

    public BigInteger getNthNumber(int n) {
        verifyNotDisposed();
        if (n < 0) {
            throw new IllegalArgumentException("Negative value of n: " + n);
        } else if (n < 2) {
            return BigInteger.ONE;
        } else {
            if (!fibonnacciNthNumberCache.containsKey(n)) {
                BigInteger nthNumber = getNthNumber(n - 1);
                nthNumber = nthNumber.add(getNthNumber(n - 2));
                fibonnacciNthNumberCache.put(n, nthNumber);
            }
            return fibonnacciNthNumberCache.get(n);
        }
    }

    @Override
    public void dispose() {
        if (isDisposed()) {
            return;
        }
        disposed = true;
        fibonnacciNthNumberCache.clear();
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }

    public static void main(String[] args) {
        final Fibonacci fibonacci = new Fibonacci();
        try {
            System.out.println("n\tgetNthNumber(n)\tNanoseconds");
            for (int n = 0; n < 1000; n++) {
                final long before = System.nanoTime();
                final BigInteger nthNumber = fibonacci.getNthNumber(n);
                final long after = System.nanoTime();
                final long timeItTook = after - before;
                System.out.println(String.format("%d\t%e\t%20d ns", n, new BigDecimal(nthNumber), timeItTook));
            }
        } finally {
            fibonacci.dispose();
        }
    }
}
