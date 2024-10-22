package vgp.fx.controls.list.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import vgp.fx.application.ApplicationBase;
import vgp.fx.listenerhandler.ListenerAndHandlerBindings;

/**
 * A {@code ListViewEditorSkin} with additional buttons for replacing the
 * {@link ListView#itemsProperty() items}
 * list of its {@link ListView}.
 * <p>
 * Currently, in addition to the buttons specified by
 * {@code ListViewEditorSkin},
 * this class and its subclasses specify buttons for the following actions:
 * <ul>
 * <li>Set the current list to {@code null}.
 * <li>Replace the current list with a new, empty list.
 * </ul>
 * <p>
 * By default, this skin ensures that the user can select more than one element
 * in
 * that {@code items} list at a time.
 * 
 * @author [...]
 * @version 6.13
 * @param <T> the type of the {@link ListView#itemsProperty() items} of this
 *            skin's {@code ListView}
 * @see ListView
 * @see ListView#itemsProperty()
 * @since 2.1
 */
public class ListEditorViewSkin2<T> extends ListEditorViewSkin<T> {

	private Button nullListButton; // Set List to Null
	private Button newListButton; // Replace With New, Empty List

	private final EventHandler<ActionEvent> nullListButton_onAction = event -> {
		verifyNotDisposed();

		System.out.println("ListEditorViewSkin2.nullListButton_onAction.handle(" + event + ")");
		System.out.println();

		getSkinnable().setItems(null);

		getSkinnable().layout();
	};

	private final EventHandler<ActionEvent> newListButton_onAction = event -> {
		verifyNotDisposed();

		System.out.println("ListEditorViewSkin2.newListButton_onAction.handle(" + event + ")");
		System.out.println();

		getSkinnable().setItems(FXCollections.observableArrayList());

		getSkinnable().layout();
	};

	/**
	 * Constructs a new {@code ListEditorViewSkin2} with the specified
	 * {@code ListView}.
	 * 
	 * @param control the {@code ListView} to bind to this skin
	 */
	public ListEditorViewSkin2(ListView<T> control) {
		super(control);
	}

	@Override
	final void enableOrDisableButtonsAccordingToTheGivenList(ObservableList<T> allItems) {
		super.enableOrDisableButtonsAccordingToTheGivenList(allItems);
		boolean isNullList = allItems == null;
		nullListButton.setDisable(isNullList);
		if (!isNullList) {
			boolean isEmptyList = allItems.isEmpty();
			newListButton.setDisable(isEmptyList);
		} else {
			newListButton.setDisable(false); // Always set the "disable" property of newListButton.
		}
	}

	@Override
	final void initButtonBar() {
		super.initButtonBar();

		nullListButton = new Button();
		nullListButton.setText("Null List");
		ListenerAndHandlerBindings.setOnAction(nullListButton.onActionProperty(), this.nullListButton_onAction);
		nullListButton.setTooltip(new Tooltip("Set the current list to 'null'."));

		newListButton = new Button();
		newListButton.setText("New List");
		ListenerAndHandlerBindings.setOnAction(newListButton.onActionProperty(), this.newListButton_onAction);
		newListButton.setTooltip(new Tooltip("Replace the current list with a new, empty list."));

		getButtons().add(nullListButton);
		getButtons().add(newListButton);
	}

	@Override
	protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return 150 + super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
	}

	@Override
	public void dispose() {

		if (isDisposed()) {
			return; // This skin has already been disposed
		}

		ApplicationBase.dispose(nullListButton);
		ApplicationBase.dispose(newListButton);
		nullListButton = null;
		newListButton = null;

		// The last line of this override must
		// be a call to the super-implementation.

		super.dispose(); // getSkinnable() is null after this line
	}

	@Override
	public String toString() {
		return "ListEditorViewSkin2 [skinnable=" + getSkinnable() + "]";
	}

}
