package finn.gui;

import finn.Finn;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/** Controller for Finn's main JavaFX window. */
public class MainWindow extends AnchorPane {
    @FXML private ScrollPane scrollPane;
    @FXML private VBox dialogContainer;
    @FXML private TextField userInput;
    private Finn finn;
    private final Image userImage = new Image(getClass().getResourceAsStream("/images/DaUser.jpg"));
    private final Image finnImage = new Image(getClass().getResourceAsStream("/images/DaDuke.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    public void setFinn(Finn finn) {
        this.finn = finn;
        dialogContainer.getChildren().add(DialogBox.getFinnDialog("Hello! I'm Finn. How can I help?", finnImage));
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isBlank()) return;
        String response = finn.getResponse(input);
        dialogContainer.getChildren().addAll(DialogBox.getUserDialog(input, userImage),
                DialogBox.getFinnDialog(response, finnImage));
        userInput.clear();
    }
}
