package vgp.fx.application;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Objects;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Cell;
import javafx.scene.control.Control;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.skin.ListViewSkin;
import javafx.scene.control.skin.TableViewSkin;
import javafx.scene.control.skin.TreeViewSkin;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vgp.dispose.Disposable;
import vgp.fx.controls.list.view.ColorListView;
import vgp.fx.controls.list.view.DateListView;
import vgp.fx.controls.table.view.ColoredDateTableView;
import vgp.fx.controls.table.view.ObjectTableView;
import vgp.fx.controls.table.view.map.MapTableView;
import vgp.fx.controls.table.view.property.BeanPropertyMetadata;
import vgp.fx.controls.table.view.property.PropertyTableView;
import vgp.fx.controls.tree.view.ColorTreeView;
import vgp.fx.controls.tree.view.DateTreeView;
import vgp.fx.listenerhandler.ListenerAndHandlerBindings;
import vgp.fx.objects.ColoredDate;

/**
 * A tester class for certain subclasses of {@link Cell} and {@link SkinBase}.
 * 
 * @author (to be added)
 * @version 6.11
 * @since 2.7
 */
public class CellViewerApplication extends ApplicationBase {

    /**
     * The {@link Scene#getRoot() root} of the return value for
     * {@link CellViewerApplication#createStartingScene()}.
     * 
     * @version 6.11
     * @since 4.1
     */
    private final class StartingPane extends ScrollPane implements Disposable {

        /**
         * The {@link Button#setOnAction(EventHandler) onAction}
         * for each {@code Button} used in {@code StartingPane}.
         * 
         * @version 4.5
         * @since 4.1
         */
        private final class HandlerForViewingViews implements EventHandler<ActionEvent> {

            private final Control control;

            public HandlerForViewingViews(Control control) {
                super();
                this.control = Objects.requireNonNull(control);
            }

            @Override
            public void handle(ActionEvent event) {
                if (event != null) {
                    final Scene sceneOfOwnerWindow = StartingPane.this.getScene();
                    if (sceneOfOwnerWindow != null) {
                        Stage stage = null;
                        try {
                            stage = new Stage();
                            stage.initOwner(sceneOfOwnerWindow.getWindow());
                            stage.initModality(Modality.WINDOW_MODAL);
                            stage.setTitle(control.getClass().toGenericString());
                            stage.setScene(new Scene(/* new Pane */(control)));
                            stage.showAndWait();
                        } finally {
                            if (stage != null) {
                                stage.setTitle(null);
                                if (stage.getScene() != null) {

                                    // disconnect 'control' from 'stage.getScene()'
                                    stage.getScene().setRoot(new Pane());

                                    stage.setScene(null);
                                }
                                stage = null;
                            }
                        }
                    }
                }
            }
        }

        private final ObservableMap<String, Double> observableMap = FXCollections.observableMap(new LinkedHashMap<>());

        private ListView<Color> colorPickerListView;
        private TreeView<Color> colorPickerTreeView;
        private ListView<LocalDate> datePickerListView;
        private TreeView<LocalDate> datePickerTreeView;
        private TableView<ColoredDate> coloredDateTableView;
        private TableView<Object> objectTableView;
        private TableView<BeanPropertyMetadata> propertyTableView;
        private MapTableView<String, Double> mapTableView;

        private Button buttonForColorPickerListView;
        private Button buttonForColorPickerTreeView;
        private Button buttonForDatePickerListView;
        private Button buttonForDatePickerTreeView;
        private Button buttonForColoredDateTableView;
        private Button buttonForObjectTableView;
        private Button buttonForPropertyTableView;
        private Button buttonForMapTableView;

        private boolean disposed = false;

