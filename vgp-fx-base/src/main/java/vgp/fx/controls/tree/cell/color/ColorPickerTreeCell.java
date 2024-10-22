package vgp.fx.controls.tree.cell.color;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import vgp.fx.application.ApplicationBase;
import vgp.fx.converter.ColorStringConverter;
import vgp.fx.logging.SubroutinePrinter;

/**
 * A {@code TreeCell} for {@code Color} instances.
 * 
 * @author [...]
 * @version 3.4
 * @see TreeCell
 * @see Color
 * @since 2.3
 */
public class ColorPickerTreeCell extends TreeCell<Color> {

	private String preEditText; // used by "startEdit()" and "cancelEdit()"
	private Color preEditItem; // used by "startEdit()" and "cancelEdit()"

	private final SubroutinePrinter subroutinePrinter = new SubroutinePrinter();

	private static final ColorStringConverter STRING_CONVERTER = new ColorStringConverter();

	/**
	 * Constructs a new {@code ColorPickerTreeCell}
	 * with the default characteristics.
	 */
	public ColorPickerTreeCell() {
		super();
		setSkin(new ColorPickerTreeCellSkin(this));
		preEditText = null;
		preEditItem = null;

		String subroutineText = "ColorPickerTreeCell()";

		subroutinePrinter.beginSubroutine(subroutineText);

		// Do nothing.

		subroutinePrinter.endSubroutine(subroutineText);
	}

	// Only to be called by methods in 'ColorPickerTreeCellSkin'
	SubroutinePrinter getSubroutinePrinter() {
		return subroutinePrinter;
	}

	@Override
	public void startEdit() {
		String subroutineText = "ColorPickerTreeCell.startEdit()";

		subroutinePrinter.beginSubroutine(subroutineText);

		super.startEdit();

		ColorPicker colorPicker = getColorPicker();
		setGraphic(colorPicker);

		preEditText = getText();
		setText(null);

		preEditItem = getItem();
		colorPicker.setValue(preEditItem);

		subroutinePrinter.endSubroutine(subroutineText);
	}

	private ColorPicker getColorPicker() {
		if (getSkin() == null) {
			setSkin(new ColorPickerTreeCellSkin(this));
		}
		if (!(getSkin() instanceof ColorPickerTreeCellSkin)) {
			setSkin(new ColorPickerTreeCellSkin(this));
		}
		return ((ColorPickerTreeCellSkin) getSkin()).getColorPicker();
	}

	@Override
	public void commitEdit(Color newValue) {
		String subroutineText = "ColorPickerTreeCell.commitEdit(" + newValue + ")";

		subroutinePrinter.beginSubroutine(subroutineText);

		super.commitEdit(newValue);

		ColorPicker colorPicker = getColorPicker();
		setGraphic(null);

		setText(STRING_CONVERTER.toString(newValue));
		preEditText = null;

		colorPicker.setValue(newValue);
		preEditItem = null;

		subroutinePrinter.endSubroutine(subroutineText);
	}

	@Override
	public void cancelEdit() {
		String subroutineText = "ColorPickerTreeCell.cancelEdit()";

		subroutinePrinter.beginSubroutine(subroutineText);

		super.cancelEdit();

		ColorPicker colorPicker = getColorPicker();
		setGraphic(null);

		setText(preEditText);
		preEditText = null;

		colorPicker.setValue(preEditItem);
		preEditItem = null;

		subroutinePrinter.endSubroutine(subroutineText);
	}

	@Override
	protected void updateItem(Color item, boolean empty) {
		String subroutineText = "ColorPickerTreeCell.updateItem(" + item + ", " + empty + ")";

		subroutinePrinter.beginSubroutine(subroutineText);

		super.updateItem(item, empty);

		if (empty || item == null) {
			setText(null);
			setGraphic(null);
		} else {
			setText(STRING_CONVERTER.toString(item));
		}
		ApplicationBase.setDisclosureNodeOpacity(this, empty ? 0.0 : 1.0);

		subroutinePrinter.endSubroutine(subroutineText);
	}

	/**
	 * A callback suitable for {@link TreeView#setCellFactory(Callback)}.
	 * 
	 * @return a new cell factory
	 */
	public static Callback<TreeView<Color>, TreeCell<Color>> forTreeView() {
		return treeView -> new ColorPickerTreeCell();
	}

}
