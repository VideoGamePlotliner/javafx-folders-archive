package vgp.fx.application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
// import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// import java.io.IOException;

/**
 * JavaFX App
 * 
 * @version 6.6
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) /* throws IOException */ {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) /* throws IOException */ {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) /* throws IOException */ {
        // FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml +
        // ".fxml"));
        // return fxmlLoader.load();
        switch (fxml) {
            case "primary":
                return loadPrimary();
            case "secondary":
                return loadSecondary();
            default:
                return new Pane();
        }
    }

    private static Parent loadPrimary() {

        // <VBox alignment="CENTER" spacing="20.0"
        // xmlns="http://javafx.com/javafx/8.0.171" xmlns:fx="http://javafx.com/fxml/1"
        // fx:controller="vgp.fx.application.PrimaryController">
        // <children>
        // <Label text="Primary View" />
        // <Button fx:id="primaryButton" text="Switch to Secondary View"
        // onAction="#switchToSecondary"/>
        // </children>
        // <padding>
        // <Insets bottom="20.0" left="20.0" right="20.0" top="20.0" />
        // </padding>
        // </VBox>

        final Button button = new Button("Switch to Secondary View");
        button.setId("primaryButton");
        button.setOnAction(event -> new PrimaryController().switchToSecondary());

        final VBox vBox = new VBox(20.0, new Label("Primary View"), button);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20.0));

        return vBox;
    }

    private static Parent loadSecondary() {

        // <VBox alignment="CENTER" spacing="20.0"
        // xmlns="http://javafx.com/javafx/8.0.171" xmlns:fx="http://javafx.com/fxml/1"
        // fx:controller="vgp.fx.application.SecondaryController">
        // <children>
        // <Label text="Secondary View" />
        // <Button fx:id="secondaryButton" text="Switch to Primary View"
        // onAction="#switchToPrimary" />
        // </children>
        // <padding>
        // <Insets bottom="20.0" left="20.0" right="20.0" top="20.0" />
        // </padding>
        // </VBox>

        final Button button = new Button("Switch to Primary View");
        button.setId("secondaryButton");
        button.setOnAction(event -> new SecondaryController().switchToPrimary());

        final VBox vBox = new VBox(20.0, new Label("Secondary View"), button);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20.0));

        return vBox;
    }

    public static void main(String[] args) {
        launch();
    }

}