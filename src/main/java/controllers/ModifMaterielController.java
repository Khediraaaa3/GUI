package controllers;

import entities.Materiel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.MaterielService;

public class ModifMaterielController {

    @FXML private TextField nomField, typeField, qteTotField, qteDispField;
    @FXML private ComboBox<String> etatComboBox;

    @FXML private Label nomErrorLabel;
    @FXML private Label typeErrorLabel;
    @FXML private Label qteTotErrorLabel;
    @FXML private Label qteDispErrorLabel;
    @FXML private Label etatErrorLabel;
    @FXML private Label messageLabel;

    private final MaterielService materielService = new MaterielService();
    private Materiel materielSelectionne;
    private CardsController parentController;

    public void initData(Materiel m) {
        this.materielSelectionne = m;

        // Ne pas afficher l'ID
        nomField.setText(m.getNom_mat());
        typeField.setText(m.getType_mat());
        qteTotField.setText(String.valueOf(m.getQte_tot()));
        qteDispField.setText(String.valueOf(m.getQte_disp()));
        etatComboBox.getItems().setAll("Neuf", "Bon état", "Usé");
        etatComboBox.setValue(m.getEtat());

        resetErreurs();
    }

    public void setParentController(CardsController controller) {
        this.parentController = controller;
    }

    @FXML
    public void saveMaterial(ActionEvent actionEvent) {
        resetErreurs();

        String name = nomField.getText().trim();
        String type = typeField.getText().trim();
        String totalStr = qteTotField.getText().trim();
        String dispStr = qteDispField.getText().trim();
        String etat = etatComboBox.getValue();

        boolean isValid = true;

        if (name.isEmpty()) {
            afficherErreur(nomErrorLabel, "Le nom est requis.");
            isValid = false;
        }

        if (type.isEmpty()) {
            afficherErreur(typeErrorLabel, "Le type est requis.");
            isValid = false;
        }

        int total = 0, disp = 0;

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
            disp = Integer.parseInt(dispStr);
            if (disp < 0) {
                afficherErreur(qteDispErrorLabel, "Doit être ≥ 0.");
                isValid = false;
            }
            if (disp > total) {
                afficherErreur(qteDispErrorLabel, "Ne peut pas dépasser la quantité totale.");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            afficherErreur(qteDispErrorLabel, "Nombre invalide.");
            isValid = false;
        }

        if (etat == null || etat.isBlank()) {
            afficherErreur(etatErrorLabel, "Veuillez choisir un état.");
            isValid = false;
        }

        if (!isValid) return;

        try {
            materielSelectionne.setNom_mat(name);
            materielSelectionne.setType_mat(type);
            materielSelectionne.setQte_tot(total);
            materielSelectionne.setQte_disp(disp);
            materielSelectionne.setEtat(etat);

            boolean success = materielService.modifierM(materielSelectionne);
            if (success) {
                afficherMessage("Matériel modifié avec succès !", "green");

                if (parentController != null) {
                    parentController.reloadData(); // Rafraîchir la liste
                }

                fermerFenetreApresDelai(2000);
            } else {
                afficherMessage("Erreur lors de la modification.", "red");
            }

        } catch (Exception e) {
            afficherMessage("Erreur : " + e.getMessage(), "red");
        }
    }

    @FXML
    public void goBackToCards(ActionEvent actionEvent) {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    private void afficherErreur(Label label, String message) {
        label.setText(message);
        label.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
    }

    private void afficherMessage(String message, String color) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");
    }

    private void resetErreurs() {
        nomErrorLabel.setText("");
        typeErrorLabel.setText("");
        qteTotErrorLabel.setText("");
        qteDispErrorLabel.setText("");
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
}