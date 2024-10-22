package vgp.fx.controls.list.view;

import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import vgp.fx.application.CellViewerApplication;
import vgp.fx.controls.list.cell.color.ColorPickerListCell;

/**
 * A {@link ListView} contained within the return value for
 * {@link CellViewerApplication#createStartingScene()}.
 * 
 * @version 5.0
 */
public final class ColorListView extends ListView<Color> {

    /**
     * Constructs a new {@code ColorListView}
     * with the default characteristics.
     */
    public ColorListView() {
        super();
        setSkin(new ListEditorViewSkin2<>(this));
        setEditable(true);
        setCellFactory(ColorPickerListCell.forListView());
    }

}