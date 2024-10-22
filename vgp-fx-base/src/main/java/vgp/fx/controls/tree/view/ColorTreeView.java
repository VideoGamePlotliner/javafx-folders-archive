package vgp.fx.controls.tree.view;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import vgp.fx.application.CellViewerApplication;
import vgp.fx.controls.list.cell.color.ColorPickerListCell;
import vgp.fx.controls.tree.cell.color.ColorPickerTreeCell;

/**
 * A {@link TreeView} contained within the return value for
 * {@link CellViewerApplication#createStartingScene()}.
 * 
 * @version 5.0
 */
public final class ColorTreeView extends TreeView<Color> {

    /**
     * Constructs a new {@code ColorTreeView}
     * with the default characteristics.
     */
    public ColorTreeView() {
        super();
        setSkin(colorTreeViewSkin(this));
        setEditable(true);
        setCellFactory(ColorPickerTreeCell.forTreeView());
    }

    // Intended as the skin for a ColorTreeView
    private static TreeEditorViewSkin2<Color> colorTreeViewSkin(ColorTreeView colorTreeView) {
        return new TreeEditorViewSkin2<>(colorTreeView) {
            @Override
            protected Callback<ListView<Color>, ListCell<Color>> createChildrenEditorCellFactory() {
                return ColorPickerListCell.forListView();
            }
        };
    }

}