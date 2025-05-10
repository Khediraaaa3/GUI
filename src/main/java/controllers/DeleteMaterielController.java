package controllers;

import entities.Materiel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import services.MaterielService;

public class DeleteMaterielController {

    @FXML private Label titleLabel;
    @FXML private Label messageLabel;

    private Materiel materiel;
    private CardsController parentController;

    public void initData(Materiel m, CardsController controller) {
        this.materiel = m;
        this.parentController = controller;

        titleLabel.setText("Supprimer : " + m.getNom_mat());
        messageLabel.setText("Souhaitez-vous vraiment supprimer ce matériel ?");
    }

    @FXML
    public void confirmDelete(ActionEvent event) {
        MaterielService service = new MaterielService();
        service.supprimerM(materiel.getId_mat());

        if (parentController != null) {
            parentController.reloadData(); // Rafraîchir la liste
        }

        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void cancelDelete(ActionEvent event) {
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }
}