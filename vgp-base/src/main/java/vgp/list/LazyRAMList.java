package vgp.list;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Spliterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import vgp.clone.CloneableBase;

/**
 * A random-access list that uses lazy evaluation to postpone its write requests.
 * <p>
 * This list's lazy evaluation forces each enqueued change to wait until a read request
 * is made via {@link #get}, {@link #set}, {@link #remove(int)}, etc.
 * <p>
 * When such a request is made, the queue is emptied, and each change is applied in the
 * order in which it was enqueued.
 * <p>
 * This list must be mutable, variable-size, and cloneable.
 * <p>
 * <em>
 * It should be stressed that parameter validations dependent on information about this
 * list's elements are postponed until changes' dequeuement.
 * This means that whether an index parameter is out-of-bounds is calculated when the change
 * queue is being emptied.
	 * (However, if said index is negative, that validation is not postponed.)
 * </em>
 * 
 * @author [...]
 * @version 2.0
 * @since 2.0
 */
public class LazyRAMList<E> extends ArrayList<E> implements CloneableBase {

	/**
	 * This list's collection of write requests.
	 * <p>
	 * This list's lazy evaluation forces each enqueued change to wait until a read request
	 * is made via {@link #get}, {@link #set}, {@link #remove(int)}, etc.
	 * <p>
	 * When such a request is made, this queue is emptied, and each change is applied in the
	 * order in which it was enqueued.
	 * <p>
	 * Do not create a getter method for this field.
	 * <p>
	 * <em>
	 * It should be stressed that parameter validations dependent on information about this
	 * list's elements are postponed until changes' dequeuement.
	 * This means that whether an index parameter is out-of-bounds is calculated when the change
	 * queue is being emptied.
	 * (However, if said index is negative, that validation is not postponed.)
	 * </em>
	 */
	private final Queue<Runnable> changes = new LinkedBlockingQueue<>();

	/**
	 * The {@code serialVersionUID} of this class, as specified by {@link Serializable}.
	 */
	private static final long serialVersionUID = 4127243943484579268L;

	/**
	 * Constructs a new, empty {@code LazyRAMList}.
	 */
	public LazyRAMList() {
		super();
	}

	/**
	 * Constructs a new {@code LazyRAMList} whose
	 * initial elements are copied from the parameter.
	 * 
	 * @param c  the collection to clone
	 * 
	 * @throws NullPointerException if the parameter is {@code null}
	 */
	public LazyRAMList(Collection<? extends E> c) {
		super(c);
	}

	/**
	 * Constructs a new, empty {@code LazyRAMList}
	 * with the specified initial capacity.
	 * 
	 * @param initialCapacity  a nonnegative integer
	 */
	public LazyRAMList(int initialCapacity) {
		super(initialCapacity);
	}

	@Override
	public boolean add(E e) {
		enqueueChange(() -> super.add(e));
		return true;
	}

	@Override
	public void add(int index, E element) {
		if (index < 0) {
			throw new IndexOutOfBoundsException("Negative index parameter: " + index);
		}
		enqueueChange(() -> super.add(index, element));
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		if (c == null) {
			throw new NullPointerException("Null collection parameter: " + c);
		}
		enqueueChange(() -> super.addAll(c));
		return !c.isEmpty();
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		if (index < 0) {
			throw new IndexOutOfBoundsException("Negative index parameter: " + index);
		}
		if (c == null) {
			throw new NullPointerException("Null collection parameter: " + c);
		}
		enqueueChange(() -> super.addAll(index, c));
		return !c.isEmpty();
	}

	/**
	 * Empty this list's change queue, and then apply each dequeued change in order.
	 * <p>
	 * <em>
	 * It should be stressed that parameter validations dependent on information about this
	 * list's elements are postponed until changes' dequeuement.
	 * This means that whether an index parameter is out-of-bounds is calculated when the change
	 * queue is being emptied.
	 * (However, if said index is negative, that validation is not postponed.)
	 * </em>
	 */
	protected final void applyChanges() {
		while (!changes.isEmpty()) {
			changes.remove().run();
		}
	}

	@Override
	public void clear() {
		enqueueChange(() -> super.clear());
	}

	@Override
	public LazyRAMList<E> clone() {
		applyChanges();
		return new LazyRAMList<E>(this);
	}

