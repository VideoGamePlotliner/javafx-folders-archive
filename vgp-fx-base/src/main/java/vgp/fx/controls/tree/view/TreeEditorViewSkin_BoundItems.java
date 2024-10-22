package vgp.fx.controls.tree.view;

import java.util.ArrayList;
import java.util.Objects;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.event.WeakEventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.control.TreeView;
import vgp.fx.application.ApplicationBase;
import vgp.fx.collections.CallbackWrapperList;
import vgp.fx.listenerhandler.ListenerAndHandlerBindings;

/**
 * A {@code TreeViewEditorSkin} whose {@link TreeItem}s are each bound to a node
 * in some other tree.
 * <p>
 * This way, modifications to that other tree (let's call it a "bound tree") are
 * {@linkplain ObservableList observably} passed to {@linkplain #getSkinnable()
 * this skin's tree}, and vice versa.
 * <p>
 * Likewise, modifications to a subtree in that bound tree (let's call it a
 * "bound subtree") are {@linkplain ObservableList observably} passed to the
 * corresponding subtree in {@linkplain #getSkinnable() this skin's tree}, and
 * vice versa.
 * <p>
 * By default, this skin will not call {@link ApplicationBase#dispose(TreeItem)}
 * on {@code TreeItem}s that are removed from this skin's {@code TreeView}.
 * <p>
 * Also, by default, this skin ensures that the user can only select one
 * {@link TreeItem} in the {@code TreeView} at a time.
 * <p>
 * <em>Warning: this class <strong>assumes</strong> that the bound tree is
 * always a
 * directed acyclic graph.</em>
 * 
 * @author [...]
 * @version 6.19
 * @param <T> the type of the values of the items of this skin's
 *            {@code TreeView}; also, the type of the nodes of
 *            this skin's "bound tree"
 * @see TreeView
 * @see TreeItem
 * @since 3.5
 */
public abstract class TreeEditorViewSkin_BoundItems<T> extends TreeEditorViewSkin2<T> {

    // If this tree changes, then we must forward a corresponding change to the
    // bound tree.
    private final ListChangeListener<TreeItem<T>> treeItem_children_listChangeListener = new ListChangeListener<>() {

        @Override
        public void onChanged(Change<? extends TreeItem<T>> c) {

            verifyNotDisposed();

            System.out.println("TreeEditorViewSkin_BoundItems.treeItem_children_" +
                    "listChangeListener.onChanged(" + c + ")");
            System.out.println();

            // DON'T USE: THIS REPLACES ROOT AND ALL ITS DESCENDANTS: makeThisTreeMirrorItsBoundTree();

            if (c != null) {
                final ObservableList<? extends TreeItem<T>> childrenOfTreeItem = c.getList();
                if (childrenOfTreeItem != null) {
                    final TreeItem<T> parentTreeItem = locateParent(childrenOfTreeItem);
                    if (parentTreeItem != null) {
                        final T valueOfParentTreeItem = parentTreeItem.getValue();
                        final ObservableList<T> childrenOfValueOfParentTreeItem = Objects
                                .requireNonNull(getChildrenOfGivenBoundTreeNode(valueOfParentTreeItem));
                        final ObservableList<T> callbackWrapperList = new CallbackWrapperList<T, TreeItem<T>>(
                                childrenOfTreeItem, treeItem -> (treeItem == null ? null : treeItem.getValue()));
                        childrenOfValueOfParentTreeItem.setAll(callbackWrapperList);
                    }
                }
            }

            getSkinnable().layout();
        }

        // null return value means not found; non-null return value means found.
        private TreeItem<T> locateParent(final ObservableList<? extends TreeItem<T>> childrenOfTreeItem) {
            Objects.requireNonNull(childrenOfTreeItem);
            return locateParentInSubtree(childrenOfTreeItem, getSkinnable().getRoot());
        }

        // null return value means not found; non-null return value means found.
        // Note: this method uses recursion.
        private TreeItem<T> locateParentInSubtree(final ObservableList<? extends TreeItem<T>> childrenOfTreeItem,
                final TreeItem<T> rootOfSubtree) {

            Objects.requireNonNull(childrenOfTreeItem);

            if (rootOfSubtree == null) {
                return null;
            }

            final ObservableList<TreeItem<T>> childrenOfRootOfSubtree = rootOfSubtree.getChildren();

            if (childrenOfRootOfSubtree == childrenOfTreeItem) {
                // Indeed, use == not equals()
                return rootOfSubtree;
            }

            for (TreeItem<T> childOfRootOfSubtree : childrenOfRootOfSubtree) {
                final TreeItem<T> searchResult = locateParentInSubtree(childrenOfTreeItem, childOfRootOfSubtree);
                if (searchResult != null) {
                    return searchResult;
                }
            }
            return null;
        }
    };

    private final WeakListChangeListener<TreeItem<T>> treeItem_children_weakListChangeListener = new WeakListChangeListener<>(
            treeItem_children_listChangeListener);

