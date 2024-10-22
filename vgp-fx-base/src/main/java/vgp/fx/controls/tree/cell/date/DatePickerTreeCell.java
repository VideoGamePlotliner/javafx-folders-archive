package vgp.fx.controls.tree.cell.date;

import java.time.LocalDate;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;
import vgp.fx.application.ApplicationBase;
import vgp.fx.logging.SubroutinePrinter;

/**
 * A {@code TreeCell} for {@code LocalDate} instances.
 * 
 * @author [...]
 * @version 3.4
 * @see TreeCell
 * @see LocalDate
 * @since 2.3
 */
public class DatePickerTreeCell extends TreeCell<LocalDate> {

	private String preEditText; // used by "startEdit()" and "cancelEdit()"
	private LocalDate preEditItem; // used by "startEdit()" and "cancelEdit()"

	private final SubroutinePrinter subroutinePrinter = new SubroutinePrinter();

	/**
	 * Constructs a new {@code DatePickerTreeCell}
	 * with the default characteristics.
	 */
	public DatePickerTreeCell() {
		super();
		setSkin(new DatePickerTreeCellSkin(this));
		preEditText = null;
		preEditItem = null;
		
		String subroutineText = "DatePickerTreeCell()";
		
		subroutinePrinter.beginSubroutine(subroutineText);

		// Do nothing.
		
		subroutinePrinter.endSubroutine(subroutineText);
	}

	// Only to be called by methods in 'DatePickerTreeCellSkin'
	SubroutinePrinter getSubroutinePrinter() {
		return subroutinePrinter;
	}

	@Override
	public void startEdit() {
		String subroutineText = "DatePickerTreeCellSkin.startEdit()";
		
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
			setSkin(new DatePickerTreeCellSkin(this));
		}
		if (!(getSkin() instanceof DatePickerTreeCellSkin)) {
			setSkin(new DatePickerTreeCellSkin(this));
		}
		return ((DatePickerTreeCellSkin) getSkin()).getDatePicker();
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
		String subroutineText = "DatePickerTreeCell.commitEdit(" + newValue + ")";
		
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
		String subroutineText = "DatePickerTreeCell.cancelEdit()";
		
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
		String subroutineText = "DatePickerTreeCell.updateItem(" + item + ", " + empty + ")";
		
		subroutinePrinter.beginSubroutine(subroutineText);

		super.updateItem(item, empty);

		if (empty || item == null) {
			setText(null);
			setGraphic(null);
		} else {
			setText(getStringConverter().toString(item));
		}
		ApplicationBase.setDisclosureNodeOpacity(this, empty ? 0.0 : 1.0);

		subroutinePrinter.endSubroutine(subroutineText);
	}

	/**
	 * A callback suitable for {@link TreeView#setCellFactory(Callback)}.
	 * 
	 * @return a new cell factory
	 */
	public static Callback<TreeView<LocalDate>, TreeCell<LocalDate>> forTreeView() {
		return treeView -> new DatePickerTreeCell();
	}

}
