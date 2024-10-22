package vgp.fx.controls.table.view;

import java.util.Arrays;
import java.util.Objects;

import javafx.beans.property.ObjectProperty;
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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeView;
import javafx.scene.control.skin.TableViewSkin;
import javafx.scene.control.skin.TreeViewSkin;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import vgp.dispose.Disposable;
import vgp.fx.application.ApplicationBase;
import vgp.fx.controls.list.cell.tablecolumn.TableColumnListCell;
import vgp.fx.controls.list.view.ListEditorViewSkin2;
import vgp.fx.controls.tree.cell.tablecolumn.TableColumnTreeCell;
import vgp.fx.controls.tree.view.TreeEditorViewSkin_BoundItems;
import vgp.fx.listenerhandler.ListenerAndHandlerBindings;

/**
 * A {@link TableViewSkin} with buttons for structurally modifying its
 * {@link TableView}.
 * <p>
 * Currently, this class and its subclasses specify buttons for the following
 * actions:
 * <ul>
 * <li>Open a window that has an editor for the {@code TableView}'s items.
 * <li>Open a window that has an editor for the {@code TableView}'s columns.
 * </ul>
 * <p>
 * By default, this skin ensures that the user can select more than one element
 * in that {@code items} list at a time.
 * 
 * @author (to be added)
 * @version 6.16
 * @see TableView
 * @since 2.12
 */
public class TableEditorViewSkin<T> extends TableViewSkin<T> implements Disposable {

    /**
     * @version 5.0
     * @since 5.0
     */
    private final class ListEditorViewSkin_TableItems extends ListEditorViewSkin2<T> {

        /**
         * Constructs a new {@code ListEditorViewSkin_TableItems} with the specified
         * {@code ListView}.
         * 
         * @param control the {@code ListView} to bind to this skin
         */
        public ListEditorViewSkin_TableItems(ListView<T> control) {
            super(Objects.requireNonNull(control));
        }

        @Override
        protected T createNewItem() {
            return TableEditorViewSkin.this.createNewItem();
        }
    }

    /**
     * @version 4.1
     */
    private final class TreeEditorViewSkin_TableColumns extends TreeEditorViewSkin_BoundItems<TableColumn<T, ?>> {

        /**
         * Constructs a new {@code TreeEditorViewSkin_BoundItems} with the specified
         * {@code TreeView}.
         * <p>
         * By default, this skin will show its TreeView's root.
         * 
         * @param control the {@code TreeView} to bind to this skin
         */
        public TreeEditorViewSkin_TableColumns(TreeView<TableColumn<T, ?>> control) {
            super(Objects.requireNonNull(control));
            if (!control.isShowRoot()) {
                control.setShowRoot(true);
            }
        }

        /**
         * {@inheritDoc}
         * <p>
         * A {@code null} input refers to the {@code TableView}, in which case the
         * return value is the {@code TableView}'s columns.
         * 
         * @throws IllegalStateException if this skin and/or the {@code TableView}'s
         *                               skin has been {@linkplain #dispose() disposed}
         */
        @Override
        protected ObservableList<TableColumn<T, ?>> getChildrenOfGivenBoundTreeNode(TableColumn<T, ?> boundTreeNode) {
            this.verifyNotDisposed();
            TableEditorViewSkin.this.verifyNotDisposed();

            // A null input refers to the TableView, in which case the return value is the
            // TableView's columns.
            return Objects.requireNonNull(
                    (boundTreeNode == null) ? TableEditorViewSkin.this.getSkinnable().getColumns()
                            : boundTreeNode.getColumns());
        }

        /**
         * {@inheritDoc}
         * <p>
         * A {@code null} output refers to the {@code TableView}.
         */
        @Override
        protected TableColumn<T, ?> getRootOfBoundTree() {
            return null;
        }

        @Override
        protected TableColumn<T, ?> createNewValue() {
            return new TableColumn<>();
        }

        @Override
        protected Callback<ListView<TableColumn<T, ?>>, ListCell<TableColumn<T, ?>>> createChildrenEditorCellFactory() {
            return TableColumnListCell.forListView();
        }
    }

