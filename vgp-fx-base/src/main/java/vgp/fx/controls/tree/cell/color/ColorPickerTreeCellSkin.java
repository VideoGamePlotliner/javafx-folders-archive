package vgp.fx.controls.tree.cell.color;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Skin;
import javafx.scene.control.TreeCell;
import javafx.scene.control.skin.TreeCellSkin;
import javafx.scene.paint.Color;
import vgp.dispose.Disposable;
import vgp.fx.listenerhandler.ListenerAndHandlerBindings;

/**
 * A {@link Skin} for {@link ColorPickerTreeCell} instances.
 * 
 * @author (to be added)
 * @version 6.12
 * @since 2.11
 */
public class ColorPickerTreeCellSkin extends TreeCellSkin<Color> implements Disposable {

	private ColorPicker colorPicker;
	private final EventHandler<ActionEvent> colorPicker_onAction = event -> {
		verifyNotDisposed();

		String subroutineText = "ColorPickerTreeCellSkin.colorPicker_onAction.handle(" + event + ")";

		beginSubroutine(subroutineText);

		if (null != colorPicker) {
			getSkinnable().commitEdit(colorPicker.getValue());
		}

		endSubroutine(subroutineText);
	};

	/**
	 * Constructs a new {@code ColorPickerTreeCellSkin} with the specified
	 * {@code TreeCell}.
	 * 
	 * @param control the {@code TreeCell} to bind to this skin
	 */
	public ColorPickerTreeCellSkin(TreeCell<Color> control) {
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
		TreeCell<Color> skinnable = getSkinnable();
		if (skinnable == null) {
			return;
		}
		if (skinnable instanceof ColorPickerTreeCell) {
			((ColorPickerTreeCell) skinnable).getSubroutinePrinter().beginSubroutine(subroutineText);
		}
	}

	private void endSubroutine(String subroutineText) {
		TreeCell<Color> skinnable = getSkinnable();
		if (skinnable == null) {
			return;
		}
		if (skinnable instanceof ColorPickerTreeCell) {
			((ColorPickerTreeCell) skinnable).getSubroutinePrinter().endSubroutine(subroutineText);
		}
	}

	// Only to be called by methods in 'ColorPickerTreeCell'
	ColorPicker getColorPicker() {
		return colorPicker;
	}
}
