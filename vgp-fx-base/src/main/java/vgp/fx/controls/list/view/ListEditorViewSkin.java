package vgp.fx.controls.list.view;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tooltip;
import javafx.scene.control.skin.ListViewSkin;
import vgp.dispose.Disposable;
import vgp.fx.application.ApplicationBase;
import vgp.fx.listenerhandler.ListenerAndHandlerBindings;

/**
 * A {@link ListViewSkin} with buttons for structurally modifying the
 * {@link ListView#itemsProperty() items}
 * list of its {@link ListView}.
 * <p>
 * Currently, this class and its subclasses specify buttons for the following
 * actions:
 * <ul>
 * <li>Add a new item to this list.
 * <li>Insert new item(s) to this list at the selected row(s).
 * <li>Remove only the selected row(s) from this list.
 * <li>Purge this list of all occurrences of each selected item.
 * <li>Clear out all items from this list.
 * <li>Set each selected item to {@code null}.
 * </ul>
 * <p>
 * By default, this skin ensures that the user can select more than one element
 * in
 * that {@code items} list at a time.
 * 
 * @author [...]
 * @version 6.19
 * @param <T> the type of the {@link ListView#itemsProperty() items} of this
 *            skin's {@code ListView}
 * @see ListView
 * @see ListView#itemsProperty()
 * @since 2.1
 */
public class ListEditorViewSkin<T> extends ListViewSkin<T> implements Disposable {

	private ButtonBar buttonBar;
	private Button appendButton; // Append New Item
	private Button insertButton; // Insert at Selected Row(s)
	private Button removeButton; // Remove Selected Row(s)
	private Button purgeButton; // Purge All Occurrences
	private Button clearButton; // Clear All Items
	private Button nullItemButton; // Set Selected Item(s) to Null

	private final ChangeListener<MultipleSelectionModel<T>> selectionModelProperty_changeListener = (observable,
			oldValue, newValue) -> {
		verifyNotDisposed();

		// DO NOT USE: Objects.requireNonNull(newValue);

		System.out.println("ListEditorViewSkin.selectionModelProperty_changeListener.changed(" + observable + ", "
				+ oldValue + ", " + newValue + ")");
		System.out.println();

		if (newValue != null) {
			newValue.setSelectionMode(SelectionMode.MULTIPLE);
		}

		getSkinnable().layout();
	};

	private final WeakChangeListener<MultipleSelectionModel<T>> selectionModelProperty_weakChangeListener = new WeakChangeListener<>(
			selectionModelProperty_changeListener);

	private final EventHandler<ActionEvent> appendButton_onAction = event -> {
		verifyNotDisposed();

		System.out.println("ListEditorViewSkin.appendButton_onAction.handle(" + event + ")");
		System.out.println();

		getSkinnable().getItems().add(createNewItem());

		getSkinnable().layout();
	};

	private final EventHandler<ActionEvent> insertButton_onAction = event -> {
		verifyNotDisposed();

		System.out.println("ListEditorViewSkin.insertButton_onAction.handle(" + event + ")");
		System.out.println();

		final ObservableList<Integer> selectedIndices = getSkinnable().getSelectionModel().getSelectedIndices();

		final ObservableList<T> items = Objects.requireNonNull(getSkinnable().getItems());

		final PriorityQueue<Integer> indicesInDecreasingOrder = new PriorityQueue<>(Comparator.reverseOrder());
		indicesInDecreasingOrder.addAll(selectedIndices);

		while (!indicesInDecreasingOrder.isEmpty()) {
			// Explicit cast must be used here to make sure that add(int, Object) is called
			// -- not add(Object).
			// Also, createNewItem() is called for EACH index.
			Integer indexAsObject = indicesInDecreasingOrder.remove();
			int indexAsPrimitive = indexAsObject.intValue();
			items.add(indexAsPrimitive, createNewItem());
		}

		getSkinnable().layout();
	};

	private final EventHandler<ActionEvent> removeButton_onAction = event -> {
		verifyNotDisposed();

		System.out.println("ListEditorViewSkin.removeButton_onAction.handle(" + event + ")");
		System.out.println();

		final ObservableList<Integer> selectedIndices = getSkinnable().getSelectionModel().getSelectedIndices();

		final ObservableList<T> items = Objects.requireNonNull(getSkinnable().getItems());

		final PriorityQueue<Integer> indicesInDecreasingOrder = new PriorityQueue<>(Comparator.reverseOrder());
		indicesInDecreasingOrder.addAll(selectedIndices);

		while (!indicesInDecreasingOrder.isEmpty()) {
			// Explicit cast must be used here to make sure that remove(int) is called --
			// not remove(Object).
			Integer indexAsObject = indicesInDecreasingOrder.remove();
			int indexAsPrimitive = indexAsObject.intValue();
			items.remove(indexAsPrimitive);
		}

		getSkinnable().layout();
	};

