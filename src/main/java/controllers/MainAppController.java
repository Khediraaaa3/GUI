package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainAppController {

    @FXML private Label notificationLabel;

    /**
     * Open the Materials Management interface.
     */
    @FXML
    public void openMaterialsInterface() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/Materiels.fxml"));
            if (loader.getLocation() == null) {
                System.out.println("Error: FXML file not found at /org/example/demo/Materiels.fxml");
                return;
            }

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Materials Management");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            displayNotification("Materials interface closed.", "green");

        } catch (IOException e) {
            e.printStackTrace();
            displayNotification("Error loading the materials interface.", "red");
        }
    }

    /**
     * Open the Suppliers Management interface.
     */
    @FXML
    public void openSuppliersInterface() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/fournisseur.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Suppliers Management");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            displayNotification("Suppliers interface closed.", "green");

        } catch (IOException e) {
            e.printStackTrace();
            displayNotification("Error loading the suppliers interface.", "red");
        }
    }

    /**
     * Display a temporary notification.
     *
     * @param message The message to display.
     * @param color   The text color ("green" or "red").
     */
    private void displayNotification(String message, String color) {
        notificationLabel.setText(message);
        notificationLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 14px;");
    }
}