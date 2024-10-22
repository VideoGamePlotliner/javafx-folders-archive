package vgp.fx.collections;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.transformation.TransformationList;
import javafx.util.Callback;
import vgp.fx.listenerhandler.ListenerAndHandlerBindings;

/**
 * A wrapper list bound to its source list by a callback.
 * 
 * @author [...]
 * @version 6.10
 * @param <E> same as superclass
 * @param <F> same as superclass
 * @since 1.9
 */
public class CallbackWrapperList<E, F> extends TransformationList<E, F> {

	/**
	 * A change made to this wrapper list&mdash;not its
	 * {@linkplain TransformationList#getSource() source list}.
	 * 
	 * @see CallbackWrapperList#sourceChanged(Change)
	 * 
	 * @version 6.0
	 */
	private final class ViewChange extends Change<E> {

		/**
		 * The backing change from the source list.
		 * <p>
		 * This backing change must not be {@code null}.
		 */
		private final Change<? extends F> sourceChange;

		/**
		 * Constructs a new {@code ViewChange} with the specified change from the source
		 * list.
		 * 
		 * @param sourceChange what connects this {@code ViewChange} to the source list
		 * 
		 * @throws NullPointerException if the parameter is {@code null}
		 */
		public ViewChange(Change<? extends F> sourceChange) {
			super(CallbackWrapperList.this);
			this.sourceChange = Objects.requireNonNull(sourceChange);
		}

		// /**
		//  * Returns the backing change from the
		//  * {@linkplain TransformationList#getSource() source list}.
		//  * 
		//  * @return what connects this {@code ViewChange} to the source list
		//  */
		// public final Change<? extends F> getSourceChange() {
		// 	return sourceChange;
		// }

		@Override
		public boolean next() {
			return sourceChange.next();
		}

		@Override
		public void reset() {
			sourceChange.reset();
		}

		@Override
		public int getFrom() {
			int sourceFrom = sourceChange.getFrom();
			int viewFrom = getViewIndex(sourceFrom);
			return viewFrom;
		}

		@Override
		public int getTo() {
			int sourceTo = sourceChange.getTo();
			int viewTo = getViewIndex(sourceTo);
			return viewTo;
		}

		@Override
		public List<E> getRemoved() {

			List<? extends F> sourceRemoved;
			ObservableList<? extends F> sourceRemoved_asObservableList;
			ObservableList<E> viewRemoved_asObservableList;
			List<E> viewRemoved;

			sourceRemoved = sourceChange.getRemoved();
			sourceRemoved_asObservableList = FXCollections.observableList(sourceRemoved);
			viewRemoved_asObservableList = new CallbackWrapperList<>(sourceRemoved_asObservableList, callback);
			viewRemoved = Collections.unmodifiableList(viewRemoved_asObservableList);

			return viewRemoved;
		}

		@Override
		protected int[] getPermutation() {
			if (!sourceChange.wasPermutated()) {
				return new int[0];
			}
			final int sourceFrom = sourceChange.getFrom();
			final int viewFrom = getViewIndex(sourceFrom);
			final int sourceTo = sourceChange.getTo();
			final int viewTo = getViewIndex(sourceTo);

			final int size = size();
			Objects.checkFromIndexSize(viewFrom, viewTo - viewFrom, size);

			final int[] viewPermutation = new int[viewTo - viewFrom];
			for (int i = 0; i < viewPermutation.length; i++) {
				final int oldViewIndex = i + viewFrom;
				final int oldSourceIndex = getSourceIndex(oldViewIndex);
				final int newSourceIndex = sourceChange.getPermutation(oldSourceIndex);
				final int newViewIndex = getViewIndex(newSourceIndex);
				viewPermutation[i] = newViewIndex;
			}
			return viewPermutation;
		}

		@Override
		public String toString() {
			return String.format("ViewChange [sourceChange=%s]", sourceChange);
		}
	}

