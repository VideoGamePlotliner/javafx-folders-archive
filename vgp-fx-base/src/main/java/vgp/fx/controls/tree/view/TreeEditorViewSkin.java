package vgp.fx.controls.tree.view;

import java.util.Arrays;
import java.util.Objects;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ModifiableObservableListBase;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.skin.TreeViewSkin;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import vgp.dispose.Disposable;
import vgp.fx.application.ApplicationBase;
import vgp.fx.controls.list.view.ListEditorViewSkin;
import vgp.fx.listenerhandler.ListenerAndHandlerBindings;

/**
 * A {@link TreeViewSkin} with buttons for structurally modifying its
 * {@link TreeView}.
 * <p>
 * Currently, this class and its subclasses specify buttons for the following
 * actions:
 * <ul>
 * <li>Remove the selected item and its descendants from this tree.
 * <li>Add a new child item to the selected item.
 * <li>If this tree is blank, create a new root for it.
 * <li>If this tree is not blank, set its root to {@code null}.
 * <li>Open a window that has an editor for the selected item's children.
 * <li>Set the value of the selected item to {@code null}.
 * </ul>
 * <p>
 * By default, this skin will call {@link ApplicationBase#dispose(TreeItem)} on
 * {@code TreeItem}s that are removed from this skin's {@code TreeView}. If you
 * desire otherwise, use code similar to this:
 * 
 * <pre>
 * new TreeEditorViewSkin&lt;&gt;(treeView, false)
 * </pre>
 * <p>
 * Also, by default, this skin ensures that the user can only select one
 * {@link TreeItem}
 * in the {@code TreeView} at a time.
 * 
 * @author [...]
 * @version 6.19
 * @param <T> the type of the nodes of this skin's {@code TreeView}
 * @see TreeView
 * @see TreeItem
 * @since 2.3
 */
public class TreeEditorViewSkin<T> extends TreeViewSkin<T> implements Disposable {

	/**
	 * @version 5.3
	 * @since 5.0
	 */
	private final class ListEditorViewSkin_ChildrenOfTreeItem extends ListEditorViewSkin<T> {

		/**
		 * Constructs a new {@code ListEditorViewSkin_ChildrenOfTreeItem} with the
		 * specified {@code ListView}.
		 * 
		 * @param control the {@code ListView} to bind to this skin
		 */
		private ListEditorViewSkin_ChildrenOfTreeItem(ListView<T> control) {
			super(Objects.requireNonNull(control));
		}

		@Override
		protected T createNewItem() {
			return TreeEditorViewSkin.this.createNewValue();
		}

		@Override
		protected void layoutChildren(double x, double y, double w, double h) {
			super.layoutChildren(x, y, w, h);
			TreeEditorViewSkin.this.getSkinnable().refresh();
		}
	}

