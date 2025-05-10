package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import entities.Fournisseur;
import services.FournisseurService;

public class AjouterFournisseurController {

    @FXML private TextField nomField;
    @FXML private TextField numField;
    @FXML private Label nomErrorLabel;
    @FXML private Label numErrorLabel;
    @FXML private Label messageLabel;

    private final FournisseurService service = new FournisseurService();
    private CardsFournisseurController parentController; // Référence directe au contrôleur principal

    public void setParentController(CardsFournisseurController controller) {
        this.parentController = controller;
    }

    @FXML
    public void ajouterFournisseur() {
        resetErreurs();

        String name = nomField.getText().trim();
        String phone = numField.getText().trim();

        boolean isValid = true;

        if (name.isEmpty()) {
            afficherErreur(nomErrorLabel, "Le nom est requis.");
            isValid = false;
        }

        if (phone.isEmpty()) {
            afficherErreur(numErrorLabel, "Le numéro est requis.");
            isValid = false;
        } else if (!Fournisseur.validatePhoneNumber(phone)) {
            afficherErreur(numErrorLabel, "Format de téléphone invalide.");
            isValid = false;
        }

        if (!isValid) return;

        try {
            Fournisseur f = new Fournisseur(name, phone);
            service.ajouterF(f);

            afficherMessage("Fournisseur ajouté avec succès !", "green");

            if (parentController != null) {
                parentController.reloadData(); // Rafraîchir la liste via le contrôleur principal
            }

            fermerFenetreApresDelai(2000);

        } catch (Exception e) {
            afficherMessage("Erreur lors de l'ajout : " + e.getMessage(), "red");
            e.printStackTrace();
        }
    }

    private void afficherErreur(Label label, String message) {
        label.setText(message);
        label.setStyle("-fx-text-fill: red;");
    }

    private void afficherMessage(String message, String color) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");
    }

    private void resetErreurs() {
        nomErrorLabel.setText("");
        numErrorLabel.setText("");
        messageLabel.setText("");
    }

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

    @FXML
    public void goBackToCards() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }
}