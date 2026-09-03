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
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane root = loader.load();
            loader.<MainWindow>getController().setFinn(new Finn("./data/Finn.txt"));
            stage.setScene(new Scene(root));
            stage.setTitle("Finn");
            stage.setMinWidth(417);
            stage.setMinHeight(220);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Unable to load Finn GUI", e);
        }
    }
}
