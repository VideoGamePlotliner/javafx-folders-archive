package vgp.fx.controls.list.cell.date;

import java.time.LocalDate;

import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;
import vgp.fx.logging.SubroutinePrinter;

/**
 * A {@code ListCell} for {@code LocalDate} instances.
 * 
 * @author [...]
 * @version 3.3
 * @see ListCell
 * @see LocalDate
 * @since 2.0
 */
public class DatePickerListCell extends ListCell<LocalDate> {

	private String preEditText; // used by "startEdit()" and "cancelEdit()"
	private LocalDate preEditItem; // used by "startEdit()" and "cancelEdit()"
	
	private final SubroutinePrinter subroutinePrinter = new SubroutinePrinter();

	/**
	 * Constructs a new {@code DatePickerListCell}
	 * with the default characteristics.
	 */
	public DatePickerListCell() {
		super();
		setSkin(new DatePickerListCellSkin(this));
		preEditText = null;
		preEditItem = null;
		
		String subroutineText = "DatePickerListCell()";
		
		subroutinePrinter.beginSubroutine(subroutineText);

		// Do nothing.
		
		subroutinePrinter.endSubroutine(subroutineText);
	}

	// Only to be called by methods in 'DatePickerListCellSkin'
	SubroutinePrinter getSubroutinePrinter() {
		return subroutinePrinter;
	}

	@Override
	public void startEdit() {
		String subroutineText = "DatePickerListCell.startEdit()";
		
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

	private DatePicker getDatePicker() {
		if (getSkin() == null) {
			setSkin(new DatePickerListCellSkin(this));
		}
		if (!(getSkin() instanceof DatePickerListCellSkin)) {
			setSkin(new DatePickerListCellSkin(this));
		}
		return ((DatePickerListCellSkin) getSkin()).getDatePicker();
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
		String subroutineText = "DatePickerListCell.commitEdit(" + newValue + ")";
		
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
		String subroutineText = "DatePickerListCell.cancelEdit()";
		
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
		String subroutineText = "DatePickerListCell.updateItem(" + item + ", " + empty + ")";
		
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
	 * A callback suitable for {@link ListView#setCellFactory(Callback)}.
	 * 
	 * @return a new cell factory
	 */
	public static Callback<ListView<LocalDate>, ListCell<LocalDate>> forListView() {
		return listView -> new DatePickerListCell();
	}

}
