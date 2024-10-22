package vgp.fx.controls.list.cell.color;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.Skin;
import javafx.scene.control.skin.ListCellSkin;
import javafx.scene.paint.Color;
import vgp.dispose.Disposable;
import vgp.fx.listenerhandler.ListenerAndHandlerBindings;

/**
 * A {@link Skin} for {@link ColorPickerListCell} instances.
 * 
 * @author (to be added)
 * @version 6.12
 * @since 2.8
 */
public class ColorPickerListCellSkin extends ListCellSkin<Color> implements Disposable {

	private ColorPicker colorPicker;
	private final EventHandler<ActionEvent> colorPicker_onAction = event -> {
		verifyNotDisposed();

		String subroutineText = "ColorPickerListCellSkin.colorPicker_onAction.handle(" + event + ")";

		beginSubroutine(subroutineText);

		if (null != colorPicker) {
			getSkinnable().commitEdit(colorPicker.getValue());
		}

		endSubroutine(subroutineText);
	};

	/**
	 * Constructs a new {@code ColorPickerListCellSkin} with the specified
	 * {@code ListCell}.
	 * 
	 * @param control the {@code ListCell} to bind to this skin
	 */
	public ColorPickerListCellSkin(ListCell<Color> control) {
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
		ListCell<Color> skinnable = getSkinnable();
		if (skinnable == null) {
			return;
		}
		if (skinnable instanceof ColorPickerListCell) {
			((ColorPickerListCell) skinnable).getSubroutinePrinter().beginSubroutine(subroutineText);
		}
	}

	private void endSubroutine(String subroutineText) {
		ListCell<Color> skinnable = getSkinnable();
		if (skinnable == null) {
			return;
		}
		if (skinnable instanceof ColorPickerListCell) {
			((ColorPickerListCell) skinnable).getSubroutinePrinter().endSubroutine(subroutineText);
		}
	}

	// Only to be called by methods in 'ColorPickerListCell'
	ColorPicker getColorPicker() {
		return colorPicker;
	}
}