        /**
         * Construct a {@code StartingPane} with the default characteristics.
         */
        public StartingPane() {
            super();

            colorPickerListView = new ColorListView();
            colorPickerTreeView = new ColorTreeView();
            datePickerListView = new DateListView();
            datePickerTreeView = new DateTreeView();
            coloredDateTableView = new ColoredDateTableView();
            objectTableView = new ObjectTableView();
            propertyTableView = new PropertyTableView(BEAN_CLASS_TO_TEST);

            observableMap.put("abc", 5.2);
            observableMap.put("def", -7.1);
            observableMap.put("ghi", 0.9);
            observableMap.put("jkl", 3.2);
            mapTableView = new MapTableView<>(observableMap);
            observableMap.put("mno", 3.2);
            observableMap.put("def", -1.8);
            observableMap.put("ghi", 8.9);
            observableMap.put("pqr", -4.4);

            buttonForColorPickerListView = new Button("ColorPicker list");
            buttonForColorPickerTreeView = new Button("ColorPicker tree");
            buttonForDatePickerListView = new Button("DatePicker list");
            buttonForDatePickerTreeView = new Button("DatePicker tree");
            buttonForColoredDateTableView = new Button("Table for ColorPicker and DatePicker");
            buttonForObjectTableView = new Button("Basic table");
            buttonForPropertyTableView = new Button("Table for bean class properties");
            buttonForMapTableView = new Button("Map table");

            ListenerAndHandlerBindings.setOnAction(buttonForColorPickerListView.onActionProperty(),
                    new HandlerForViewingViews(colorPickerListView));
            ListenerAndHandlerBindings.setOnAction(buttonForColorPickerTreeView.onActionProperty(),
                    new HandlerForViewingViews(colorPickerTreeView));
            ListenerAndHandlerBindings.setOnAction(buttonForDatePickerListView.onActionProperty(),
                    new HandlerForViewingViews(datePickerListView));
            ListenerAndHandlerBindings.setOnAction(buttonForDatePickerTreeView.onActionProperty(),
                    new HandlerForViewingViews(datePickerTreeView));
            ListenerAndHandlerBindings.setOnAction(buttonForObjectTableView.onActionProperty(),
                    new HandlerForViewingViews(objectTableView));
            ListenerAndHandlerBindings.setOnAction(buttonForColoredDateTableView.onActionProperty(),
                    new HandlerForViewingViews(coloredDateTableView));
            ListenerAndHandlerBindings.setOnAction(buttonForPropertyTableView.onActionProperty(),
                    new HandlerForViewingViews(propertyTableView));
            ListenerAndHandlerBindings.setOnAction(buttonForMapTableView.onActionProperty(),
                    new HandlerForViewingViews(mapTableView));

            final GridPane gridPane = new GridPane(20, 20);

            gridPane.add(buttonForColorPickerListView, 0, 0);
            gridPane.add(buttonForColorPickerTreeView, 1, 0);
            gridPane.add(buttonForDatePickerListView, 0, 1);
            gridPane.add(buttonForDatePickerTreeView, 1, 1);
            gridPane.add(buttonForObjectTableView, 0, 2);
            gridPane.add(buttonForColoredDateTableView, 1, 2);
            gridPane.add(buttonForPropertyTableView, 0, 3);
            gridPane.add(buttonForMapTableView, 1, 3);

            this.setMaxSize(1200, 700);
            this.setContent(gridPane);
        }

        @Override
        public void dispose() {
            if (this.isDisposed()) {
                return;
            }
            disposed = true;
            disposeButtons();
            disposeViews();
        }

        private void disposeViews() {
            if (colorPickerListView != null) {
                colorPickerListView.setSkin(new ListViewSkin<>(colorPickerListView));
                colorPickerListView = null;
            }
            if (colorPickerTreeView != null) {
                colorPickerTreeView.setSkin(new TreeViewSkin<>(colorPickerTreeView));
                colorPickerTreeView = null;
            }
            if (datePickerListView != null) {
                datePickerListView.setSkin(new ListViewSkin<>(datePickerListView));
                datePickerListView = null;
            }
            if (datePickerTreeView != null) {
                datePickerTreeView.setSkin(new TreeViewSkin<>(datePickerTreeView));
                datePickerTreeView = null;
            }
            if (coloredDateTableView != null) {
                coloredDateTableView.setSkin(new TableViewSkin<>(coloredDateTableView));
                coloredDateTableView = null;
            }
            if (objectTableView != null) {
                objectTableView.setSkin(new TableViewSkin<>(objectTableView));
                objectTableView = null;
            }
            if (propertyTableView != null) {
                propertyTableView.setSkin(new TableViewSkin<>(propertyTableView));
                propertyTableView = null;
            }
            if (mapTableView != null) {
                mapTableView.setSkin(new TableViewSkin<>(mapTableView));
                mapTableView = null;
            }
        }

        private void disposeButtons() {
            if (buttonForColorPickerListView != null) {
                ApplicationBase.dispose(buttonForColorPickerListView);
                buttonForColorPickerListView = null;
            }
            if (buttonForColorPickerTreeView != null) {
                ApplicationBase.dispose(buttonForColorPickerTreeView);
                buttonForColorPickerTreeView = null;
            }
            if (buttonForDatePickerListView != null) {
                ApplicationBase.dispose(buttonForDatePickerListView);
                buttonForDatePickerListView = null;
            }
            if (buttonForDatePickerTreeView != null) {
                ApplicationBase.dispose(buttonForDatePickerTreeView);
                buttonForDatePickerTreeView = null;
            }
            if (buttonForColoredDateTableView != null) {
                ApplicationBase.dispose(buttonForColoredDateTableView);
                buttonForColoredDateTableView = null;
            }
            if (buttonForObjectTableView != null) {
                ApplicationBase.dispose(buttonForObjectTableView);
                buttonForObjectTableView = null;
            }
            if (buttonForPropertyTableView != null) {
                ApplicationBase.dispose(buttonForPropertyTableView);
                buttonForPropertyTableView = null;
            }
            if (buttonForMapTableView != null) {
                ApplicationBase.dispose(buttonForMapTableView);
                buttonForMapTableView = null;
            }
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }

    }

