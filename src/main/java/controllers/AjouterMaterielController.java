package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import entities.Materiel;
import services.MaterielService;

import java.util.concurrent.atomic.AtomicBoolean;

public class AjouterMaterielController {

    // Champs existants
    @FXML private TextField nomField;
    @FXML private TextField typeField;
    @FXML private TextField qteTotField;
    @FXML private TextField qteDispField;
    @FXML private TextField idLieuField;
    @FXML private TextField idFournField;
    @FXML private TextField prixField;
    @FXML private ComboBox<String> etatComboBox;

    // Labels d'erreurs
    @FXML private Label nomErrorLabel;
    @FXML private Label typeErrorLabel;
    @FXML private Label qteTotErrorLabel;
    @FXML private Label qteDispErrorLabel;
    @FXML private Label idLieuErrorLabel;
    @FXML private Label idFournErrorLabel;
    @FXML private Label prixErrorLabel;
    @FXML private Label etatErrorLabel;
    @FXML private Label messageLabel;

    private final MaterielService materielService = new MaterielService();
    private CardsController cardsController; // Référence au contrôleur principal

    public void setCardsController(CardsController controller) {
        this.cardsController = controller;
    }

    @FXML
    public void initialize() {
        etatComboBox.getItems().addAll("Neuf", "Bon état", "Usé");
    }

    @FXML
    public void ajouterMateriel() {
        resetErreurs();

        String name = nomField.getText().trim();
        String type = typeField.getText().trim();
        String totalStr = qteTotField.getText().trim();
        String availableStr = qteDispField.getText().trim();
        String lieuIdStr = idLieuField.getText().trim();
        String fournIdStr = idFournField.getText().trim();
        String prixStr = prixField.getText().trim();
        String etatSelectionne = etatComboBox.getValue();

        boolean isValid = true;

        int total = 0, available = 0, lieuId = 0, fournId = 0;
        double prix = 0.0;

        if (name.isEmpty()) {
            afficherErreur(nomErrorLabel, "Le nom est requis.");
            isValid = false;
        }

        if (type.isEmpty()) {
            afficherErreur(typeErrorLabel, "Le type est requis.");
            isValid = false;
        }

        try {
            total = Integer.parseInt(totalStr);
            if (total < 0) {
                afficherErreur(qteTotErrorLabel, "Doit être ≥ 0.");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            afficherErreur(qteTotErrorLabel, "Nombre invalide.");
            isValid = false;
        }

        try {
            available = Integer.parseInt(availableStr);
            if (available < 0) {
                afficherErreur(qteDispErrorLabel, "Doit être ≥ 0.");
                isValid = false;
            }
            if (available > total) {
                afficherErreur(qteDispErrorLabel, "Ne peut pas dépasser la quantité totale.");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            afficherErreur(qteDispErrorLabel, "Nombre invalide.");
            isValid = false;
        }

        try {
            lieuId = Integer.parseInt(lieuIdStr);
            if (lieuId < 0) {
                afficherErreur(idLieuErrorLabel, "Doit être ≥ 0.");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            afficherErreur(idLieuErrorLabel, "ID Lieu doit être un entier.");
            isValid = false;
        }

        try {
            fournId = Integer.parseInt(fournIdStr);
            if (fournId < 0) {
                afficherErreur(idFournErrorLabel, "Doit être ≥ 0.");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            afficherErreur(idFournErrorLabel, "ID Fournisseur doit être un entier.");
            isValid = false;
        }

        try {
            prix = Double.parseDouble(prixStr);
            if (prix < 0) {
                afficherErreur(prixErrorLabel, "Doit être ≥ 0.");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            afficherErreur(prixErrorLabel, "Prix invalide.");
            isValid = false;
        }

        if (etatSelectionne == null || etatSelectionne.isEmpty()) {
            afficherErreur(etatErrorLabel, "Veuillez choisir un état.");
            isValid = false;
        }

        if (!isValid) return;

        try {
            Materiel m = new Materiel(name, type, total, available, lieuId, fournId, prix, etatSelectionne);
            materielService.ajouterM(m);

            afficherMessage("Matériau ajouté avec succès !", "green");

            if (cardsController != null) {
                cardsController.reloadData(); // Rafraîchir la vue principale
            }

            fermerFenetreApresDelai(2000);

        } catch (Exception e) {
            afficherMessage("Erreur lors de l'ajout : " + e.getMessage(), "red");
            e.printStackTrace();
        }
    }

    private void afficherErreur(Label label, String message) {
        label.setText(message);
        label.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
    }

    private void afficherMessage(String message, String color) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 20px; -fx-font-weight: bold;");
    }

    private void resetErreurs() {
        nomErrorLabel.setText("");
        typeErrorLabel.setText("");
        qteTotErrorLabel.setText("");
        qteDispErrorLabel.setText("");
        idLieuErrorLabel.setText("");
        idFournErrorLabel.setText("");
        prixErrorLabel.setText("");
        etatErrorLabel.setText("");
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