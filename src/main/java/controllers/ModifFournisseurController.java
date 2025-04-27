package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import entities.fournisseur;
import services.FournisseurService;

public class ModifFournisseurController {

    @FXML private TextField idField;
    @FXML private TextField nomField;
    @FXML private TextField numField;
    @FXML private Label messageLabel;

    private final FournisseurService fournisseurService = new FournisseurService();
    private fournisseur fournisseurSelectionne;

    /**
     * Initialiser les champs avec les données du fournisseur sélectionné.
     *
     * @param fournisseur Le fournisseur à modifier.
     */
    public void initData(fournisseur fournisseur) {
        this.fournisseurSelectionne = fournisseur;

        // Pré-remplir les champs
        idField.setText(String.valueOf(fournisseur.getId_fourn()));
        nomField.setText(fournisseur.getNom_fourn());
        numField.setText(String.valueOf(fournisseur.getNum_fourn()));
    }

    /**
     * Modifier le fournisseur sélectionné.
     */
    @FXML
    public void modifierFournisseur() {
        String nom = nomField.getText();
        String numStr = numField.getText();

        // Validation des champs
        if (nom.isEmpty() || numStr.isEmpty()) {
            afficherMessage("Tous les champs sont obligatoires.", "red");
            return;
        }

        try {
            int num = Integer.parseInt(numStr);

            // Mettre à jour les propriétés du fournisseur sélectionné
            fournisseurSelectionne.setNom_fourn(nom);
            fournisseurSelectionne.setNum_fourn(num);

            // Appeler le service pour enregistrer les modifications dans la base de données
            boolean success = fournisseurService.modifierF(fournisseurSelectionne);
            if (success) {
                afficherMessage("Fournisseur modifié avec succès !", "green");

                // Fermer la fenêtre après modification
                Stage stage = (Stage) messageLabel.getScene().getWindow();
                stage.close();
            } else {
                afficherMessage("Erreur lors de la modification.", "red");
            }

        } catch (NumberFormatException e) {
            afficherMessage("Le champ 'Numéro' doit être un nombre entier.", "red");
        }
    }

    /**
     * Afficher un message dans l'interface.
     *
     * @param message Le message à afficher.
     * @param color   La couleur du texte ("green" ou "red").
     */
    private void afficherMessage(String message, String color) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 14px;");
    }
}