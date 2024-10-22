package vgp.fx.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
// import java.util.SequencedCollection;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ModifiableObservableListBase;
import javafx.collections.ObservableList;
import vgp.clone.CloneableBase;
import vgp.dispose.Disposable;
import vgp.fx.listenerhandler.ListenerAndHandlerBindings;

/**
 * A custom implementation of {@link ObservableList}.
 * <p>
 * For simplicity's sake, this implementation is {@link RandomAccess}.
 * <p>
 * Feel free to add non-inherited methods to this class.
 * <p>
 * Listeners are not removed when {@link #dispose()} is called. Either manually
 * remove them when you need to, or just use weak listeners.
 * <p>
 * Call {@link #verifyNotDisposed()} at the start of every method in this class,
 * except for {@link #dispose()}, {@link #isDisposed()}, and {@code static}
 * methods.
 * <p>
 * Do not use nor override the {@link #finalize()} method in this class, because
 * that method is {@linkplain Deprecated#forRemoval() deprecated for removal}.
 * <p>
 * Do not use nor override the methods declared in
 * {@code java.util.SequencedCollection} in this class, because
 * {@code SequencedCollection} is incredibly recent and therefore non-standard.
 * 
 * @author (to be added)
 * @version 6.7
 * @param <E> a type shared by each element in this list
 * @since 4.6
 */
