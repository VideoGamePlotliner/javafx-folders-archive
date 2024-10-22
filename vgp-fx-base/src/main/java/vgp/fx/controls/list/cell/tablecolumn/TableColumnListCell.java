package vgp.fx.controls.list.cell.tablecolumn;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import vgp.fx.logging.SubroutinePrinter;

/**
 * A {@code ListCell} for {@code TableColumn} instances.
 * 
 * @author (to be added)
 * @version 5.3
 * @see ListCell
 * @see TableColumn
 * @since 2.12
 */
public class TableColumnListCell<T> extends ListCell<TableColumn<T, ?>> {

	private String preEditText; // used by "startEdit()" and "cancelEdit()"
	private TableColumn<T, ?> preEditItem; // used by "startEdit()" and "cancelEdit()"

	private final SubroutinePrinter subroutinePrinter = new SubroutinePrinter();

	/**
	 * Constructs a new {@code TableColumnTreeCell}
	 * with the default characteristics.
	 */
	public TableColumnListCell() {
		super();
		setSkin(new TableColumnListCellSkin<T>(this));

		preEditItem = null;
		preEditText = null;

		final String subroutineText = "TableColumnListCell()";

		subroutinePrinter.beginSubroutine(subroutineText);

		// Do nothing.

		subroutinePrinter.endSubroutine(subroutineText);
	}

	// Only to be called by methods in 'TableColumnListCellSkin'
	SubroutinePrinter getSubroutinePrinter() {
		return subroutinePrinter;
	}

	@Override
	public void startEdit() {
		final String subroutineText = "TableColumnListCell.startEdit()";

		subroutinePrinter.beginSubroutine(subroutineText);

		super.startEdit();

		TextField textField = getTextField();
		setGraphic(textField);

		preEditText = getText();
		setText(null);

		preEditItem = getItem();
		textField.setText(toString(preEditItem));

		subroutinePrinter.endSubroutine(subroutineText);
	}

	private TextField getTextField() {
		if (getSkin() == null) {
			setSkin(new TableColumnListCellSkin<T>(this));
		}
		if (!(getSkin() instanceof TableColumnListCellSkin<?>)) {
			setSkin(new TableColumnListCellSkin<T>(this));
		}
		return ((TableColumnListCellSkin<?>) getSkin()).getTextField();
	}

	@Override
	public void commitEdit(TableColumn<T, ?> newValue) {
		final String newText = toString(newValue);

		final String subroutineText = "TableColumnListCell.commitValue(" + newValue + ")";

		subroutinePrinter.beginSubroutine(subroutineText);

		super.commitEdit(newValue);

		TextField textField = getTextField();
		setGraphic(null);

		setText(newText);
		preEditText = null;

		textField.setText(newText);
		preEditItem = null;

		subroutinePrinter.endSubroutine(subroutineText);
	}

	private String toString(TableColumn<T, ?> tableColumn) {
		return tableColumn == null ? null : tableColumn.getText();
	}

	@Override
	public void cancelEdit() {
		final String subroutineText = "TableColumnListCell.cancelEdit()";

		subroutinePrinter.beginSubroutine(subroutineText);

		super.cancelEdit();

		TextField textField = getTextField();
		setGraphic(null);

		setText(preEditText);
		preEditText = null;

		textField.setText(toString(preEditItem));
		preEditItem = null;

		subroutinePrinter.endSubroutine(subroutineText);
	}

	@Override
	protected void updateItem(TableColumn<T, ?> item, boolean empty) {
		final String subroutineText = "TableColumnListCell.updateItem(" + item + ", " + empty + ")";

		subroutinePrinter.beginSubroutine(subroutineText);

		super.updateItem(item, empty);

		if (empty || item == null) {
			setText(null);
			setGraphic(null);
		} else {
			setText(toString(item));
		}

		subroutinePrinter.endSubroutine(subroutineText);
	}

	/**
	 * A callback suitable for {@link ListView#setCellFactory(Callback)}.
	 * 
	 * @return a new cell factory
	 */
	public static <T> Callback<ListView<TableColumn<T, ?>>, ListCell<TableColumn<T, ?>>> forListView() {
		return listView -> new TableColumnListCell<T>();
	}
}