	private final EventHandler<ActionEvent> purgeButton_onAction = event -> {
		verifyNotDisposed();

		System.out.println("ListEditorViewSkin.purgeButton_onAction.handle(" + event + ")");
		System.out.println();

		getSkinnable().getItems().removeAll(getSkinnable().getSelectionModel().getSelectedItems());

		getSkinnable().layout();
	};

	private final EventHandler<ActionEvent> clearButton_onAction = event -> {
		verifyNotDisposed();

		System.out.println("ListEditorViewSkin.clearButton_onAction.handle(" + event + ")");
		System.out.println();

		getSkinnable().getItems().clear();

		getSkinnable().layout();
	};

	private final EventHandler<ActionEvent> nullItemButton_onAction = event -> {
		verifyNotDisposed();

		System.out.println("ListEditorViewSkin.nullItemButton_onAction.handle(" + event + ")");
		System.out.println();

		ObservableList<Integer> selectedIndices = getSkinnable().getSelectionModel().getSelectedIndices();
		for (Integer indexAsObject : selectedIndices) {
			int indexAsPrimitive = indexAsObject.intValue();
			getSkinnable().getItems().set(indexAsPrimitive, null);
		}

		getSkinnable().layout();
	};

	/**
	 * Constructs a new {@code ListEditorViewSkin} with the specified
	 * {@code ListView}.
	 * 
	 * @param control the {@code ListView} to bind to this skin
	 */
	public ListEditorViewSkin(ListView<T> control) {
		super(control);
		control.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		control.setEditable(true);
		if (control.getCellFactory() == null) {
			control.setCellFactory(ApplicationBase.defaultListViewCellFactory());
		}
	}

	/**
	 * Returns this skin's {@code ButtonBar}'s buttons.
	 * <p>
	 * Use the code below to add a button to this skin when an
	 * override of {@link #initButtonBar()} is called:
	 * 
	 * <pre>
	 * getButtons().add(fooButton);
	 * </pre>
	 * <p>
	 * Use the code below to remove a button from this skin when an
	 * override of {@link #dispose()} is called:
	 * 
	 * <pre>
	 * getButtons().remove(barButton);
	 * </pre>
	 * <p>
	 * However, it's unnecessary to use that code in {@link #dispose()},
	 * because the default implementation already takes care of button
	 * removal by calling...
	 * 
	 * <pre>
	 * getButtons().clear();
	 * </pre>
	 * 
	 * @return the "buttons" list contained in the {@code ButtonBar} embedded in
	 *         this skin
	 * 
	 * @throws IllegalStateException if this skin has been {@linkplain #dispose()
	 *                               disposed}
	 * 
	 * @see ButtonBar#getButtons()
	 */
	final ObservableList<Node> getButtons() {
		verifyNotDisposed();

		// If needed, set up the ButtonBar.
		if (buttonBar == null) {
			initButtonBar();
		}
		return buttonBar.getButtons();
	}

