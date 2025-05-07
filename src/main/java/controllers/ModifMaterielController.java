package controllers;

import entities.Materiel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.MaterielService;

public class ModifMaterielController {

    @FXML private TextField idField;
    @FXML private TextField nomField;
    @FXML private TextField typeField;
    @FXML private TextField qteTotField;
    @FXML private TextField qteDispField;
    @FXML private TextField idLieuField;
    @FXML private TextField idFournField;

    @FXML private Label nomErrorLabel;
    @FXML private Label typeErrorLabel;
    @FXML private Label qteTotErrorLabel;
    @FXML private Label qteDispErrorLabel;
    @FXML private Label idLieuErrorLabel;
    @FXML private Label idFournErrorLabel;
    @FXML private Label messageLabel;

    private final MaterielService materielService = new MaterielService();
    private Materiel materielSelectionne;

    /**
     * Initialiser les champs avec les données du matériel sélectionné.
     */
    public void initData(Materiel materiel) {
        this.materielSelectionne = materiel;

        idField.setText(String.valueOf(materiel.getId_mat()));
        nomField.setText(materiel.getNom_mat());
        typeField.setText(materiel.getType_mat());
        qteTotField.setText(String.valueOf(materiel.getQte_tot()));
        qteDispField.setText(String.valueOf(materiel.getQte_disp()));
        idLieuField.setText(String.valueOf(materiel.getIdLieu()));
        idFournField.setText(String.valueOf(materiel.getId_fourn()));

        resetErreurs();
    }

    /**
     * Modifier le matériel sélectionné.
     */
    @FXML
    public void modifierMateriel() {
        resetErreurs();

        String name = nomField.getText().trim();
        String type = typeField.getText().trim();
        String totalStr = qteTotField.getText().trim();
        String dispStr = qteDispField.getText().trim();
        String lieuStr = idLieuField.getText().trim();
        String fournStr = idFournField.getText().trim();

        boolean isValid = true;

        if (name.isEmpty()) {
            afficherErreur(nomErrorLabel, "Le nom est requis.");
            isValid = false;
        }

        if (type.isEmpty()) {
            afficherErreur(typeErrorLabel, "Le type est requis.");
            isValid = false;
        }

        int total = 0, disp = 0, lieu = 0, fourn = 0;

        try {
            total = Integer.parseInt(totalStr);
            if (total < 0) {
                afficherErreur(qteTotErrorLabel, "La quantité doit être ≥ 0.");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            afficherErreur(qteTotErrorLabel, "Quantité totale invalide.");
            isValid = false;
        }

        try {
            disp = Integer.parseInt(dispStr);
            if (disp < 0) {
                afficherErreur(qteDispErrorLabel, "La quantité doit être ≥ 0.");
                isValid = false;
            }
            if (disp > total) {
                afficherErreur(qteDispErrorLabel, "Ne peut pas dépasser la quantité totale.");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            afficherErreur(qteDispErrorLabel, "Quantité disponible invalide.");
            isValid = false;
        }

        try {
            lieu = Integer.parseInt(lieuStr);
            if (lieu < 0) {
                afficherErreur(idLieuErrorLabel, "Doit être ≥ 0.");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            afficherErreur(idLieuErrorLabel, "ID Lieu doit être un entier.");
            isValid = false;
        }

        try {
            fourn = Integer.parseInt(fournStr);
            if (fourn < 0) {
                afficherErreur(idFournErrorLabel, "Doit être ≥ 0.");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            afficherErreur(idFournErrorLabel, "ID Fournisseur doit être un entier.");
            isValid = false;
        }

        if (!isValid) return;

        try {
            materielSelectionne.setNom_mat(name);
            materielSelectionne.setType_mat(type);
            materielSelectionne.setQte_tot(total);
            materielSelectionne.setQte_disp(disp);
            materielSelectionne.setIdLieu(lieu);
            materielSelectionne.setId_fourn(fourn);

            boolean success = materielService.modifierM(materielSelectionne);
            if (success) {
                afficherMessage("Matériel modifié avec succès !", "green");
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
     * Réinitialiser tous les labels d'erreurs.
     */
    private void resetErreurs() {
        nomErrorLabel.setText("");
        typeErrorLabel.setText("");
        qteTotErrorLabel.setText("");
        qteDispErrorLabel.setText("");
        idLieuErrorLabel.setText("");
        idFournErrorLabel.setText("");
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

    public void saveMaterial(ActionEvent actionEvent) {
    }

    public void goBackToCards(ActionEvent actionEvent) {

    }
}