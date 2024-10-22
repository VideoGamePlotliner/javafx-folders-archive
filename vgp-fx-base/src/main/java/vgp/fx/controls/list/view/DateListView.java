package vgp.fx.controls.list.view;

import java.time.LocalDate;

import javafx.scene.control.ListView;
import vgp.fx.application.CellViewerApplication;
import vgp.fx.controls.list.cell.date.DatePickerListCell;

/**
 * A {@link ListView} contained within the return value for
 * {@link CellViewerApplication#createStartingScene()}.
 * 
 * @version 5.0
 */
public final class DateListView extends ListView<LocalDate> {

    /**
     * Constructs a new {@code DateListView}
     * with the default characteristics.
     */
    public DateListView() {
        super();
        setSkin(new ListEditorViewSkin2<>(this));
        setEditable(true);
        setCellFactory(DatePickerListCell.forListView());
    }

}