	/**
	 * A view list that imports a reference to a source list of {@link TreeItem}s.
	 * <p>
	 * This class is used by the method
	 * {@link TreeEditorViewSkin#openChildrenEditor(ActionEvent)}.
	 * <p>
	 * Calling {@link #set} on this list will change the
	 * {@link TreeItem#valueProperty() value} of the {@code TreeItem} at the
	 * specified index&mdash;without constructing a new {@code TreeItem} to replace
	 * the one currently at that index.
	 * <p>
	 * Calling {@link #dispose()} on this list will merely dissociate it from its
	 * source&mdash; without making any further change to that source.
	 * 
	 * @author [...]
	 * @version 3.5
	 * @param <T>
	 * @since 2.3
	 */
	private static class WrapperList_ValuesOfTreeItems<T> extends ModifiableObservableListBase<T>
			implements Disposable {

		/**
		 * A backing list of {@code TreeItem} siblings.
		 * <p>
		 * This field is {@code null} if and only if its wrapper list
		 * has been {@linkplain #isDisposed() disposed}.
		 */
		private ObservableList<TreeItem<T>> source;

		/**
		 * Constructs a new {@code WrapperList_ValuesOfTreeItems}
		 * with the specified source list.
		 * 
		 * @param source a list of {@code TreeItem} siblings
		 */
		private WrapperList_ValuesOfTreeItems(ObservableList<TreeItem<T>> source) {
			super();
			if (source == null) {
				throw new NullPointerException("Null source");
			}
			this.source = source;
		}

		/**
		 * @throws IllegalStateException if this list has been {@linkplain #dispose()
		 *                               disposed}
		 */
		@Override
		public T get(int index) {
			verifyNotDisposed();
			return source.get(index).getValue();
		}

		/**
		 * @throws IllegalStateException if this list has been {@linkplain #dispose()
		 *                               disposed}
		 */
		@Override
		public int size() {
			verifyNotDisposed();
			return source.size();
		}

		/**
		 * @throws IllegalStateException if this list has been {@linkplain #dispose()
		 *                               disposed}
		 */
		@Override
		protected void doAdd(int index, T element) {
			verifyNotDisposed();
			source.add(index, new TreeItem<>(element));
		}

		/**
		 * @throws IllegalStateException if this list has been {@linkplain #dispose()
		 *                               disposed}
		 */
		@Override
		protected T doSet(int index, T element) {
			verifyNotDisposed();
			T oldValue = get(index);
			source.get(index).setValue(element);
			return oldValue;
		}

		/**
		 * @throws IllegalStateException if this list has been {@linkplain #dispose()
		 *                               disposed}
		 */
		@Override
		protected T doRemove(int index) {
			verifyNotDisposed();
			return source.remove(index).getValue();
		}

		@Override
		public void dispose() {
			source = null;
		}

		@Override
		public boolean isDisposed() {
			return source == null;
		}
	}

	private ButtonBar buttonBar;
	private Button removeButton; // Remove Selected Item
	private Button addChildButton; // Add New Child to Selected Item
	private Button newRootButton; // Change Root
	private Button nullRootButton; // Set Root to Null
	private Button editChildrenButton; // Edit Children of Selected Item
	private Button nullValueButton; // Set Value of Selected Item to Null

	private final boolean disposeRemovedTreeItems;

	private final EventHandler<ActionEvent> removeButton_onAction = event -> {
		verifyNotDisposed();

		System.out.println("TreeEditorViewSkin.removeButton_onAction.handle(" + event + ")");
		System.out.println();

		TreeItem<T> selectedItem = getSkinnable().getSelectionModel().getSelectedItem();
		if (selectedItem != null) {
			TreeItem<T> parent = selectedItem.getParent();
			if (parent == null) {
				getSkinnable().setRoot(null);
			} else {
				parent.getChildren().remove(selectedItem);
			}

			disposeTreeItemIfNeeded(selectedItem);
		}

		getSkinnable().layout();
	};

	private final EventHandler<ActionEvent> addChildButton_onAction = event -> {
		verifyNotDisposed();

		System.out.println("TreeEditorViewSkin.addChildButton_onAction.handle(" + event + ")");
		System.out.println();

		TreeItem<T> selectedItem = getSkinnable().getSelectionModel().getSelectedItem();
		if (selectedItem != null) {
			selectedItem.getChildren().add(new TreeItem<>(createNewValue()));
		}

		getSkinnable().layout();
	};

	private final EventHandler<ActionEvent> newRootButton_onAction = event -> {
		verifyNotDisposed();

		System.out.println("TreeEditorViewSkin.newRootButton_onAction.handle(" + event + ")");
		System.out.println();

		getSkinnable().setRoot(new TreeItem<>(createNewValue()));
		getSkinnable().getSelectionModel().clearAndSelect(0);

		getSkinnable().layout();
	};

	private final EventHandler<ActionEvent> nullRootButton_onAction = event -> {
		verifyNotDisposed();

		System.out.println("TreeEditorViewSkin.nullRootButton_onAction.handle(" + event + ")");
		System.out.println();

		getSkinnable().setRoot(null);

		getSkinnable().layout();
	};

