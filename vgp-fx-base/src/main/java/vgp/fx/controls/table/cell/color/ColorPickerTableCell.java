package vgp.fx.controls.table.cell.color;

import javafx.scene.control.TableView;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import vgp.fx.converter.ColorStringConverter;
import vgp.fx.logging.SubroutinePrinter;

/**
 * A {@code TableCell} for {@code Color} instances.
 * 
 * @author [...]
 * @version 3.11
 * @param <S> the type of the {@link TableView#itemsProperty() items} of this cell's {@code TableView}
 * @see TableCell
 * @see Color
 * @since 2.5
 */
public class ColorPickerTableCell<S> extends TableCell<S, Color> {

	private String preEditText; // used by "startEdit()" and "cancelEdit()"
	private Color preEditItem; // used by "startEdit()" and "cancelEdit()"

	private final SubroutinePrinter subroutinePrinter = new SubroutinePrinter();

	private static final ColorStringConverter STRING_CONVERTER = new ColorStringConverter();

	/**
	 * Constructs a new {@code ColorPickerTableCell}
	 * with the default characteristics.
	 */
	public ColorPickerTableCell() {
		super();
		setSkin(new ColorPickerTableCellSkin<>(this));
		preEditText = null;
		preEditItem = null;

		String subroutineText = "ColorPickerTableCell()";

		subroutinePrinter.beginSubroutine(subroutineText);

		// Do nothing.

		subroutinePrinter.endSubroutine(subroutineText);
	}

	// Only to be called by methods in 'ColorPickerTableCellSkin'
	SubroutinePrinter getSubroutinePrinter() {
		return subroutinePrinter;
	}

	@Override
	public void startEdit() {
		String subroutineText = "ColorPickerTableCell.startEdit()";

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

	@SuppressWarnings("unchecked")
	private ColorPicker getColorPicker() {
		if (getSkin() == null) {
			setSkin(new ColorPickerTableCellSkin<>(this));
		}
		if (!(getSkin() instanceof ColorPickerTableCellSkin<?>)) {
			setSkin(new ColorPickerTableCellSkin<>(this));
		}
		return ((ColorPickerTableCellSkin<S>) getSkin()).getColorPicker();
	}

	@Override
	public void commitEdit(Color newValue) {
		String subroutineText = "ColorPickerTableCell.commitEdit(" + newValue + ")";

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
		String subroutineText = "ColorPickerTableCell.cancelEdit()";

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
		String subroutineText = "ColorPickerTableCell.updateItem(" + item + ", " + empty + ")";

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
	 * A callback suitable for {@link TableColumn#setCellFactory(Callback)}.
	 * 
	 * @return a new cell factory
	 */
	public static <S> Callback<TableColumn<S, Color>, TableCell<S, Color>> forTableColumn() {
		return tableColumn -> new ColorPickerTableCell<>();
	}

}
