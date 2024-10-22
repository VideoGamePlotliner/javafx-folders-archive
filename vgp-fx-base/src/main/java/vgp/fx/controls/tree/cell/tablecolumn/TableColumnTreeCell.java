package vgp.fx.controls.tree.cell.tablecolumn;

import java.util.Objects;

import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.util.Callback;
import vgp.fx.application.ApplicationBase;
import vgp.fx.logging.SubroutinePrinter;

/**
 * A {@code TreeCell} for {@code TableColumn} instances.
 * 
 * @author (to be added)
 * @version 5.0
 * @see TreeCell
 * @see TableColumn
 * @since 2.12
 */
public class TableColumnTreeCell<T> extends TreeCell<TableColumn<T, ?>> {

	/**
	 * @version 5.0
	 * @since 5.0
	 */
	private static final class GraphicToUseIfItemHasNoParent extends Text {

		/**
		 * Construct a new {@code Node} to initialize
		 * {@link TableColumnTreeCell#graphicToUseIfItemHasNoParent} to.
		 */
		public GraphicToUseIfItemHasNoParent() {
			super(TableView.class.getSimpleName());
			if (this.getFont() == null) {
				this.setFont(Objects.requireNonNull(Font.getDefault()));
			}

			final Font font = Objects.requireNonNull(this.getFont());
			final String fontFamily = font.getFamily();
			final double fontSize = font.getSize();
			final FontPosture fontPosture = FontPosture.ITALIC;

			this.setFont(Font.font(fontFamily, fontPosture, fontSize));
		}

	}

	private String preEditText; // used by "startEdit()" and "cancelEdit()"
	private TableColumn<T, ?> preEditItem; // used by "startEdit()" and "cancelEdit()"

	private final SubroutinePrinter subroutinePrinter = new SubroutinePrinter();

	private final Node graphicToUseIfItemHasNoParent = new GraphicToUseIfItemHasNoParent();

	/**
	 * Constructs a new {@code TableColumnTreeCell}
	 * with the default characteristics.
	 */
	public TableColumnTreeCell() {
		super();
		setSkin(new TableColumnTreeCellSkin<>(this));
		preEditItem = null;
		preEditText = null;

		String subroutineText = "TableColumnTreeCell()";

		subroutinePrinter.beginSubroutine(subroutineText);

		// Do nothing.

		subroutinePrinter.endSubroutine(subroutineText);
	}

	// Only to be called by methods in 'TableColumnTreeCellSkin'
	SubroutinePrinter getSubroutinePrinter() {
		return subroutinePrinter;
	}

	@Override
	public void startEdit() {
		String subroutineText = "TableColumnTreeCell.startEdit()";

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
			setSkin(new TableColumnTreeCellSkin<>(this));
		}
		if (!(getSkin() instanceof TableColumnTreeCellSkin)) {
			setSkin(new TableColumnTreeCellSkin<>(this));
		}
		return ((TableColumnTreeCellSkin<?>) getSkin()).getTextField();
	}

	@Override
	public void commitEdit(TableColumn<T, ?> newValue) {
		String newText = toString(newValue);

		String subroutineText = "TableColumnTreeCell.commitEdit(" + newValue + ")";

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
		String subroutineText = "TableColumnTreeCell.cancelEdit()";

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
		String subroutineText = "TableColumnTreeCell.updateItem(" + item + ", " + empty + ")";

		subroutinePrinter.beginSubroutine(subroutineText);

		super.updateItem(item, empty);

		if (empty || item == null) {
			setText(null);
			setGraphic(null);
		} else {
			setText(toString(item));
		}
		ApplicationBase.setDisclosureNodeOpacity(this, empty ? 0.0 : 1.0);

		if (!empty && !isEditing()) {
			final TreeItem<TableColumn<T, ?>> treeItem = getTreeItem();
			if (treeItem != null) {
				final TreeItem<TableColumn<T, ?>> parent = treeItem.getParent();
				setEditable(parent != null);
				if (parent == null) {
					setGraphic(graphicToUseIfItemHasNoParent);
				}
			}
		}
		subroutinePrinter.endSubroutine(subroutineText);
	}

	/**
	 * A callback suitable for {@link TreeView#setCellFactory(Callback)}.
	 * 
	 * @return a new cell factory
	 */
	public static <T> Callback<TreeView<TableColumn<T, ?>>, TreeCell<TableColumn<T, ?>>> forTreeView() {
		return treeView -> new TableColumnTreeCell<T>();
	}

}