	/**
	 * A helper method for {@link #layoutChildren}.
	 * <p>
	 * When overriding, always call the super-implementation
	 * <em>first.</em> (Otherwise, {@link #getButtons()} will
	 * throw an {@link IllegalStateException}.) For example,
	 * 
	 * <pre>
	 * public class CustomListEditorViewSkin&lt;T&gt; extends ListEditorViewSkin&lt;T&gt; {
	 * 
	 * 	private Button deselectButton; // Deselect the currently selected item
	 * 
	 * 	private final EventHandler<ActionEvent> deselectButton_onAction = event -> {
	 * 		verifyNotDisposed();
	 * 
	 * 		...
	 * 
	 * 		getSkinnable().layout();
	 * 	};
	 * 
	 * 	public CustomListEditorViewSkin(ListView&lt;T&gt; control) {
	 * 		super(control);
	 * 	}
	 * 
	 * 	&#64;Override
	 * 	protected void initButtonBar() {
	 * 
	 * 		// The first line of this override must
	 * 		// be a call to the super-implementation.
	 * 		super.initButtonBar();
	 * 
	 * 		// Add any necessary buttons to this
	 * 		// skin's ButtonBar.
	 * 
	 * 		deselectButton = new Button();
	 * 		deselectButton.setText("Deselect");
	 * 		ListenerAndHandlerBindings.setOnAction(deselectButton.onActionProperty(), this);
	 * 		deselectButton.setTooltip("Deselect the currently selected item.");
	 * 
	 * 		getButtons().add(deselectButton);
	 * 	}
	 * 
	 * }
	 * </pre>
	 * 
	 * @see #layoutChildren
	 */
	void initButtonBar() {
		// buttonBar must not be null after this method returns

		ListenerAndHandlerBindings.addChangeListener(getSkinnable().selectionModelProperty(),
				this.selectionModelProperty_weakChangeListener);

		buttonBar = new ButtonBar();
		getChildren().add(buttonBar);

		appendButton = new Button();
		appendButton.setText("Append");
		ListenerAndHandlerBindings.setOnAction(appendButton.onActionProperty(), this.appendButton_onAction);
		appendButton.setTooltip(new Tooltip("Add a new item to this list."));

		insertButton = new Button();
		insertButton.setText("Insert");
		ListenerAndHandlerBindings.setOnAction(insertButton.onActionProperty(), this.insertButton_onAction);
		insertButton.setTooltip(new Tooltip("Insert new item(s) to this list at the selected row(s)."));

		removeButton = new Button();
		removeButton.setText("Remove");
		ListenerAndHandlerBindings.setOnAction(removeButton.onActionProperty(), this.removeButton_onAction);
		removeButton.setTooltip(new Tooltip("Remove only the selected row(s) from this list."));

		purgeButton = new Button();
		purgeButton.setText("Purge");
		ListenerAndHandlerBindings.setOnAction(purgeButton.onActionProperty(), this.purgeButton_onAction);
		purgeButton.setTooltip(new Tooltip("Purge this list of all occurrences of each selected item."));

		clearButton = new Button();
		clearButton.setText("Clear");
		ListenerAndHandlerBindings.setOnAction(clearButton.onActionProperty(), this.clearButton_onAction);
		clearButton.setTooltip(new Tooltip("Clear out all items from this list."));

		nullItemButton = new Button();
		nullItemButton.setText("Null Item");
		ListenerAndHandlerBindings.setOnAction(nullItemButton.onActionProperty(), this.nullItemButton_onAction);
		nullItemButton.setTooltip(new Tooltip("Set each selected item to 'null'."));

		getButtons().add(appendButton);
		getButtons().add(insertButton);
		getButtons().add(removeButton);
		getButtons().add(purgeButton);
		getButtons().add(clearButton);
		getButtons().add(nullItemButton);

	}

	/**
	 * Construct a new object of the type {@code T}.
	 * <p>
	 * Use the code below to append to a new item to
	 * this skin's list of items:
	 * 
	 * <pre>
	 * getSkinnable().getItems().add(createNewItem());
	 * </pre>
	 * 
	 * @return {@code null} by default
	 */
	protected T createNewItem() {
		// This method is called by insertAtSelectedRows for as many times as there are
		// selected rows.
		// If two rows are selected, call this method twice.
		return null;
	}

