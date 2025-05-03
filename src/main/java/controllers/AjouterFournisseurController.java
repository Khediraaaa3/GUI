package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import entities.Fournisseur;
import services.FournisseurService;

public class AjouterFournisseurController {

    @FXML private TextField nomField;
    @FXML private TextField numField;
    @FXML private Label messageLabel;

    private final FournisseurService fournisseurService = new FournisseurService();

    /**
     * Add a new supplier.
     */
    @FXML
    public void ajouterFournisseur() {
        String name = nomField.getText();
        String phoneStr = numField.getText();

        // Validate that all fields are filled
        if (name.isEmpty() || phoneStr.isEmpty()) {
            afficherMessage("All fields are required.", "orange");
            return;
        }

        try {
            // Parse the phone number field
            String phone = phoneStr;

            // Create a new supplier object
            Fournisseur supplier = new Fournisseur(name, phone);

            // Add the supplier to the database
            fournisseurService.ajouterF(supplier);

            // Display success message
            afficherMessage("Supplier added successfully!", "orange");

            // Close the modal window after adding
            Stage stage = (Stage) messageLabel.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            // Handle invalid numeric input
            afficherMessage("The 'Phone Number' field must be an integer.", "orange");
        }
    }

    /**
     * Display a message in the interface.
     *
     * @param message The message to display.
     * @param color   The text color ("green" or "red").
     */
    private void afficherMessage(String message, String color) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 20px; -fx-font-weight: bold;");
    }
}