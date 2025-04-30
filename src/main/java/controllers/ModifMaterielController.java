package controllers;

import entities.Materiel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    @FXML private Label messageLabel;

    private final MaterielService materielService = new MaterielService();
    private Materiel materielSelectionne;

    /**
     * Initialiser les champs avec les données du matériel sélectionné.
     *
     * @param materiel Le matériel à modifier.
     */
    public void initData(Materiel materiel) {
        this.materielSelectionne = materiel;

        // Pré-remplir les champs
        idField.setText(String.valueOf(materiel.getId_mat()));
        nomField.setText(materiel.getNom_mat());
        typeField.setText(materiel.getType_mat());
        qteTotField.setText(String.valueOf(materiel.getQte_tot()));
        qteDispField.setText(String.valueOf(materiel.getQte_disp()));
        idLieuField.setText(String.valueOf(materiel.getIdLieu()));
        idFournField.setText(String.valueOf(materiel.getId_fourn()));

        System.out.println("Données initialisées pour la modification : " + materiel);
    }

    /**
     * Modifier le matériel sélectionné.
     */
    @FXML
    public void modifierMateriel() {
        String nom = nomField.getText();
        String type = typeField.getText();
        String qteTotStr = qteTotField.getText();
        String qteDispStr = qteDispField.getText();
        String idLieuStr = idLieuField.getText();
        String idFournStr = idFournField.getText();

        // Validation des champs
        if (nom.isEmpty() || type.isEmpty() || qteTotStr.isEmpty() || qteDispStr.isEmpty() || idLieuStr.isEmpty() || idFournStr.isEmpty()) {
            afficherMessage("Tous les champs sont obligatoires.", "orange");
            return;
        }

        try {
            int qteTot = Integer.parseInt(qteTotStr);
            int qteDisp = Integer.parseInt(qteDispStr);
            int idLieu = Integer.parseInt(idLieuStr);
            int idFourn = Integer.parseInt(idFournStr);

            // Mettre à jour les propriétés du matériel sélectionné
            materielSelectionne.setNom_mat(nom);
            materielSelectionne.setType_mat(type);
            materielSelectionne.setQte_tot(qteTot);
            materielSelectionne.setQte_disp(qteDisp);
            materielSelectionne.setIdLieu(idLieu);
            materielSelectionne.setId_fourn(idFourn);

            System.out.println("Données à modifier : " + materielSelectionne);

            // Appeler le service pour enregistrer les modifications dans la base de données
            boolean success = materielService.modifierM(materielSelectionne);
            if (success) {
                afficherMessage("Matériel modifié avec succès !", "orange");

                // Fermer la fenêtre après modification
                Stage stage = (Stage) messageLabel.getScene().getWindow();
                stage.close();
            } else {
                afficherMessage("Erreur lors de la modification.", "orange");
            }

        } catch (NumberFormatException e) {
            afficherMessage("Les champs numériques doivent être des nombres entiers.", "orange");
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
        messageLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 20px;");
    }
}