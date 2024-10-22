package vgp.fx.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javafx.application.Application;
import javafx.beans.property.adapter.JavaBeanBooleanProperty;
import javafx.beans.property.adapter.JavaBeanBooleanPropertyBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import javafx.util.Callback;
import vgp.fx.listenerhandler.ListenerAndHandlerBindings;

/**
 * A skeleton concrete class that directly extends {@code Application}
 * and redefines that class's {@link #start} method.
 * <p>
 * This class's implementation of said method
 * <ol>
 * <li>sets the primary stage's title to {@code getClass().getName()},
 * <li>sets the primary stage's initial scene to the return value of
 * {@link #createStartingScene()}, and
 * <li>shows the primary stage.
 * </ol>
 * <p>
 * Subclasses should redefine the {@code createStartingScene} method
 * in order to return a non-{@code null} scene.
 * <p>
 * This class also contains a {@link #main} method in order to test
 * the default {@code start} implementation.
 * 
 * @author [...]
 * @version 6.9
 * @since 1.0
 */
public class ApplicationBase extends Application {

	/*
	 * Constructors
	 */

	/**
	 * Constructs a new {@code ApplicationBase}
	 * with the default characteristics.
	 * <p>
	 * This constructor is required by the
	 * documentation for {@link Application}.
	 */
	public ApplicationBase() {
		super();
	}