	@Override
	public boolean contains(Object o) {
		applyChanges();
		return super.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		applyChanges();
		return super.containsAll(c);
	}

	/**
	 * Postpone the {@linkplain Runnable#run() execution} of the parameter.
	 * <p>
	 * This list's lazy evaluation forces each enqueued change to wait until a read request
	 * is made via {@link #get}, {@link #set}, {@link #remove(int)}, etc.
	 * <p>
	 * When such a request is made, this queue is emptied, and each change is applied in the
	 * order in which it was enqueued.
	 * <p>
	 * <em>
	 * It should be stressed that parameter validations dependent on information about this
	 * list's elements are postponed until changes' dequeuement.
	 * This means that whether an index parameter is out-of-bounds is calculated when the change
	 * queue is being emptied.
	 * (However, if said index is negative, that validation is not postponed.)
	 * </em>
	 * 
	 * @param change  the write request whose execution to postpone
	 */
	protected final void enqueueChange(Runnable change) {
		changes.add(change);
	}

	@Override
	public void ensureCapacity(int minCapacity) {
		enqueueChange(() -> super.ensureCapacity(minCapacity));
	}

	@Override
	public boolean equals(Object o) {
		applyChanges();
		return super.equals(o);
	}

	@Override
	public void forEach(Consumer<? super E> action) {
		applyChanges();
		super.forEach(action);
	}

	@Override
	public E get(int index) {
		applyChanges();
		return super.get(index);
	}

	@Override
	public int hashCode() {
		applyChanges();
		return super.hashCode();
	}

	@Override
	public int indexOf(Object o) {
		applyChanges();
		return super.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		applyChanges();
		return super.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		applyChanges();
		return super.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		applyChanges();
		return super.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		applyChanges();
		return super.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		applyChanges();
		return super.listIterator(index);
	}

	@Override
	public Stream<E> parallelStream() {
		applyChanges();
		return super.parallelStream();
	}

	@Override
	public E remove(int index) {
		applyChanges();
		return super.remove(index);
	}

	@Override
	public boolean remove(Object o) {
		applyChanges();
		return super.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		applyChanges();
		return super.removeAll(c);
	}

	@Override
	public boolean removeIf(Predicate<? super E> filter) {
		applyChanges();
		return super.removeIf(filter);
	}

	@Override
	public void removeRange(int fromIndex, int toIndex) {
		if (fromIndex < 0) {
			throw new IndexOutOfBoundsException("Negative fromIndex parameter: " + fromIndex);
		}
		if (toIndex < 0) {
			throw new IndexOutOfBoundsException("Negative toIndex parameter: " + toIndex);
		}
		if (toIndex < fromIndex) {
			throw new IndexOutOfBoundsException("fromIndex parameter (" + fromIndex
					+ ") cannot exceed toIndex parameter (" + toIndex + ").");
		}
		enqueueChange(() -> super.removeRange(fromIndex, toIndex));
	}

	@Override
	public void replaceAll(UnaryOperator<E> operator) {
		applyChanges();
		super.replaceAll(operator);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		applyChanges();
		return super.retainAll(c);
	}

	@Override
	public E set(int index, E element) {
		applyChanges();
		return super.set(index, element);
	}

	@Override
	public int size() {
		applyChanges();
		return super.size();
	}

	@Override
	public void sort(Comparator<? super E> c) {
		applyChanges();
		super.sort(c);
	}

	@Override
	public Spliterator<E> spliterator() {
		applyChanges();
		return super.spliterator();
	}

	@Override
	public Stream<E> stream() {
		applyChanges();
		return super.stream();
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		applyChanges();
		return super.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		applyChanges();
		return super.toArray();
	}

	@Override
	public <T> T[] toArray(IntFunction<T[]> generator) {
		applyChanges();
		return super.toArray(generator);
	}

	@Override
	public <T> T[] toArray(T[] a) {
		applyChanges();
		return super.toArray(a);
	}

	@Override
	public String toString() {
		applyChanges();
		return super.toString();
	}

	@Override
	public void trimToSize() {
		enqueueChange(() -> super.trimToSize());
	}

	/**
	 * The entry point for this class to be run from the command line.
	 * 
	 * @param args the string array passed from the command line
	 */
	public static void main(String[] args) {
	
	}
}