public class CustomObservableList<E> extends ModifiableObservableListBase<E>
        implements RandomAccess, Disposable, CloneableBase {

    /**
     * @version 5.7
     * @since 5.7
     */
    private final class BackingArrayList extends ArrayList<E> {
        private final int getModCount() {
            return this.modCount;
        }

        @Override
        protected void removeRange(int fromIndex, int toIndex) {
            super.removeRange(fromIndex, toIndex);
        }
    }

    private boolean disposed = false;
    private final BackingArrayList backingList = new BackingArrayList();

    /**
     * Create an empty list.
     */
    public CustomObservableList() {
        super();
    }

    /**
     * Copy the given collection's contents to this list.
     * <p>
     * (Perform a shallow copy.)
     * 
     * @param c what to make a shallow copy of
     * 
     * @throws NullPointerException if the given collection is {@code null}
     */
    public CustomObservableList(Collection<? extends E> c) {
        super();
        this.addAll(Objects.requireNonNull(c));
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalStateException if this list is {@link #isDisposed() disposed}
     */
    @Override
    public CustomObservableList<E> clone() {
        verifyNotDisposed();
        return new CustomObservableList<>(this);
    }

    @Override
    public void dispose() {
        if (this.isDisposed()) {
            return;
        }
        disposed = true;
        backingList.clear();
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalStateException if this list is {@link #isDisposed() disposed}
     */
    @Override
    public E get(int index) {
        verifyNotDisposed();
        return backingList.get(index);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalStateException if this list is {@link #isDisposed() disposed}
     */
    @Override
    public int size() {
        verifyNotDisposed();
        return backingList.size();
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalStateException if this list is {@link #isDisposed() disposed}
     */
    @Override
    protected void doAdd(int index, E element) {
        verifyNotDisposed();
        backingList.add(index, element);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalStateException if this list is {@link #isDisposed() disposed}
     */
    @Override
    protected E doSet(int index, E element) {
        verifyNotDisposed();
        return backingList.set(index, element);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalStateException if this list is {@link #isDisposed() disposed}
     */
    @Override
    protected E doRemove(int index) {
        verifyNotDisposed();
        return backingList.remove(index);
    }

    /**
     * Swap the elements at the two given indices.
     * <p>
     * If the two given indices are the same, do nothing. Otherwise, make a
     * {@linkplain #nextPermutation(int, int, int[]) permutation change}.
     * 
     * @param i the index of the first element to swap
     * @param j the index of the second element to swap
     * 
     * @throws IllegalStateException     if this list is {@link #isDisposed()
     *                                   disposed}
     * @throws IndexOutOfBoundsException if either index is negative or is at least
     *                                   {@link #size()}
     * 
     * @see Collections#swap(List, int, int)
     */
    public void swap(int i, int j) {
        verifyNotDisposed();

        final int size = size();
        Objects.checkIndex(i, size);
        Objects.checkIndex(j, size);

        if (i == j) {
            return;
        }

        beginChange();
        try {
            final E firstElement = get(i);
            final E secondElement = get(j);
            doSet(i, secondElement);
            doSet(j, firstElement);

            final int from = Math.min(i, j);
            final int to = Math.max(i, j) + 1;
            final int[] perm = new int[to - from];
            for (int k = 0; k < perm.length; k++) {
                perm[k] = k + from;
            }
            perm[0] = to - 1;
            perm[perm.length - 1] = from;
            nextPermutation(from, to, perm);
        } finally {
            endChange();
        }
    }

    /**
     * Reverse all the elements in this list.
     * <p>
     * If this list has fewer than 2 elements, do nothing. Otherwise, make a
     * {@linkplain #nextPermutation(int, int, int[]) permutation change}.
     * 
     * @throws IllegalStateException if this list is {@link #isDisposed() disposed}
     * 
     * @see FXCollections#reverse(ObservableList)
     */
    public void reverse() {
        verifyNotDisposed();

        final int size = size();

        if (size < 2) {
            return;
        }

        beginChange();
        try {
            for (int i = 0; i < size; i++) {
                final int j = size - i - 1;
                if (i >= j) {
                    break;
                }
                final E firstElement = get(i);
                final E secondElement = get(j);
                doSet(i, secondElement);
                doSet(j, firstElement);
            }

            final int[] perm = new int[size];
            for (int i = 0; i < perm.length; i++) {
                final int j = size - i - 1;
                perm[i] = j;
            }

            nextPermutation(0, size, perm);
        } finally {
            endChange();
        }
    }

    /**
     * Rotate all the elements in this list by the given rotation distance.
     * <p>
     * For the exact definition of "rotation distance", please refer to
     * {@link Collections#rotate(List, int)}.
     * <p>
     * If {@code distance % size()} is zero, or if this list has fewer than 2
     * elements, do nothing. Otherwise, make a
     * {@linkplain #nextPermutation(int, int, int[]) permutation change}.
     * 
     * @param distance the signed rotation distance
     * 
     * @throws IllegalStateException if this list is {@link #isDisposed() disposed}
     * 
     * @see FXCollections#rotate(ObservableList, int)
     * @see Collections#rotate(List, int)
     */
    public void rotate(int distance) {
        verifyNotDisposed();

        final int size = size();

        if (size < 2) {
            return;
        }

        if (distance % size == 0) {
            return;
        }

        final List<E> listToUseForRotation = Collections.unmodifiableList(new ArrayList<>(this) {
            {
                Collections.rotate(this, distance);
            }
        });

        // DO NOT MODIFY listToUseForRotation IN ANY LINE IN THIS METHOD
        // THAT IS BELOW THIS LINE.

        beginChange();
        try {
            for (int i = 0; i < size; i++) {
                this.doSet(i, listToUseForRotation.get(i));
            }

            int equivalentDistance = distance % size;
            if (equivalentDistance < 0) {
                equivalentDistance += size;
            }

            final int[] perm = new int[size];
            for (int i = 0; i < size; i++) {
                perm[i] = (i + equivalentDistance) % size;
            }

            nextPermutation(0, size, perm);
        } finally {
            endChange();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalStateException if this list is {@link #isDisposed() disposed}
     */
    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        verifyNotDisposed();
        Objects.requireNonNull(filter);

        final int size = size();

        final ArrayList<Integer> list_firstParameter_nextRemove = new ArrayList<>();
        final ArrayList<ArrayList<E>> list_secondParameter_nextRemove = new ArrayList<>();

        beginChange();
        try {
            {
                boolean buildingParameterLists_nextRemove = false;
                int firstParameter_nextRemove = -1;
                ArrayList<E> secondParameter_nextRemove = null;

                for (int i = 0; i < size; i++) {
                    final E element = get(i);
                    final boolean mustRemoveTheElementAtThisIndex = filter.test(element);

                    if (mustRemoveTheElementAtThisIndex && buildingParameterLists_nextRemove) {

                        // Continue build
                        secondParameter_nextRemove.add(element);

                    } else if (mustRemoveTheElementAtThisIndex && !buildingParameterLists_nextRemove) {

                        // Start build
                        buildingParameterLists_nextRemove = true;
                        firstParameter_nextRemove = i;
                        secondParameter_nextRemove = new ArrayList<E>();
                        secondParameter_nextRemove.add(element);

                    } else if (!mustRemoveTheElementAtThisIndex && buildingParameterLists_nextRemove) {

                        // Finish build
                        buildingParameterLists_nextRemove = false;
                        list_firstParameter_nextRemove.add(firstParameter_nextRemove);
                        list_secondParameter_nextRemove.add(secondParameter_nextRemove);
                        firstParameter_nextRemove = -1;
                        secondParameter_nextRemove = null;
                    }
                }

                if (buildingParameterLists_nextRemove) {

                    // Finish build
                    buildingParameterLists_nextRemove = false;
                    list_firstParameter_nextRemove.add(firstParameter_nextRemove);
                    list_secondParameter_nextRemove.add(secondParameter_nextRemove);
                    firstParameter_nextRemove = -1;
                    secondParameter_nextRemove = null;
                }
            }

            final int listSize_firstParameter_nextRemove = list_firstParameter_nextRemove.size();
            final int listSize_secondParameter_nextRemove = list_secondParameter_nextRemove.size();

            if (listSize_firstParameter_nextRemove != listSize_secondParameter_nextRemove) {
                throw new Error("listSize_firstParameter_nextRemove != listSize_secondParameter_nextRemove");
            }

            final int oldModCountForBackingList = backingList.getModCount();
            final boolean wasAnythingRemoved = backingList.removeIf(filter);
            final int newModCountForBackingList = backingList.getModCount();

            for (int i = 0; i < listSize_firstParameter_nextRemove; i++) {
                nextRemove(list_firstParameter_nextRemove.get(i), list_secondParameter_nextRemove.get(i));
            }

            this.modCount += (newModCountForBackingList - oldModCountForBackingList);
            return wasAnythingRemoved;
        } finally {
            list_firstParameter_nextRemove.clear();
            list_secondParameter_nextRemove.clear();
            endChange();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalStateException if this list is {@link #isDisposed() disposed}
     */
    @Override
    public void removeRange(int fromIndex, int toIndex) {
        verifyNotDisposed();
        beginChange();
        try {
            final ArrayList<E> whatToRemove = new ArrayList<>(subList(fromIndex, toIndex));
            final int oldModCountForBackingList = backingList.getModCount();
            backingList.removeRange(fromIndex, toIndex);
            final int newModCountForBackingList = backingList.getModCount();
            nextRemove(fromIndex, whatToRemove);
            this.modCount += (newModCountForBackingList - oldModCountForBackingList);
        } finally {
            endChange();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalStateException if this list is {@link #isDisposed() disposed}
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        verifyNotDisposed();
        Objects.requireNonNull(c);
        final int thisOldSize = this.size();
        final int cSize = c.size();
        beginChange();
        try {
            final int oldModCountForBackingList = backingList.getModCount();
            final boolean wasAnythingAdded = backingList.addAll(c);
            final int newModCountForBackingList = backingList.getModCount();
            nextAdd(thisOldSize, thisOldSize + cSize);
            this.modCount += (newModCountForBackingList - oldModCountForBackingList);
            return wasAnythingAdded;
        } finally {
            endChange();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalStateException if this list is {@link #isDisposed() disposed}
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        verifyNotDisposed();
        Objects.requireNonNull(c);
        final int cSize = c.size();
        Objects.checkIndex(index, cSize + 1);
        beginChange();
        try {
            final int oldModCountForBackingList = backingList.getModCount();
            final boolean wasAnythingAdded = backingList.addAll(index, c);
            final int newModCountForBackingList = backingList.getModCount();
            nextAdd(index, index + cSize);
            this.modCount += (newModCountForBackingList - oldModCountForBackingList);
            return wasAnythingAdded;
        } finally {
            endChange();
        }
    }

    /**
     * Write information about this list to the standard output stream.
     * 
     * @throws IllegalStateException if this list is {@link #isDisposed() disposed}
     */
    private void output() {
        verifyNotDisposed();
        System.out.println("list is " + this);
        System.out.println("modCount is " + modCount);
        System.out.println("backingList.modCount is " + backingList.getModCount());
        System.out.println();
        System.out.println();
        System.out.println();
    }

    @Override
    public String toString() {
        return disposed ? "{Disposed CustomObservableList}" : super.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalStateException if this list is {@link #isDisposed() disposed}
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        verifyNotDisposed();
        Objects.requireNonNull(c);
        beginChange();
        try {
            System.out.println("================================UNTESTED================================");
            return this.removeIf(c::contains);
        } finally {
            endChange();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalStateException if this list is {@link #isDisposed() disposed}
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        verifyNotDisposed();
        Objects.requireNonNull(c);
        beginChange();
        try {
            System.out.println("================================UNTESTED================================");
            return this.removeIf(element -> !c.contains(element));
        } finally {
            endChange();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalStateException if this list is {@link #isDisposed() disposed}
     * @throws NullPointerException  if the parameter is {@code null}
     */
    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        verifyNotDisposed();
        Objects.requireNonNull(operator);
        beginChange();
        try {
            final int size = size();
            for (int i = 0; i < size; i++) {
                final E oldElement = get(i);
                final E newElement = operator.apply(oldElement);
                set(i, newElement);
            }
        } finally {
            endChange();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalStateException if this list is {@link #isDisposed() disposed}
     */
    @Override
    public void sort(Comparator<? super E> c) {
        verifyNotDisposed();
        Objects.requireNonNull(c);

        final int size = size();

        if (size < 2) {
            return;
        }

        final class IndexAndElementObject {
            private final int index;
            private final E element;

            private IndexAndElementObject(int index, E element) {
                super();
                this.index = index;
                this.element = element;
            }
        }

        final ArrayList<IndexAndElementObject> indexAndElementList = new ArrayList<>();
        beginChange();
        try {

            for (int i = 0; i < size; i++) {
                indexAndElementList.add(new IndexAndElementObject(i, get(i)));
            }

            indexAndElementList.sort((o1, o2) -> c.compare(o1.element, o2.element));

            for (int i = 0; i < size; i++) {
                doSet(i, indexAndElementList.get(i).element);
            }

            final int[] perm = new int[size];
            for (int i = 0; i < perm.length; i++) {
                perm[indexAndElementList.get(i).index] = i;
            }

            nextPermutation(0, size, perm);
        } finally {
            indexAndElementList.clear();
            endChange();
        }
    }

    /**
     * The entry point for this class to be run from the command line.
     * 
     * @param args the string array passed from the command line
     */
    public static void main(String[] args) {
        for (int i = 0; i < 25; i++) {
            final int j = -i;
            final int k = 3;
            int r = j % k;
            if (r < 0) {
                r += k;
            }
            System.out.println(j + ", " + k + " -> " + r);
        }
        final InvalidationListener invalidationListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                System.out.println("START: invalidationListener");
                System.out.println();
                System.out.println(observable);
                System.out.println();
                System.out.println("END: invalidationListener");
                System.out.println();
                System.out.println();
                System.out.println();
            }
        };
        final ListChangeListener<String> listChangeListener = new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                System.out.println("START: listChangeListener");
                System.out.println();
                System.out.println(c);
                System.out.println();
                if (c != null) {
                    while (c.next()) {
                        final int addedSize = c.getAddedSize();
                        final List<? extends String> addedSubList = c.getAddedSubList();
                        final int from = c.getFrom();
                        final int to = c.getTo();
                        final ObservableList<? extends String> list = c.getList();
                        final List<? extends String> removed = c.getRemoved();
                        final int removedSize = c.getRemovedSize();
                        final boolean wasAdded = c.wasAdded();
                        final boolean wasPermutated = c.wasPermutated();
                        final boolean wasRemoved = c.wasRemoved();
                        final boolean wasReplaced = c.wasReplaced();
                        final boolean wasUpdated = c.wasUpdated();

                        System.out.println("addedSize is " + addedSize);
                        System.out.println("addedSubList is " + addedSubList);
                        System.out.println("from is " + from);
                        System.out.println("to is " + to);
                        System.out.println("list is " + list);
                        System.out.println("removed is " + removed);
                        System.out.println("removedSize is " + removedSize);
                        System.out.println("wasAdded is " + wasAdded);
                        System.out.println("wasPermutated is " + wasPermutated);
                        if (wasPermutated) {
                            for (int oldIndex = from; oldIndex < to; oldIndex++) {
                                final int newIndex = c.getPermutation(oldIndex);
                                System.out.println("newIndex for " + oldIndex + " is " + newIndex);
                            }
                        }
                        System.out.println("wasRemoved is " + wasRemoved);
                        System.out.println("wasReplaced is " + wasReplaced);
                        System.out.println("wasUpdated is " + wasUpdated);
                        System.out.println();
                    }
                }
                System.out.println("END: listChangeListener");
                System.out.println();
                System.out.println();
                System.out.println();
            }
        };
        final CustomObservableList<String> list = new CustomObservableList<>();
        ListenerAndHandlerBindings.addInvalidationListener(list, invalidationListener);
        ListenerAndHandlerBindings.addListChangeListener(list, listChangeListener);
        try {
            if (!list.equals(Collections.emptyList())) {
                throw new Error("List mismatch");
            }

            list.output();

            list.add("Hello");

            if (!list.equals(Arrays.asList("Hello"))) {
                throw new Error("List mismatch");
            }

            list.output();

            list.add(0, "I");

            if (!list.equals(Arrays.asList("I", "Hello"))) {
                throw new Error("List mismatch");
            }

            list.output();

            list.add("am");

            if (!list.equals(Arrays.asList("I", "Hello", "am"))) {
                throw new Error("List mismatch");
            }

            list.output();

            list.add(2, "yours");

            if (!list.equals(Arrays.asList("I", "Hello", "yours", "am"))) {
                throw new Error("List mismatch");
            }

            list.output();

            list.add(1, "truly");

            if (!list.equals(Arrays.asList("I", "truly", "Hello", "yours", "am"))) {
                throw new Error("List mismatch");
            }

            list.output();

            list.add("How");

            if (!list.equals(Arrays.asList("I", "truly", "Hello", "yours", "am", "How"))) {
                throw new Error("List mismatch");
            }

            list.output();

            list.add(5, "are");

            if (!list.equals(Arrays.asList("I", "truly", "Hello", "yours", "am", "are", "How"))) {
                throw new Error("List mismatch");
            }

            list.output();

            list.add("you");

            if (!list.equals(Arrays.asList("I", "truly", "Hello", "yours", "am", "are", "How", "you"))) {
                throw new Error("List mismatch");
            }

            list.output();

            list.set(4, "Good");

            if (!list.equals(Arrays.asList("I", "truly", "Hello", "yours", "Good", "are", "How", "you"))) {
                throw new Error("List mismatch");
            }

            list.output();

            list.set(0, "thanks");

            if (!list.equals(Arrays.asList("thanks", "truly", "Hello", "yours", "Good", "are", "How", "you"))) {
                throw new Error("List mismatch");
            }

            list.output();

            list.swap(3, 6);

            if (!list.equals(Arrays.asList("thanks", "truly", "Hello", "How", "Good", "are", "yours", "you"))) {
                throw new Error("List mismatch");
            }

            list.output();

            list.reverse();

            if (!list.equals(Arrays.asList("you", "yours", "are", "Good", "How", "Hello", "truly", "thanks"))) {
                throw new Error("List mismatch");
            }

            list.output();

            list.rotate(-13);

            if (!list.equals(Arrays.asList("Hello", "truly", "thanks", "you", "yours", "are", "Good", "How"))) {
                throw new Error("List mismatch");
            }

            list.output();

            if (!list.removeIf(string -> ((string == null) ? false : string.contains("a")))) {
                throw new Error("Failed to remove anything");
            }

            if (!list.equals(Arrays.asList("Hello", "truly", "you", "yours", "Good", "How"))) {
                throw new Error("List mismatch");
            }

            list.output();

            list.removeRange(1, 3);

            if (!list.equals(Arrays.asList("Hello", "yours", "Good", "How"))) {
                throw new Error("List mismatch");
            }

            list.output();

            list.addAll(1, Arrays.asList("We", "must", "behave"));

            if (!list.equals(Arrays.asList("Hello", "We", "must", "behave", "yours", "Good", "How"))) {
                throw new Error("List mismatch");
            }

            list.output();

            list.addAll(Arrays.asList("Okay", "sure"));

            if (!list.equals(Arrays.asList("Hello", "We", "must", "behave", "yours", "Good", "How", "Okay", "sure"))) {
                throw new Error("List mismatch");
            }

            list.output();

            if (list.removeIf(string -> false)) {
                throw new Error("Tried to remove something");
            }

            if (!list.removeIf(string -> ((string == null) ? false : string.contains("e")))) {
                throw new Error("Failed to remove anything");
            }

            if (!list.equals(Arrays.asList("must", "yours", "Good", "How", "Okay"))) {
                throw new Error("List mismatch");
            }

            list.output();

            if (!list.removeIf(string -> ((string == null) ? false
                    : (string.isEmpty() ? false : Character.isUpperCase(string.charAt(0)))))) {
                throw new Error("Failed to remove anything");
            }

            if (!list.equals(Arrays.asList("must", "yours"))) {
                throw new Error("List mismatch");
            }

            list.output();

            list.addAll("Testing", "this", "list", "class");

            if (!list.equals(Arrays.asList("must", "yours", "Testing", "this", "list", "class"))) {
                throw new Error("List mismatch");
            }

            list.output();

            list.replaceAll(string -> (string == null ? null : new StringBuilder(string).reverse().toString()));

            if (!list.equals(Arrays.asList("tsum", "sruoy", "gnitseT", "siht", "tsil", "ssalc"))) {
                throw new Error("List mismatch");
            }

            list.output();

            list.sort(Comparator.naturalOrder());

            if (!list.equals(Arrays.asList("gnitseT", "siht", "sruoy", "ssalc", "tsil", "tsum"))) {
                throw new Error("List mismatch");
            }

            list.output();

            list.clear();

            if (!list.equals(Collections.emptyList())) {
                throw new Error("List mismatch");
            }

            list.output();
        } finally {
            // What happens if you dispose before removing listeners? Last I checked,
            // nothing.
            list.dispose();
            ListenerAndHandlerBindings.removeInvalidationListener(list, invalidationListener);
            ListenerAndHandlerBindings.removeListChangeListener(list, listChangeListener);
        }
    }

}