    private StartingPane startingPane;

    private static final Class<?> BEAN_CLASS_TO_TEST = Parent.class;

    /**
     * Constructs a new {@code CellViewerApplication}
     * with the default characteristics.
     * <p>
     * This constructor is required by the
     * documentation for {@link Application}.
     */
    public CellViewerApplication() {
        super();
        startingPane = null;
    }

    @Override
    public void init() throws Exception {
        super.init();
        startingPane = new StartingPane();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        changeUncaughtExceptionHandler();
        super.start(primaryStage);
        {
            final Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText("Problem with " + MapTableView.class);
            alert.setContentText("If 'observableMap.put(...);' is used after "
                    + "'mapTableView = new MapTableView<>(observableMap)' "
                    + "in the 'StartingPane' constructor, then things in the "
                    + "'MapTableView' won't update properly.");
            alert.showAndWait();
        }
    }

    private void changeUncaughtExceptionHandler() {
        if (!Platform.isFxApplicationThread()) {
            new Error("!Platform.isFxApplicationThread()").printStackTrace();
            Platform.exit();
        }
        final Thread currentThread = Thread.currentThread();
        final UncaughtExceptionHandler oldUncaughtExceptionHandler = currentThread.getUncaughtExceptionHandler();
        final UncaughtExceptionHandler newUncaughtExceptionHandler = new UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                showAlert(t, e);
                oldUncaughtExceptionHandler.uncaughtException(t, e);
            }

            private void showAlert(Thread t, Throwable e) {
                final Alert alert = new Alert(AlertType.ERROR);

                String stackTraceText = getStackTraceText(e);

                if (alert.getDialogPane() == null) {
                    alert.setDialogPane(new DialogPane());
                }
                final Node content = new ScrollPane(new Text(stackTraceText));
                alert.getDialogPane().setContent(content);
                final Node header = createHeaderForDialogPaneOfAlert(t, e);
                alert.getDialogPane().setHeader(header);

                alert.setWidth(800);
                alert.setHeight(500);
                alert.setTitle("Uncaught Exception");
                alert.showAndWait();
            }

            private Node createHeaderForDialogPaneOfAlert(Thread t, Throwable e) {
                final Text threadText1 = new Text("Thread: ");
                final Text threadText2 = new Text(String.valueOf(t));
                final HBox threadHBox = new HBox(threadText1, threadText2);
                ensureBoldFont(threadText1);

                final Text exceptionText1 = new Text("Exception: ");
                final Text exceptionText2 = new Text(String.valueOf(e));
                final HBox exceptionHBox = new HBox(exceptionText1, exceptionText2);
                ensureBoldFont(exceptionText1);

                final Pane header = new VBox(threadHBox, exceptionHBox);
                return header;
            }

            private void ensureBoldFont(Text text) {
                Objects.requireNonNull(text);

                ensureNonNullFont(text);

                final Font font = Objects.requireNonNull(text.getFont());
                final String fontFamily = font.getFamily();
                final double fontSize = font.getSize();
                final FontWeight fontWeight = FontWeight.BOLD;

                text.setFont(Font.font(fontFamily, fontWeight, fontSize));
            }

            private void ensureNonNullFont(Text text) {
                Objects.requireNonNull(text);

                if (text.getFont() == null) {
                    text.setFont(Objects.requireNonNull(Font.getDefault()));
                }
            }

            private String getStackTraceText(Throwable e) {
                String stackTraceText;
                try (final StringWriter stringWriter = new StringWriter()) {
                    if (e == null) {
                        stringWriter.append(null);
                    } else {
                        try (final PrintWriter printWriter = new PrintWriter(stringWriter)) {
                            e.printStackTrace(printWriter);
                        }
                    }
                    stackTraceText = String.valueOf(stringWriter);
                } catch (IOException ioException) {
                    stackTraceText = "";
                }
                return stackTraceText;
            }
        };
        currentThread.setUncaughtExceptionHandler(newUncaughtExceptionHandler);
    }

    @Override
    protected Scene createStartingScene() throws Exception {
        return new Scene(startingPane);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (startingPane != null) {
            startingPane.dispose();
            startingPane = null;
        }
        ListenerAndHandlerBindings.outputListenerBindings();
    }

    /**
     * The entry point for this launcher class. This method
     * merely calls {@link Application#launch(String...)}.
     * 
     * @param args the string array passed from the command line
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

}
