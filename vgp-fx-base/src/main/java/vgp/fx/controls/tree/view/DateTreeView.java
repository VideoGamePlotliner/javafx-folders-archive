package vgp.fx.controls.tree.view;

import java.time.LocalDate;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeView;
import javafx.util.Callback;
import vgp.fx.application.CellViewerApplication;
import vgp.fx.controls.list.cell.date.DatePickerListCell;
import vgp.fx.controls.tree.cell.date.DatePickerTreeCell;

/**
 * A {@link TreeView} contained within the return value for
 * {@link CellViewerApplication#createStartingScene()}.
 * 
 * @version 5.0
 */
public final class DateTreeView extends TreeView<LocalDate> {

    /**
     * Constructs a new {@code DateTreeView}
     * with the default characteristics.
     */
    public DateTreeView() {
        super();
        setSkin(dateTreeViewSkin(this));
        setEditable(true);
        setCellFactory(DatePickerTreeCell.forTreeView());
    }

    // Intended as the skin for a DateTreeView
    private static TreeEditorViewSkin2<LocalDate> dateTreeViewSkin(DateTreeView dateTreeView) {
        return new TreeEditorViewSkin2<>(dateTreeView) {
            @Override
            protected Callback<ListView<LocalDate>, ListCell<LocalDate>> createChildrenEditorCellFactory() {
                return DatePickerListCell.forListView();
            }
        };
    }

}