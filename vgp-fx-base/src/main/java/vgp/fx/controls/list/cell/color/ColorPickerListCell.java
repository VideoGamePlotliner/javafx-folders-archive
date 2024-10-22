package vgp.fx.controls.list.cell.color;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import vgp.fx.converter.ColorStringConverter;
import vgp.fx.logging.SubroutinePrinter;

/**
 * A {@code ListCell} for {@code Color} instances.
 * 
 * @author [...]
 * @version 3.3
 * @see ListCell
 * @see Color
 * @since 2.0
 */
public class ColorPickerListCell extends ListCell<Color> {

	private String preEditText; // used by "startEdit()" and "cancelEdit()"
	private Color preEditItem; // used by "startEdit()" and "cancelEdit()"

	private final SubroutinePrinter subroutinePrinter = new SubroutinePrinter();

	private static final ColorStringConverter STRING_CONVERTER = new ColorStringConverter();

	/**
	 * Constructs a new {@code ColorPickerListCell}
	 * with the default characteristics.
	 */
	public ColorPickerListCell() {
		super();
		setSkin(new ColorPickerListCellSkin(this));
		preEditText = null;
		preEditItem = null;

		String subroutineText = "ColorPickerListCell()";

		subroutinePrinter.beginSubroutine(subroutineText);

		// Do nothing.

		subroutinePrinter.endSubroutine(subroutineText);
	}

	// Only to be called by methods in 'ColorPickerListCellSkin'
	SubroutinePrinter getSubroutinePrinter() {
		return subroutinePrinter;
	}

	@Override
	public void startEdit() {
		String subroutineText = "ColorPickerListCell.startEdit()";
		
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
			setSkin(new ColorPickerListCellSkin(this));
		}
		if (!(getSkin() instanceof ColorPickerListCellSkin)) {
			setSkin(new ColorPickerListCellSkin(this));
		}
		return ((ColorPickerListCellSkin) getSkin()).getColorPicker();
	}

	@Override
	public void commitEdit(Color newValue) {
		String subroutineText = "ColorPickerListCell.commitEdit(" + newValue + ")";

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
		String subroutineText = "ColorPickerListCell.cancelEdit()";

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
		String subroutineText = "ColorPickerListCell.updateItem(" + item + ", " + empty + ")";

		subroutinePrinter.beginSubroutine(subroutineText);

		super.updateItem(item, empty);

		// ColorPicker colorPicker = getColorPicker();

		if (empty || item == null) {
			setText(null);
			setGraphic(null);
		} else {
			setText(STRING_CONVERTER.toString(item));
		}

		subroutinePrinter.endSubroutine(subroutineText);
	}

	/**
	 * A callback suitable for {@link ListView#setCellFactory(Callback)}.
	 * 
	 * @return a new cell factory
	 */
	public static Callback<ListView<Color>, ListCell<Color>> forListView() {
		return listView -> new ColorPickerListCell();
	}

}