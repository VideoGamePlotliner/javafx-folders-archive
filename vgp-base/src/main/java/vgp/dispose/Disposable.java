package vgp.dispose;

/**
 * An object that will permanently, flawlessly, idempotently
 * release resources.
 * <p>
 * A disposed object should disallow all method calls that would
 * access or modify its attributes.
 * <p>
 * As mentioned above, this disposal must be...
 * <ol>
 * <li>Permanent: this object cannot be un-disposed.
 * <li>Flawless: the {@link #dispose()} method cannot throw any instances of {@link Throwable}.
 * <li>Idempotent: if called a second time, the {@link #dispose()} method does nothing.
 * </ol>
 * <p>
 * Example:
 * <pre>
 * import java.util.Objects;
 * 
 * public class Matrix implements Disposable {
 * 
 *     private ObjectArray&lt;DoubleArray&gt; data;
 *     
 *     public Matrix(int rowCount, int colCount) {
 *         if (rowCount &lt;= 0) {
 *             throw new IllegalArgumentException("Nonpositive row count: " + rowCount);
 *         }
 *         if (colCount &lt;= 0) {
 *             throw new IllegalArgumentException("Nonpositive column count: " + colCount);
 *         }
 *         try {
 *             data = new ObjectArray&lt;&gt;(rowCount);
 *             for (int i = 0; i &lt; data.length(); i++) {
 *                 data.set(i, new DoubleArray(colCount)); 
 *             }
 *         } catch (RuntimeException | Error e) {
 *             // No need to let this object hog any memory
 *             // if its constructor didn't even finish!
 *             this.dispose();
 *             throw e;
 *         }
 *     }
 *     
 *     &#64;Override
 *     public void dispose() {
 *         if (data != null) {
 *             for (int i = 0; i &lt; data.length(); i++) {
 *                 if (data.get(i) != null) {
 *                     data.get(i).dispose();
 *                     data.set(i, null);
 *                 }
 *             }
 *             data.dispose();
 *             data = null;
 *         }
 *     }
 *     
 *     &#64;Override
 *     public void isDisposed() {
 *         return data == null;
 *     }
 *     
 *     // This class uses ZERO-based indices.
 *     public double get(int row, int col) throws IndexOutOfBoundsException {
 *     
 *         verifyNotDisposed(); // IMPORTANT!!!
 *         
 *         final int ROW_COUNT = data.length();
 *         final int COL_COUNT = data.get(row).length();
 *         Objects.checkIndex(row, ROW_COUNT);
 *         Objects.checkIndex(col, COL_COUNT);
 *         return data.get(row).get(col);
 *     }
 * }
 * </pre>
 * 
 * @author [...]
 * @version 1.9
 * @since 1.9
 */
public interface Disposable {

	/**
	 * Delete all of this object's fields' values.
	 * <p>
	 * As mentioned in this class's description, this disposal must be...
	 * <ol>
	 * <li>Permanent: this object cannot be un-disposed.
	 * <li>Flawless: this method cannot throw any instances of {@link Throwable}.
	 * <li>Idempotent: if called a second time, this method does nothing.
	 * </ol>
	 */
	void dispose();

	/**
	 * Find out whether this object can be used at all anymore.
	 * <p>
	 * If not, then it is disposed. If so, then it is not disposed.
	 * 
	 * @return {@code true} if this object is disposed as defined above; {@code false} otherwise
	 */
	boolean isDisposed();

	/**
	 * Verify that this object can be used at all anymore.
	 * <p>
	 * If not, then throw an exception. If so, then do nothing.
	 * 
	 * @throws IllegalStateException if this object is disposed as defined above
	 */
	default void verifyNotDisposed() {
		if (this.isDisposed()) {
			throw new IllegalStateException("This object is disposed");
		}
	}

}