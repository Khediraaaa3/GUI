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
    @FXML private Label nomErrorLabel;
    @FXML private Label numErrorLabel;
    @FXML private Label messageLabel;

    private final FournisseurService fournisseurService = new FournisseurService();

    /**
     * Add a new supplier with field-specific error messages.
     */
    @FXML
    public void ajouterFournisseur() {
        resetErreurs();

        String name = nomField.getText().trim();
        String phone = numField.getText().trim();

        boolean isValid = true;

        // Validation du nom
        if (name.isEmpty()) {
            afficherErreur(nomErrorLabel, "Le nom est requis.");
            isValid = false;
        }

        // Validation du numéro de téléphone
        if (phone.isEmpty()) {
            afficherErreur(numErrorLabel, "Le numéro est requis.");
            isValid = false;
        } else if (!Fournisseur.validatePhoneNumber(phone)) {
            afficherErreur(numErrorLabel, "Format de téléphone invalide.");
            isValid = false;
        }

        if (!isValid) return;

        try {
            Fournisseur fournisseur = new Fournisseur(name, phone);
            fournisseurService.ajouterF(fournisseur);

            afficherMessage("Fournisseur ajouté avec succès !", "green");

            fermerFenetreApresDelai(2000);

        } catch (Exception e) {
            afficherMessage("Erreur lors de l'ajout : " + e.getMessage(), "red");
            e.printStackTrace();
        }
    }

    /**
     * Display an error message under a specific label.
     */
    private void afficherErreur(Label label, String message) {
        label.setText(message);
        label.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
    }

    /**
     * Display a general message at the bottom of the form.
     */
    private void afficherMessage(String message, String color) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 20px; -fx-font-weight: bold;");
    }

    /**
     * Clear all error labels.
     */
    private void resetErreurs() {
        nomErrorLabel.setText("");
        numErrorLabel.setText("");
        messageLabel.setText("");
    }

    /**
     * Close window after delay.
     */
    private void fermerFenetreApresDelai(long millis) {
        new javafx.animation.AnimationTimer() {
            private long startTime = -1;

            @Override
            public void handle(long now) {
                if (startTime < 0) startTime = now;
                else if (now - startTime > millis * 1_000_000) {
                    Stage stage = (Stage) nomField.getScene().getWindow();
                    stage.close();
                    this.stop();
                }
            }
        }.start();
    }
}