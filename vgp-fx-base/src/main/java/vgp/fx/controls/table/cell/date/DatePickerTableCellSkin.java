package vgp.fx.controls.table.cell.date;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Skin;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.skin.TableCellSkin;
import vgp.dispose.Disposable;
import vgp.fx.listenerhandler.ListenerAndHandlerBindings;

/**
 * A {@link Skin} for {@link DatePickerTableCell} instances.
 * 
 * @author (to be added)
 * @version 6.9
 * @param <S> the type of the {@link TableView#itemsProperty() items} of this
 *            skin's cell's {@code TableView}
 * @see TableCell
 * @see LocalDate
 * @since 3.8
 */
public class DatePickerTableCellSkin<S> extends TableCellSkin<S, LocalDate> implements Disposable {

    private DatePicker datePicker;
    private final EventHandler<ActionEvent> datePicker_onAction = event -> {
        verifyNotDisposed();

        String subroutineText = "DatePickerTableCellSkin.datePicker_onAction.handle(" + event + ")";

        beginSubroutine(subroutineText);

        if (null != datePicker) {
            getSkinnable().commitEdit(datePicker.getValue());
        }

        endSubroutine(subroutineText);
    };

    /**
     * Constructs a new {@code DatePickerTableCellSkin} with the specified
     * {@code TableCell}.
     * 
     * @param control the {@code TableCell}
     */
    public DatePickerTableCellSkin(TableCell<S, LocalDate> control) {
        super(control);
        datePicker = new DatePicker();
        ListenerAndHandlerBindings.setOnAction(datePicker.onActionProperty(), this.datePicker_onAction);
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
     */
    @Override
    public void dispose() {
        if (isDisposed()) {
            return; // This skin has already been disposed
        }

        ListenerAndHandlerBindings.setOnAction(datePicker.onActionProperty(), null);
        datePicker = null;

        super.dispose(); // getSkinnable() is null after this line
    }

    @Override
    public boolean isDisposed() {
        return getSkinnable() == null;
    }

    private void beginSubroutine(String subroutineText) {
        TableCell<S, LocalDate> skinnable = getSkinnable();
        if (skinnable == null) {
            return;
        }
        if (skinnable instanceof DatePickerTableCell<?>) {
            ((DatePickerTableCell<S>) skinnable).getSubroutinePrinter().beginSubroutine(subroutineText);
        }
    }

    private void endSubroutine(String subroutineText) {
        TableCell<S, LocalDate> skinnable = getSkinnable();
        if (skinnable == null) {
            return;
        }
        if (skinnable instanceof DatePickerTableCell<?>) {
            ((DatePickerTableCell<S>) skinnable).getSubroutinePrinter().endSubroutine(subroutineText);
        }
    }

    // Only to be called by methods in 'DatePickerTableCell'
    DatePicker getDatePicker() {
        return datePicker;
    }
}
