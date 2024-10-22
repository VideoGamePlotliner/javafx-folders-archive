package vgp.fx.controls.table.cell.color;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Skin;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.skin.TableCellSkin;
import javafx.scene.paint.Color;
import vgp.dispose.Disposable;
import vgp.fx.listenerhandler.ListenerAndHandlerBindings;

/**
 * A {@link Skin} for {@link ColorPickerTableCell} instances.
 * 
 * @author (to be added)
 * @param <S> the type of the {@link TableView#itemsProperty() items} of this
 *            skin's cell's {@code TableView}
 * @version 6.12
 * @see TableCell
 * @see Color
 * @since 3.8
 */
public class ColorPickerTableCellSkin<S> extends TableCellSkin<S, Color> implements Disposable {

	private ColorPicker colorPicker;
	private final EventHandler<ActionEvent> colorPicker_onAction = event -> {
		verifyNotDisposed();

		String subroutineText = "ColorPickerTableCellSkin.colorPicker_onAction.handle(" + event + ")";

		beginSubroutine(subroutineText);

		if (null != colorPicker) {
			getSkinnable().commitEdit(colorPicker.getValue());
		}

		endSubroutine(subroutineText);
	};

	/**
	 * Constructs a new {@code ColorPickerTableCellSkin} with the specified
	 * {@code TableCell}.
	 * 
	 * @param control the {@code TableCell} to bind to this skin
	 */
	public ColorPickerTableCellSkin(TableCell<S, Color> control) {
		super(control);
		colorPicker = new ColorPicker();
		ListenerAndHandlerBindings.setOnAction(colorPicker.onActionProperty(), this.colorPicker_onAction);
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

		ListenerAndHandlerBindings.setOnAction(colorPicker.onActionProperty(), null);
		colorPicker = null;

		super.dispose(); // getSkinnable() is null after this line
	}

	@Override
	public boolean isDisposed() {
		return getSkinnable() == null;
	}

	private void beginSubroutine(String subroutineText) {
		TableCell<S, Color> skinnable = getSkinnable();
		if (skinnable == null) {
			return;
		}
		if (skinnable instanceof ColorPickerTableCell<?>) {
			((ColorPickerTableCell<S>) skinnable).getSubroutinePrinter().beginSubroutine(subroutineText);
		}
	}

	private void endSubroutine(String subroutineText) {
		TableCell<S, Color> skinnable = getSkinnable();
		if (skinnable == null) {
			return;
		}
		if (skinnable instanceof ColorPickerTableCell<?>) {
			((ColorPickerTableCell<S>) skinnable).getSubroutinePrinter().endSubroutine(subroutineText);
		}
	}

	// Only to be called by methods in 'ColorPickerTableCell'
	ColorPicker getColorPicker() {
		return colorPicker;
	}
}