	/**
	 * Refresh the {@link Button#disableProperty() disable} status of
	 * <em>each</em> button embedded in this skin's {@code ButtonBar}.
	 * 
	 * @param items a possibly-{@code null} list of items of the type {@code T}
	 */
	void enableOrDisableButtonsAccordingToTheGivenList(ObservableList<T> items) {
		System.out.println("enableOrDisableButtonsAccordingToTheGivenList(" + items + ")");
		System.out.println();
		boolean isNullList = items == null;
		appendButton.setDisable(isNullList);
		insertButton.setDisable(isNullList);
		removeButton.setDisable(isNullList);
		purgeButton.setDisable(isNullList);
		clearButton.setDisable(isNullList);
		nullItemButton.setDisable(isNullList);
		if (!isNullList) {
			boolean isEmptyList = items.isEmpty();
			removeButton.setDisable(isEmptyList);
			purgeButton.setDisable(isEmptyList);
			clearButton.setDisable(isEmptyList);
			nullItemButton.setDisable(isEmptyList);
		}
		boolean noSelectedIndices = getSkinnable().getSelectionModel().getSelectedIndices().isEmpty();
		insertButton.setDisable(noSelectedIndices);
		removeButton.setDisable(noSelectedIndices);
		purgeButton.setDisable(noSelectedIndices);
		nullItemButton.setDisable(noSelectedIndices);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * When overriding, always call the super-implementation
	 * <em>first.</em>
	 */
	@Override
	protected void layoutChildren(double x, double y, double w, double h) {
		System.out.println("ListEditorViewSkin.layoutChildren: " + Arrays.asList(x, y, w, h));
		System.out.println();

		super.layoutChildren(x, y, w, h);

		// If needed, set up the ButtonBar.
		if (buttonBar == null) {
			initButtonBar();
		}

		// Set the ButtonBar's dimensions.
		final Insets buttonBarInsets = Insets.EMPTY;
		final double buttonBarBaselineOffset = Node.BASELINE_OFFSET_SAME_AS_HEIGHT;
		final double buttonBarX = x;
		final double buttonBarY = y;
		final double buttonBarW = w;
		final double buttonBarH = h;
		layoutInArea(buttonBar, buttonBarX, buttonBarY, buttonBarW, buttonBarH, buttonBarBaselineOffset,
				buttonBarInsets, false, false, HPos.CENTER, VPos.BOTTOM);

		// Enable/disable each button.
		enableOrDisableButtonsAccordingToTheGivenList(getSkinnable().getItems());

		// Set the "uniform size" option for each button.
		for (Node node : getButtons()) {
			ButtonBar.setButtonUniformSize(node, false);
		}
	}

	/**
	 * Specify a width for this skin that ensures that its {@link ButtonBar}
	 * is fully visible to the user.
	 */
	@Override
	protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return 600;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * When overriding, always call the super-implementation <em>last.</em>
	 * That way, {@code getSkinnable()} is {@code null} after the call to
	 * that super-implementation.
	 * <p>
	 * Also, in order to preserve this method's idempotence (as specified by
	 * {@link Disposable#dispose()}), always start the override with this:
	 * 
	 * <pre>
	 * if (isDisposed()) {
	 * 	return; // This skin has already been disposed
	 * }
	 * </pre>
	 * <p>
	 * For example,
	 * 
	 * <pre>
	 * public class CustomListEditorViewSkin&lt;T&gt; extends ListEditorViewSkin&lt;T&gt; {
	 * 
	 * 	private Button deselectButton; // Deselect the currently selected item
	 * 
	 * 	private final EventHandler<ActionEvent> deselectButton_onAction = event -> {
	 * 		verifyNotDisposed();
	 * 
	 * 		...
	 * 
	 * 		getSkinnable().layout();
	 * 	};
	 * 
	 * 	public CustomListEditorViewSkin(ListView&lt;T&gt; control) {
	 * 		super(control);
	 * 	}
	 * 
	 * 	&#64;Override
	 * 	public void dispose() {
	 * 
	 * 		if (isDisposed()) {
	 * 			return; // This skin has already been disposed
	 * 		}
	 * 
	 * 		ApplicationBase.dispose(deselectButton);
	 * 		deselectButton = null;
	 * 
	 * 		// The last line of this override must
	 * 		// be a call to the super-implementation.
	 * 
	 * 		super.dispose(); // getSkinnable() is null after this line
	 * 	}
	 * 
	 * }
	 * </pre>
	 * 
	 * @see ApplicationBase#dispose(Button)
	 */
	@Override
	public void dispose() {
		if (isDisposed()) {
			return; // This skin has already been disposed
		}

		ListenerAndHandlerBindings.removeChangeListener(getSkinnable().selectionModelProperty(),
				this.selectionModelProperty_weakChangeListener);

		if (buttonBar != null) {
			getButtons().clear();
			getChildren().remove(buttonBar);
			buttonBar = null;
		}
		ApplicationBase.dispose(appendButton);
		ApplicationBase.dispose(insertButton);
		ApplicationBase.dispose(removeButton);
		ApplicationBase.dispose(purgeButton);
		ApplicationBase.dispose(clearButton);
		ApplicationBase.dispose(nullItemButton);
		appendButton = null;
		insertButton = null;
		removeButton = null;
		purgeButton = null;
		clearButton = null;
		nullItemButton = null;

		super.dispose(); // getSkinnable() is null after this line
	}

	@Override
	public boolean isDisposed() {
		return getSkinnable() == null;
	}

	@Override
	public String toString() {
		return "ListEditorViewSkin [skinnable=" + getSkinnable() + "]";
	}

}