	private final EventHandler<ActionEvent> editChildrenButton_onAction = event -> {
		verifyNotDisposed();

		System.out.println("TreeEditorViewSkin.editChildrenButton_onAction.handle(" + event + ")");
		System.out.println();

		TreeItem<T> selectedItem = getSkinnable().getSelectionModel().getSelectedItem();
		if (selectedItem != null) {
			ObservableList<TreeItem<T>> children = selectedItem.getChildren();
			if (children != null) {
				TreeView<T> treeView = getSkinnable();
				if (treeView != null) {
					Scene scene = treeView.getScene();
					if (scene != null) {
						Window window = scene.getWindow();
						if (window != null) {
							// Declare
							WrapperList_ValuesOfTreeItems<T> valuesOfChildTreeItems;
							ListView<T> listView;
							Stage stage;

							// Initialize
							valuesOfChildTreeItems = new WrapperList_ValuesOfTreeItems<>(children);
							listView = new ListView<>();
							stage = new Stage();

							try {
								// Use
								listView.setItems(valuesOfChildTreeItems);
								listView.setCellFactory(this.createChildrenEditorCellFactory());
								listView.setSkin(new ListEditorViewSkin_ChildrenOfTreeItem(listView));
								stage.initOwner(window);
								stage.initModality(Modality.WINDOW_MODAL);
								stage.setTitle("List Editor");
								stage.setScene(new Scene(/* new Pane */(listView)));
								stage.showAndWait();
							} finally {
								// Dispose
								if (listView != null) {
									listView.setItems(null);
									listView.setSkin(null);
									listView.setCellFactory(null);
									listView = null;
								}
								if (stage != null) {
									stage.setTitle(null);
									stage.setScene(null);
									stage = null;
								}
								if (valuesOfChildTreeItems != null) {
									valuesOfChildTreeItems.dispose();
									valuesOfChildTreeItems = null;
								}
							}
						}
					}
				}
			}
		}

		getSkinnable().layout();
	};

	private final EventHandler<ActionEvent> nullValueButton_onAction = event -> {
		verifyNotDisposed();

		System.out.println("TreeEditorViewSkin.nullValueButton_onAction.handle(" + event + ")");
		System.out.println();

		TreeItem<T> selectedItem = getSkinnable().getSelectionModel().getSelectedItem();
		if (selectedItem != null) {
			selectedItem.setValue(null);
		}

		getSkinnable().layout();
	};

	private final ChangeListener<MultipleSelectionModel<TreeItem<T>>> selectionModelProperty_changeListener = (
			observable, oldValue, newValue) -> {
		verifyNotDisposed();

		System.out.println("TreeEditorViewSkin.selectionModelProperty_changeListener.changed(" + observable + ", "
				+ oldValue + ", " + newValue + ")");
		System.out.println();

		if (newValue != null) {
			newValue.setSelectionMode(SelectionMode.SINGLE);
		}

		getSkinnable().layout();
	};

	private final WeakChangeListener<MultipleSelectionModel<TreeItem<T>>> selectionModelProperty_weakChangeListener = new WeakChangeListener<>(
			selectionModelProperty_changeListener);

	private final ChangeListener<TreeItem<T>> rootProperty_changeListener = (observable, oldValue, newValue) -> {
		verifyNotDisposed();

		System.out.println("TreeEditorViewSkin.rootProperty_changeListener.changed(" + observable + ", "
				+ oldValue + ", " + newValue + ")");
		System.out.println();

		disposeTreeItemIfNeeded(oldValue);

		getSkinnable().layout();
	};

	private final WeakChangeListener<TreeItem<T>> rootProperty_weakChangeListener = new WeakChangeListener<>(
			rootProperty_changeListener);

