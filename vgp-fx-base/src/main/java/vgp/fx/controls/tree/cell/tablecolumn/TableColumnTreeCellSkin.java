package vgp.fx.controls.tree.cell.tablecolumn;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Skin;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.skin.TreeCellSkin;
import vgp.dispose.Disposable;
import vgp.fx.listenerhandler.ListenerAndHandlerBindings;

/**
 * A {@link Skin} for {@link TableColumnTreeCell} instances.
 * 
 * @author (to be added)
 * @version 6.12
 * @since 2.12
 */
public class TableColumnTreeCellSkin<T> extends TreeCellSkin<TableColumn<T, ?>> implements Disposable {

	private TextField textField;
	private final EventHandler<ActionEvent> textField_onAction = event -> {
		verifyNotDisposed();

		String subroutineText = "TableColumnTreeCellSkin.textField_onAction.handle(" + event + ")";

		beginSubroutine(subroutineText);

		if (null != textField) {

			TableColumn<T, ?> item = getSkinnable().getItem();
			if (item == null) {
				item = new TableColumn<>();
			}
			item.setText(textField.getText());
			getSkinnable().commitEdit(item);
		}

		endSubroutine(subroutineText);
	};

	/**
	 * Constructs a new {@code TableColumnTreeCellSkin} with the specified
	 * {@code TreeCell}.
	 * 
	 * @param control the {@code TreeCell} to bind to this skin
	 */
	public TableColumnTreeCellSkin(TreeCell<TableColumn<T, ?>> control) {
		super(control);
		textField = new TextField();
		ListenerAndHandlerBindings.setOnAction(textField.onActionProperty(), this.textField_onAction);
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

		ListenerAndHandlerBindings.setOnAction(textField.onActionProperty(), null);
		textField = null;

		super.dispose(); // getSkinnable() is null after this line
	}

	@Override
	public boolean isDisposed() {
		return getSkinnable() == null;
	}

	private void beginSubroutine(String subroutineText) {
		TreeCell<TableColumn<T, ?>> skinnable = getSkinnable();
		if (skinnable == null) {
			return;
		}
		if (skinnable instanceof TableColumnTreeCell) {
			((TableColumnTreeCell<T>) skinnable).getSubroutinePrinter().beginSubroutine(subroutineText);
		}
	}

	private void endSubroutine(String subroutineText) {
		TreeCell<TableColumn<T, ?>> skinnable = getSkinnable();
		if (skinnable == null) {
			return;
		}
		if (skinnable instanceof TableColumnTreeCell) {
			((TableColumnTreeCell<T>) skinnable).getSubroutinePrinter().endSubroutine(subroutineText);
		}
	}

	// Only to be called by methods in 'TableColumnTreeCell'
	TextField getTextField() {
		return textField;
	}
}
