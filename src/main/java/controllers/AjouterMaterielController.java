package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import entities.Materiel;
import services.MaterielService;

public class AjouterMaterielController {

    @FXML private TextField nomField;
    @FXML private TextField typeField;
    @FXML private TextField qteTotField;
    @FXML private TextField qteDispField;
    @FXML private TextField idLieuField;
    @FXML private TextField idFournField;
    @FXML private Label messageLabel;

    private final MaterielService materielService = new MaterielService();

    /**
     * Add a new material.
     */
    @FXML
    public void ajouterMateriel() {
        String name = nomField.getText();
        String type = typeField.getText();
        String totalQuantityStr = qteTotField.getText();
        String availableQuantityStr = qteDispField.getText();
        String locationIdStr = idLieuField.getText();
        String supplierIdStr = idFournField.getText();

        // Validate that all fields are filled
        if (name.isEmpty() || type.isEmpty() || totalQuantityStr.isEmpty() || availableQuantityStr.isEmpty()
                || locationIdStr.isEmpty() || supplierIdStr.isEmpty()) {
            afficherMessage("All fields are required.", "orange");
            return;
        }

        try {
            // Parse numeric fields
            int totalQuantity = Integer.parseInt(totalQuantityStr);
            int availableQuantity = Integer.parseInt(availableQuantityStr);
            int locationId = Integer.parseInt(locationIdStr);
            int supplierId = Integer.parseInt(supplierIdStr);

            // Create a new material object
            Materiel material = new Materiel(name, type, totalQuantity, availableQuantity, locationId, supplierId);

            // Add the material to the database
            materielService.ajouterM(material);

            // Display success message
            afficherMessage("Material added successfully!", "orange");

            // Close the modal window after adding
            Stage stage = (Stage) messageLabel.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            // Handle invalid numeric input
            afficherMessage("Numeric fields must be integers.", "orange");
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
        messageLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 20px;");
    }
}