	/**
	 * Constructs a new {@code TreeEditorViewSkin} with the specified
	 * {@code TreeView}.
	 * <p>
	 * By default, this skin will call {@link ApplicationBase#dispose(TreeItem)} on
	 * {@code TreeItem}s that are removed from this skin's {@code TreeView}.
	 * 
	 * @param control the {@code TreeView} to bind to this skin
	 */
	public TreeEditorViewSkin(TreeView<T> control) {
		this(control, true);
	}

	/**
	 * Constructs a new {@code TreeEditorViewSkin} with the specified
	 * {@code TreeView}.
	 * 
	 * @param control                 the {@code TreeView} to bind to this skin
	 * 
	 * @param disposeRemovedTreeItems whether to call
	 *                                {@link ApplicationBase#dispose(TreeItem)}
	 *                                on {@code TreeItem}s that are removed from
	 *                                this skin's
	 *                                {@code TreeView}
	 */
	public TreeEditorViewSkin(TreeView<T> control, boolean disposeRemovedTreeItems) {
		super(control);
		control.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		control.setEditable(true);
		if (control.getCellFactory() == null) {
			control.setCellFactory(ApplicationBase.defaultTreeViewCellFactory());
		}
		this.disposeRemovedTreeItems = disposeRemovedTreeItems;
	}

