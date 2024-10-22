package vgp.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedHashMap;

import vgp.dispose.Disposable;

/**
 * A means to calculate the factorial of n.
 * 
 * @author (to be added)
 * @version 2.4
 * @since 2.4
 */
public final class Factorial implements Disposable {

    /**
     * Keys are values of n. Value for each key is the factorial of that key.
     */
    private final LinkedHashMap<Integer, BigInteger> factorialOfNCache = new LinkedHashMap<>();

    /**
     * Must never change from {@code true} to {@code false}.
     */
    private boolean disposed = false;

    /**
     * Create a new object of this class with default characteristics.
     */
    public Factorial() {
        super();
    }

    public BigInteger getFactorialOfN(int n) {
        verifyNotDisposed();
        if (n < 0) {
            throw new IllegalArgumentException("Negative value of n: " + n);
        } else if (n < 2) {
            return BigInteger.ONE;
        } else {
            int i = 2;
            BigInteger factorialOfI = BigInteger.TWO;
            while (i < n) {
                i++;
                if (factorialOfNCache.containsKey(i)) {
                    factorialOfI = factorialOfNCache.get(i);
                } else {
                    factorialOfI = factorialOfI.multiply(BigInteger.valueOf(i));
                    factorialOfNCache.put(i, factorialOfI);
                }
            }
            return factorialOfI;
        }
    }

    @Override
    public void dispose() {
        if (isDisposed()) {
            return;
        }
        disposed = true;
        factorialOfNCache.clear();
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }

    public static void main(String[] args) {
        final Factorial factorial = new Factorial();
        try {
            System.out.println("n\tgetFactorial(n)\tNanoseconds");
            for (int n = 0; n < 1000; n++) {
                final long before = System.nanoTime();
                final BigInteger nthNumber = factorial.getFactorialOfN(n);
                final long after = System.nanoTime();
                final long timeItTook = after - before;
                System.out.println(String.format("%d\t%e\t%20d ns", n, new BigDecimal(nthNumber), timeItTook));
            }
        } finally {
            factorial.dispose();
        }
    }
}
