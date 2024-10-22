package vgp.fx.controls.table.view;

import java.time.LocalDate;
import java.util.Objects;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import vgp.fx.application.CellViewerApplication;
import vgp.fx.controls.table.cell.color.ColorPickerTableCell;
import vgp.fx.controls.table.cell.date.DatePickerTableCell;
import vgp.fx.listenerhandler.ListenerAndHandlerBindings;
import vgp.fx.logging.SubroutinePrinter;
import vgp.fx.objects.ColoredDate;

/**
 * A {@link TableView} contained within the return value for
 * {@link CellViewerApplication#createStartingScene()}.
 * 
 * @version 6.9
 */
public final class ColoredDateTableView extends TableView<ColoredDate> {

    /**
     * @since 5.4
     * @version 5.4
     */
    private static final class ColoredDateListCell extends ListCell<ColoredDate> {

        private final SubroutinePrinter subroutinePrinter = new SubroutinePrinter();

        /**
         * Create a new list cell.
         */
        public ColoredDateListCell() {
            super();

            final String subroutineText = "ColoredDateListCell()";

            subroutinePrinter.beginSubroutine(subroutineText);

            // Do nothing.

            subroutinePrinter.endSubroutine(subroutineText);
        }

        @Override
        protected void updateItem(ColoredDate item, boolean empty) {
            final String subroutineText = "ColoredDateListCell.updateItem(" + item + ", " + empty + ")";

            subroutinePrinter.beginSubroutine(subroutineText);

            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(item.toString());
            }

            subroutinePrinter.endSubroutine(subroutineText);
        }

        @Override
        public void startEdit() {
            final String subroutineText = "ColoredDateListCell.startEdit()";

            subroutinePrinter.beginSubroutine(subroutineText);

            super.startEdit();

            final Scene scene = this.getScene();
            if (scene != null) {
                final Window ownerWindow = scene.getWindow();
                if (ownerWindow != null) {
                    openColoredDateEditor(ownerWindow, getItem());
                }
            }

            subroutinePrinter.endSubroutine(subroutineText);
        }

        private void openColoredDateEditor(final Window ownerWindow, final ColoredDate coloredDate) {
            Objects.requireNonNull(ownerWindow);
            Objects.requireNonNull(coloredDate);

            final String subroutineText = "ColoredDateListCell.openColoredDateEditor(" + ownerWindow + ")";

            subroutinePrinter.beginSubroutine(subroutineText);

            // Declare
            final GridPane gridPane;
            final Stage stage;
            final ColorPicker colorPicker;
            final DatePicker datePicker;
            final TextField textField;
            final Label labelForColorPicker;
            final Label labelForDatePicker;
            final Label labelForTextField;
            final EventHandler<ActionEvent> onAction;

            // Initialize
            gridPane = new GridPane();
            stage = new Stage();
            colorPicker = new ColorPicker();
            datePicker = new DatePicker();
            textField = new TextField();
            labelForColorPicker = new Label(coloredDate.colorProperty().getName());
            labelForDatePicker = new Label(coloredDate.dateProperty().getName());
            labelForTextField = new Label(coloredDate.textProperty().getName());
            onAction = event -> coloredDateEditor_onAction(coloredDate, colorPicker, datePicker, textField, event);

            try {
                // Use

                labelForColorPicker.setLabelFor(colorPicker);
                labelForDatePicker.setLabelFor(datePicker);
                labelForTextField.setLabelFor(textField);

                GridPane.setHalignment(labelForColorPicker, HPos.RIGHT);
                GridPane.setHalignment(labelForDatePicker, HPos.RIGHT);
                GridPane.setHalignment(labelForTextField, HPos.RIGHT);

                GridPane.setHalignment(colorPicker, HPos.LEFT);
                GridPane.setHalignment(datePicker, HPos.LEFT);
                GridPane.setHalignment(textField, HPos.LEFT);

                ListenerAndHandlerBindings.setOnAction(colorPicker.onActionProperty(), onAction);
                ListenerAndHandlerBindings.setOnAction(datePicker.onActionProperty(), onAction);
                ListenerAndHandlerBindings.setOnAction(textField.onActionProperty(), onAction);

                colorPicker.setValue(coloredDate.getColor());
                datePicker.setValue(coloredDate.getDate());
                textField.setText(coloredDate.getText());

                gridPane.add(colorPicker, 1, 0);
                gridPane.add(datePicker, 1, 1);
                gridPane.add(textField, 1, 2);
                gridPane.add(labelForColorPicker, 0, 0);
                gridPane.add(labelForDatePicker, 0, 1);
                gridPane.add(labelForTextField, 0, 2);

                gridPane.setPrefWidth(220);
                gridPane.setHgap(5);

                stage.initOwner(ownerWindow);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.setTitle("Item Editor");
                stage.setScene(new Scene(gridPane));
                stage.showAndWait();

                this.commitEdit(coloredDate);
            } finally {
                // Dispose
                stage.setTitle(null);
                stage.setScene(null);
                gridPane.getChildren().forEach(GridPane::clearConstraints);
                gridPane.getChildren().clear();
                labelForColorPicker.setLabelFor(null);
                labelForDatePicker.setLabelFor(null);
                labelForTextField.setLabelFor(null);
                ListenerAndHandlerBindings.setOnAction(colorPicker.onActionProperty(), null);
                ListenerAndHandlerBindings.setOnAction(datePicker.onActionProperty(), null);
                ListenerAndHandlerBindings.setOnAction(textField.onActionProperty(), null);

                subroutinePrinter.endSubroutine(subroutineText);
            }
        }

