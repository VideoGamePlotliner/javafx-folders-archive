package vgp.fx.controls.table.cell.date;

import java.time.LocalDate;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;
import vgp.fx.logging.SubroutinePrinter;

/**
 * A {@code TableCell} for {@code LocalDate} instances.
 * 
 * @author [...]
 * @version 3.11
 * @param <S> the type of the {@link TableView#itemsProperty() items} of this
 *            cell's {@code TableView}
 * @see TableCell
 * @see LocalDate
 * @since 2.5
 */
public class DatePickerTableCell<S> extends TableCell<S, LocalDate> {

	private String preEditText; // used by "startEdit()" and "cancelEdit()"
	private LocalDate preEditItem; // used by "startEdit()" and "cancelEdit()"

	private final SubroutinePrinter subroutinePrinter = new SubroutinePrinter();

	/**
	 * Constructs a new {@code DatePickerTableCell}
	 * with the default characteristics.
	 */
	public DatePickerTableCell() {
		super();
		setSkin(new DatePickerTableCellSkin<>(this));
		preEditText = null;
		preEditItem = null;

		String subroutineText = "DatePickerTableCell()";

		subroutinePrinter.beginSubroutine(subroutineText);

		// Do nothing.

		subroutinePrinter.endSubroutine(subroutineText);
	}

	// Only to be called by methods in 'DatePickerTableCellSkin'
	SubroutinePrinter getSubroutinePrinter() {
		return subroutinePrinter;
	}

	@Override
	public void startEdit() {
		String subroutineText = "DatePickerTableCell.startEdit()";

		subroutinePrinter.beginSubroutine(subroutineText);

		super.startEdit();

		DatePicker datePicker = getDatePicker();
		setGraphic(datePicker);

		preEditText = getText();
		setText(null);

		preEditItem = getItem();
		datePicker.setValue(preEditItem);

		subroutinePrinter.endSubroutine(subroutineText);
	}

	@SuppressWarnings("unchecked")
	private DatePicker getDatePicker() {
		if (getSkin() == null) {
			setSkin(new DatePickerTableCellSkin<>(this));
		}
		if (!(getSkin() instanceof DatePickerTableCellSkin<?>)) {
			setSkin(new DatePickerTableCellSkin<>(this));
		}
		return ((DatePickerTableCellSkin<S>) getSkin()).getDatePicker();
	}

	private StringConverter<LocalDate> getStringConverter() {
		DatePicker datePicker = getDatePicker();

		if (datePicker.getConverter() == null) {
			datePicker.setConverter(new LocalDateStringConverter());
		}
		return datePicker.getConverter();
	}

	@Override
	public void commitEdit(LocalDate newValue) {
		String subroutineText = "DatePickerTableCell.commitEdit(" + newValue + ")";

		subroutinePrinter.beginSubroutine(subroutineText);
		
		super.commitEdit(newValue);

		DatePicker datePicker = getDatePicker();
		setGraphic(null);

		setText(getStringConverter().toString(newValue));
		preEditText = null;

		datePicker.setValue(newValue);
		preEditItem = null;

		subroutinePrinter.endSubroutine(subroutineText);
	}

	@Override
	public void cancelEdit() {
		String subroutineText = "DatePickerTableCell.cancelEdit()";

		subroutinePrinter.beginSubroutine(subroutineText);

		super.cancelEdit();

		DatePicker datePicker = getDatePicker();
		setGraphic(null);

		setText(preEditText);
		preEditText = null;

		datePicker.setValue(preEditItem);
		preEditItem = null;

		subroutinePrinter.endSubroutine(subroutineText);
	}

	@Override
	protected void updateItem(LocalDate item, boolean empty) {
		String subroutineText = "DatePickerTableCell.updateItem(" + item + ", " + empty + ")";

		subroutinePrinter.beginSubroutine(subroutineText);

		super.updateItem(item, empty);

		if (empty || item == null) {
			setText(null);
			setGraphic(null);
		} else {
			setText(getStringConverter().toString(item));
		}

		subroutinePrinter.endSubroutine(subroutineText);
	}

	/**
	 * A callback suitable for {@link TableColumn#setCellFactory(Callback)}.
	 * 
	 * @return a new cell factory
	 */
	public static <S> Callback<TableColumn<S, LocalDate>, TableCell<S, LocalDate>> forTableColumn() {
		return tableColumn -> new DatePickerTableCell<>();
	}

}
