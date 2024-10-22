package vgp.fx.controls.list.cell.date;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.Skin;
import javafx.scene.control.skin.ListCellSkin;
import vgp.dispose.Disposable;
import vgp.fx.listenerhandler.ListenerAndHandlerBindings;

/**
 * A {@link Skin} for {@link DatePickerListCell} instances.
 * 
 * @author (to be added)
 * @version 6.12
 * @since 2.9
 */
public class DatePickerListCellSkin extends ListCellSkin<LocalDate> implements Disposable {

	private DatePicker datePicker;
	private final EventHandler<ActionEvent> datePicker_onAction = event -> {
		verifyNotDisposed();

		String subroutineText = "DatePickerListCellSkin.datePicker_onAction.handle(" + event + ")";

		beginSubroutine(subroutineText);

		if (null != datePicker) {
			getSkinnable().commitEdit(datePicker.getValue());
		}

		endSubroutine(subroutineText);
	};

	/**
	 * Constructs a new {@code DatePickerListCellSkin} with the specified
	 * {@code ListCell}.
	 * 
	 * @param control the {@code ListCell} to bind to this skin
	 */
	public DatePickerListCellSkin(ListCell<LocalDate> control) {
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
	 * 	return; // This skin has already been disposed
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
		ListCell<LocalDate> skinnable = getSkinnable();
		if (skinnable == null) {
			return;
		}
		if (skinnable instanceof DatePickerListCell) {
			((DatePickerListCell) skinnable).getSubroutinePrinter().beginSubroutine(subroutineText);
		}
	}

	private void endSubroutine(String subroutineText) {
		ListCell<LocalDate> skinnable = getSkinnable();
		if (skinnable == null) {
			return;
		}
		if (skinnable instanceof DatePickerListCell) {
			((DatePickerListCell) skinnable).getSubroutinePrinter().endSubroutine(subroutineText);
		}
	}

	// Only to be called by methods in 'DatePickerListCell'
	DatePicker getDatePicker() {
		return datePicker;
	}
}
