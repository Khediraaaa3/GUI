package controllers;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import entities.Fournisseur;
import services.FournisseurService;

public class ModifFournisseurController {

    @FXML private TextField nomField;
    @FXML private TextField numField;
    @FXML private Label nomErrorLabel;
    @FXML private Label numErrorLabel;
    @FXML private Label messageLabel;

    private Cards parentController;
    private Fournisseur fournisseurSelectionne;

    public void initData(Fournisseur f) {
        this.fournisseurSelectionne = f;
        nomField.setText(f.getNom_fourn());
        numField.setText(f.getNum_fourn());
    }

    public void setCardsController(Cards controller) {
        this.parentController = controller;
    }

    @FXML
    public void saveFournisseur() {
        String name = nomField.getText().trim();
        String phone = numField.getText().trim();

        boolean isValid = true;

        if (name.isEmpty()) {
            afficherErreur(nomErrorLabel, "Le nom est requis.");
            isValid = false;
        }

        if (phone.isEmpty()) {
            afficherErreur(numErrorLabel, "Le téléphone est requis.");
            isValid = false;
        } else if (!validerNumero(phone)) {
            afficherErreur(numErrorLabel, "Format invalide (ex: 53197136)");
            isValid = false;
        }

        if (!isValid) return;

        try {
            fournisseurSelectionne.setNom_fourn(name);
            fournisseurSelectionne.setNum_fourn(phone);

            new FournisseurService().modifierF(fournisseurSelectionne);

            if (parentController != null) {
                parentController.reloadData(); // Rafraîchir la liste
            }

            fermerFenetreApresDelai(2000);

        } catch (Exception e) {
            afficherMessage(messageLabel, "Erreur : " + e.getMessage(), "red");
        }
    }

    private boolean validerNumero(String numero) {
        return numero.matches("\\d{8}"); // Numéro tunisien sur 8 chiffres
    }

    private void afficherErreur(Label label, String message) {
        label.setText(message);
        label.setStyle("-fx-text-fill: red;");
    }

    private void afficherMessage(Label label, String message, String color) {
        label.setText(message);
        label.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");
    }

    private void fermerFenetreApresDelai(long millis) {
        new AnimationTimer() {
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