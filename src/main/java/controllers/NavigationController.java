package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class NavigationController {

    @FXML
    private ScrollPane mainContentArea;

    public void handleMateriels(ActionEvent event) {
        loadView("cardsContainer.fxml");
    }

    public void handleFournisseurs(ActionEvent event) {
        loadView("cardsFournisseur.fxml");
    }

    public void handleExit(ActionEvent event) {
        System.exit(0);
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/" + fxmlFile));
            Parent view = loader.load();
            mainContentArea.setContent(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}