    private ButtonBar buttonBar;
    private Button editItemsButton; // Edit Items List
    private Button editColumnsButton; // Edit Columns of TableView

    private final EventHandler<ActionEvent> editItemsButton_onAction = event -> {
        verifyNotDisposed();

        System.out.println("TableEditorViewSkin.editItemsButton_onAction.handle(" + event + ")");
        System.out.println();

        TableView<T> tableView = getSkinnable();
        if (tableView != null) {
            Scene scene = tableView.getScene();
            if (scene != null) {
                Window ownerWindow = scene.getWindow();
                if (ownerWindow != null) {
                    // Declare
                    ListView<T> listView;
                    Stage stage;
                    ObjectProperty<ObservableList<T>> itemsProperty;
                    ObjectProperty<ObservableList<T>> itemsProperty2;

                    // Initialize
                    listView = new ListView<>();
                    stage = new Stage();
                    itemsProperty = getSkinnable().itemsProperty();
                    itemsProperty2 = listView.itemsProperty();

                    try {
                        // Use
                        listView.setItems(getSkinnable().getItems());
                        itemsProperty2.bindBidirectional(itemsProperty);
                        listView.setCellFactory(this.createItemsEditorCellFactory());
                        listView.setSkin(new ListEditorViewSkin_TableItems(listView));
                        stage.initOwner(ownerWindow);
                        stage.initModality(Modality.WINDOW_MODAL);
                        stage.setTitle("Items Editor");
                        stage.setScene(new Scene(/* new Pane */(listView)));
                        listView.scrollTo(0);
                        stage.showAndWait();
                    } finally {
                        // Dispose
                        itemsProperty2.unbindBidirectional(itemsProperty);
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
                    }
                }
            }
        }

        getSkinnable().layout();
    };

    private final EventHandler<ActionEvent> editColumnsButton_onAction = event -> {
        verifyNotDisposed();

        System.out.println("TableEditorViewSkin.editColumnsButton_onAction.handle(" + event + ")");
        System.out.println();

        TableView<T> tableView = getSkinnable();
        if (tableView != null) {
            Scene scene = tableView.getScene();
            if (scene != null) {
                Window ownerWindow = scene.getWindow();
                if (ownerWindow != null) {
                    // Declare
                    TreeView<TableColumn<T, ?>> treeView;
                    Stage stage;

                    // Initialize
                    treeView = new TreeView<>();
                    stage = new Stage();

                    try {
                        // Use
                        treeView.setCellFactory(TableColumnTreeCell.forTreeView());
                        treeView.setSkin(new TreeEditorViewSkin_TableColumns(treeView));
                        // fillColumnsEditorTreeView(treeView, tableView);
                        stage.initOwner(ownerWindow);
                        stage.initModality(Modality.WINDOW_MODAL);
                        stage.setTitle("Columns Editor");
                        stage.setScene(new Scene(/* new Pane */(treeView)));
                        stage.showAndWait();
                    } finally {
                        // Dispose
                        // emptyColumnsEditorTreeView(treeView, tableView);
                        if (treeView != null) {
                            treeView.setSkin(new TreeViewSkin<>(treeView));
                            treeView.setCellFactory(null);
                            treeView = null;
                        }
                        if (stage != null) {
                            stage.setTitle(null);
                            stage.setScene(null);
                            stage = null;
                        }
                    }
                }
            }
        }

        getSkinnable().layout();
    };