	/*
	 * Overriding methods
	 */

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(getClass().getName());
		primaryStage.setScene(createStartingScene());
		primaryStage.show();
	}

	/*
	 * Regular methods
	 */

	/**
	 * Returns the initial scene for {@code primaryStage}, the
	 * parameter for this object's {@link #start} method.
	 * <p>
	 * Subclasses must override this method in order to properly
	 * set up said stage for rendering.
	 * <p>
	 * The name for this method originates
	 * from the method {@code createDefaultSkin}
	 * in the class {@code Control}
	 * in the package {@code javafx.scene.control}
	 * in the module {@code javafx.controls}.
	 * <p>
	 * <strong>Implementation Note</strong>
	 * The default implementation returns {@code null}.
	 * 
	 * @return the starting scene
	 * 
	 * @throws Exception if an undesirable error occurs while attempting
	 *                   scene construction
	 */
	protected Scene createStartingScene() throws Exception {
		return null;
	}

	/**
	 * A callback suitable for {@link ListView#setCellFactory(Callback)}.
	 * 
	 * @param <T> the type of a {@code ListView}'s {@link ListView#itemsProperty()
	 *            items}
	 * 
	 * @return a new cell factory
	 */
	public static <T> Callback<ListView<T>, ListCell<T>> defaultListViewCellFactory() {
		return listView -> new ListCell<>() {
			@Override
			protected void updateItem(T item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.toString());
				}
			}
		};
	}

	/**
	 * A callback suitable for {@link TreeView#setCellFactory(Callback)}.
	 * 
	 * @param <T> the type of a {@code TreeView}'s nodes' values
	 * 
	 * @return a new cell factory
	 */
	public static <T> Callback<TreeView<T>, TreeCell<T>> defaultTreeViewCellFactory() {
		return treeView -> new TreeCell<>() {
			@Override
			protected void updateItem(T item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.toString());
				}
				ApplicationBase.setDisclosureNodeOpacity(this, empty ? 0.0 : 1.0);
			}
		};
	}

	/**
	 * A callback suitable for {@link TableColumn#setCellFactory(Callback)}.
	 * 
	 * @param <S> the type of a {@code TableView}'s {@link TableView#itemsProperty()
	 *            items}
	 * @param <T> the type of a {@code TableColumn}'s cells' values
	 * 
	 * @return a new cell factory
	 */
	@SuppressWarnings("unchecked")
	public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> defaultTableColumnCellFactory() {
		return tableColumn -> (TableCell<S, T>) TableColumn.DEFAULT_CELL_FACTORY.call(tableColumn);
	}

	/**
	 * A callback suitable for {@link TreeTableColumn#setCellFactory(Callback)}.
	 * 
	 * @param <S> the type of a {@code TreeTableView}'s nodes' values
	 * @param <T> the type of a {@code TreeTableColumn}'s cells' values
	 * 
	 * @return a new cell factory
	 */
	@SuppressWarnings("unchecked")
	public static <S, T> Callback<TreeTableColumn<S, T>, TreeTableCell<S, T>> defaultTreeTableColumnCellFactory() {
		return treeTableColumn -> (TreeTableCell<S, T>) TreeTableColumn.DEFAULT_CELL_FACTORY.call(treeTableColumn);
	}

	/**
	 * A callback suitable for {@link TableView#setSortPolicy(Callback)}.
	 * 
	 * @param <S> the type of a {@code TableView}'s {@link TableView#itemsProperty()
	 *            items}
	 * 
	 * @return a new sort policy
	 */
	public static <S> Callback<TableView<S>, Boolean> defaultTableViewSortPolicy() {
		return tableView -> TableView.DEFAULT_SORT_POLICY.call(tableView);
	}

	/**
	 * A callback suitable for {@link TreeTableView#setSortPolicy(Callback)}.
	 * 
	 * @param <S> the type of a {@code TreeTableView}'s nodes' values
	 * 
	 * @return a new sort policy
	 */
	public static <S> Callback<TreeTableView<S>, Boolean> defaultTreeTableViewSortPolicy() {
		return treeTableView -> TreeTableView.DEFAULT_SORT_POLICY.call(treeTableView);
	}

	/**
	 * Create a {@link JavaBeanBooleanProperty} whose "get" relies on
	 * "isDisabled" and whose "set" relies on "setDisable".
	 * 
	 * @param node the property's bean
	 * 
	 * @return a new {@code JavaBeanBooleanProperty} called "trulyDisabled"
	 * 
	 * @throws NullPointerException if the parameter is {@code null}
	 */
	public static JavaBeanBooleanProperty createProperty_trulyDisabled(Node node) {
		Objects.requireNonNull(node);

		JavaBeanBooleanPropertyBuilder builder;
		builder = JavaBeanBooleanPropertyBuilder.create();
		builder.bean(node);
		builder.name("trulyDisabled");
		builder.getter("isDisabled");
		builder.setter("setDisable");
		try {
			return builder.build();
		} catch (NoSuchMethodException e) {
			// It is expected that this block will never be executed.
			StringBuilder sb = new StringBuilder();
			sb.append("Method 'createProperty_trulyDisabled' ");
			sb.append("was unable to locate the appropriate ");
			sb.append("getter and setter methods.");
			throw new NoSuchMethodError(sb.toString());
		}
	}

	/**
	 * Helper method for the method {@code updateItem} in subclasses of
	 * {@link TreeCell}.
	 * <p>
	 * There is a bug in {@code TreeCell} where empty cells nonetheless display
	 * their
	 * {@linkplain TreeCell#disclosureNodeProperty() disclosure nodes}. Use this
	 * code
	 * when you override {@code updateItem}:
	 * 
	 * <pre>
	 * ApplicationBase.setDisclosureNodeOpacity(this, empty ? 0.0 : 1.0);
	 * </pre>
	 * 
	 * @param <T>      the type of the cell's contents
	 * 
	 * @param treeCell the cell whose disclosure node to edit
	 * @param opacity  the new opacity for the given cell's disclosure node
	 */
	public static <T> void setDisclosureNodeOpacity(TreeCell<T> treeCell, double opacity) {
		if (treeCell != null) {
			Node disclosureNode = treeCell.getDisclosureNode();
			if (disclosureNode != null) {
				disclosureNode.setOpacity(opacity);
			}
		}
	}

	/**
	 * Nulls out the parameter's
	 * {@link TreeItem#getChildren() children},
	 * {@link TreeItem#graphicProperty() graphic}, and
	 * {@link TreeItem#valueProperty() value}&mdash;
	 * but not its event handlers.
	 * <p>
	 * Also, this method disposes of the parameter's descendants.
	 * 
	 * @param <T>      the type of the parameter's value
	 * 
	 * @param treeItem the object whose properties to clear
	 */
	public static <T> void dispose(TreeItem<T> treeItem) {
		System.out.println("ApplicationBase.dispose(" + treeItem + ")");
		System.out.println();
		if (treeItem != null) {
			for (Iterator<TreeItem<T>> iterator = treeItem.getChildren().iterator(); iterator.hasNext();) {
				TreeItem<T> child = iterator.next();
				iterator.remove();
				dispose(child);
			}
			treeItem.setGraphic(null);
			treeItem.setValue(null);
			treeItem = null;
		}
	}

	/**
	 * Nulls out the parameter's
	 * {@link Button#textProperty() text},
	 * {@link Button#onActionProperty() onAction}, and
	 * {@link Button#tooltipProperty() tooltip}&mdash;
	 * but not its event handlers.
	 * 
	 * @param button the object whose properties to clear
	 */
	public static void dispose(Button button) {
		if (button != null) {
			button.setText(null);
			ListenerAndHandlerBindings.setOnAction(button.onActionProperty(), null);
			button.setTooltip(null);
		}
	}

	/**
	 * Returns the number of nodes in the parameter.
	 * <p>
	 * Each node is a {@link TreeItem}.
	 * 
	 * @param <T>      the type of the value of each node
	 * 
	 * @param treeView the tree in question
	 * 
	 * @return the parameter's size
	 * 
	 * @throws NullPointerException if the parameter is {@code null}
	 */
	public static <T> int size(TreeView<T> treeView) {
		if (treeView == null) {
			throw new NullPointerException("Null parameter");
		}
		return subtreeSize(treeView.getRoot());
	}

	/**
	 * Returns the number of nodes in the subtree represented by the parameter.
	 * <p>
	 * Each node is a {@link TreeItem}, and a subtree is "represented" by the
	 * parameter if and only if its root is the parameter.
	 * <p>
	 * <strong>Implementation Requirements</strong>
	 * <p>
	 * If the parameter is {@code null}, return 0. Otherwise, return one plus
	 * the size of each subtree represented by a child of the parameter.
	 * 
	 * @param <T>         the type of the value of each node
	 * 
	 * @param subtreeRoot the tree in question
	 * 
	 * @return the size of the subtree whose root is the parameter
	 */
	public static <T> int subtreeSize(TreeItem<T> subtreeRoot) {
		if (subtreeRoot == null) {
			return 0;
		}
		int returned = 1;
		for (TreeItem<T> child : subtreeRoot.getChildren()) {
			returned += subtreeSize(child);
		}
		return returned;
	}

	/**
	 * Swap the elements at the given indexes in the given list.
	 * 
	 * @implSpec A permutation {@linkplain ListChangeListener.Change change} must be
	 *           fired. If the indices are the same, nothing happens.
	 * 
	 * @param <E>    the type of each element in the given list
	 * @param list   the {@code ObservableList} containing the two elements to swap
	 * @param index1 the index of the first of the two elements to swap
	 * @param index2 the index of the second of the two elements to swap
	 * 
	 * @throws NullPointerException      if the given list is {@code null}
	 * @throws IndexOutOfBoundsException if at least one index is negative
	 *                                   or is greater than or equal to the
	 *                                   {@linkplain List#size() size} of
	 *                                   the given list
	 */
	public static <E> void swapElements(ObservableList<E> list, int index1, int index2) {
		Objects.requireNonNull(list);
		Objects.checkIndex(index1, list.size());
		Objects.checkIndex(index2, list.size());
		if (index1 > index2) {
			swapElements(list, index2, index1);
		} else if (index1 == index2) {
			// Do nothing.
		} else {
			// 0 <= index1 && index1 < index2 && index2 < list.size()

			List<E> list2 = new ArrayList<>(list);

			Collections.swap(list2, index1, index2);

			list.setAll(list2);
		}
	}

	/**
	 * Test the method {@link #swapElements(ObservableList, int, int)}.
	 */
	private static void testSwapElements() {
		ObservableList<Character> observableList = FXCollections
				.observableArrayList(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f'));
		ListChangeListener<Character> listener = c -> {
			System.out.println("=====");
			System.out.println("START");
			System.out.println("=====");
			System.out.println();
			System.out.println("c == " + c);
			if (c != null) {
				while (c.next()) {
					System.out.println();
					System.out.println("c.next() called");
					System.out.println();
					System.out.println("c == " + c);
					System.out.println();
					System.out.println("c.getAddedSize()    == " + c.getAddedSize());
					System.out.println("c.getAddedSubList() == " + c.getAddedSubList());
					System.out.println("c.getFrom()         == " + c.getFrom());
					System.out.println("c.getList()         == " + c.getList());
					System.out.println("c.getRemoved()      == " + c.getRemoved());
					System.out.println("c.getRemovedSize()  == " + c.getRemovedSize());
					System.out.println("c.getTo()           == " + c.getTo());
					System.out.println("c.wasAdded()        == " + c.wasAdded());
					System.out.println("c.wasPermutated()   == " + c.wasPermutated());
					System.out.println("c.wasRemoved()      == " + c.wasRemoved());
					System.out.println("c.wasReplaced()     == " + c.wasReplaced());
					System.out.println("c.wasUpdated()      == " + c.wasUpdated());
					if (c.wasPermutated()) {
						System.out.println();
						for (int i = c.getFrom(); i < c.getTo(); i++) {
							System.out.println("c.getPermutation(" + i + ") == " + c.getPermutation(i));
						}
					}
				}
			}
			System.out.println();
			System.out.println("=====");
			System.out.println(" END ");
			System.out.println("=====");
		};

		ListenerAndHandlerBindings.addListChangeListener(observableList, listener);
		swapElements(observableList, 2, 4);
		// observableList.sort(Comparator.reverseOrder());
		ListenerAndHandlerBindings.removeListChangeListener(observableList, listener);

		if (!observableList.equals(Arrays.asList('a', 'b', 'e', 'd', 'c', 'f'))) {
			throw new RuntimeException("'testSwapElements()' failed");
		}
	}

	/**
	 * The entry point for this launcher class. This method
	 * may call {@link Application#launch(String...)}.
	 * 
	 * @param args the string array passed from the command line
	 */
	public static void main(String[] args) {
		Application.launch(args);
		testSwapElements();
	}

}