	private void disposeTreeItemIfNeeded(TreeItem<T> selectedItem) {
		if (disposeRemovedTreeItems) {
			ApplicationBase.dispose(selectedItem);
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
	protected final ObservableList<Node> getButtons() {
		verifyNotDisposed();

		// If needed, set up the ButtonBar.
		if (buttonBar == null) {
			initButtonBar();
		}
		return buttonBar.getButtons();
	}

	/**
	 * Construct a cell factory for the {@code ListView} belonging to the window
	 * opened
	 * by this skin's "Edit Children" button.
	 * 
	 * @return a non-{@code null} cell factory
	 * 
	 * @see ListView#cellFactoryProperty()
	 */
	protected Callback<ListView<T>, ListCell<T>> createChildrenEditorCellFactory() {
		return ApplicationBase.defaultListViewCellFactory();
	}

	/**
	 * A helper method for {@link #layoutChildren}.
	 * <p>
	 * When overriding, always call the super-implementation
	 * <em>first.</em> (Otherwise, {@link #getButtons()} will
	 * throw an {@link IllegalStateException}.) For example,
	 * 
	 * <pre>
	 * public class CustomTreeEditorViewSkin&lt;T&gt; extends TreeEditorViewSkin&lt;T&gt; {
	 * 
	 * 	private Button deselectButton; // Deselect the currently selected item
	 * 
	 * 	public CustomTreeEditorViewSkin(TreeView&lt;T&gt; control) {
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

		ListenerAndHandlerBindings.addChangeListener(getSkinnable().rootProperty(),
				this.rootProperty_weakChangeListener);

		ListenerAndHandlerBindings.addChangeListener(getSkinnable().selectionModelProperty(),
				this.selectionModelProperty_weakChangeListener);

		buttonBar = new ButtonBar();
		getChildren().add(buttonBar);

		removeButton = new Button();
		removeButton.setText("Remove");
		ListenerAndHandlerBindings.setOnAction(removeButton.onActionProperty(), this.removeButton_onAction);
		removeButton.setTooltip(new Tooltip("Remove the selected item and its descendants from this tree."));

		addChildButton = new Button();
		addChildButton.setText("Add Child");
		ListenerAndHandlerBindings.setOnAction(addChildButton.onActionProperty(), this.addChildButton_onAction);
		addChildButton.setTooltip(new Tooltip("Add a new child item to the selected item."));

		newRootButton = new Button();
		newRootButton.setText("New Root");
		ListenerAndHandlerBindings.setOnAction(newRootButton.onActionProperty(), this.newRootButton_onAction);
		newRootButton.setTooltip(new Tooltip("If this tree is blank, create a new root for it."));

		nullRootButton = new Button();
		nullRootButton.setText("Null Root");
		ListenerAndHandlerBindings.setOnAction(nullRootButton.onActionProperty(), this.nullRootButton_onAction);
		nullRootButton.setTooltip(new Tooltip("If this tree is not blank, set its root to 'null'."));

		editChildrenButton = new Button();
		editChildrenButton.setText("Edit Children");
		ListenerAndHandlerBindings.setOnAction(editChildrenButton.onActionProperty(), this.editChildrenButton_onAction);
		editChildrenButton
				.setTooltip(new Tooltip("Open a window that has an editor for the selected item's children."));

		nullValueButton = new Button();
		nullValueButton.setText("Null Value");
		ListenerAndHandlerBindings.setOnAction(nullValueButton.onActionProperty(), this.nullValueButton_onAction);
		nullValueButton.setTooltip(new Tooltip("Set the value of the selected item to 'null'."));

		getButtons().add(removeButton);
		getButtons().add(addChildButton);
		getButtons().add(newRootButton);
		getButtons().add(nullRootButton);
		getButtons().add(editChildrenButton);
		getButtons().add(nullValueButton);
	}

	/**
	 * Construct a new object of the type {@code T}.
	 * <p>
	 * Use the code below to create a new {@link TreeItem}
	 * for this {@code TreeView}.
	 * 
	 * <pre>
	 * new TreeItem<>(createNewValue())
	 * </pre>
	 * 
	 * @return {@code null} by default
	 */
	protected T createNewValue() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * When overriding, always call the super-implementation
	 * <em>first.</em>
	 */
	@Override
	protected void layoutChildren(double x, double y, double w, double h) {
		System.out.println("TreeEditorViewSkin.layoutChildren: " + Arrays.asList(x, y, w, h));
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
		boolean anyItemSelected = getSkinnable().getSelectionModel().getSelectedItem() != null;
		boolean emptyTree = getSkinnable().getRoot() == null;
		removeButton.setDisable(!anyItemSelected); // i.e., enable if any item selected; disable otherwise
		addChildButton.setDisable(!anyItemSelected); // i.e., enable if any item selected; disable otherwise
		newRootButton.setDisable(!emptyTree); // i.e., enable if empty tree; disable otherwise
		nullRootButton.setDisable(emptyTree); // i.e., disable if empty tree; enable otherwise
		editChildrenButton.setDisable(!anyItemSelected); // i.e., enable if any item selected; disable otherwise
		nullValueButton.setDisable(!anyItemSelected); // i.e., enable if any item selected; disable otherwise

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
	 * public class CustomTreeEditorViewSkin&lt;T&gt; extends TreeEditorViewSkin&lt;T&gt; {
	 * 
	 * 	private Button deselectButton; // Deselect the currently selected item
	 * 
	 * 	public CustomTreeEditorViewSkin(TreeView&lt;T&gt; control) {
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
		if (getSkinnable() == null) {
			return; // This skin has already been disposed
		}

		ListenerAndHandlerBindings.removeChangeListener(getSkinnable().selectionModelProperty(),
				this.selectionModelProperty_weakChangeListener);

		ListenerAndHandlerBindings.removeChangeListener(getSkinnable().rootProperty(),
				this.rootProperty_weakChangeListener);

		if (buttonBar != null) {
			getButtons().clear();
			getChildren().remove(buttonBar);
			buttonBar = null;
		}
		ApplicationBase.dispose(removeButton);
		ApplicationBase.dispose(addChildButton);
		ApplicationBase.dispose(newRootButton);
		ApplicationBase.dispose(nullRootButton);
		ApplicationBase.dispose(editChildrenButton);
		ApplicationBase.dispose(nullValueButton);
		removeButton = null;
		addChildButton = null;
		newRootButton = null;
		nullRootButton = null;
		editChildrenButton = null;
		nullValueButton = null;

		super.dispose(); // getSkinnable() is null after this line
	}

	@Override
	public boolean isDisposed() {
		return getSkinnable() == null;
	}

	@Override
	public String toString() {
		return "TreeEditorViewSkin [skinnable=" + getSkinnable() + "]";
	}

}