    /**
     * Constructs a new {@code TableEditorViewSkin} with the specified
     * {@code TableView}.
     * 
     * @param control the {@code TableView} to bind to this skin
     */
    public TableEditorViewSkin(TableView<T> control) {
        super(control);
        control.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        control.setEditable(true);
        control.setTableMenuButtonVisible(true);
        if (control.getSortPolicy() == null) {
            control.setSortPolicy(ApplicationBase.defaultTableViewSortPolicy());
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
    private ObservableList<Node> getButtons() {
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
     * by this skin's "Edit Items" button.
     * 
     * @return a non-{@code null} cell factory
     * 
     * @see ListView#cellFactoryProperty()
     */
    protected Callback<ListView<T>, ListCell<T>> createItemsEditorCellFactory() {
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
     * public class CustomTableEditorViewSkin&lt;T&gt; extends TableEditorViewSkin&lt;T&gt; {
     * 
     *     private Button deselectButton; // Deselect the currently selected item
     * 
     *     public CustomTableEditorViewSkin(TableView&lt;T&gt; control) {
     *         super(control);
     *     }
     * 
     *     &#64;Override
     *     protected void initButtonBar() {
     * 
     *         // The first line of this override must
     *         // be a call to the super-implementation.
     *         super.initButtonBar();
     * 
     *         // Add any necessary buttons to this
     *         // skin's ButtonBar.
     * 
     *         deselectButton = new Button();
     *         deselectButton.setText("Deselect");
     *         ListenerAndHandlerBindings.setOnAction(deselectButton.onActionProperty(), this);
     *         deselectButton.setTooltip("Deselect the currently selected item.");
     * 
     *         getButtons().add(deselectButton);
     *     }
     * 
     * }
     * </pre>
     * 
     * @see #layoutChildren
     */
    private void initButtonBar() {
        // buttonBar must not be null after this method returns

        buttonBar = new ButtonBar();
        getChildren().add(buttonBar);

        editItemsButton = new Button();
        editItemsButton.setText("Edit Items");
        ListenerAndHandlerBindings.setOnAction(editItemsButton.onActionProperty(), this.editItemsButton_onAction);
        editItemsButton.setTooltip(new Tooltip("Open a window that has an editor for"
                + " this table's items (each of which corresponds to a row in this table)."));

        editColumnsButton = new Button();
        editColumnsButton.setText("Edit Columns");
        ListenerAndHandlerBindings.setOnAction(editColumnsButton.onActionProperty(), this.editColumnsButton_onAction);
        editColumnsButton.setTooltip(new Tooltip("Open a window that has an editor for"
                + " this table's columns."));

        getButtons().add(editItemsButton);
        getButtons().add(editColumnsButton);
    }

    /**
     * Construct a new object of the type {@code T}.
     * <p>
     * Use the code below to append to a new item to
     * this skin's list of items:
     * 
     * <pre>
     * getItems().add(createNewItem());
     * </pre>
     * 
     * @return {@code null} by default
     */
    protected T createNewItem() {
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
        System.out.println("TableEditorViewSkin.layoutChildren: " + Arrays.asList(x, y, w, h));
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

        // Set the "uniform size" option for each button.
        for (Node node : getButtons()) {
            ButtonBar.setButtonUniformSize(node, false);
        }
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
     *     return; // This skin has already been disposed
     * }
     * </pre>
     * <p>
     * For example,
     * 
     * <pre>
     * public class CustomTableEditorViewSkin&lt;T&gt; extends TableEditorViewSkin&lt;T&gt; {
     * 
     *     private Button deselectButton; // Deselect the currently selected item
     * 
     *     public CustomTableEditorViewSkin(TableView&lt;T&gt; control) {
     *         super(control);
     *     }
     * 
     *     &#64;Override
     *     public void dispose() {
     * 
     *         if (isDisposed()) {
     *             return; // This skin has already been disposed
     *         }
     * 
     *         ApplicationBase.dispose(deselectButton);
     *         deselectButton = null;
     * 
     *         // The last line of this override must
     *         // be a call to the super-implementation.
     * 
     *         super.dispose(); // getSkinnable() is null after this line
     *     }
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

        if (buttonBar != null) {
            getButtons().clear();
            getChildren().remove(buttonBar);
            buttonBar = null;
        }
        ApplicationBase.dispose(editItemsButton);
        ApplicationBase.dispose(editColumnsButton);
        editItemsButton = null;
        editColumnsButton = null;

        super.dispose(); // getSkinnable() is null after this line
    }

    @Override
    public boolean isDisposed() {
        return getSkinnable() == null;
    }

    @Override
    public String toString() {
        return "TableEditorViewSkin [skinnable=" + getSkinnable() + "]";
    }

}