	/**
	 * A function that converts an element from the
	 * {@linkplain TransformationList#getSource() source list} into an element for
	 * this wrapper list.
	 * <p>
	 * This function must not be {@code null}.
	 * 
	 * @see Callback
	 */
	private final Callback<F, E> callback;

	/**
	 * Constructs a new {@code CallbackWrapperList} with the specified backing list
	 * and callback.
	 * 
	 * @param source   the backing list
	 * @param callback the means to calculate this list's elements
	 * 
	 * @throws NullPointerException if either parameter is {@code null}
	 */
	public CallbackWrapperList(ObservableList<? extends F> source, Callback<F, E> callback) {
		super(Objects.requireNonNull(source));
		this.callback = Objects.requireNonNull(callback);
	}

	/**
	 * Returns the function that converts an element from the
	 * {@linkplain TransformationList#getSource() source list} into an element for
	 * this wrapper list.
	 * 
	 * @return what connects this list to its source list
	 * 
	 * @see Callback
	 */
	public final Callback<F, E> getCallback() {
		return callback;
	}

	@Override
	protected void sourceChanged(Change<? extends F> sourceChange) {
		this.fireChange(new ViewChange(sourceChange));
	}

	@Override
	public int getSourceIndex(int viewIndex) {
		return viewIndex;
	}

	@Override
	public int getViewIndex(int sourceIndex) {
		int viewIndex = sourceIndex;

		if (viewIndex < 0 || viewIndex >= this.size()) {
			return -1;
		}
		return viewIndex;
	}

	@Override
	public E get(int viewIndex) {
		int sourceIndex = getSourceIndex(viewIndex);
		F sourceElement = getSource().get(sourceIndex);
		E viewElement = callback.call(sourceElement);
		return viewElement;
	}

	@Override
	public int size() {
		return getSource().size();
	}

	/**
	 * The entry point for this class to be run from the command line.
	 * 
	 * @param args the string array passed from the command line
	 */
	public static void main(String[] args) {

		Callback<Integer, Object[]> callback = new Callback<Integer, Object[]>() {
			@Override
			public Object[] call(Integer i) {
				Objects.requireNonNull(i);
				return new Object[i];
			}
		};

		ObservableList<Integer> source = FXCollections.observableArrayList();
		ObservableList<Object[]> view = new CallbackWrapperList<>(source, callback);

		InvalidationListener invalidationListener = new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				System.out.println("START: invalidationListener");
				System.out.println(observable);
				System.out.println("END: invalidationListener");
			}
		};
		ListChangeListener<Integer> listChangeListener = new ListChangeListener<Integer>() {
			@Override
			public void onChanged(Change<? extends Integer> c) {
				System.out.println("START: listChangeListener");
				System.out.println(c);
				System.out.println("END: listChangeListener");
			}
		};
		ListChangeListener<Object[]> listChangeListener2 = new ListChangeListener<Object[]>() {
			@Override
			public void onChanged(Change<? extends Object[]> c) {
				System.out.println("START: listChangeListener2");
				System.out.println(c);
				System.out.println("END: listChangeListener2");
			}
		};

		ListenerAndHandlerBindings.addInvalidationListener(source, invalidationListener);
		ListenerAndHandlerBindings.addListChangeListener(source, listChangeListener);

		ListenerAndHandlerBindings.addInvalidationListener(view, invalidationListener);
		ListenerAndHandlerBindings.addListChangeListener(view, listChangeListener2);

		source.add(3);
		System.out.println(view);

		ListenerAndHandlerBindings.removeInvalidationListener(source, invalidationListener);
		ListenerAndHandlerBindings.removeListChangeListener(source, listChangeListener);

		ListenerAndHandlerBindings.removeInvalidationListener(view, invalidationListener);
		ListenerAndHandlerBindings.removeListChangeListener(view, listChangeListener2);

		source = null;
		view = null;
		invalidationListener = null;
		listChangeListener = null;
		listChangeListener2 = null;

		ListenerAndHandlerBindings.outputListenerBindings();
	}

}
