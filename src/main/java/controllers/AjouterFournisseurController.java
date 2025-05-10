package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import entities.Fournisseur;
import services.FournisseurService;

public class AjouterFournisseurController {

    @FXML private TextField nomField;
    @FXML private TextField numField;
    @FXML private Label nomErrorLabel;
    @FXML private Label numErrorLabel;
    @FXML private Label messageLabel;

    private Cards parentController;
    private Fournisseur fournisseurEnCours; // Pour modification

    public void setCardsController(Cards controller) {
        this.parentController = controller;
    }

    /**
     * Méthode appelée lors de l’ajout d’un nouveau fournisseur
     */
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
        } else if (!validerNumero(phone)) {
            afficherErreur(numErrorLabel, "Format de téléphone invalide.");
            isValid = false;
        }

        if (!isValid) return;

        try {
            if (fournisseurEnCours == null) {
                // Mode ajout
                Fournisseur f = new Fournisseur(name, phone);
                new FournisseurService().ajouterF(f);
            } else {
                // Mode modification
                fournisseurEnCours.setNom_fourn(name);
                fournisseurEnCours.setNum_fourn(phone);
                new FournisseurService().modifierF(fournisseurEnCours);
            }

            if (parentController != null) {
                parentController.reloadData(); // Rafraîchir la liste
            }

            fermerFenetreApresDelai(2000);

        } catch (Exception e) {
            afficherMessage("Erreur : " + e.getMessage(), "red");
        }
    }

    /**
     * Remplit les champs avec les données du fournisseur existant (mode modification)
     */
    public void initData(Fournisseur f) {
        this.fournisseurEnCours = f;

        nomField.setText(f.getNom_fourn());
        numField.setText(f.getNum_fourn());
    }

    /**
     * Valide le format du numéro de téléphone
     */
    private boolean validerNumero(String numero) {
        return numero.matches("\\d{8}"); // Numéro tunisien sur 8 chiffres
    }

    /**
     * Affiche un message d'erreur sous un champ
     */
    private void afficherErreur(Label label, String message) {
        label.setText(message);
        label.setStyle("-fx-text-fill: red;");
    }

    /**
     * Réinitialise tous les messages d'erreurs
     */
    private void resetErreurs() {
        nomErrorLabel.setText("");
        numErrorLabel.setText("");
        messageLabel.setText("");
    }

    /**
     * Ferme la fenêtre après un délai
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

    /**
     * Bouton Retour → ferme la fenêtre
     */
    @FXML
    public void goBackToCards() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    private void afficherMessage(String message, String color) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 16px; -fx-font-weight: bold;");
    }

}