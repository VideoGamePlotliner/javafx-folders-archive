package vgp.fx.controls.tree.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import vgp.fx.application.ApplicationBase;
import vgp.fx.listenerhandler.ListenerAndHandlerBindings;

/**
 * A {@code TreeViewEditorSkin} with additional buttons for expanding and
 * collapsing the
 * nodes of its {@link TreeView}.
 * <p>
 * Currently, in addition to the buttons specified by
 * {@code ListViewEditorSkin},
 * this class and its subclasses specify buttons for the following actions:
 * <ul>
 * <li>Expand each item in this tree.
 * <li>Collapse each item in this tree.
 * </ul>
 * <p>
 * By default, this skin will call {@link ApplicationBase#dispose(TreeItem)} on
 * {@code TreeItem}s that are removed from this skin's {@code TreeView}. If you
 * desire otherwise, use code similar to this:
 * 
 * <pre>
 * new TreeEditorViewSkin2&lt;&gt;(treeView, false)
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
 * @see TreeItem#expandedProperty()
 * @since 2.3
 */
public class TreeEditorViewSkin2<T> extends TreeEditorViewSkin<T> {

	private Button expandAllButton; // Expand All Nodes
	private Button collapseAllButton; // Collapse All nodes

	private final EventHandler<ActionEvent> expandAllButton_onAction = event -> {
		verifyNotDisposed();

		System.out.println("TreeEditorViewSkin2.expandAllButton_onAction.handle(" + event + ")");
		System.out.println();

		recursivelyExpand(getSkinnable().getRoot());

		getSkinnable().layout();
	};

	private final EventHandler<ActionEvent> collapseAllButton_onAction = event -> {
		verifyNotDisposed();

		System.out.println("TreeEditorViewSkin2.collapseAllButton_onAction.handle(" + event + ")");
		System.out.println();

		recursivelyCollapse(getSkinnable().getRoot());

		getSkinnable().layout();
	};

	/**
	 * Constructs a new {@code TreeEditorViewSkin2} with the specified
	 * {@code TreeView}.
	 * <p>
	 * By default, this skin will call {@link ApplicationBase#dispose(TreeItem)} on
	 * {@code TreeItem}s that are removed from this skin's {@code TreeView}.
	 * 
	 * @param control the {@code TreeView} to bind to this skin
	 */
	public TreeEditorViewSkin2(TreeView<T> control) {
		super(control);
	}

	/**
	 * Constructs a new {@code TreeEditorViewSkin2} with the specified
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
	public TreeEditorViewSkin2(TreeView<T> control, boolean disposeRemovedTreeItems) {
		super(control, disposeRemovedTreeItems);
	}

	private void recursivelyExpand(TreeItem<T> treeItem) {
		if (treeItem != null) {
			treeItem.setExpanded(true);
			treeItem.getChildren().forEach(this::recursivelyExpand);
		}
	}

	private void recursivelyCollapse(TreeItem<T> treeItem) {
		if (treeItem != null) {
			treeItem.setExpanded(false);
			treeItem.getChildren().forEach(this::recursivelyCollapse);
		}
	}

	/**
	 * Retrieve how many nodes are contained in this skin's {@code TreeView}.
	 * <p>
	 * This is different from {@link #getItemCount()}&mdash;which does not count
	 * descendants
	 * of collapsed nodes.
	 * 
	 * @return the size of this skin's {@code TreeView}
	 * 
	 * @throws IllegalStateException if this skin has been {@linkplain #dispose()
	 *                               disposed}
	 * 
	 * @see TreeView#getExpandedItemCount()
	 */
	private int size() {
		verifyNotDisposed();
		return ApplicationBase.size(getSkinnable());
	}

	@Override
	protected void initButtonBar() {
		super.initButtonBar();

		expandAllButton = new Button();
		expandAllButton.setText("Expand All");
		ListenerAndHandlerBindings.setOnAction(expandAllButton.onActionProperty(), this.expandAllButton_onAction);
		expandAllButton.setTooltip(new Tooltip("Expand each item in this tree."));

		collapseAllButton = new Button();
		collapseAllButton.setText("Collapse All");
		ListenerAndHandlerBindings.setOnAction(collapseAllButton.onActionProperty(), this.collapseAllButton_onAction);
		collapseAllButton.setTooltip(new Tooltip("Collapse each item in this tree."));

		getButtons().add(expandAllButton);
		getButtons().add(collapseAllButton);
	}

	// Always call the SUPER-implementation first.
	@Override
	protected void layoutChildren(double x, double y, double w, double h) {
		super.layoutChildren(x, y, w, h);

		// To clarify, a TreeItem is "expanded" if and only if it AND its ancestors are
		// expanded.
		// Conversely, a TreeItem is "collapsed" if and only if it AND/OR one or more of
		// its ancestors are collapsed.

		// Each item is either collapsed or expanded.
		// Hence, one of the following is true of this tree right now:
		// 1. This tree is empty (i.e., there are no items).
		// 2. All items are expanded, and no items are collapsed.
		// 3. Some items are expanded, and some items are collapsed.
		// 4. No items are expanded, and all items are collapsed.

		int expandedCount = getSkinnable().getExpandedItemCount();
		int size = size();
		boolean emptyTree = getSkinnable().getRoot() == null;
		boolean allItemsExpanded = (expandedCount >= size);
		boolean allItemsCollapsed = (expandedCount <= 1);

		System.out.println("expandedCount == " + expandedCount);
		System.out.println("size == " + size);
		System.out.println("emptyTree == " + emptyTree);
		System.out.println("allItemsExpanded == " + allItemsExpanded);
		System.out.println("allItemsCollapsed == " + allItemsCollapsed);
		System.out.println();

		if (emptyTree) {
			expandAllButton.setDisable(true); // Disable if all items are expanded; enable otherwise.
			collapseAllButton.setDisable(true); // Disable if all items are collapsed; enable otherwise.
		} else {
			expandAllButton.setDisable(allItemsExpanded); // Disable if all items are expanded; enable otherwise.
			collapseAllButton.setDisable(allItemsCollapsed); // Disable if all items are collapsed; enable otherwise.
		}

	}

	@Override
	protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return 150 + super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
	}

	@Override
	public void dispose() {

		if (isDisposed()) {
			return; // This skin has already been disposed
		}

		ApplicationBase.dispose(expandAllButton);
		ApplicationBase.dispose(collapseAllButton);
		expandAllButton = null;
		collapseAllButton = null;

		// The last line of this override must
		// be a call to the super-implementation.

		super.dispose(); // getSkinnable() is null after this line
	}

	@Override
	public String toString() {
		return "TreeEditorViewSkin2 [skinnable=" + getSkinnable() + "]";
	}

}