        private void coloredDateEditor_onAction(final ColoredDate coloredDate, final ColorPicker colorPicker,
                final DatePicker datePicker, final TextField textField, final ActionEvent event) {

            Objects.requireNonNull(coloredDate);
            Objects.requireNonNull(colorPicker);
            Objects.requireNonNull(datePicker);
            Objects.requireNonNull(textField);

            final String subroutineText = "ColoredDateListCell.coloredDateEditor_onAction(" + event + ")";

            subroutinePrinter.beginSubroutine(subroutineText);

            if (event != null) {
                final Object source = event.getSource();
                if (source == colorPicker) {
                    coloredDate.setColor(colorPicker.getValue());
                } else if (source == datePicker) {
                    coloredDate.setDate(datePicker.getValue());
                } else if (source == textField) {
                    coloredDate.setText(textField.getText());
                }
            }

            setText(coloredDate == null ? null : coloredDate.toString());

            subroutinePrinter.endSubroutine(subroutineText);
        }

        @Override
        public void cancelEdit() {
            final String subroutineText = "ColoredDateListCell.cancelEdit()";

            subroutinePrinter.beginSubroutine(subroutineText);

            super.cancelEdit();

            subroutinePrinter.endSubroutine(subroutineText);
        }

        @Override
        public void commitEdit(ColoredDate newValue) {
            final String subroutineText = "ColoredDateListCell.commitEdit(" + newValue + ")";

            subroutinePrinter.beginSubroutine(subroutineText);

            super.commitEdit(newValue);

            subroutinePrinter.endSubroutine(subroutineText);
        }
    }

    /**
     * A {@link TableColumn} contained within a {@link ColoredDateTableView}.
     */
    private static final class ColorTableColumn extends TableColumn<ColoredDate, Color> {

        /**
         * Constructs a new {@code ColorTableColumn}
         * with the default characteristics.
         */
        public ColorTableColumn() {
            super();
            setText("color");
            setEditable(true);
            setCellFactory(ColorPickerTableCell.forTableColumn());
            setCellValueFactory(colorColumnCellValueFactory());
        }

        // Intended as the cell value factory for a ColorTableColumn
        private static Callback<CellDataFeatures<ColoredDate, Color>, ObservableValue<Color>> colorColumnCellValueFactory() {
            return cdf -> {
                if (cdf == null) {
                    return null;
                }
                ColoredDate value = cdf.getValue();
                return (value == null) ? null : value.colorProperty();
            };
        }

    }

    /**
     * A {@link TableColumn} contained within a {@link ColoredDateTableView}.
     */
    private static final class DateTableColumn extends TableColumn<ColoredDate, LocalDate> {

        /**
         * Constructs a new {@code DateTableColumn}
         * with the default characteristics.
         */
        public DateTableColumn() {
            super();
            setText("date");
            setEditable(true);
            setCellFactory(DatePickerTableCell.forTableColumn());
            setCellValueFactory(dateColumnCellValueFactory());
        }

        // Intended as the cell value factory for a DateTableColumn
        private static Callback<CellDataFeatures<ColoredDate, LocalDate>, ObservableValue<LocalDate>> dateColumnCellValueFactory() {
            return cdf -> {
                if (cdf == null) {
                    return null;
                }
                ColoredDate value = cdf.getValue();
                return (value == null) ? null : value.dateProperty();
            };
        }

    }

    /**
     * A {@link TableColumn} contained within a {@link ColoredDateTableView}.
     */
    private static final class TextTableColumn extends TableColumn<ColoredDate, String> {

        /**
         * Constructs a new {@code TextTableColumn}
         * with the default characteristics.
         */
        public TextTableColumn() {
            super();
            setText("text");
            setEditable(true);
            setCellFactory(TextFieldTableCell.forTableColumn());
            setCellValueFactory(textColumnCellValueFactory());
        }

        // Intended as the cell value factory for a TextTableColumn
        private static Callback<CellDataFeatures<ColoredDate, String>, ObservableValue<String>> textColumnCellValueFactory() {
            return cdf -> {
                if (cdf == null) {
                    return null;
                }
                ColoredDate value = cdf.getValue();
                return (value == null) ? null : value.textProperty();
            };
        }

    }

    /**
     * Constructs a new {@code ColoredDateTableView}
     * with the default characteristics.
     */
    public ColoredDateTableView() {
        super();
        setSkin(coloredDateTableViewSkin(this));
        setItems(FXCollections.observableArrayList());
        setPrefSize(600, 300);
        for (int i = 0; i < 4; i++) {
            getItems().add(new ColoredDate());
        }
        getItems().add(null);
        for (int i = 0; i < 3; i++) {
            getItems().add(new ColoredDate());
        }

        ColoredDateTableView.ColorTableColumn colorColumn = new ColorTableColumn();
        ColoredDateTableView.DateTableColumn dateColumn = new DateTableColumn();
        ColoredDateTableView.TextTableColumn textColumn = new TextTableColumn();

        colorColumn.setPrefWidth(getPrefWidth() * 0.3);
        getColumns().add(colorColumn);

        dateColumn.setPrefWidth(getPrefWidth() * 0.3);
        getColumns().add(dateColumn);

        textColumn.setPrefWidth(getPrefWidth() * 0.3);
        getColumns().add(textColumn);
    }

    // Intended as the skin for a ColoredDateTableView
    private static TableEditorViewSkin<ColoredDate> coloredDateTableViewSkin(
            ColoredDateTableView coloredDateTableView) {
        return new TableEditorViewSkin<>(coloredDateTableView) {
            @Override
            protected ColoredDate createNewItem() {
                return new ColoredDate();
            }

            @Override
            protected Callback<ListView<ColoredDate>, ListCell<ColoredDate>> createItemsEditorCellFactory() {
                return listView -> new ColoredDateListCell();
            }
        };
    }

}