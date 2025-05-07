package controllers;

import entities.Fournisseur;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.FournisseurService;

public class ModifFournisseurController {

    @FXML private TextField idField;
    @FXML private TextField nomField;
    @FXML private TextField numField;
    @FXML private Label nomErrorLabel;
    @FXML private Label numErrorLabel;
    @FXML private Label messageLabel;

    private final FournisseurService fournisseurService = new FournisseurService();
    private Fournisseur fournisseurSelectionne;

    /**
     * Initialiser les champs avec les données du fournisseur sélectionné.
     */
    public void initData(Fournisseur fournisseur) {
        this.fournisseurSelectionne = fournisseur;

        // Pré-remplir les champs
        idField.setText(String.valueOf(fournisseur.getId_fourn()));
        nomField.setText(fournisseur.getNom_fourn());
        numField.setText(fournisseur.getNum_fourn());

        resetErreurs();
    }

    /**
     * Modifier le fournisseur sélectionné.
     */
    @FXML
    public void modifierFournisseur() {
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
            fournisseurSelectionne.setNom_fourn(name);
            fournisseurSelectionne.setNum_fourn(phone);

            boolean success = fournisseurService.modifierF(fournisseurSelectionne);
            if (success) {
                afficherMessage("Fournisseur modifié avec succès !", "green");
                fermerFenetreApresDelai(2000);
            } else {
                afficherMessage("Erreur lors de la modification.", "red");
            }

        } catch (Exception e) {
            afficherMessage("Erreur : " + e.getMessage(), "red");
        }
    }

    /**
     * Afficher une erreur sous un champ spécifique.
     */
    private void afficherErreur(Label label, String message) {
        label.setText(message);
        label.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
    }

    /**
     * Afficher un message global dans l'interface.
     */
    private void afficherMessage(String message, String color) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 20px; -fx-font-weight: bold;");
    }

    /**
     * Réinitialiser tous les labels d'erreur.
     */
    private void resetErreurs() {
        nomErrorLabel.setText("");
        numErrorLabel.setText("");
        messageLabel.setText("");
    }

    /**
     * Fermer la fenêtre après un délai.
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