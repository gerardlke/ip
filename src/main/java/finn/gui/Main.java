package finn.gui;

import java.io.IOException;

import finn.Finn;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/** JavaFX entry point for Finn. */
public class Main extends Application {
    /** Creates the application instance used by the JavaFX launcher. */
    public Main() {
    }

    /**
     * Loads the main window, connects persistent task storage, and shows a resizable stage.
     *
     * @param stage The primary window supplied by JavaFX.
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane root = loader.load();
            loader.<MainWindow>getController().setFinn(new Finn("./data/Finn.txt"));
            stage.setScene(new Scene(root));
            stage.setTitle("Pip | Your task stash");
            stage.setMinWidth(380);
            stage.setMinHeight(360);
            stage.setResizable(true);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Unable to load Pip GUI", e);
        }
    }
}
