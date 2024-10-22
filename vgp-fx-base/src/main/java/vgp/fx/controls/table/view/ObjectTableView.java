package vgp.fx.controls.table.view;

import javafx.collections.FXCollections;
import javafx.scene.control.TableView;
import vgp.fx.application.CellViewerApplication;

/**
 * A {@link TableView} contained within the return value for
 * {@link CellViewerApplication#createStartingScene()}.
 * 
 * @version 5.0
 */
public final class ObjectTableView extends TableView<Object> {

    /**
     * Constructs a new {@code ObjectTableView}
     * with the default characteristics.
     */
    public ObjectTableView() {
        super();
        setSkin(objectTableViewSkin(this));
        setEditable(true);
        setItems(FXCollections.observableArrayList());
    }

    // Intended as the skin for an ObjectTableView
    private static TableEditorViewSkin<Object> objectTableViewSkin(ObjectTableView objectTableView) {
        return new TableEditorViewSkin<>(objectTableView) {
            @Override
            protected Object createNewItem() {
                return new Object();
            }
        };
    }

}