    // If this tree changes, then we must forward a corresponding change to the
    // bound tree.
    private final EventHandler<TreeModificationEvent<T>> root_treeNotificationEvent_eventHandler = new EventHandler<TreeModificationEvent<T>>() {
        @Override
        public void handle(TreeModificationEvent<T> event) {
            verifyNotDisposed();

            System.out.println("TreeEditorViewSkin_BoundItems.root_treeNotificationEvent_eventHandler.handle("
                    + event + ")");
            System.out.println();

            if (event != null) {
                final EventType<? extends Event> eventType = event.getEventType();
                if (eventType != null) {
                    final TreeItem<T> treeItem = event.getTreeItem();
                    if (treeItem != null) {
                        if (eventType.equals(TreeItem.valueChangedEvent())) {

                            final TreeItem<T> parent = treeItem.getParent();
                            final T newValue = event.getNewValue();

                            if (parent == null) {
                                // treeItem is root

                                // Indeed, use == not equals()
                                if (newValue != getRootOfBoundTree()) {
                                    throw new Error("Error in TreeEditorViewSkin_BoundItems."
                                            + "root_treeNotificationEvent_eventHandler: "
                                            + "newValue != getRootOfBoundTree()");
                                }
                            } else {
                                final ObservableList<TreeItem<T>> childrenOfParent = parent.getChildren();
                                int index = 0;
                                for (TreeItem<T> childOfParent : childrenOfParent) {

                                    // Indeed, use == not equals()
                                    if (childOfParent == treeItem) {
                                        break;
                                    } else {
                                        index++;
                                    }
                                }
                                Objects.checkIndex(index, childrenOfParent.size());

                                getChildrenOfGivenBoundTreeNode(parent.getValue()).set(index, newValue);
                            }

                        } else if (eventType.equals(TreeItem.childrenModificationEvent())) {

                            getAddedChildrenThatWereNotRemoved(event)
                                    .forEach(child -> addChildrenWeakListChangeListener(child));
                            getRemovedChildrenThatWereNotAdded(event)
                                    .forEach(child -> removeChildrenWeakListChangeListener(child));
                        }
                    }
                }
            }

            getSkinnable().layout();
        }

        private ArrayList<TreeItem<T>> getRemovedChildrenThatWereNotAdded(TreeModificationEvent<T> event) {
            final ArrayList<TreeItem<T>> children = new ArrayList<>();
            children.addAll(event.getRemovedChildren());
            children.removeAll(event.getAddedChildren());
            return children;
        }

        private ArrayList<TreeItem<T>> getAddedChildrenThatWereNotRemoved(TreeModificationEvent<T> event) {
            final ArrayList<TreeItem<T>> children = new ArrayList<>();
            children.addAll(event.getAddedChildren());
            children.removeAll(event.getRemovedChildren());
            return children;
        }
    };

    private final WeakEventHandler<TreeModificationEvent<T>> root_treeNotificationEvent_weakEventHandler = new WeakEventHandler<>(
            root_treeNotificationEvent_eventHandler);

    /**
     * If 'tree' is the TreeView bound to this skin, then 'oldRoot' is what
     * 'tree.getRoot()' was before 'tree' was bound to this skin, and 'oldRoot' is
     * also what 'tree.getRoot()' will be after this skin is disposed.
     */
    private TreeItem<T> oldRoot = null;

    // If this tree's root changes, then we must forward a corresponding change to
    // the bound tree.
    private final ChangeListener<TreeItem<T>> rootProperty_changeListener_BoundItems = new ChangeListener<TreeItem<T>>() {

        @Override
        public void changed(ObservableValue<? extends TreeItem<T>> observable, TreeItem<T> oldValue,
                TreeItem<T> newValue) {

            verifyNotDisposed();

            System.out.println("TreeEditorViewSkin_BoundItems.rootProperty_changeListener_BoundItems.changed("
                    + observable + ", " + oldValue + ", " + newValue + ")");
            System.out.println();

            if (oldValue != null && oldValue != oldRoot) {
                unbindFromTheBoundSubtreeForTheGivenTreeItem(oldValue, true);
            }
            if (newValue != null && newValue != oldRoot) {
                bindToTheBoundSubtreeForTheGivenTreeItem(newValue, true);
            }

            getSkinnable().layout();
        }

        // Note: this method uses recursion.
        private void unbindFromTheBoundSubtreeForTheGivenTreeItem(final TreeItem<T> treeItem, final boolean isRoot) {
            if (treeItem == null) {
                return;
            }

            verifyNotDisposed();

            if (isRoot) {
                ListenerAndHandlerBindings.removeEventHandler(treeItem, TreeItem.treeNotificationEvent(),
                        root_treeNotificationEvent_weakEventHandler);
            }

            removeChildrenWeakListChangeListener(treeItem);

            treeItem.getChildren().forEach(childOfTreeItem -> {
                unbindFromTheBoundSubtreeForTheGivenTreeItem(childOfTreeItem, false);
            });

            if (isRoot) {
                treeItem.setValue(null);
            }
        }

        // Note: this method uses recursion.
        private void bindToTheBoundSubtreeForTheGivenTreeItem(final TreeItem<T> treeItem, final boolean isRoot) {
            if (treeItem == null) {
                return;
            }

            verifyNotDisposed();

            if (isRoot) {
                treeItem.setValue(getRootOfBoundTree());
            }

            treeItem.getChildren().forEach(childOfTreeItem -> {
                bindToTheBoundSubtreeForTheGivenTreeItem(childOfTreeItem, false);
            });

            addChildrenWeakListChangeListener(treeItem);

            if (isRoot) {
                ListenerAndHandlerBindings.addEventHandler(treeItem, TreeItem.treeNotificationEvent(),
                        root_treeNotificationEvent_weakEventHandler);
            }
        }
    };

    private final WeakChangeListener<TreeItem<T>> rootProperty_weakChangeListener_BoundItems = new WeakChangeListener<>(
            rootProperty_changeListener_BoundItems);

    /**
     * Constructs a new {@code TreeEditorViewSkin_BoundItems} with the specified
     * {@code TreeView}.
     * <p>
     * By default, this skin will call not {@link ApplicationBase#dispose(TreeItem)}
     * on {@code TreeItem}s that are removed from this skin's {@code TreeView}.
     * 
     * @param control the {@code TreeView} to bind to this skin
     */
    public TreeEditorViewSkin_BoundItems(TreeView<T> control) {
        super(Objects.requireNonNull(control), false);

        ListenerAndHandlerBindings.addChangeListener(control.rootProperty(),
                this.rootProperty_weakChangeListener_BoundItems);

        oldRoot = control.getRoot();

        makeThisTreeMirrorItsBoundTree();
    }

    @Override
    public void dispose() {
        if (isDisposed()) {
            return; // This skin has already been disposed
        }

        if (getSkinnable().getRoot() != oldRoot) {
            getSkinnable().setRoot(oldRoot);
        }

        ListenerAndHandlerBindings.removeChangeListener(getSkinnable().rootProperty(),
                this.rootProperty_weakChangeListener_BoundItems);

        // The last line of this override must
        // be a call to the super-implementation.

        super.dispose(); // getSkinnable() is null after this line
    }

    /**
     * Given the input, return the list of its children.
     * <p>
     * Each input is a node in this skin's bound tree.
     * <p>
     * Null inputs are allowed, but interpreting them is up to the subclass.
     * Null outputs are not allowed.
     * 
     * @param boundTreeNode the (possibly {@code null}) node whose list of child
     *                      nodes to return
     * 
     * @return a non-{@code null} list of the given node's children
     * 
     * @throws IllegalStateException if this skin has been {@linkplain #dispose()
     *                               disposed}
     */
    protected abstract ObservableList<T> getChildrenOfGivenBoundTreeNode(T boundTreeNode);

    /**
     * Return the root node of this skin's bound tree.
     * 
     * @return the root of that other tree
     * 
     * @throws IllegalStateException if this skin has been {@linkplain #dispose()
     *                               disposed}
     */
    protected abstract T getRootOfBoundTree();

    // Always call the SUPER-implementation first.
    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h);
    }

    private void addChildrenWeakListChangeListener(final TreeItem<T> treeItem) {
        ListenerAndHandlerBindings.addListChangeListener(treeItem.getChildren(),
                treeItem_children_weakListChangeListener);
    }

    private void removeChildrenWeakListChangeListener(final TreeItem<T> treeItem) {
        ListenerAndHandlerBindings.removeListChangeListener(treeItem.getChildren(),
                treeItem_children_weakListChangeListener);
    }

    /**
     * <em>After</em> the bound tree's changes, we must forward a corresponding
     * change to this tree.
     * <p>
     * Call this method to do so&mdash;but only <em>after</em> the bound tree
     * changes.
     * 
     * @throws IllegalStateException if this skin has been {@linkplain #dispose()
     *                               disposed}
     */
    private final void makeThisTreeMirrorItsBoundTree() {
        verifyNotDisposed();

        System.out.println("TreeEditorViewSkin_BoundItems.makeThisTreeMirrorItsBoundTree()");
        System.out.println();

        final TreeItem<T> rootTreeItem = rebuildFromBoundSubtree(getRootOfBoundTree());

        getSkinnable().setRoot(rootTreeItem);

        getSkinnable().layout();
    }

    // Using recursion, return a new TreeItem whose descendants correspond to those
    // of the parameter.
    private TreeItem<T> rebuildFromBoundSubtree(final T rootOfBoundSubtree) {

        final ObservableList<T> childrenOfGivenBoundTreeNode = getChildrenOfGivenBoundTreeNode(rootOfBoundSubtree);
        final ObservableList<TreeItem<T>> childrenOfTreeItem = FXCollections.observableArrayList();

        for (T child : childrenOfGivenBoundTreeNode) {
            childrenOfTreeItem.add(rebuildFromBoundSubtree(child));
        }

        final TreeItem<T> treeItem = new TreeItem<>(rootOfBoundSubtree);
        treeItem.getChildren().setAll(childrenOfTreeItem);
        return treeItem;
    }

}
